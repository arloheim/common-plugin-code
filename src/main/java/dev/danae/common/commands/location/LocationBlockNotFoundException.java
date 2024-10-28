package dev.danae.common.commands.location;

import dev.danae.common.commands.ArgumentException;
import org.bukkit.Material;


public class LocationBlockNotFoundException extends ArgumentException
{
  // The message format for the exception
  private static final String MESSAGE_FORMAT = "No block of material %0$s could be found in a radius of %1$d blocks";


  // The material of the argument
  private final Material material;

  // The radius of the argument
  private final int radius;


  // Constructor for a material, radius and cause
  public LocationBlockNotFoundException(Material material, int radius, Throwable cause)
  {
    super(String.format(MESSAGE_FORMAT, material.toString().toLowerCase(), radius), cause);

    this.material = material;
    this.radius = radius;
  }

  // Constructor for a material and radius
  public LocationBlockNotFoundException(Material material, int radius)
  {
    super(String.format(MESSAGE_FORMAT, material.toString().toLowerCase(), radius));

    this.material = material;
    this.radius = radius;
  }


  // Return the material of the argument
  public Material getMaterial()
  {
    return this.material;
  }

  // Return the radius of the argument
  public int getRadius()
  {
    return this.radius;
  }
}
