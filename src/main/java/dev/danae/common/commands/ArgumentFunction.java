package dev.danae.common.commands;


@FunctionalInterface
public interface ArgumentFunction<T, R> 
{
  // Apply the function to the specified argument
  public R apply(T t) throws ArgumentException;
}
