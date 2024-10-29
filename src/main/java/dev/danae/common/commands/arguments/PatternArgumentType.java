package dev.danae.common.commands.arguments;

import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.regex.MatchResult;


public interface PatternArgumentType<T> extends StringArgumentType<T>
{
  // Return the pattern for the argument type
  public Pattern getPattern();

  // Parse the argument from the specified match result
  public abstract T parseFromMatchResult(MatchResult m) throws ArgumentException;


  // Parse the argument from the specified string
  public default T parseFromString(String input) throws ArgumentException
  {
    try
    {
      if (this.getPattern() == null)
        return this.parseFromMatchResult(new SimpleMatchResult(input));

      var m = this.getPattern().matcher(input);
      if (!m.matches())
        throw new ArgumentTypeMismatchException(this, input);
      return this.parseFromMatchResult(m.toMatchResult());
    }
    catch (IllegalArgumentException ex)
    {
      throw new ArgumentTypeMismatchException(this, input, ex); 
    }
  }


  // Return a pattern argument type using the specified match result parser
  public static <T> PatternArgumentType<T> of(Class<T> type, String typeName, Pattern pattern, ArgumentTypeFunction<MatchResult, T> parser, Stream<String> suggestions)
  {
    return new PatternArgumentType<T>()
    {
      @Override
      public Class<T> getType()
      {
        return type;
      }

      @Override
      public String getTypeName()
      {
        return typeName;
      }

      @Override
      public Pattern getPattern()
      {
        return pattern;
      }

      @Override
      public T parseFromMatchResult(MatchResult matchResult) throws ArgumentException
      {
        return parser.apply(matchResult, this);
      }

      @Override
      public Stream<String> suggestFromString(String input)
      {
        return suggestions;
      }
    };
  }

  // Return a pattern argument type using the specified matcher group as argument
  public static <T> PatternArgumentType<T> ofGroup(Class<T> type, String typeName, Pattern pattern, int group, ArgumentTypeFunction<String, T> parser, Stream<String> suggestions)
  {
    return of(type, typeName, pattern, (m, t) -> parser.apply(m.group(group), t), suggestions);
  }

  // Return a pattern argument type using the whole match as argument
  public static <T> PatternArgumentType<T> ofMatch(Class<T> type, String typeName, Pattern pattern, ArgumentTypeFunction<String, T> parser, Stream<String> suggestions)
  {
    return ofGroup(type, typeName, pattern, 0, parser, suggestions);
  }
}
