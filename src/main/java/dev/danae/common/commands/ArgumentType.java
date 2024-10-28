package dev.danae.common.commands;

import dev.danae.common.commands.location.LocationArgumentType;
import dev.danae.common.commands.location.LocationArgumentTypeSupplier;
import dev.danae.common.commands.location.LocationFormat;
import dev.danae.common.commands.material.MaterialArgumentType;
import dev.danae.common.commands.material.MaterialFilter;
import dev.danae.common.commands.player.PlayerArgumentType;
import dev.danae.common.commands.regex.EnumArgumentType;
import dev.danae.common.commands.regex.EnumSetArgumentType;
import dev.danae.common.commands.regex.NamespacedKeyArgumentType;
import dev.danae.common.commands.regex.PatternArgumentType;
import java.util.EnumSet;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;


public abstract class ArgumentType<T>
{
  // Patterns for parsing argument types
  protected static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
  protected static final Pattern NAMESPACED_KEY_PATTERN = Pattern.compile("[a-zA-Z0-9._-]+:[a-zA-Z0-9._\\/-]+");
  protected static final Pattern INT_PATTERN = Pattern.compile("0|-?[1-9][0-9]*");
  protected static final Pattern UNSIGNED_INT_PATTERN = Pattern.compile("0|[1-9][0-9]*");
  protected static final Pattern FLOAT_PATTERN = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");

  // Predefined argument types
  private static final ArgumentType<String> STRING_TYPE = PatternArgumentType.ofMatch("string", null, s -> s);
  private static final ArgumentType<String> IDENTIFIER_TYPE = PatternArgumentType.ofMatch("identifier", IDENTIFIER_PATTERN, s -> s);
  private static final ArgumentType<Integer> INT_TYPE = PatternArgumentType.ofMatch("integer", INT_PATTERN, Integer::parseInt);
  private static final ArgumentType<Long> LONG_TYPE = PatternArgumentType.ofMatch("integer", INT_PATTERN, Long::parseLong);
  private static final ArgumentType<Integer> UNSIGNED_INT_TYPE = PatternArgumentType.ofMatch("unsigned integer", UNSIGNED_INT_PATTERN, Integer::parseUnsignedInt);
  private static final ArgumentType<Long> UNSIGNED_LONG_TYPE = PatternArgumentType.ofMatch("unsigned integer", UNSIGNED_INT_PATTERN, Long::parseUnsignedLong);
  private static final ArgumentType<Float> FLOAT_TYPE = PatternArgumentType.ofMatch("float", FLOAT_PATTERN, Float::parseFloat);
  private static final ArgumentType<Double> DOUBLE_TYPE = PatternArgumentType.ofMatch("float", FLOAT_PATTERN, Double::parseDouble);

  private static final ArgumentType<NamespacedKey> NAMESPACED_KEY_TYPE = new NamespacedKeyArgumentType();
  private static final ArgumentType<Player> PLAYER_TYPE = new PlayerArgumentType();


  // Return the type of the argument type
  public abstract String getType();

  // Parse the argument
  public abstract T parse(String input) throws ArgumentException;

  // Parse the argument, or return the default value if parsing fails
  public T parse(String input, T defaultValue)
  {
    try
    {
      return this.parse(input);
    }
    catch (ArgumentException ex)
    {
      return defaultValue;
    }
  }

  // Parse the next element in the scanner
  public T parse(Scanner scanner) throws ArgumentException
  {
    return scanner.take(input -> this.parse(input), this.getType());
  }

  // Parse the next element in the scanner, or return the default value if parsing fails
  public T parse(Scanner scanner, T defaultValue) throws ArgumentException
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

  // Return suggestions for the specified input
  public Stream<String> suggest(CommandContext context, int argumentIndex)
  {
    return Stream.empty();
  }


  // Return a string argument type
  public static ArgumentType<String> getString()
  {
    return STRING_TYPE;
  }

  // Return a string argument type
  public static ArgumentType<String> getIdentifier()
  {
    return IDENTIFIER_TYPE;
  }

  // Return a namespaced key argument type
  public static ArgumentType<NamespacedKey> getNamespacedKey()
  {
    return NAMESPACED_KEY_TYPE;
  }

  // Return an integer argument type
  public static ArgumentType<Integer> getInt()
  {
    return INT_TYPE;
  }

  // Return a long argument type
  public static ArgumentType<Long> getLong()
  {
    return LONG_TYPE;
  }

  // Return an unsigned integer argument type
  public static ArgumentType<Integer> getUnsignedInt()
  {
    return UNSIGNED_INT_TYPE;
  }

  // Return an unsigned long argument type
  public static ArgumentType<Long> getUnsignedLong()
  {
    return UNSIGNED_LONG_TYPE;
  }

  // Return a float argument type
  public static ArgumentType<Float> getFloat()
  {
    return FLOAT_TYPE;
  }

  // Return a double argument type
  public static ArgumentType<Double> getDouble()
  {
    return DOUBLE_TYPE;
  }

  // Return an enum argument type
  public static <E extends Enum<E>> ArgumentType<E> getEnum(Class<E> enumType)
  {
    return new EnumArgumentType<>(enumType);
  }

  // Return an enum set argument type
  public static <E extends Enum<E>> ArgumentType<EnumSet<E>> getEnumSet(Class<E> enumType, String delimiter)
  {
    return new EnumSetArgumentType<>(enumType, delimiter);
  }

  // Return a player argument type
  public static ArgumentType<Player> getPlayer()
  {
    return PLAYER_TYPE;
  }
  
  // Return a material argument type
  public static ArgumentType<Material> getMaterial(MaterialFilter filter)
  {
    return new MaterialArgumentType(filter);
  }

  // Return a location argument type supplier
  public static LocationArgumentTypeSupplier createLocation(Location origin)
  {
    return new LocationArgumentTypeSupplier(origin);
  }
}
