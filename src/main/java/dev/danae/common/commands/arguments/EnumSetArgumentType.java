package dev.danae.common.commands.arguments;

import com.google.common.reflect.TypeToken;
import dev.danae.common.commands.Suggestion;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


final class EnumSetArgumentType<E extends Enum<E>> implements PatternListArgumentType<E, EnumSet<E>>
{
  // The enum type for the argument type
  private final Class<E> type;

  // The delimiter for the argument type
  private final String delimiter;


  // Constructor
  public EnumSetArgumentType(Class<E> type, String delimiter)
  {
    this.type = type;
    this.delimiter = delimiter;
  }
  

  // Return the type of the argument type
  @Override
  public Class<? super EnumSet<E>> getType()
  {
    return new TypeToken<EnumSet<E>>() {}.getRawType();
  }

  // Return the type name for the argument type
  @Override
  public String getTypeName()
  {
    return String.format("%s enum set", type.getSimpleName());
  }

  // Return the pattern for the argument type
  @Override
  public Pattern getPattern()
  {
    return IDENTIFIER_PATTERN;
  }

  // Return the delimiter for the argument type
  @Override
  public String getDelimiter()
  {
    return this.delimiter;
  }

  // Parse an enum set from the specified match result
  @Override
  public E parseFromMatchResult(MatchResult m) throws ArgumentException
  {
    var input = m.group();

    try
    {
      return Enum.valueOf(this.type, input.toUpperCase());
    }
    catch (IllegalArgumentException ex)
    {
      throw new ArgumentTypeMismatchException(this, input);
    }
  }

  // Collect the stream of parsed values
  @Override
  public EnumSet<E> collect(Stream<E> stream)
  {
    return stream.collect(Collectors.toCollection(() -> EnumSet.noneOf(type)));
  }

  // Return suggestions for the specified string part
  @Override
  public Stream<String> suggestFromStringPart(String input)
  {
    return Suggestion.find(input, Arrays.stream(this.type.getEnumConstants())
      .map(e -> e.name().toLowerCase()));
  }
}
