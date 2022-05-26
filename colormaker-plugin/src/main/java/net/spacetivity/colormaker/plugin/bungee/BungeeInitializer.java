package net.spacetivity.colormaker.plugin.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import net.spacetivity.colormaker.api.ColorAPI;
import net.spacetivity.colormaker.api.ColorRepository;

public class BungeeInitializer extends Plugin {

    @Override
    public void onEnable() {
        ColorAPI.onEnableProxy();
        getProxy().getPluginManager().registerListener(this, new PlayerListener());
    }

    @Override
    public void onDisable() {
        ColorRepository.getInstance().onDisable();
    }
}
