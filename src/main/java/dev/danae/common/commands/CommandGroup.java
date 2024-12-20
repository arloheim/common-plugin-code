package dev.danae.common.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.bukkit.plugin.Plugin;


public class CommandGroup extends Command
{  
  // Constructor
  public CommandGroup(String... permissions)
  {
    super(permissions);
  }
  
  // Constructor without permissions
  public CommandGroup()
  {
  }
  
  // Map of subcommands
  private final Map<String, Command> subcommands = new HashMap<>();
  private Command emptySubcommand = null;
  
  
  // Register a subcommand
  public CommandGroup registerSubcommand(String name, Command command)
  {
    this.subcommands.put(name, command);
    return this;
  }
  
  // Unregister a subcommand
  public CommandGroup unregisterSubcommand(String name)
  {
    this.subcommands.remove(name);
    return this;
  }

  // Register a subcommand for when no other subcommand has been specified
  public CommandGroup registerEmptySubcommand(Command command)
  {
    this.emptySubcommand = command;
    return this;
  }

  // Register a subcommand for when no other subcommand has been specified to an existing subcommand
  public CommandGroup registerEmptySubcommand(String name)
  {
    if (!this.subcommands.containsKey(name))
      throw new IllegalArgumentException(String.format("%s is not an existing subcommand", name));

    return this.registerEmptySubcommand(this.subcommands.get(name));
  }
  
  // Unregister a subcommand for when no other subcommand has been specified
  public CommandGroup unregisterEmptySubcommand()
  {
    this.emptySubcommand = null;
    return this;
  }
  
  
  // Return a subcommand for the specified name
  public Command getSubcommand(String name)
  {
    return this.subcommands.entrySet().stream()
      .filter(e -> e.getKey().equalsIgnoreCase(name))
      .map(e -> e.getValue())
      .findFirst()
      .orElse(null);
  }
  
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    // Check if there is a subcommand that matches
    var handler = context.hasAtLeastArgumentsCount(1) ? this.getSubcommand(context.getArgument(0)) : null;
    if (handler != null)
    {
      // Handle the subcommand
      handler.handle(context.withSlicedArguments(1));
    }
    else if (this.emptySubcommand != null)
    {
      // Handle the empty subcommand
      this.emptySubcommand.handle(context);
    }
    else
    {
      // No valid subcommand
      throw new CommandException("You must provide a valid subcommand");
    }
  }
  
  // Handle tab completion of the command
  @Override
  public Stream<String> suggest(CommandContext context)
  {
    // Check if the arguments provide a subcommand
    if (!context.hasAtLeastArgumentsCount(1))
      return null;
    
    // If there only is one argument, then tab complete the name of the subcommand
    if (context.hasArgumentsCount(1))
    {
      var arg = context.getArgument(0);
      
      // Create the stream of the subcommands
      var stream = this.subcommands.keySet().stream();

      // Add the tab completion for the empty subcommand if applicable
      if (this.emptySubcommand != null)
        stream = Stream.concat(stream, this.emptySubcommand.suggest(context));

      // Filter the stream based on the argument
      return Suggestion.find(arg, stream);
    }
      
    // Otherwise, check if there is a subcommand that matches and delegate to that
    var handler = this.getSubcommand(context.getArgument(0));
    if (handler == null)
      return null;
    
    return handler.suggest(context.withSlicedArguments(1));
  }
  
  // Register the command handler as a listener for the specified plugin
  @Override
  public void publishEvents(Plugin plugin)
  {    
    for (var handler : this.subcommands.values())
      handler.publishEvents(plugin);
  }
}
