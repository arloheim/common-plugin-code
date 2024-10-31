package dev.danae.common.plugin;

import org.bukkit.plugin.java.JavaPlugin;


public interface PluginComponent<P extends JavaPlugin>
{
  // Return the plugin of the component
  public P getPlugin();
}
