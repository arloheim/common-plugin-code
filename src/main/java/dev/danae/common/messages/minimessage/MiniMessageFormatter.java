package dev.danae.common.messages.minimessage;

import java.util.HashMap;
import java.util.Map;
import dev.danae.common.messages.MessageFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;


public class MiniMessageFormatter implements MessageFormatter
{
  // The component serializer for the formatter
  private final MiniMessage serializer;

  // The custom resolvers for the formatter
  private final Map<Class<?>, TagResolverFactory<?>> customResolvers = new HashMap<>();;


  // Constructor
  public MiniMessageFormatter()
  {
    this.serializer = MiniMessage.miniMessage();
  }


  // Add the specified custom resolver factory to the formatter
  public <T> MiniMessageFormatter registerCustomResolver(Class<T> type, TagResolverFactory<T> tagResolverFactory)
  {
    this.customResolvers.put(type, tagResolverFactory);
    return this;
  }
  
  // Add the specified custom component factory to the formatter
  public <T> MiniMessageFormatter registerCustomResolver(Class<T> type, ComponentFactory<T> componentFactory)
  {
    this.customResolvers.put(type, componentFactory);
    return this;
  }
  
  // Add the specified custom argument component factory to the formatter
  public <T> MiniMessageFormatter registerCustomResolver(Class<T> type, ArgumentComponentFactory<T> argumentComponentFactory)
  {
    this.customResolvers.put(type, argumentComponentFactory);
    return this;
  }
  
  // Add the specified custom string component factory to the formatter
  public <T> MiniMessageFormatter registerCustomResolver(Class<T> type, StringComponentFactory<T> stringComponentFactory)
  {
    this.customResolvers.put(type, stringComponentFactory);
    return this;
  }

  // Add the specified custom resolver to the formatter
  public <T> MiniMessageFormatter unregisterCustomResolver(Class<T> type)
  {
    this.customResolvers.remove(type);
    return this;
  }


  // Format the specified message and arguments to a component
  public Component format(String message, Map<String, Object> arguments)
  {
    return this.serializer.deserialize(message, arguments.entrySet().stream()
      .map(e -> this.getArgumentResolver(e.getKey(), e.getValue()))
      .collect(TagResolver.toTagResolver()));
  }

  // Return a tag resolver for the specified argument
  private <T> TagResolver getArgumentResolver(String key, T value)
  {
    if (value instanceof Component component)
      return Placeholder.component(key, component);
    else if (value instanceof ComponentLike componentLike)
      return Placeholder.component(key, componentLike);
    else if (value instanceof ArgumentComponentLike argumentComponentLike)
      return TagResolver.resolver(key, (args, context) -> Tag.selfClosingInserting(argumentComponentLike.asComponent(args)));
    
    var customResolverType = this.customResolvers.keySet().stream()
      .filter(type -> type.isAssignableFrom(value.getClass()))
      .findFirst();

    if (customResolverType.isPresent())
    {
      @SuppressWarnings("unchecked")
      var resolver = (TagResolverFactory<T>)this.customResolvers.get(customResolverType.get());
      return resolver.create(value, key);
    }
    else
    {
      return Placeholder.parsed(key, value.toString());
    }
  }
}
