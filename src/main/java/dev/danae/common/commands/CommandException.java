package dev.danae.common.commands;


public class CommandException extends Exception
{
  // Constructor for a message and cause
  public CommandException(String message, Throwable cause)
  {
    super(message, cause);
  }

  // Constructor for a message
  public CommandException(String message)
  {
    super(message);
  }
}
