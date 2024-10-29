package dev.danae.common.commands.arguments;


@FunctionalInterface
public interface ArgumentTypeFunction<T, R> 
{
  // Apply the function to the specified arguments
  public R apply(T t, ArgumentType<?> type) throws ArgumentException;
}
