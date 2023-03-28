package ml.empee.orbitaltrial.services;

import lombok.RequiredArgsConstructor;
import ml.empee.ioc.Bean;
import ml.empee.orbitaltrial.model.content.SyncedFuture;
import ml.empee.orbitaltrial.model.entities.Account;
import ml.empee.orbitaltrial.repositories.AccountRepository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class AccountService implements Bean {

  private final AccountRepository repository;

  public void saveAccount(Account account) {
    repository.save(account.toDto());
  }

  public SyncedFuture<Optional<Account>> findAccountByUUID(UUID uuid) {
    return SyncedFuture.of(
      repository.findByID(uuid.toString()).thenApply(dto -> dto.map(Account::new))
    );
  }

}
