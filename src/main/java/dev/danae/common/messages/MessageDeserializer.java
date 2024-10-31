package dev.danae.common.messages;

import java.util.Map;
import net.kyori.adventure.text.Component;


public interface MessageDeserializer 
{
  // Deserialize the specified message and arguments to a component
  public Component deserialize(String message, Map<String, Object> arguments);
}
