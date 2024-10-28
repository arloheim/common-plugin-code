package dev.danae.common.commands.regex;

import dev.danae.common.commands.ArgumentException;
import dev.danae.common.commands.ArgumentFunction;
import java.util.regex.MatchResult;


public abstract class IdentifierType<T> extends PatternType<T>
{
  // Constructor
  public IdentifierType(String type)
  {
    super(type, IDENTIFIER_PATTERN);
  }


  // Return a pattern argument type using the specified match result parser
  public static <T> IdentifierType<T> of(String type, ArgumentFunction<MatchResult, T> parser)
  {
    return new IdentifierType<T>(type)
    {
      @Override
      public T parse(MatchResult matchResult) throws ArgumentException
      {
        return parser.apply(matchResult);
      }
    };
  }
}
