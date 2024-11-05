package dev.danae.common.messages.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;


@FunctionalInterface
public interface ContextualComponentFactory<T> extends TagResolverFactory<T>
{
  // Create a tag resolver for the specified value and key
  public Component create(T value, ArgumentQueue args);


  // Create a tag resolver for the specified value and key
  public default TagResolver create(T value, String key)
  {
    return TagResolver.resolver(key, (args, context) -> Tag.selfClosingInserting(this.create(value, args)));
  }
}