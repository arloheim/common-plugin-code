package dev.danae.common.messages.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;


@FunctionalInterface
public interface StringComponentFactory<T> extends ArgumentComponentFactory<T>
{
  // Create a component for the specified value and argument
  public Component createComponent(T value, String arg);


  // Create a component for the specified value and arguments
  public default Component createComponent(T value, ArgumentQueue args)
  {
    return this.createComponent(value, args.hasNext() ? args.pop().value() : "");
  }
}