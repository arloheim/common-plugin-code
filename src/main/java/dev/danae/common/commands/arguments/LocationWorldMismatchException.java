package dev.danae.common.commands.arguments;

import org.bukkit.entity.Player;


public class LocationWorldMismatchException extends ArgumentException
{
  // The message format for the exception
  private static final String MESSAGE_FORMAT = "%0$s is not in the same world as the origin location";


  // The player of the argument
  private final Player player;


  // Constructor for a player and cause
  public LocationWorldMismatchException(Player player, Throwable cause)
  {
    super(String.format(MESSAGE_FORMAT, player.getName()), cause);

    this.player = player;
  }

  // Constructor for a player
  public LocationWorldMismatchException(Player player)
  {
    super(String.format(MESSAGE_FORMAT, player.getName()));

    this.player = player;
  }


  // Return the player of the argument
  public Player getPlayer()
  {
    return this.player;
  }
}
