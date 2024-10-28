package dev.danae.common.commands;

import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Scanner
{  
  // The arguments that will be iterated over
  private final String[] arguments;
  
  // The current index of the scanner
  private int index;
  
  
  // Constructor
  public Scanner(String[] arguments)
  {
    this.arguments = arguments;
    this.index = -1;
  }
  
  
  // Return if the scanner reached the end of the tokens at the next index
  public boolean isAtEnd()
  {
    return this.isAtEnd(1);
  }
  
  // Return if the scanner reached the end of the tokens at the index with the specified lookahead
  public boolean isAtEnd(int lookahead)
  {
    return this.index + lookahead >= this.arguments.length;
  }

  // Return the token that has just been scanned
  public String current()
  {
    return this.index >= 0  ? this.arguments[this.index] : null;
  }

  
  // Return the next element in the scanner as a string
  public String take(String expectedType) throws ArgumentException
  {
    if (this.isAtEnd())
      throw new ArgumentException(String.format("Expected %s, but reached end of arguments", expectedType));
    
    this.index ++;
    return this.current();
  }
  
  // Return the next specified amount of elements in the scanner as a stream of strings
  public Stream<String> takeMany(int amount, String expectedType) throws ArgumentException
  {
    Stream.Builder<String> builder = Stream.builder();
    for (var i = 0; i < amount; i ++)      
      builder.add(this.take(expectedType));
    return builder.build();
  }

  // Return the next specified amount of elements in the scanner as a stream of strings
  public String takeMany(int amount, String delimiter, String expectedType) throws ArgumentException
  {
    return this.takeMany(amount, expectedType).collect(Collectors.joining(delimiter));
  }

  // Return the remaining elements in the scanner as a stream of strings
  public Stream<String> takeRemaining(String expectedType) throws ArgumentException
  {
    Stream.Builder<String> builder = Stream.builder();
    while (!this.isAtEnd())
      builder.add(this.take(expectedType));
    return builder.build();
  }

  // Return the remaining elements in the scanner as a stream of strings
  public String takeRemaining(String delimiter, String expectedType) throws ArgumentException
  {
    return this.takeRemaining(expectedType).collect(Collectors.joining(delimiter));
  }

  
  // Return the next element in the scanner and parse it using the specified function
  public <T> T take(ArgumentFunction<String, T> function, String expectedType) throws ArgumentException
  {
    var previousIndex = this.index;
    try
    {
      return function.apply(this.take(expectedType));
    }
    catch (ArgumentException ex)
    {
      this.index = previousIndex;
      throw ex;
    }
  }

  // Return the next specified amount of elements in the scanner and parse each using the specified function
  public <T> Stream<T> takeMany(int amount, ArgumentFunction<String, T> function, String expectedType) throws ArgumentException
  {    
    var previousIndex = this.index;
    try
    {
      Stream.Builder<T> builder = Stream.builder();
      for (var i = 0; i < amount; i ++)      
        builder.add(function.apply(this.take(expectedType)));
      return builder.build();
    }
    catch (ArgumentException ex)
    {
      this.index = previousIndex;
      throw ex;
    }
  }

  // Return the next specified amount of elements in the scanner
  public <T> T takeMany(int amount, String delimiter, ArgumentFunction<String, T> function, String expectedType) throws ArgumentException
  {    
    var previousIndex = this.index;
    try
    {
      return function.apply(this.takeMany(amount, delimiter, expectedType));
    }
    catch (ArgumentException ex)
    {
      this.index = previousIndex;
      throw ex;
    }
  }

  // Return the remaining elements in the scanner and parse each using the specified function
  public <T> Stream<T> takeRemaining(ArgumentFunction<String, T> function, String expectedType) throws ArgumentException
  {    
    var previousIndex = this.index;
    try
    {
      Stream.Builder<T> builder = Stream.builder();
      while (!this.isAtEnd())
        builder.add(function.apply(this.take(expectedType)));
      return builder.build();
    }
    catch (ArgumentException ex)
    {
      this.index = previousIndex;
      throw ex;
    }
  }

  // Return the remaining elements in the scanner
  public <T> T takeRemaining(String delimiter, ArgumentFunction<String, T> function, String expectedType) throws ArgumentException
  {    
    var previousIndex = this.index;
    try
    {
      return function.apply(this.takeRemaining(delimiter, expectedType));
    }
    catch (ArgumentException ex)
    {
      this.index = previousIndex;
      throw ex;
    }
  }
}
