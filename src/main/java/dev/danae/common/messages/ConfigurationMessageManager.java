package dev.danae.common.messages;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;


public interface ConfigurationMessageManager extends MessageManager
{
  // Return the messages of the plugin
  public Map<String, String> getMessages();


  // Return the message with the specified key
  public default String getMessage(String key)
  {
    return this.getMessages().getOrDefault(key, null);
  }

  // Load the messages from the specified configuration section
  public default void loadMessagesFromConfiguration(ConfigurationSection section, Map<String, String> defaultMessages)
  {
    this.getMessages().clear();

    if (section != null)
    {
      for (var name : section.getKeys(false))
      {
        var message = section.getString(name, defaultMessages.get(name));
        this.getMessages().put(name, message);
      }
    }
  }

  // Load the messages from the specified configuration file
  public default void loadMessagesFromConfiguration(File file, Map<String, String> defaultMessages)
  {
    try
    {
      var configuration = new YamlConfiguration();
      configuration.load(file);
      this.loadMessagesFromConfiguration(configuration, defaultMessages);
    }
    catch (IOException | InvalidConfigurationException ex)
    {
      this.getMessages().clear();
      this.getMessages().putAll(defaultMessages);
    }
  }
  
  // Load the messages from the specified configuration file in the plugin data folder
  public default void loadMessagesFromConfiguration(Plugin plugin, String file, Map<String, String> defaultMessages)
  {
    this.loadMessagesFromConfiguration(new File(plugin.getDataFolder(), file), defaultMessages);
  }
}
