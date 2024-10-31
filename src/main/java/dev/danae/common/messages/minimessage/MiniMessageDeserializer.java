package dev.danae.common.messages.minimessage;

import dev.danae.common.messages.MessageDeserializer;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;


public class MiniMessageDeserializer implements MessageDeserializer
{
  // The component serializer for the formatter
  private final MiniMessage serializer;


  // Constructor
  public MiniMessageDeserializer()
  {
    this.serializer = MiniMessage.miniMessage();
  }


  // Deserialize the specified message and arguments to a component
  public Component deserialize(String message, Map<String, Object> arguments)
  {
    return this.serializer.deserialize(message, arguments.entrySet().stream()
      .map(e -> this.getArgumentResolver(e.getKey(), e.getValue()))
      .collect(TagResolver.toTagResolver()));
  }

  // Return a tag resolver for the specified argument
  private TagResolver getArgumentResolver(String key, Object value)
  {
    if (value instanceof TagResolverFunction tagResolverFunction)
      return tagResolverFunction.apply(key);
    else if (value instanceof TagFunction tagFunction)
      return TagResolver.resolver(key, (args, context) -> Tag.selfClosingInserting(tagFunction.apply(args)));
    else if (value instanceof Component component)
      return Placeholder.component(key, component);
    else
      return Placeholder.parsed(key, value.toString());
  }
}
