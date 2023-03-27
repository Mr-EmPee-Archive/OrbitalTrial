package ml.empee.orbitaltrial.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Translator {
  private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Translator.class);
  private static final ResourceBundle bundle;

  static {
    bundle = getResourceBundle(Locale.getDefault());
  }

  @SneakyThrows
  private static ResourceBundle getResourceBundle(Locale locale) {
    File messageFile = new File(plugin.getDataFolder(), "messages.properties");
    if(!messageFile.exists()) {
      messageFile.getParentFile().mkdirs();
      FileUtils.copyInputStreamToFile(plugin.getResource("messages.properties"), messageFile);
    }

    URL url = messageFile.getParentFile().toURI().toURL();
    return ResourceBundle.getBundle("messages", locale, new URLClassLoader(new URL[]{url}));
  }

  public static String translate(String key) {
    return bundle.getString(key);
  }
  public static String translate(String key, Locale locale) {
    return getResourceBundle(locale).getString(key);
  }

  private static String[] parseBlock(String input) {
    if(input.endsWith("\n")) {
      input += " ";
    }

    return input.split("\n");
  }
  public static String[] translateBlock(String key) {
    return parseBlock(translate(key));
  }
  public static String[] translateBlock(String key, Locale locale) {
    return parseBlock(Translator.translate(key, locale));
  }

}
