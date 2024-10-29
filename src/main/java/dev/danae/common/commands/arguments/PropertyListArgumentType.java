package dev.danae.common.commands.arguments;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.Suggestion;


final class PropertyListArgumentType implements ArgumentType<PropertyList>
{
  // The pattern for parsing a property
  private static final Pattern PROPERTY_PATTERN = Pattern.compile("#([a-z][a-z0-9-]*)(?:=(.+))?");


  // The property types for the argument type
  private final Map<String, StringArgumentType<?>> propertyTypes;


  // Constructor
  public PropertyListArgumentType(Map<String, StringArgumentType<?>> propertyTypes)
  {
    this.propertyTypes = propertyTypes;
  }


  // Return the type of the argument type
  @Override
  public Class<PropertyList> getType()
  {
    return PropertyList.class;
  }

  // Return the type name for the argument type
  @Override
  public String getTypeName()
  {
    return "property list";
  }

  // Parse a property list from the specified scanner 
  @Override
  public PropertyList parse(Scanner scanner) throws ArgumentException
  {
    var properties = new HashMap<String, Object>();
    
    while (!scanner.isAtEnd())
    {
      var m = scanner.match(PROPERTY_PATTERN, "property");
      if (m == null)
        break;

      var name = m.group(1);
      if (this.propertyTypes.containsKey(name))
        throw new ArgumentException(String.format("Undefined property %s", name));

      var valueType = this.propertyTypes.get(name);
      var value = valueType.parseFromString(m.group(2));
      properties.put(name, value);
    }
    
    return new PropertyList(properties);
  }

  @Override
  public Stream<String> suggest(CommandContext context, int argumentIndex)
  {
    return Suggestion.find(context.getArgument(argumentIndex + 1), this.propertyTypes.keySet().stream()
      .map(name -> String.format("#%s=", name)));
  }
}
