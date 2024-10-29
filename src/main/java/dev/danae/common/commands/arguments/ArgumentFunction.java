package dev.danae.common.commands.arguments;


@FunctionalInterface
public interface ArgumentFunction<T, R> 
{
  // Apply the function to the specified argument
  public R apply(T t) throws ArgumentException;
}
