package dev.danae.common.commands;


public class ArgumentException extends CommandException
{
  // Constructor for a message and cause
  public ArgumentException(String message, Throwable cause)
  {
    super(message, cause);
  }

  // Constructor for a message
  public ArgumentException(String message)
  {
    super(message);
  }
}
