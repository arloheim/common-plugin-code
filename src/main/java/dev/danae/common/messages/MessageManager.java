package dev.danae.common.messages;

import java.util.Map;
import net.kyori.adventure.text.Component;


public interface MessageManager
{
  // Return the message deserializer
  public MessageDeserializer getMessageDeserializer();

  // Return the message with the specified key
  public String getMessage(String key);


  // Deserialize the message with the specified name key and arguments
  public default Component deserializeMessage(String key, Map<String, Object> arguments)
  {
    var message = this.getMessage(key);
    return message != null ? this.getMessageDeserializer().deserialize(message, arguments) : Component.empty();
  }
}
