package dev.danae.common.commands.arguments;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Stream;
import dev.danae.common.commands.Suggestion;


public interface PatternListArgumentType<T, C> extends StringListArgumentType<C>
{
  // Return the pattern for the argument type
  public Pattern getPattern();

  // Return the collector for the argument type
  public Collector<T, ?, C> getCollector();

  // Parse an argument from the specified match result
  public abstract T parseFromMatchResult(MatchResult matchResult) throws ArgumentException;
  

  // Parse the argument from the specified iterable of strings
  public default C parseFromStringList(Iterable<String> inputs) throws ArgumentException
  {
    Stream.Builder<T> builder = Stream.builder();

    for (var input : inputs)
    {
      try
      {
        var m = this.getPattern().matcher(input);
        if (!m.matches())
          throw new ArgumentTypeMismatchException(this, input);
        builder.add(this.parseFromMatchResult(m.toMatchResult()));
      }
      catch (IllegalArgumentException ex)
      {
        throw new ArgumentTypeMismatchException(this, input, ex); 
      }
    }

    return builder.build().collect(this.getCollector());
  }


  // Return a pattern list argument type using the specified match result parser
  public static <T, C> PatternListArgumentType<T, C> of(Class<C> type, String typeName, Pattern pattern, String delimiter, ArgumentTypeFunction<MatchResult, T> parser, Collector<T, ?, C> collector, Stream<String> suggestions)
  {
    return new PatternListArgumentType<T, C>()
    {
      @Override
      public Class<C> getType()
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
      public Collector<T, ?, C> getCollector()
      {
        return collector;
      }

      @Override
      public String getDelimiter()
      {
        return delimiter;
      }

      @Override
      public T parseFromMatchResult(MatchResult matchResult) throws ArgumentException
      {
        return parser.apply(matchResult, this);
      }

      @Override
      public Stream<String> suggestFromStringList(String input)
      {
        return Suggestion.find(input, suggestions);
      }
    };
  }

  // Return a pattern list argument type using the specified matcher group as argument
  public static <T, C> PatternListArgumentType<T, C> ofGroup(Class<C> type, String typeName, Pattern pattern, String delimiter, int group, ArgumentTypeFunction<String, T> parser, Collector<T, ?, C> collector, Stream<String> suggestions)
  {
    return of(type, typeName, pattern, delimiter, (m, t) -> parser.apply(m.group(group), t), collector, suggestions);
  }

  // Return a pattern list argument type using the whole match as argument
  public static <T, C> PatternListArgumentType<T, C> ofMatch(Class<C> type, String typeName, Pattern pattern, String delimiter, ArgumentTypeFunction<String, T> parser, Collector<T, ?, C> collector, Stream<String> suggestions)
  {
    return ofGroup(type, typeName, pattern, delimiter, 0, parser, collector, suggestions);
  }
}
