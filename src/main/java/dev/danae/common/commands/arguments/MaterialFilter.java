package dev.danae.common.commands.arguments;

import java.util.Arrays;
import java.util.stream.Stream;
import org.bukkit.Material;


public enum MaterialFilter
{
  ALL,
  BLOCKS,
  GRAVITY_BLOCKS,
  ITEMS;
  

  // Return if the specified material matches the filter
  public boolean matches(Material material)
  {
    return switch (this)
    {
      case BLOCKS -> material.isBlock();
      case GRAVITY_BLOCKS -> material.isBlock() && material.hasGravity();
      case ITEMS -> material.isItem();
      default -> true;
    };
  }

  // Return a stream of all materials matching the filter
  public Stream<Material> stream()
  {
    return Arrays.stream(Material.values())
      .filter(material -> this.matches(material));
  }
}
