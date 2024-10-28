package dev.danae.common.commands.regex;

import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Stream;
import dev.danae.common.commands.ArgumentException;
import dev.danae.common.commands.ArgumentFunction;
import dev.danae.common.commands.ArgumentType;
import dev.danae.common.commands.ArgumentTypeMismatchException;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.MatchResult;


public abstract class PatternListArgumentType<T, C extends Collection<T>> extends ArgumentType<C>
{
  // The type for the argument type
  protected final String type;

  // The pattern for the argument type
  private final Pattern pattern;

  // The delimiter for the argument type
  protected final String delimiter;

  // The collector for the argument type
  protected final Collector<T, ?, C> collector;


  // Constructor
  public PatternListArgumentType(String type, Pattern pattern, String delimiter, Collector<T, ?, C> collector)
  {
    this.type = type;
    this.pattern = pattern;
    this.delimiter = delimiter;
    this.collector = collector;
  }


  // Parse the argument component from the specified match result
  public abstract T parse(MatchResult matchResult) throws ArgumentException;


  // Return the type of the argument type
  @Override
  public String getType()
  {
    return this.type;
  }

  // Parse the argument from the specified input
  @Override
  public C parse(String input) throws ArgumentException
  {
    return this.parse(Arrays.asList(input.split(this.delimiter)));
  }

  // Parse the argument from the specified iterable of inputs
  public C parse(Iterable<String> inputs) throws ArgumentException
  {
    Stream.Builder<T> builder = Stream.builder();

    for (var input : inputs)
    {
      try
      {
        var m = this.pattern.matcher(input);
        if (!m.matches())
          throw new ArgumentTypeMismatchException(this, input);
        builder.add(this.parse(m.toMatchResult()));
      }
      catch (IllegalArgumentException ex)
      {
        throw new ArgumentTypeMismatchException(this, input, ex); 
      }
    }

    return builder.build().collect(this.collector);
  }


  // Return a pattern list argument type using the specified match result parser
  public static <T, C extends Collection<T>> PatternListArgumentType<T, C> of(String type, Pattern pattern, String delimiter, ArgumentFunction<MatchResult, T> parser, Collector<T, ?, C> collector)
  {
    return new PatternListArgumentType<T, C>(type, pattern, delimiter, collector)
    {
      @Override
      public T parse(MatchResult matchResult) throws ArgumentException
      {
        return parser.apply(matchResult);
      }
    };
  }

  // Return a pattern list argument type using the specified matcher group as function argument
  public static <T, C extends Collection<T>> PatternListArgumentType<T, C> ofGroup(String type, Pattern pattern, String delimiter, int group, ArgumentFunction<String, T> parser, Collector<T, ?, C> collector)
  {
    return of(type, pattern, delimiter, m -> parser.apply(m.group(group)), collector);
  }

  // Return a pattern list argument type using the whole match as function argument
  public static <T, C extends Collection<T>> PatternListArgumentType<T, C> ofMatch(String type, Pattern pattern, String delimiter, ArgumentFunction<String, T> parser, Collector<T, ?, C> collector)
  {
    return ofGroup(type, pattern, delimiter, 0, parser, collector);
  }
}
