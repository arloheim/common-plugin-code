package dev.danae.common.commands.arguments;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;


public final class LocationArgumentTypeBuilder implements Function<Location, ArgumentType<Location>>
{
  // The allowed formats for the argument type
  private EnumSet<LocationFormat> allowedFormats = EnumSet.of(LocationFormat.NUMERIC);

  // The block search radius for the argument type
  private int blockSearchRadius = 10;

  // The aliases for the argument type
  private Map<NamespacedKey, Location> aliases = Map.of();



  // Constructor
  private LocationArgumentTypeBuilder(EnumSet<LocationFormat> allowedFormats, int blockSearchRadius, Map<NamespacedKey, Location> aliases)
  {
    this.allowedFormats = allowedFormats;
    this.blockSearchRadius = blockSearchRadius;
    this.aliases = aliases;
  }

  // Constructor for copying an existing builder
  private LocationArgumentTypeBuilder(LocationArgumentTypeBuilder builder)
  {
    this(EnumSet.copyOf(builder.allowedFormats), builder.blockSearchRadius, Map.copyOf(builder.aliases));
  }

  // Constructor for creating a new builder
  public LocationArgumentTypeBuilder()
  {
    this(EnumSet.of(LocationFormat.NUMERIC), 10, Map.of());
  }
  

  // Add the allowed formats to the builder
  public LocationArgumentTypeBuilder withAllowedFormats(LocationFormat... allowedFormats)
  {
    var newBuilder = new LocationArgumentTypeBuilder(this);
    newBuilder.allowedFormats.addAll(Arrays.asList(allowedFormats));
    return newBuilder;
  }

  // Remove the allowed formats from the builder
  public LocationArgumentTypeBuilder withoutAllowedFormats(LocationFormat... allowedFormats)
  {
    var newBuilder = new LocationArgumentTypeBuilder(this);
    newBuilder.allowedFormats.removeAll(Arrays.asList(allowedFormats));
    return newBuilder;
  }

  // Set the block search radius of the builder
  public LocationArgumentTypeBuilder withBlockSearchRadius(int blockSearchRadius)
  {
    var newBuilder = new LocationArgumentTypeBuilder(this);
    newBuilder.blockSearchRadius = blockSearchRadius;
    return newBuilder;
  }

  // Set the aliases of the builder
  public LocationArgumentTypeBuilder withAliases(Map<NamespacedKey, Location> aliases)
  {
    var newBuilder = new LocationArgumentTypeBuilder(this);
    newBuilder.aliases = aliases;
    return newBuilder;
  }


  // Build the location argument type
  public ArgumentType<Location> build(Location origin)
  {
    return new LocationArgumentType(origin, this.allowedFormats, this.blockSearchRadius, this.aliases);
  }

  // Apply the builder as a function
  @Override
  public ArgumentType<Location> apply(Location origin)
  {
    return this.build(origin);
  }
}
