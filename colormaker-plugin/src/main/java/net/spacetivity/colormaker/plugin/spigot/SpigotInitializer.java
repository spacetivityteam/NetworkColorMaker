package net.spacetivity.colormaker.plugin.spigot;

import lombok.Getter;
import net.spacetivity.colormaker.api.ColorAPI;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.InventoryManager;
import net.spacetivity.colormaker.plugin.spigot.setup.ColorCommand;
import net.spacetivity.colormaker.plugin.spigot.setup.SetupListener;
import net.spacetivity.colormaker.plugin.spigot.setup.SetupManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SpigotInitializer extends JavaPlugin {

    private ConfigurationFile configurationFile;
    private InventoryManager inventoryManager;
    private SetupManager setupManager;


    @Override
    public void onEnable() {
        this.configurationFile = new ConfigurationFile(this);
        ColorAPI.onEnable(configurationFile.isUseProxy());
        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();
        this.setupManager = new SetupManager();
        new PlayerListener(this);
        new SetupListener(this);
        new ColorCommand(this);
    }

    @Override
    public void onDisable() {
        ColorAPI.onDisable();
    }
}
