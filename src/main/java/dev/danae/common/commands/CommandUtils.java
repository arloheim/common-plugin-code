package dev.danae.common.commands;


public class CommandUtils 
{
  // Clamp an integer value
  public static int clamp(int value, int min, int max) 
  {
    return Math.max(min, Math.min(max, value));
  }
  
  // Clamp a float value
  public static float clamp(float value, float min, float max) 
  {
    return Math.max(min, Math.min(max, value));
  }
  
  // Clamp a double value
  public static double clamp(double value, double min, double max) 
  {
    return Math.max(min, Math.min(max, value));
  }
}
