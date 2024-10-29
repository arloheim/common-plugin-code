package dev.danae.common.commands.arguments;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import dev.danae.common.commands.Suggestion;
import org.bukkit.NamespacedKey;


final class NamespacedKeyArgumentType implements PatternArgumentType<NamespacedKey>
{
  // The suggestions for the argument type
  private final Stream<String> suggestions;


  // Constructor
  public NamespacedKeyArgumentType(Stream<String> suggestions)
  {
    this.suggestions = suggestions;
  }


  // Return the type of the argument type
  @Override
  public Class<NamespacedKey> getType()
  {
    return NamespacedKey.class;
  }

  // Return the type name for the argument type
  @Override
  public String getTypeName()
  {
    return "namespaced key";
  }

  // Return the pattern for the argument type
  @Override
  public Pattern getPattern()
  {
    return NAMESPACED_KEY_PATTERN;
  }

  // Parse a namespaced key from the specified match result
  @Override
  public NamespacedKey parseFromMatchResult(MatchResult m) throws ArgumentException 
  {
    var key = NamespacedKey.fromString(m.group());
    if (key == null)
      throw new ArgumentTypeMismatchException(this, m.group());
    return key;
  }

  // Return suggestions for the specified string
  @Override
  public Stream<String> suggestFromString(String input)
  {
    return Suggestion.find(input, this.suggestions);
  }
}
