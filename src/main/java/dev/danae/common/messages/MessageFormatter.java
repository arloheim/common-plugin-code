package dev.danae.common.messages;

import java.util.Map;
import net.kyori.adventure.text.Component;


public interface MessageFormatter 
{
  // Format the specified message and arguments to a component
  public Component format(String message, Map<String, Object> arguments);


  // Format the specified message to a component
  public default Component formatMessage(String message)
  {
    return this.format(message, Map.of());
  }
}
