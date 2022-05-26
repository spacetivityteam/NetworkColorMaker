package net.spacetivity.colormaker.plugin.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import net.spacetivity.colormaker.api.ColorAPI;
import net.spacetivity.colormaker.api.ColorRepository;

public class BungeeInitializer extends Plugin {

    @Override
    public void onEnable() {
        ColorAPI.onEnable(true);
    }

    @Override
    public void onDisable() {
        ColorRepository.getInstance().onDisable();
    }
}