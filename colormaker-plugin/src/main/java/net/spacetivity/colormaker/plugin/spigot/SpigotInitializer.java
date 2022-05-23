package net.spacetivity.colormaker.plugin.spigot;

import net.spacetivity.colormaker.api.ColorAPI;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotInitializer extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigurationFile configurationFile = new ConfigurationFile(this);
        ColorAPI.onEnable(configurationFile.isUseProxy());
        new PlayerListener(this);
    }

    @Override
    public void onDisable() {
        ColorAPI.onDisable();
    }
}
