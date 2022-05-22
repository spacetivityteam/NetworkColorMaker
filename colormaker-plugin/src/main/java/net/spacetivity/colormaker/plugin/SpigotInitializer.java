package net.spacetivity.colormaker.plugin;

import net.spacetivity.colormaker.api.ColorAPI;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotInitializer extends JavaPlugin {

    @Override
    public void onEnable() {
        ColorAPI.onEnable(true);
    }

    @Override
    public void onDisable() {
        ColorAPI.onDisable();
    }
}
