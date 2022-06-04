package net.spacetivity.colormaker.plugin.spigot.file;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class ConfigurationFile extends FileAPI {

    private boolean useProxy;
    private String setupCommandPermission;
    private String colorGuiCommandPermission;
    private String customColorDisplayItem;

    public ConfigurationFile(JavaPlugin plugin) {
        super(plugin, "config");
    }

    @Override
    public void insertDefaults(YamlConfiguration configuration) {
        configuration.set("useProxy", false);
        configuration.set("setupCommandPermission", "colormaker.command.setup");
        configuration.set("colorGuiCommandPermission","colormaker.command.gui");
        configuration.set("customColorDisplayItem", "MOJANG_BANNER_PATTERN");
        saveFile();
    }

    @Override
    public void cacheData(YamlConfiguration configuration) {
        this.useProxy = configuration.getBoolean("useProxy");
        this.setupCommandPermission = configuration.getString("setupCommandPermission");
        this.colorGuiCommandPermission = configuration.getString("colorGuiCommandPermission");
        this.customColorDisplayItem = configuration.getString("customColorDisplayItem");
    }
}
