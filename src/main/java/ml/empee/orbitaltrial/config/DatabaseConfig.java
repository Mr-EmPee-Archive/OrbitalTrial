package ml.empee.orbitaltrial.config;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.LogBackendType;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.support.ConnectionSource;
import lombok.Getter;
import lombok.SneakyThrows;
import ml.empee.orbitaltrial.utils.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/** DB Config properties **/

public class DatabaseConfig {

  private final JdbcConnectionSource connectionSource;
  @Getter
  private final ExecutorService executor = Executors.newSingleThreadExecutor();

  @SneakyThrows
  public DatabaseConfig(JavaPlugin plugin) {
    File dbFile = new File(plugin.getDataFolder(), "safe.sqlite");
    String dbURL = "jdbc:sqlite:" + dbFile.getAbsolutePath();

    dbFile.getParentFile().mkdirs();
    LoggerFactory.setLogBackendFactory(LogBackendType.NULL);
    connectionSource = new JdbcConnectionSource(dbURL);

    createTables();
  }

  @SneakyThrows
  private void createTables() {
    //TableUtils.createTableIfNotExists(connectionSource, VaultData.class);
  }

  @SneakyThrows
  public void closeConnection() {
    Logger.info("Shutting down db connections (Forced timeout in 60seconds)");
    executor.shutdown();
    executor.awaitTermination(60, TimeUnit.SECONDS);
    connectionSource.close();
  }

  public ConnectionSource getConnectionSource() {
    return connectionSource;
  }

}
