package dev.danae.common.commands.location;

import java.util.EnumSet;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.MatchResult;
import java.util.stream.Stream;
import dev.danae.common.commands.ArgumentException;
import dev.danae.common.commands.ArgumentType;
import dev.danae.common.commands.ArgumentTypeMismatchException;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.Scanner;
import dev.danae.common.commands.material.MaterialFilter;
import dev.danae.common.commands.regex.PatternArgumentType;
import dev.danae.common.util.Cuboid;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;


public class LocationArgumentType extends PatternArgumentType<Location>
{
  // The pattern for parsing a location
  private static final Pattern PATTERN = Pattern.compile("((?:(0|-?[1-9][0-9]*)|(~(-?[1-9][0-9]*)?)) (?:(0|-?[1-9][0-9]*)|(~(-?[1-9][0-9]*)?)) (?:(0|-?[1-9][0-9]*)|(~(-?[1-9][0-9]*)?)))|([a-zA-Z0-9_]{2,16})|(([@^])([a-z_][a-z0-9_]*))|(\\$([a-zA-Z0-9._-]+:[a-zA-Z0-9._\\/-]+))|(~)");

  // The argument types for parsing a location
  public static final ArgumentType<NamespacedKey> NAMESPACED_KEY_TYPE = ArgumentType.getNamespacedKey();
  public static final ArgumentType<Player> PLAYER_ARGUMENT_TYPE = ArgumentType.getPlayer();
  private static final ArgumentType<Material> BLOCK_ARGUMENT_TYPE = ArgumentType.getMaterial(MaterialFilter.BLOCKS);
  

  // The origin location for the argument type
  private final Location origin;

  // The allowed formats for the argument type
  private final EnumSet<LocationFormat> allowedFormats;

  // The block search radius for the argument type
  private final int blockSearchRadius;

  // The aliases for the argument type
  private final Map<NamespacedKey, Location> aliases;


  // Constructor
  public LocationArgumentType(Location origin, EnumSet<LocationFormat> allowedFormats, int blockSearchRadius, Map<NamespacedKey, Location> aliases)
  {
    super("location", PATTERN);

    this.origin = origin;
    this.allowedFormats = allowedFormats;
    this.blockSearchRadius = blockSearchRadius;
    this.aliases = aliases;
  }


  // Parse a location from the specified string
  @Override
  public Location parse(MatchResult m) throws ArgumentException
  {    
    // Check for a current location
    if (this.allowedFormats.contains(LocationFormat.NUMERIC) && m.group(17) != null)
      return origin;
    
    // Check for a numeric location
    if (this.allowedFormats.contains(LocationFormat.NUMERIC) && m.group(1) != null)
    {
      var x = m.group(3) != null ? origin.getBlockX() + (m.group(4) != null ? Integer.parseInt(m.group(4)) : 0) : Integer.parseInt(m.group(2));
      var y = m.group(6) != null ? origin.getBlockY() + (m.group(7) != null ? Integer.parseInt(m.group(7)) : 0) : Integer.parseInt(m.group(5));
      var z = m.group(9) != null ? origin.getBlockZ() + (m.group(10) != null ? Integer.parseInt(m.group(10)) : 0) : Integer.parseInt(m.group(8));
      
      return new Location(origin.getWorld(), x, y, z);
    }

    // Check for a player location
    if (this.allowedFormats.contains(LocationFormat.PLAYER) && m.group(11) != null)
    {
      var player = PLAYER_ARGUMENT_TYPE.parse(m.group(11));
      if (player.getLocation().getWorld() != origin.getWorld())
        throw new LocationWorldMismatchException(player);

      return player.getLocation();
    }

    // Check for a block location
    if (this.allowedFormats.contains(LocationFormat.BLOCK) && m.group(12) != null)
    {      
      var mode = m.group(13);
      var material = BLOCK_ARGUMENT_TYPE.parse(m.group(14));
      
      // Find the block
      var block = Cuboid.around(origin, this.blockSearchRadius).findNearestBlockToCenter(material);
      if (block == null)
        throw new LocationBlockNotFoundException(material, this.blockSearchRadius);
      
      // Return the appropriate position based on the mode
      return switch (mode)
      {
        case "@" -> block.getLocation();
        case "^" -> block.getLocation().add(0, 1, 0);
        default -> throw new ArgumentTypeMismatchException(this, m.group());
      };
    }

    // Check for an alias location
    if (this.allowedFormats.contains(LocationFormat.ALIAS) && m.group(15) != null)
    {
      var key = NAMESPACED_KEY_TYPE.parse(m.group(16));
      var location = aliases.get(key);
      if (location == null)
        throw new LocationAliasNotFoundException(key);
      
      return location;
    }
    
    // Invalid location format
    throw new ArgumentTypeMismatchException(this, m.group());
  }

  // Parse a location from the next element in the specified scanner
  @Override
  public Location parse(Scanner scanner) throws ArgumentException
  {
    try
    {
      return scanner.takeMany(3, " ", s -> this.parse(s), this.getType());
    }
    catch (ArgumentException ex)
    {
      return scanner.take(input -> this.parse(input), this.getType());
    }
  }

  // Return suggestions for the specified input
  @Override
  public Stream<String> suggest(CommandContext context, int argumentIndex)
  {
    // Check if there are location-related arguments
    if (!context.hasAtLeastArgumentsCount(argumentIndex + 1) || context.hasAtLeastArgumentsCount(argumentIndex + 4))
      return Stream.empty();

    // Add the suggestions
    Stream<String> suggestions = Stream.of();

    // Check for a numeric location
    if (this.allowedFormats.contains(LocationFormat.NUMERIC))
      suggestions = Stream.concat(suggestions, Stream.of("~"));

    // Check if there is only one location argument
    if (context.hasArgumentsCount(argumentIndex + 1))
    {
      // Check for a player location
      if (this.allowedFormats.contains(LocationFormat.PLAYER))
      {
        suggestions = Stream.concat(suggestions, PLAYER_ARGUMENT_TYPE.suggest(context, argumentIndex));
      }

      // Check for a block location
      if (this.allowedFormats.contains(LocationFormat.BLOCK))
      {
        suggestions = Stream.concat(suggestions, BLOCK_ARGUMENT_TYPE.suggest(context, argumentIndex)
          .map(material -> String.format("@%s", material)));
        suggestions = Stream.concat(suggestions, BLOCK_ARGUMENT_TYPE.suggest(context, argumentIndex)
          .map(material -> String.format("^%s", material)));
      }

      // Check for an alias location
      if (this.allowedFormats.contains(LocationFormat.ALIAS))
      {
        suggestions = Stream.concat(suggestions, this.aliases.keySet().stream()
          .map(alias -> String.format("$%s", alias)));
      }
    }
  
    // Return the suggestions
    return suggestions;
  }
}
