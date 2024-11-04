package dev.danae.common.commands.arguments;

import java.util.HashMap;
import java.util.Map;
import com.google.common.base.Supplier;


public final class PropertyListArgumentTypeBuilder implements Supplier<ArgumentType<PropertyList>>
{
  // The property types for the argument type
  private Map<String, StringArgumentType<?>> propertyTypes;


  // Constructor
  private PropertyListArgumentTypeBuilder(Map<String, StringArgumentType<?>> propertyTypes)
  {
    this.propertyTypes = propertyTypes;
  }

  // Constructor for copying an existing builder
  private PropertyListArgumentTypeBuilder(PropertyListArgumentTypeBuilder builder)
  {
    this(new HashMap<>(builder.propertyTypes));
  }

  // Constructor for creating a new builder
  public PropertyListArgumentTypeBuilder()
  {
    this(new HashMap<>());
  }


  // Add the property type to the builder
  public PropertyListArgumentTypeBuilder withPropertyType(String name, StringArgumentType<?> type)
  {
    var newBuilder = new PropertyListArgumentTypeBuilder(this);
    newBuilder.propertyTypes.put(name, type);
    return newBuilder;
  }

  // Remove the property type from the builder
  public PropertyListArgumentTypeBuilder withoutPropertyType(String name)
  {
    var newBuilder = new PropertyListArgumentTypeBuilder(this);
    newBuilder.propertyTypes.remove(name);
    return newBuilder;
  }


  // Build the property list argument type
  public ArgumentType<PropertyList> build()
  {
    return new PropertyListArgumentType(this.propertyTypes);
  }

  // Apply the builder as a supplier
  @Override
  public ArgumentType<PropertyList> get()
  {
    return this.build();
  }
}
