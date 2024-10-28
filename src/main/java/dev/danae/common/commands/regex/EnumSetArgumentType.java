package dev.danae.common.commands.regex;

import dev.danae.common.commands.ArgumentException;
import dev.danae.common.commands.ArgumentTypeMismatchException;
import java.util.EnumSet;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;


public class EnumSetArgumentType<E extends Enum<E>> extends PatternListArgumentType<E, EnumSet<E>>
{
  // The enum type for the argument type
  private final Class<E> enumType;


  // Constructor
  public EnumSetArgumentType(Class<E> enumType, String delimiter)
  {
    super(String.format("%s enum", enumType.getSimpleName()), IDENTIFIER_PATTERN, delimiter, Collectors.toCollection(() -> EnumSet.noneOf(enumType)));

    this.enumType = enumType;
  }


  // Parse the matched argument
  @Override
  public E parse(MatchResult m) throws ArgumentException
  {
    var input = m.group();

    try
    {
      return Enum.valueOf(this.enumType, input.toUpperCase());
    }
    catch (IllegalArgumentException ex)
    {
      throw new ArgumentTypeMismatchException(this, input);
    }
  }
}
