package dev.danae.common.commands;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import dev.danae.common.commands.arguments.Scanner;
import dev.danae.common.util.Cuboid;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public class CommandContext
{  
  // The command that is being executed
  private final Command command;
  
  // The arguments of the command that is being executed
  private final String[] arguments;
  
  // The sender of the command that is being executed
  private final CommandSender sender;
  
  
  // Constructor
  public CommandContext(Command command, String[] arguments, CommandSender sender)
  {
    this.command = command;
    this.arguments = arguments;
    this.sender = sender;
  }
  
  
  // Return the command that is being executed
  public Command getCommand()
  {
    return this.command;
  }
  
  // Return the arguments of the command that is being executed
  public String[] getArguments()
  {
    return this.arguments;
  }

  // Return the joined arguments of the command that is being executed starting from the specified index
  public String getJoinedArguments(int startIndex)
  {
    return String.join(" ", Arrays.stream(this.arguments).skip(startIndex).toList());
  }

  // Return the joined arguments of the command that is being executed
  public String getJoinedArguments()
  {
    return this.getJoinedArguments(0);
  }
  
  // Return a scanner over the arguments of the context
  public Scanner getArgumentsScanner()
  {
    return new Scanner(this.arguments);
  }
  
  // Return the argument of the context with the specified index
  public String getArgument(int index)
  {
    return index >= 0 && index < this.arguments.length ? this.arguments[index] : "";
  }

  // Return the argument of the context with the specified index starting at the end
  public String getLastArgument(int index)
  {
    return index >= 0 && index < this.arguments.length ? this.arguments[this.arguments.length - index - 1] : "";
  }

  // Return the index of the first argument that matches the specified predicate
  public int findArgumentIndex(Predicate<String> predicate)
  {
    for (var i = 0; i < this.arguments.length; i ++)
    {
      if (predicate.test(this.getArgument(i)))
        return i;
    }
    return -1;
  }
  
  // Return the index of the first argument that equals the specified string
  public int findArgumentIndex(String other)
  {
    return this.findArgumentIndex(s -> s.equals(other));
  }

  // Return the index of the last argument that matches the specified predicate starting at the end
  public int findLastArgumentIndex(Predicate<String> predicate)
  {
    for (var i = 0; i < this.arguments.length; i ++)
    {
      if (predicate.test(this.getLastArgument(i)))
        return i;
    }
    return -1;
  }

  // Return the index of the last argument that equals the specified string starting at the end
  public int findLastArgumentIndex(String other)
  {
    return this.findLastArgumentIndex(s -> s.equals(other));
  }

  // Return the number of arguments of the contact
  public int getArgumentsCount()
  {
    return this.arguments.length;
  }
  
  // Return if the context has exactly the specified number of arguments
  public boolean hasArgumentsCount(int length)
  {
    return this.arguments.length == length;
  }
  
  // Return if the context has at least the specified number of arguments
  public boolean hasAtLeastArgumentsCount(int length)
  {
    return this.arguments.length >= length;
  }
  
  // Return if the context has at most the specified number of arguments
  public boolean hasAtMostArgumentsCount(int length)
  {
    return this.arguments.length <= length;
  }
  
  // Return the sender of the command that is being executed
  public CommandSender getSender()
  {
    return this.sender;
  }
  
  
  // Assert if the sender is a console sender
  public ConsoleCommandSender assertSenderIsConsole() throws CommandException
  {
    if (this.sender instanceof ConsoleCommandSender console)
      return console;
    else
      throw new CommandException("This command must be executed by the console");
  }
  
  // Assert if the sender is a player sender
  public Player assertSenderIsPlayer() throws CommandException
  {
    if (this.sender instanceof Player player)
      return player;
    else
      throw new CommandException("This command must be executed by a player");
  }

  // Assert if the sender is a player sender in the specified game mode
  public Player assertSenderIsPlayerWithGameMode(GameMode gameMode) throws CommandException
  {
    if (this.sender instanceof Player player && player.getGameMode() == gameMode)
      return player;
    else
      throw new CommandException(String.format("This command must be executed by a player in %s game mode", gameMode.name().toLowerCase()));
  }
  
  // Assert if the sender is a conversable sender
  public Conversable assertSenderIsConversable() throws CommandException
  {
    if (this.sender instanceof Conversable conversable)
      return conversable;
    else
      throw new CommandException("This command must be executed by a player or the console");
  }
  
  // Assert if the sender has a location
  public Location assertSenderHasLocation() throws CommandException
  {
    if (this.sender instanceof BlockCommandSender blockCommandSender)
      return blockCommandSender.getBlock().getLocation();
    if (this.sender instanceof Entity entity)
      return entity.getLocation();
    else
      throw new CommandException("This command must be executed by a block or entity");
  }
  
  // Assert if the sender has sufficient permissions
  public void assertSenderHasPermissions(List<String> permissions) throws CommandException
  {
    if (!permissions.stream().allMatch(p -> this.sender.hasPermission(p)))
      throw new CommandException("You don't have sufficient permission to execute this command");
  }
  public void assertSenderHasPermissions(String... permissions) throws CommandException
  {
    this.assertSenderHasPermissions(Arrays.asList(permissions));
  }
  
  
  // Return the nearest player to the sender
  public Player nearestPlayer(int radius) throws CommandException
  {
    var senderLocation = this.assertSenderHasLocation();
    return Cuboid.around(senderLocation, radius).findNearestEntityToCenter(Player.class);
  }
  
  // Return the nearest player to the sender, or the sender itself if it is a player
  public Player nearestPlayerOrSender(int radius) throws CommandException
  {
    if (this.sender instanceof Player player)
      return player;
    else
      return this.nearestPlayer(radius);
  }
  
  
  // Send a message to the sender of the command
  public void sendMessage(Component message)
  {
    this.sender.sendMessage(message);
  }
  
  // Send a plain message to the sender of the command
  public void sendPlainMessage(String message)
  {
    this.sender.sendPlainMessage(message);
  }
  
  // Send a rich message to the sender of the command
  public void sendRichMessage(String message)
  {
    this.sender.sendRichMessage(message);
  }
  
  
  // Return a new context with the arguments of the context sliced within the specified range
  public CommandContext withSlicedArguments(int from, int to)
  {
    return new CommandContext(this.command, Arrays.copyOfRange(this.arguments, from, to), this.sender);
  }
  
  // Return a new context with the arguments of the context sliced from the specified start
  public CommandContext withSlicedArguments(int from)
  {
    return this.withSlicedArguments(from, this.arguments.length);
  }
}
