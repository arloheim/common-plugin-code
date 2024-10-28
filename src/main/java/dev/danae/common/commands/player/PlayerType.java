package dev.danae.common.commands.player;

import dev.danae.common.commands.ArgumentException;
import dev.danae.common.commands.ArgumentType;
import dev.danae.common.commands.ArgumentTypeMismatchException;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.Suggestion;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerType extends ArgumentType<Player> 
{
  // Return the type of the argument type
  @Override
  public String getType()
  {
    return "player";
  }

  // Parse a player from the specified string
  @Override
  public Player parse(String input) throws ArgumentException
  {    
    var player = Bukkit.getPlayerExact(input);
    if (player == null)
      throw new ArgumentTypeMismatchException(this, input);
    return player;
  }

  // Return suggestions for the specified input
  @Override
  public Stream<String> suggest(CommandContext context, int argumentIndex)
  {
    return Suggestion.find(context.getArgument(argumentIndex), Bukkit.getOnlinePlayers().stream()
      .map(p -> p.getName()));
  }  
}
