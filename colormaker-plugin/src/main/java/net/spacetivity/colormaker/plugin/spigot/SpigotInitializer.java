package net.spacetivity.colormaker.plugin.spigot;

import lombok.Getter;
import net.spacetivity.colormaker.api.ColorAPI;
import net.spacetivity.colormaker.api.color.NetworkColor;
import net.spacetivity.colormaker.plugin.spigot.file.ColorHeadFile;
import net.spacetivity.colormaker.plugin.spigot.file.ConfigurationFile;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.InventoryManager;
import net.spacetivity.colormaker.plugin.spigot.item.ItemBuilder;
import net.spacetivity.colormaker.plugin.spigot.item.SkullBuilder;
import net.spacetivity.colormaker.plugin.spigot.setup.ColorCommand;
import net.spacetivity.colormaker.plugin.spigot.setup.SetupListener;
import net.spacetivity.colormaker.plugin.spigot.setup.SetupManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public class SpigotInitializer extends JavaPlugin {

    private ConfigurationFile configurationFile;
    private ColorHeadFile colorHeadFile;
    private InventoryManager inventoryManager;
    private SetupManager setupManager;

    @Override
    public void onEnable() {
        this.configurationFile = new ConfigurationFile(this);
        this.colorHeadFile = new ColorHeadFile(this);
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

    public ItemStack getDisplayItem(NetworkColor color) {
        if (color.isUseHexadecimal()) {
            return new ItemBuilder(Material.valueOf(configurationFile.getCustomColorDisplayItem()))
                    .setDisplayNameSpigot(color.toSpigot() + color.getColorName())
                    .setLores(List.of("§7Permission: §b" + color.getPermission(), " ", color.isUseHexadecimal() ? "§8§oCustom" : "§8§oChatColor"))
                    .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .addItemFlag(ItemFlag.HIDE_DYE)
                    .build();
        } else {
            return new SkullBuilder()
                    .setOwnerWithGameProfile(getValueForChatColor(color.getColorName()))
                    .build();
        }
    }

    public String getValueForChatColor(String chatColorName) {
        return colorHeadFile.getConfiguration().getString("color." + chatColorName);
    }
}
