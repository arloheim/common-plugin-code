package dev.danae.common.commands;


public class CommandUsageException extends Exception
{
  // Constructor for a cause
  public CommandUsageException(Throwable cause)
  {
    super(cause);
  }

  // Constructor
  public CommandUsageException()
  {
    super();
  }
}
