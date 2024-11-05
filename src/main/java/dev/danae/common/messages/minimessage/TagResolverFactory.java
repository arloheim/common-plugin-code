package dev.danae.common.messages.minimessage;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;


@FunctionalInterface
public interface TagResolverFactory<T>
{
  // Create a tag resolver for the specified value and key
  public TagResolver create(T value, String key);
}
