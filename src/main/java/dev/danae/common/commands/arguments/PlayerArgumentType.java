package dev.danae.common.commands.arguments;

import dev.danae.common.commands.Suggestion;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerArgumentType implements StringArgumentType<Player> 
{
  // Return the type of the argument type
  @Override
  public Class<Player> getType()
  {
    return Player.class;
  }

  // Return the type name for the argument type
  @Override
  public String getTypeName()
  {
    return "player";
  }

  // Parse a player from the specified string
  @Override
  public Player parseFromString(String input) throws ArgumentException
  {    
    var player = Bukkit.getPlayerExact(input);
    if (player == null)
      throw new ArgumentTypeMismatchException(this, input);
    return player;
  }

  // Return suggestions for the specified input
  @Override
  public Stream<String> suggestFromString(String input)
  {
    return Suggestion.find(input, Bukkit.getOnlinePlayers().stream()
      .map(p -> p.getName()));
  }  
}
