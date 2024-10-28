package dev.danae.common.commands.regex;

import dev.danae.common.commands.ArgumentException;
import dev.danae.common.commands.ArgumentTypeMismatchException;
import java.util.EnumSet;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.checkerframework.checker.units.qual.C;


public class EnumSetArgumentType<E extends Enum<E>> extends PatternListArgumentType<E, EnumSet<E>>
{
  // The enum type for the argument type
  private final Class<E> enumType;


  // Constructor
  public EnumSetArgumentType(Class<E> enumType, String delimiter)
  {
    super(String.format("%s enum", enumType.getSimpleName()), IDENTIFIER_PATTERN, delimiter);

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

  // Collect the stream of parsed components
  @Override
  public EnumSet<E> collect(Stream<E> stream)
  {
    return stream.collect(Collectors.toCollection(() -> EnumSet.noneOf(enumType)));
  }
}
