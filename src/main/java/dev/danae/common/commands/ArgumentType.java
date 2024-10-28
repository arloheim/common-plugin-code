package dev.danae.common.commands;

import dev.danae.common.commands.regex.PatternArgumentType;
import java.util.regex.Pattern;
import java.util.stream.Stream;


public abstract class ArgumentType<T>
{
  // Patterns for parsing argument types
  protected static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
  protected static final Pattern NAMESPACED_KEY_PATTERN = Pattern.compile("[a-zA-Z0-9._-]+:[a-zA-Z0-9._\\/-]+");
  protected static final Pattern INT_PATTERN = Pattern.compile("0|-?[1-9][0-9]*");
  protected static final Pattern UNSIGNED_INT_PATTERN = Pattern.compile("0|[1-9][0-9]*");
  protected static final Pattern FLOAT_PATTERN = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");

  // Predefined argument types
  public static final ArgumentType<String> STRING = PatternArgumentType.ofMatch("string", null, (s, t) -> s);
  public static final ArgumentType<String> IDENTIFIER = PatternArgumentType.ofMatch("identifier", IDENTIFIER_PATTERN, (s, t) -> s);
  public static final ArgumentType<Integer> INT = PatternArgumentType.ofMatch("integer", INT_PATTERN, (s, t) -> Integer.parseInt(s));
  public static final ArgumentType<Long> LONG = PatternArgumentType.ofMatch("integer", INT_PATTERN, (s, t) -> Long.parseLong(s));
  public static final ArgumentType<Integer> UNSIGNED_INT = PatternArgumentType.ofMatch("unsigned integer", UNSIGNED_INT_PATTERN, (s, t) -> Integer.parseUnsignedInt(s));
  public static final ArgumentType<Long> UNSIGNED_LONG = PatternArgumentType.ofMatch("unsigned integer", UNSIGNED_INT_PATTERN, (s, t) -> Long.parseUnsignedLong(s));
  public static final ArgumentType<Float> FLOAT = PatternArgumentType.ofMatch("float", FLOAT_PATTERN, (s, t) -> Float.parseFloat(s));
  public static final ArgumentType<Double> DOUBLE = PatternArgumentType.ofMatch("float", FLOAT_PATTERN, (s, t) -> Double.parseDouble(s));


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
