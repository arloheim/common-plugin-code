package dev.danae.common.commands.arguments;

import java.util.stream.Stream;
import dev.danae.common.commands.Suggestion;
import org.bukkit.Material;


final class MaterialArgumentType implements StringArgumentType<Material>
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
  public Class<Material> getType()
  {
    return Material.class;
  }
  
  // Return the type name for the argyment type
  @Override
  public String getTypeName()
  {
    return "material";
  }

  // Parse a material from the specified string
  @Override
  public Material parseFromString(String input) throws ArgumentException
  {    
    var material = Material.matchMaterial(input);
    if (material == null)
      throw new ArgumentTypeMismatchException(this, input);
    if (!this.filter.matches(material))
      throw new IllegalArgumentException(String.format("\"%s\" is not in range of the valid material values", input.toLowerCase()));
    return material;
  }

  // Return suggestions for the specified string
  @Override
  public Stream<String> suggestFromString(String input)
  {
    return Suggestion.find(input, this.filter.stream()
      .map(material -> material.name().toLowerCase()));
  }
}
