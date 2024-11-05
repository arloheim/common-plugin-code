package dev.danae.common.commands.arguments;

import java.util.Map;

public final class PropertyList 
{
  // The property types for the property list
  private final Map<String, Object> properties;


  // Constructor
  public PropertyList(Map<String, Object> properties)
  {
    this.properties = properties;
  }


  // Return if a property with the specified name exists
  public <T> boolean has(String name, ArgumentType<T> type)
  {
    if (!this.properties.containsKey(name))
      return false;

    var value = this.properties.get(name);
    if (!value.getClass().isAssignableFrom(type.getType()))
      return false;

    return true;
  }

  // Return the property with the specified name
  @SuppressWarnings("unchecked")
  public <T> T get(String name, ArgumentType<T> type) throws ArgumentException
  {
    if (!this.properties.containsKey(name))
      throw new ArgumentException(String.format("Undefined property \"%s\"", type.getTypeName()));

    var value = this.properties.get(name);
    if (!value.getClass().isAssignableFrom(type.getType()))
      throw new ArgumentException(String.format("Invalid property \"%s\" is not of type %s", type.getTypeName()));

    return (T)value;
  }

  // Return the property with the specified name, or the default value if no such property exists
  public <T> T get(String name, ArgumentType<T> type, T defaultValue)
  {
    try
    {
      return this.get(name, type);
    }
    catch (ArgumentException ex)
    {
      return defaultValue;
    }
  }
}
