package ml.empee.orbitaltrial;

import lombok.Getter;
import ml.empee.ioc.SimpleIoC;
import ml.empee.orbitaltrial.config.DatabaseConfig;
import ml.empee.orbitaltrial.utils.Logger;
import ml.empee.orbitaltrial.utils.Translator;
import org.bukkit.plugin.java.JavaPlugin;

/** Boot class of this plugin. **/

public final class OrbitalTrial extends JavaPlugin {

  private final DatabaseConfig databaseConfig = new DatabaseConfig(this);

  @Getter
  private final SimpleIoC iocContainer = new SimpleIoC(this);

  public void onEnable() {
    Translator.init(this);
    Logger.setPrefix(Translator.translate("prefix"));

    iocContainer.addBean(databaseConfig);
    iocContainer.initialize("relocations");
  }

  public void onDisable() {
    iocContainer.removeAllBeans(true);
    databaseConfig.closeConnection();
  }
}
