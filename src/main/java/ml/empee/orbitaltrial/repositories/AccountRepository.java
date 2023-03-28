package ml.empee.orbitaltrial.repositories;

import com.j256.ormlite.dao.DaoManager;
import ml.empee.ioc.Bean;
import ml.empee.orbitaltrial.config.DatabaseConfig;
import ml.empee.orbitaltrial.model.dto.AccountDTO;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AccountRepository extends AbstractRepository<AccountDTO, String> implements Bean {

  public AccountRepository(DatabaseConfig config) throws SQLException {
    super(
      config.getExecutor(),
      DaoManager.createDao(config.getConnectionSource(), AccountDTO.class)
    );
  }

  @Override
  public CompletableFuture<Void> save(@NotNull AccountDTO data) {
    return super.save(data);
  }

  @Override
  public CompletableFuture<Optional<AccountDTO>> findByID(@NotNull String s) {
    return super.findByID(s);
  }
}
