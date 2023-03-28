package ml.empee.orbitaltrial.model.entities;

import lombok.Getter;
import ml.empee.orbitaltrial.model.dto.AccountDTO;

import java.util.UUID;

public class Account {

  private final UUID uuid;
  @Getter
  private double balance = 0;

  public Account(UUID uuid) {
    this.uuid = uuid;
  }

  public Account(AccountDTO dto) {
    this.uuid = UUID.fromString(dto.getUuid());
    if (dto.getBalance() != null) {
      this.balance = dto.getBalance();
    }
  }

  public AccountDTO toDto() {
    return AccountDTO.builder()
      .uuid(uuid.toString())
      .balance(balance)
      .build();
  }

  /**
   * @throws IllegalArgumentException if the amount is less then 0
   */
  public void setBalance(double amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("The balance of " + uuid + " can't be less 0");
    }

    this.balance = amount;
  }

  /**
   * @throws IllegalArgumentException if the total balance (With the amount added) is less then 0
   */
  public void addBalance(double amount) {
    setBalance(this.balance + amount);
  }

}
