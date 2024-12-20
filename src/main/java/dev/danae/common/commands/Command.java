package dev.danae.common.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public abstract class Command implements CommandExecutor, TabCompleter, Listener
{
  // The required permissions to execute this command
  private final List<String> permissions;
  
  
  // Constructor
  public Command(String... permissions)
  {    
    this.permissions = Arrays.asList(permissions);
  }
  
  // Constructor without permissions
  public Command()
  {    
    this.permissions = Collections.emptyList();
  }
  
  
  // Register the command handler to the specified plugin command
  public void publishCommandHandler(Plugin plugin, PluginCommand pluginCommand)
  {
    // Set the command executor
    pluginCommand.setExecutor(this);
    pluginCommand.setTabCompleter(this);
    
    // Set the command listener
    this.publishEvents(plugin);
  }

  // Register the command handler to the plugin command with the specified name
  public void publishCommandHandler(JavaPlugin plugin, String pluginCommandName)
  {
    this.publishCommandHandler(plugin, plugin.getCommand(pluginCommandName));
  }
  
  // Register the command handler as a listener for the specified plugin
  public void publishEvents(Plugin plugin)
  {
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }
  
  
  // Handle the command
  public abstract void handle(CommandContext context) throws CommandException, CommandUsageException;
  
  // Return suggestions for the specified command context
  public abstract Stream<String> suggest(CommandContext context);
  
  
  // Event handler for a command event
  @Override
  public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
  {
    try
    {
      var context = new CommandContext(command, args, sender);
      context.assertSenderHasPermissions(this.permissions);
      this.handle(context);
      
      return true;
    }
    catch (CommandUsageException ex)
    {
      return false;
    }
    catch (CommandException ex)
    {
      sender.sendRichMessage(String.format("<red>%s</red>", ex.getMessage()));
      return true;
    }
  }
  
  // Event handler for a tab complete event
  @Override
  public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
  {
    var context = new CommandContext(command, args, sender);
    return this.suggest(context).toList();
  }
}
