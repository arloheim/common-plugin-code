package dev.danae.common.commands.arguments;

import org.bukkit.NamespacedKey;


public class LocationAliasNotFoundException extends ArgumentException
{
  // The message format for the exception
  private static final String MESSAGE_FORMAT = "No block of material %0$s could be found in a radius of %1$d blocks";


  // The alias of the argument
  private final NamespacedKey alias;


  // Constructor for an alias and cause
  public LocationAliasNotFoundException(NamespacedKey alias, Throwable cause)
  {
    super(String.format(MESSAGE_FORMAT, alias), cause);

    this.alias = alias;
  }

  // Constructor for an alias
  public LocationAliasNotFoundException(NamespacedKey alias)
  {
    super(String.format(MESSAGE_FORMAT, alias));

    this.alias = alias;
  }


  // Return the alias of the argument
  public NamespacedKey getAlias()
  {
    return this.alias;
  }
}
