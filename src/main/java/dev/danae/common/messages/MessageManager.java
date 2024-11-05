package dev.danae.common.messages;

import java.util.Map;
import net.kyori.adventure.text.Component;


public interface MessageManager
{
  // Return the message formatter
  public MessageFormatter getMessageFormatter();

  // Return the message with the specified key
  public String getMessage(String key);


  // Format the message with the specified key and arguments
  public default Component formatMessage(String key, Map<String, Object> arguments)
  {
    var message = this.getMessage(key);
    return message != null ? this.getMessageFormatter().format(message, arguments) : Component.empty();
  }

  // Format the message with the specified key
  public default Component formatMessage(String key)
  {
    return this.formatMessage(key, Map.of());
  }
}
