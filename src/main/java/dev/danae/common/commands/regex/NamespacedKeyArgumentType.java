package dev.danae.common.commands.regex;

import dev.danae.common.commands.ArgumentException;
import dev.danae.common.commands.ArgumentTypeMismatchException;
import java.util.regex.MatchResult;
import org.bukkit.NamespacedKey;


public final class NamespacedKeyArgumentType extends PatternArgumentType<NamespacedKey>
{
  // Constructor
  public NamespacedKeyArgumentType()
  {
    super("namespaced key", NAMESPACED_KEY_PATTERN);
  }


  // Parse a namespaced key from the specified match result
  @Override
  public NamespacedKey parse(MatchResult m) throws ArgumentException 
  {
    var key = NamespacedKey.fromString(m.group());
    if (key == null)
      throw new ArgumentTypeMismatchException(this, m.group());
    return key;
  }
}
