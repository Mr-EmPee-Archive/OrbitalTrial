package ml.empee.orbitaltrial.controllers;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import ml.empee.ioc.Bean;
import ml.empee.orbitaltrial.Permissions;
import ml.empee.orbitaltrial.config.CommandsConfig;
import ml.empee.orbitaltrial.model.entities.Account;
import ml.empee.orbitaltrial.services.AccountService;
import ml.empee.orbitaltrial.utils.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class PluginController implements Bean {

  private final CommandsConfig commandsConfig;
  private final AccountService accountService;

  private final Cache<UUID, Account> earnTimer = CacheBuilder.newBuilder()
    .expireAfterWrite(1, TimeUnit.MINUTES)
    .build();

  @Override
  public void onStart() {
    commandsConfig.register(this);
  }

  @Override
  public void onStop() {
    commandsConfig.unregister("bal");
    commandsConfig.unregister("give");
    commandsConfig.unregister("setbal");
    commandsConfig.unregister("earn");
  }

  @CommandMethod("bal [target]")
  public void printBalance(
    CommandSender sender, @Argument Player target
  ) {
    UUID accountOwner;
    if (target == null) {
      if (sender instanceof Player) {
        accountOwner = ((Player) sender).getUniqueId();
      } else {
        Logger.translatedLog(sender, "cmd-invalid-sender");
        return;
      }
    } else {
      accountOwner = target.getUniqueId();
    }

    accountService.findAccountByUUID(accountOwner).thenConsume(account -> {
      double balance = account.map(Account::getBalance).orElse(0.0);
      if (target == null || accountOwner.equals(((Player) sender).getUniqueId())) {
        Logger.translatedLog(sender, "self-balance-view", balance);
      } else {
        Logger.translatedLog(sender, "balance-view", target.getName(), balance);
      }
    });
  }

  @CommandPermission(Permissions.ADMIN)
  @CommandMethod("setbal <target> <amount>")
  public void setBalance(
    CommandSender sender, @Argument Player target, @Argument double amount
  ) {
    if (amount < 0) {
      Logger.translatedLog(sender, "invalid-amount");
      return;
    }

    accountService.findAccountByUUID(target.getUniqueId()).thenConsume(account -> {
      Account result = account.orElse(new Account(target.getUniqueId()));
      result.setBalance(amount);
      accountService.saveAccount(result);

      Logger.translatedLog(sender, "balance-set", target.getName(), amount);
    });
  }

  @CommandMethod("give <target> <amount>")
  public void sendBalance(
    Player sender, @Argument Player target, @Argument double amount
  ) {
    if (target.getUniqueId().equals(sender.getUniqueId())) {
      Logger.translatedLog(sender, "self-give");
      return;
    } else if (amount <= 0) {
      Logger.translatedLog(sender, "invalid-amount");
      return;
    }

    accountService.findAccountByUUID(sender.getUniqueId()).thenConsume(account -> {
      Account senderAccount = account.orElse(new Account(sender.getUniqueId()));
      if (senderAccount.getBalance() - amount < 0) {
        Logger.translatedLog(sender, "not-enough-money");
        return;
      }

      accountService.findAccountByUUID(target.getUniqueId()).thenConsume(receiver -> {
        Account receiverAccount = receiver.orElse(new Account(target.getUniqueId()));

        senderAccount.addBalance(-amount);
        receiverAccount.addBalance(amount);

        accountService.saveAccount(senderAccount);
        accountService.saveAccount(receiverAccount);

        Logger.translatedLog(sender, "balance-give", amount, target.getName());
        Logger.translatedLog(target, "balance-receive", amount, sender.getName());
      });
    });
  }

  @CommandMethod("earn")
  public void earnMoney(Player sender) {
    if (earnTimer.getIfPresent(sender.getUniqueId()) != null) {
      Logger.translatedLog(sender, "earn-wait");
      return;
    }

    accountService.findAccountByUUID(sender.getUniqueId()).thenConsume(account -> {
      Account result = account.orElse(new Account(sender.getUniqueId()));

      double amount = 1 + (Math.random() * 4);
      amount = Math.round(amount * 100) / 100.0;

      result.addBalance(amount);
      accountService.saveAccount(result);

      earnTimer.put(sender.getUniqueId(), result);
      Logger.translatedLog(sender, "earn", amount);
    });
  }

}
