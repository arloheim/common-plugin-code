package dev.danae.common.messages.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;


@FunctionalInterface
public interface TagFunction
{
  // Return a component based on the specified arguments
  public Component apply(ArgumentQueue args);
}
