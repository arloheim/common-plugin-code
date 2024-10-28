package dev.danae.common.commands.regex;

import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Stream;
import dev.danae.common.commands.ArgumentBiFunction;
import dev.danae.common.commands.ArgumentException;
import dev.danae.common.commands.ArgumentFunction;
import dev.danae.common.commands.ArgumentType;
import dev.danae.common.commands.ArgumentTypeMismatchException;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.MatchResult;


public abstract class PatternListArgumentType<T, C> extends ArgumentType<C>
{
  // The type for the argument type
  protected final String type;

  // The pattern for the argument type
  private final Pattern pattern;

  // The delimiter for the argument type
  protected final String delimiter;


  // Constructor
  public PatternListArgumentType(String type, Pattern pattern, String delimiter)
  {
    this.type = type;
    this.pattern = pattern;
    this.delimiter = delimiter;
  }


  // Parse the argument component from the specified match result
  public abstract T parse(MatchResult matchResult) throws ArgumentException;

  // Collect the stream of parsed components
  public abstract C collect(Stream<T> stream);


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

    return this.collect(builder.build());
  }

  // Return a pattern list argument type using the specified match result parser
  public static <T, C> PatternListArgumentType<T, C> of(String type, Pattern pattern, String delimiter, ArgumentBiFunction<MatchResult, ArgumentType<C>, T> parser, Collector<T, ?, C> collector)
  {
    return new PatternListArgumentType<T, C>(type, pattern, delimiter)
    {
      @Override
      public T parse(MatchResult matchResult) throws ArgumentException
      {
        return parser.apply(matchResult, this);
      }

      @Override
      public C collect(Stream<T> stream)
      {
        return stream.collect(collector);
      }
    };
  }

  // Return a pattern list argument type using the specified matcher group as argument
  public static <T, C> PatternListArgumentType<T, C> ofGroup(String type, Pattern pattern, String delimiter, int group, ArgumentBiFunction<String, ArgumentType<C>, T> parser, Collector<T, ?, C> collector)
  {
    return of(type, pattern, delimiter, (m, t) -> parser.apply(m.group(group), t), collector);
  }

  // Return a pattern list argument type using the whole match as argument
  public static <T, C> PatternListArgumentType<T, C> ofMatch(String type, Pattern pattern, String delimiter, ArgumentBiFunction<String, ArgumentType<C>, T> parser, Collector<T, ?, C> collector)
  {
    return ofGroup(type, pattern, delimiter, 0, parser, collector);
  }
}
