package dev.danae.common.commands.arguments;

import dev.danae.common.commands.CommandContext;
import java.util.EnumSet;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;


public interface ArgumentType<T>
{
  // Patterns for parsing argument types
  public static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
  public static final Pattern NAMESPACED_KEY_PATTERN = Pattern.compile("[a-zA-Z0-9._-]+:[a-zA-Z0-9._\\/-]+");
  public static final Pattern INT_PATTERN = Pattern.compile("0|-?[1-9][0-9]*");
  public static final Pattern UNSIGNED_INT_PATTERN = Pattern.compile("0|[1-9][0-9]*");
  public static final Pattern FLOAT_PATTERN = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");


  // Return the type of the argument type
  public Class<? super T> getType();

  // Return the type name of the argument type
  public String getTypeName();

  // Parse an argument from the specified scanner
  public T parse(Scanner scanner) throws ArgumentException;

  // Return suggestions for the specified command context and argument
  public Stream<String> suggest(CommandContext context, int argumentIndex);
  

  // // Parse an argument from the specified scanner, or return the default value if parsing fails
  public default T parse(Scanner scanner, T defaultValue) throws ArgumentException
  {
    try
    {
      return this.parse(scanner);
    }
    catch (ArgumentException ex)
    {
      return defaultValue;
    }
  }


  // Return a string argument type
  public static PatternArgumentType<String> getStringArgumentType(Stream<String> suggestions)
  {
    return PatternArgumentType.ofMatch(String.class, "string", null, (s, t) -> s, suggestions);
  }

  // Return an identifier argument type
  public static PatternArgumentType<String> getIdentifierArgumentType(Stream<String> suggestions)
  {
    return PatternArgumentType.ofMatch(String.class, "identifier", IDENTIFIER_PATTERN, (s, t) -> s, suggestions);
  }

  // Return an int argument type
  public static PatternArgumentType<Integer> getIntArgumentType(int radix, Stream<Integer> suggestions)
  {
    return PatternArgumentType.ofMatch(Integer.class, "integer", INT_PATTERN, (s, t) -> Integer.parseInt(s, radix), suggestions.map(v -> v.toString()));
  }

  // Return a base-10 int argument type
  public static PatternArgumentType<Integer> getIntArgumentType(Stream<Integer> suggestions)
  {
    return getIntArgumentType(10, suggestions);
  }

  // Return a long argument type
  public static PatternArgumentType<Long> getLongArgumentType(int radix, Stream<Long> suggestions)
  {
    return PatternArgumentType.ofMatch(Long.class, "integer", INT_PATTERN, (s, t) -> Long.parseLong(s, radix), suggestions.map(v -> v.toString()));
  }

  // Return a base-10 long argument type
  public static PatternArgumentType<Long> getLongArgumentType(Stream<Long> suggestions)
  {
    return getLongArgumentType(10, suggestions);
  }

  // Return an unsigned int argument type
  public static PatternArgumentType<Integer> getUnsignedIntArgumentType(int radix, Stream<Integer> suggestions)
  {
    return PatternArgumentType.ofMatch(Integer.class, "unsigned integer", INT_PATTERN, (s, t) -> Integer.parseUnsignedInt(s, radix), suggestions.map(v -> v.toString()));
  }

  // Return a base-10 unsigned int argument type
  public static PatternArgumentType<Integer> getUnsignedIntArgumentType(Stream<Integer> suggestions)
  {
    return getUnsignedIntArgumentType(10, suggestions);
  }

  // Return an unsigned long argument type
  public static PatternArgumentType<Long> getUnsignedLongArgumentType(int radix, Stream<Long> suggestions)
  {
    return PatternArgumentType.ofMatch(Long.class, "unsigned integer", INT_PATTERN, (s, t) -> Long.parseUnsignedLong(s, radix), suggestions.map(v -> v.toString()));
  }

  // Return a base-10 unsigned long argument type
  public static PatternArgumentType<Long> getUnsignedLongArgumentType(Stream<Long> suggestions)
  {
    return getUnsignedLongArgumentType(10, suggestions);
  }

  // Return a float argument type
  public static PatternArgumentType<Float> getFloatArgumentType(Stream<Float> suggestions)
  {
    return PatternArgumentType.ofMatch(Float.class, "float", FLOAT_PATTERN, (s, t) -> Float.parseFloat(s), suggestions.map(v -> v.toString()));
  }

  // Return a double argument type
  public static PatternArgumentType<Double> getDoubleArgumentType(Stream<Double> suggestions)
  {
    return PatternArgumentType.ofMatch(Double.class, "float", FLOAT_PATTERN, (s, t) -> Double.parseDouble(s), suggestions.map(v -> v.toString()));
  }


  // Return an enum argument type
  public static <E extends Enum<E>> StringArgumentType<E> getEnumArgumentType(Class<E> enumType)
  {
    return new EnumArgumentType<>(enumType);
  }

  // Return an enum setargument type
  public static <E extends Enum<E>> StringListArgumentType<EnumSet<E>> getEnumSetArgumentType(Class<E> enumType, String delimiter)
  {
    return new EnumSetArgumentType<>(enumType, delimiter);
  }
  
  // Return a material argument type
  public static StringArgumentType<Material> getMaterialArgumentType(MaterialFilter filter)
  {
    return new MaterialArgumentType(filter);
  }
  
  // Return a namespaced key argument type
  public static StringArgumentType<NamespacedKey> getNamespacedKeyArgumentType(Stream<NamespacedKey> suggestions)
  {
    return new NamespacedKeyArgumentType(suggestions.map(k -> k.toString()));
  }
  
  // Return a player argument type
  public static StringArgumentType<Player> getPlayerArgumentType()
  {
    return new PlayerArgumentType();
  }


  // Return a location argument type builder
  public static LocationArgumentTypeBuilder getLocationArgumentTypeBuilder()
  {
    return new LocationArgumentTypeBuilder();
  }

  // Return a property list argument type builder
  public static PropertyListArgumentTypeBuilder getPropertyListArgumentTypeBuilder()
  {
    return new PropertyListArgumentTypeBuilder();
  }
}
