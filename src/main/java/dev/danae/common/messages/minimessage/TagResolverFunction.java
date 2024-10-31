package dev.danae.common.messages.minimessage;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;


@FunctionalInterface
public interface TagResolverFunction
{
  // Return a tag resolver with the specified key
  public TagResolver apply(String key);
}
