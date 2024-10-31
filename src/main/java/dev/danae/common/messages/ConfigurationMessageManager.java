package dev.danae.common.messages;

import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;


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
}
