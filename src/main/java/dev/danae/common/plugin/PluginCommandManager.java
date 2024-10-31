package dev.danae.common.plugin;

import dev.danae.common.commands.Command;
import org.bukkit.plugin.java.JavaPlugin;


public interface PluginCommandManager<P extends JavaPlugin> extends PluginComponent<P>
{
  // Publish a command handler
  public default void publishCommand(String name, Command command)
  {
    command.publishCommandHandler(this.getPlugin(), this.getPlugin().getCommand(name));
  }
}
