package dev.danae.common.commands.arguments;

import java.util.stream.Stream;
import dev.danae.common.commands.CommandContext;


public interface StringArgumentType<T> extends ArgumentType<T>
{
  // Parse the argument from the specified string
  public abstract T parseFromString(String input) throws ArgumentException;

  // Return suggestions for the specified string
  public abstract Stream<String> suggestFromString(String input);


  // Parse an argument from the specified scanner
  public default T parse(Scanner scanner) throws ArgumentException
  {
    return scanner.take(input -> this.parseFromString(input), this.getTypeName());
  }

  // Return suggestions for the specified command context and argument
  public default Stream<String> suggest(CommandContext context, int argumentIndex)
  {
    return this.suggestFromString(context.getArgument(argumentIndex));
  }
}
