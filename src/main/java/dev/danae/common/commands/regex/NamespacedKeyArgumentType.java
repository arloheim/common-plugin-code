package dev.danae.common.commands.regex;

import java.util.regex.MatchResult;
import java.util.stream.Stream;
import dev.danae.common.commands.ArgumentException;
import dev.danae.common.commands.ArgumentTypeMismatchException;
import dev.danae.common.commands.CommandContext;
import org.bukkit.NamespacedKey;


public final class NamespacedKeyArgumentType extends PatternArgumentType<NamespacedKey>
{
  // The suggestions for the argument type
  private final Stream<String> suggestions;


  // Constructor
  public NamespacedKeyArgumentType(Stream<String> suggestions)
  {
    super("namespaced key", NAMESPACED_KEY_PATTERN);

    this.suggestions = suggestions;
  }

  // Constructor without suggestions
  public NamespacedKeyArgumentType()
  {
    this(Stream.empty());
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

  // Return suggestions for the specified input
  @Override
  public Stream<String> suggest(CommandContext context, int argumentIndex)
  {
    return this.suggestions;
  }
}
