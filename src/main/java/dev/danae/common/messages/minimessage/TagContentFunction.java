package dev.danae.common.messages.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;


@FunctionalInterface
public interface TagContentFunction extends TagFunction
{
  // Return a component based on the specified content
  public Component apply(String content);


  // Return a component based on the specified arguments
  public default Component apply(ArgumentQueue args)
  {
    var content = args.popOr("Content required").value();
    return this.apply(content);
  }
}
