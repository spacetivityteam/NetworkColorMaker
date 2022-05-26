package net.spacetivity.colormaker.plugin.spigot.file;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class ConfigurationFile extends FileAPI {

    private boolean useProxy;
    private String commandPermission;
    private String customColorDisplayItem;

    public ConfigurationFile(JavaPlugin plugin) {
        super(plugin, "config");
    }

    @Override
    public void insertDefaults(YamlConfiguration configuration) {
        configuration.set("useProxy", false);
        configuration.set("commandPermission", "colormaker.commands");
        configuration.set("customColorDisplayItem", "MOJANG_BANNER_PATTERN");
        saveFile();
    }

    @Override
    public void cacheData(YamlConfiguration configuration) {
        this.useProxy = configuration.getBoolean("useProxy");
        this.commandPermission = configuration.getString("commandPermission");
        this.customColorDisplayItem = configuration.getString("customColorDisplayItem");
    }
}
