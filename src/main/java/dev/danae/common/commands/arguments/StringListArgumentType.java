package dev.danae.common.commands.arguments;

import java.util.Arrays;
import java.util.stream.Stream;


public interface StringListArgumentType<T> extends StringArgumentType<T>
{
  // Return the delimiter for the argument type
  public String getDelimiter();
  
  // Parse the argument from the specified iterable of strings
  public T parseFromStringList(Iterable<String> inputs) throws ArgumentException;

  // Return suggestions for the specified string list item
  public abstract Stream<String> suggestFromStringList(String input);


  // Parse an argument from the specified string
  public default T parseFromString(String input) throws ArgumentException
  {
    return this.parseFromStringList(Arrays.asList(input.split(this.getDelimiter())));
  }

  // Return suggestions for the specified string
  public default Stream<String> suggestFromString(String input)
  {
    var lastDelimiterIndex = input.lastIndexOf(this.getDelimiter());
    var lastParts = lastDelimiterIndex > -1 ? input.substring(0, lastDelimiterIndex) : "";
    var currentPart = input.substring(lastParts.length());

    return this.suggestFromStringList(currentPart)
      .map(s -> lastParts + s);
  }
}
