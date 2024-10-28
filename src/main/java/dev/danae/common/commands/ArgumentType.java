package dev.danae.common.commands;

import dev.danae.common.commands.material.MaterialFilter;
import dev.danae.common.commands.material.MaterialType;
import dev.danae.common.commands.player.PlayerType;
import dev.danae.common.commands.regex.NamespacedKeyType;
import dev.danae.common.commands.regex.PatternType;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;


public abstract class ArgumentType<T>
{
  // Predefined argument types
  public static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
  public static final Pattern NAMESPACED_KEY_PATTERN = Pattern.compile("[a-zA-Z0-9._-]+:[a-zA-Z0-9._\\/-]+");
  public static final Pattern INT_PATTERN = Pattern.compile("0|-?[1-9][0-9]*");
  public static final Pattern UNSIGNED_INT_PATTERN = Pattern.compile("0|[1-9][0-9]*");
  public static final Pattern FLOAT_PATTERN = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");

  // Predefined simple argument types
  //public static final ArgumentType<String> STRING = s -> s;

  // Predefined value argument types
  public static final ArgumentType<String> IDENTIFIER = PatternType.ofMatch("identifier", IDENTIFIER_PATTERN, s -> s);
  public static final ArgumentType<Integer> INT = PatternType.ofMatch("integer", INT_PATTERN, Integer::parseInt);
  public static final ArgumentType<Long> LONG = PatternType.ofMatch("integer", INT_PATTERN, Long::parseLong);
  public static final ArgumentType<Integer> UNSIGNED_INT = PatternType.ofMatch("unsigned integer", UNSIGNED_INT_PATTERN, Integer::parseUnsignedInt);
  public static final ArgumentType<Long> UNSIGNED_LONG = PatternType.ofMatch("unsigned integer", UNSIGNED_INT_PATTERN, Long::parseUnsignedLong);
  public static final ArgumentType<Float> FLOAT = PatternType.ofMatch("float", FLOAT_PATTERN, Float::parseFloat);
  public static final ArgumentType<Double> DOUBLE = PatternType.ofMatch("float", FLOAT_PATTERN, Double::parseDouble);

  // Predefined compound argument types
  public static final ArgumentType<NamespacedKey> NAMESPACED_KEY = new NamespacedKeyType();
  public static final ArgumentType<Player> PLAYER = new PlayerType();
  public static final ArgumentType<Material> MATERIAL = new MaterialType(MaterialFilter.ALL);
  public static final ArgumentType<Material> BLOCK_MATERIAL = new MaterialType(MaterialFilter.BLOCKS);
  public static final ArgumentType<Material> GRAVITY_BLOCK_MATERIAL = new MaterialType(MaterialFilter.GRAVITY_BLOCKS);
  public static final ArgumentType<Material> ITEM_MATERIAL = new MaterialType(MaterialFilter.ITEMS);


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
}
