package dev.danae.common.commands.arguments;

import java.util.HashMap;
import java.util.Map;
import com.google.common.base.Supplier;


public final class PropertyListArgumentTypeSupplier implements Supplier<ArgumentType<PropertyList>>
{
  // The property types for the argument type
  private final Map<String, StringArgumentType<?>> propertyTypes = new HashMap<>();


  // Add the property to the argument type
  public PropertyListArgumentTypeSupplier withProperty(String name, StringArgumentType<?> type)
  {
    this.propertyTypes.put(name, type);
    return this;
  }

  // Remove the property from the argument type
  public PropertyListArgumentTypeSupplier withoutProperty(String name)
  {
    this.propertyTypes.remove(name);
    return this;
  }


  // Get the property list argument type
  public ArgumentType<PropertyList> get()
  {
    return new PropertyListArgumentType(this.propertyTypes);
  }
}
