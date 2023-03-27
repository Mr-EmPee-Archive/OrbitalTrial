package ml.empee.orbitaltrial;

import lombok.Getter;
import ml.empee.ioc.SimpleIoC;
import ml.empee.orbitaltrial.config.DatabaseConfig;
import ml.empee.orbitaltrial.utils.Translator;
import org.bukkit.plugin.java.JavaPlugin;

/** Boot class of this plugin. **/

public final class OrbitalTrial extends JavaPlugin {

  public static final String PREFIX = Translator.translate("prefix");
  //private static final String SPIGOT_PLUGIN_ID = "";
  //private static final Integer METRICS_PLUGIN_ID = 0;

  private final DatabaseConfig databaseConfig = new DatabaseConfig(this);

  @Getter
  private final SimpleIoC iocContainer = new SimpleIoC(this);

  public void onEnable() {
    iocContainer.addBean(databaseConfig);
    iocContainer.initialize("relocations");

    //Metrics.of(this, METRICS_PLUGIN_ID);
    //Notifier.listenForUpdates(this, SPIGOT_PLUGIN_ID);
  }

  public void onDisable() {
    iocContainer.removeAllBeans();
    databaseConfig.closeConnection();
  }
}
