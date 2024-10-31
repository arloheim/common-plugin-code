package dev.danae.common.plugin;

import java.util.Map;
import dev.danae.common.messages.MessageManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;


public interface PluginMessageManager<P extends JavaPlugin> extends PluginComponent<P>, MessageManager
{
  // Return the messages of the plugin
  public Map<String, String> getMessages();


  // Return the message with the specified key
  public default String getMessage(String key)
  {
    return this.getMessages().getOrDefault(key, null);
  }

  // Load the messages from the specified configuration section
  public default void loadMessagesFromConfiguration(ConfigurationSection section)
  {
    this.getMessages().clear();

    if (section != null)
    {
      for (var name : section.getKeys(false))
      {
        var message = section.getString(name);
        this.getMessages().put(name, message);
      }
    }
  }

  // Load the messages from the configuration section with the specified name in the plugin
  public default void loadMessagesFromConfiguration(String sectionPath)
  {
    this.loadMessagesFromConfiguration(this.getPlugin().getConfig().getConfigurationSection(sectionPath));
  }
}
