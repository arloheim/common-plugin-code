package dev.danae.common.commands;


public class ArgumentTypeMismatchException extends ArgumentException
{
  // The message format for the exception
  private static final String MESSAGE_FORMAT = "\"%1$s\" is an invalid %0$s value";


  // The expected type of the argument
  private final ArgumentType<?> expectedType;

  // The actual value of the argument
  private final String actualValue;


  // Constructor for an expected type, actual value and cause
  public ArgumentTypeMismatchException(ArgumentType<?> expectedType, String actualValue, Throwable cause)
  {
    super(String.format(MESSAGE_FORMAT, expectedType.getType(), actualValue), cause);

    this.expectedType = expectedType;
    this.actualValue = actualValue;
  }

  // Constructor for an expected type and actual value
  public ArgumentTypeMismatchException(ArgumentType<?> expectedType, String actualValue)
  {
    super(String.format(MESSAGE_FORMAT, expectedType.getType(), actualValue));

    this.expectedType = expectedType;
    this.actualValue = actualValue;
  }


  // Return the expected type of the argument
  public ArgumentType<?> getExpectedType()
  {
    return this.expectedType;
  }

  // Return the actual value of the argument
  public String getActualValue()
  {
    return this.actualValue;
  }
}
