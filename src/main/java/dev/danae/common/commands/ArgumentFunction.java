package dev.danae.common.commands;


@FunctionalInterface
public interface ArgumentFunction<T, U> 
{
  // Apply the function to the specified argument
  public U apply(T t) throws ArgumentException;
}
