package dev.danae.common.messages.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;


@FunctionalInterface
public interface StringComponentLike extends ArgumentComponentLike
{
  // Return the component representation based on the supplied argument
  public Component asComponent(String arg);

  
  // Return the component representation based on the supplied arguments
  public default Component asComponent(ArgumentQueue args)
  {
    return this.asComponent(args.hasNext() ? args.pop().value() : "");
  }
}