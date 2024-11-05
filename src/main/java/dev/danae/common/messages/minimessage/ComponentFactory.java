package dev.danae.common.messages.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;


@FunctionalInterface
public interface ComponentFactory<T> extends TagResolverFactory<T>
{
  // Create a tag resolver for the specified value and key
  public Component create(T value);


  // Create a tag resolver for the specified value and key
  public default TagResolver create(T value, String key)
  {
    return Placeholder.component(key, this.create(value));
  }
}