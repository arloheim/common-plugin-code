package dev.danae.common.commands;


@FunctionalInterface
public interface ArgumentBiFunction<T, U, R> 
{
  // Apply the function to the specified arguments
  public R apply(T t, U u) throws ArgumentException;
}
