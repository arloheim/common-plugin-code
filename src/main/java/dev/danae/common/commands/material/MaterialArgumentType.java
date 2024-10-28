package dev.danae.common.commands.material;

import dev.danae.common.commands.ArgumentException;
import dev.danae.common.commands.ArgumentType;
import dev.danae.common.commands.ArgumentTypeMismatchException;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.Suggestion;
import java.util.stream.Stream;
import org.bukkit.Material;


public class MaterialArgumentType extends ArgumentType<Material>
{
  // The material filter for the argument type
  private final MaterialFilter filter;


  // Constructor
  public MaterialArgumentType(MaterialFilter filter)
  {
    this.filter = filter;
  }


  // Return the type of the argument type
  @Override
  public String getType()
  {
    return "material";
  }

  // Parse a material from the specified string
  @Override
  public Material parse(String input) throws ArgumentException
  {    
    var material = Material.matchMaterial(input);
    if (material == null)
      throw new ArgumentTypeMismatchException(this, input);
    if (!this.filter.matches(material))
      throw new IllegalArgumentException(String.format("\"%s\" is not in range of the valid material values", input.toLowerCase()));
    return material;
  }

  // Return suggestions for the specified input
  @Override
  public Stream<String> suggest(CommandContext context, int argumentIndex)
  {
    return Suggestion.find(context.getArgument(argumentIndex), this.filter.stream()
      .map(material -> material.name().toLowerCase()));
  }
}
