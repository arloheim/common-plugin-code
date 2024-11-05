package dev.danae.common.messages.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;


@FunctionalInterface
public interface ArgumentComponentLike
{
  // Return the component representation based on the supplied arguments
  public Component asComponent(ArgumentQueue args);
}