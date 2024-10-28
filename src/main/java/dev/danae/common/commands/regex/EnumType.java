package dev.danae.common.commands.regex;

import dev.danae.common.commands.ArgumentException;
import dev.danae.common.commands.ArgumentTypeMismatchException;
import java.util.regex.MatchResult;


public class EnumType<T extends Enum<T>> extends PatternType<T>
{
  // The enum type for the argument type
  private final Class<T> enumType;


  // Constructor
  public EnumType(Class<T> enumType)
  {
    super(String.format("%s enum", enumType.getSimpleName()), IDENTIFIER_PATTERN);

    this.enumType = enumType;
  }


  // Parse the matched argument
  @Override
  public T parse(MatchResult m) throws ArgumentException
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
