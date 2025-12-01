package dev.danae.common.commands.arguments;

import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import dev.danae.common.commands.Suggestion;


final class EnumArgumentType<E extends Enum<E>> implements PatternArgumentType<E>
{
  // The enum type for the argument type
  private final Class<E> type;


  // Constructor
  public EnumArgumentType(Class<E> type)
  {
    this.type = type;
  }


  // Return the type of the argument type
  @Override
  public Class<E> getType()
  {
    return this.type;
  }
  
  // Return the type name for the argument type
  @Override
  public String getTypeName()
  {
    return String.format("%s enum", type.getSimpleName());
  }

  // Return the pattern for the argument type
  @Override
  public Pattern getPattern()
  {
    return IDENTIFIER_PATTERN;
  }

  // Parse an enum from the specifued match result
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

  // Return suggestions for the specified string
  @Override
  public Stream<String> suggestFromString(String input)
  {
    return Suggestion.find(input, Arrays.stream(this.type.getEnumConstants())
      .map(e -> e.name().toLowerCase()));
  }
}
