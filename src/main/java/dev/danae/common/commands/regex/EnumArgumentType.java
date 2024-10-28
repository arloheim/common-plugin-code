package dev.danae.common.commands.regex;

import dev.danae.common.commands.ArgumentException;
import dev.danae.common.commands.ArgumentTypeMismatchException;
import java.util.regex.MatchResult;


public class EnumArgumentType<E extends Enum<E>> extends PatternArgumentType<E>
{
  // The enum type for the argument type
  private final Class<E> enumType;


  // Constructor
  public EnumArgumentType(Class<E> enumType)
  {
    super(String.format("%s enum", enumType.getSimpleName()), IDENTIFIER_PATTERN);

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
