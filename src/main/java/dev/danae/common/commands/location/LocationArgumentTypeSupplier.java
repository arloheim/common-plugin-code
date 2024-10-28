package dev.danae.common.commands.location;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Supplier;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;


public class LocationArgumentTypeSupplier implements Supplier<LocationType>
{
  // The origin location for the argument type
  private final Location origin;


  // The allowed formats for the argument type
  private EnumSet<LocationFormat> allowedFormats = EnumSet.of(LocationFormat.NUMERIC);

  // The block search radius for the argument type
  private int blockSearchRadius = 10;

  // The aliases for the argument type
  private Map<NamespacedKey, Location> aliases = Map.of();



  // Constructor
  public LocationArgumentTypeSupplier(Location origin)
  {
    this.origin = origin;
  }
  

  // Add the allowed formats to the supplier
  public LocationArgumentTypeSupplier withAllowedFormats(LocationFormat... allowedFormats)
  {
    this.allowedFormats.addAll(Arrays.asList(allowedFormats));
    return this;
  }

  //Remove the allowed formats from the supplier
  public LocationArgumentTypeSupplier withoutAllowedFormats(LocationFormat... allowedFormats)
  {
    this.allowedFormats.removeAll(Arrays.asList(allowedFormats));
    return this;
  }

  // Set the block search radius of the supplier
  public LocationArgumentTypeSupplier withBlockSearchRadius(int blockSearchRadius)
  {
    this.blockSearchRadius = blockSearchRadius;
    return this;
  }

  // Set the aliases of the supplier
  public LocationArgumentTypeSupplier withAliases(Map<NamespacedKey, Location> aliases)
  {
    this.aliases = aliases;
    return this;
  }


  // Get the location argument type
  public LocationType get()
  {
    return new LocationType(this.origin, this.allowedFormats, this.blockSearchRadius, this.aliases);
  }
}
