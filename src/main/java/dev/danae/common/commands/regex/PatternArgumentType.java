package dev.danae.common.commands.regex;

import java.util.regex.Pattern;
import dev.danae.common.commands.ArgumentBiFunction;
import dev.danae.common.commands.ArgumentException;
import dev.danae.common.commands.ArgumentFunction;
import dev.danae.common.commands.ArgumentType;
import dev.danae.common.commands.ArgumentTypeMismatchException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;


public abstract class PatternArgumentType<T> extends ArgumentType<T>
{
  // The type for the argument type
  protected final String type;

  // The pattern for the argument type
  private final Pattern pattern;


  // Constructor
  public PatternArgumentType(String type, Pattern pattern)
  {
    this.type = type;
    this.pattern = pattern;
  }


  // Parse the argument from the specified match result
  public abstract T parse(MatchResult matchResult) throws ArgumentException;


  // Return the type of the argument type
  @Override
  public String getType()
  {
    return this.type;
  }

  // Parse the argument
  @Override
  public T parse(String input) throws ArgumentException
  {
    try
    {
      if (pattern == null)
        return this.parse(new DefaultMatchResult(input));

      var m = this.pattern.matcher(input);
      if (!m.matches())
        throw new ArgumentTypeMismatchException(this, input);
      return this.parse(m.toMatchResult());
    }
    catch (IllegalArgumentException ex)
    {
      throw new ArgumentTypeMismatchException(this, input, ex); 
    }
  }


  // Return a pattern argument type using the specified match result parser
  public static <T> PatternArgumentType<T> of(String type, Pattern pattern, ArgumentBiFunction<MatchResult, ArgumentType<T>, T> parser)
  {
    return new PatternArgumentType<T>(type, pattern)
    {
      @Override
      public T parse(MatchResult matchResult) throws ArgumentException
      {
        return parser.apply(matchResult, this);
      }
    };
  }

  // Return a pattern argument type using the specified matcher group as argument
  public static <T> PatternArgumentType<T> ofGroup(String type, Pattern pattern, int group, ArgumentBiFunction<String, ArgumentType<T>, T> parser)
  {
    return of(type, pattern, (m, t) -> parser.apply(m.group(group), t));
  }

  // Return a pattern argument type using the whole match as argument
  public static <T> PatternArgumentType<T> ofMatch(String type, Pattern pattern, ArgumentBiFunction<String, ArgumentType<T>, T> parser)
  {
    return ofGroup(type, pattern, 0, parser);
  }
}
