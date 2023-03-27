package ml.empee.orbitaltrial.repositories;

import com.j256.ormlite.dao.Dao;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.logging.Level;

/**
 * CRUD Repository
 *
 * @param <ID> Id type of the repo
 * @param <T> Type of the repo
 */

public abstract class AbstractRepository<T, ID> {

  protected final Executor executor;
  protected final Dao<T, ID> dao;

  protected AbstractRepository(Executor executor, Dao<T, ID> dao) {
    this.executor = executor;
    this.dao = dao;
  }

  protected CompletableFuture<Void> save(@NotNull T data) {
    return CompletableFuture.runAsync(() -> {
      try {
        dao.createOrUpdate(data);
      } catch (SQLException e) {
        throw new RuntimeException("Error while saving lock data, " + data, e);
      }
    });
  }

  protected CompletableFuture<Void> delete(@NotNull ID id) {
    return CompletableFuture.runAsync(() -> {
      try {
        dao.deleteById(id);
      } catch (SQLException e) {
        throw new RuntimeException("Error while deleting lock data with" + id, e);
      }
    });
  }

  protected CompletableFuture<Optional<T>> findByID(@NotNull ID id) {
    return buildFutureSupplier(() -> {
      try {
        return Optional.ofNullable(dao.queryForId(id));
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
  }

  protected <K> CompletableFuture<K> buildFutureSupplier(Supplier<K> supplier) {
    return CompletableFuture.supplyAsync(supplier, executor).whenComplete((result, exception) -> {
      if (exception != null) {
        Bukkit.getLogger().log(
            Level.SEVERE, "Exception on thread: LudoLock Async Persistence Thread", exception
        );
      }
    });
  }

}
