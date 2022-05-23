package net.spacetivity.colormaker.plugin.spigot;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class ConfigurationFile extends FileAPI {

    private boolean useProxy;

    public ConfigurationFile(JavaPlugin plugin) {
        super(plugin, "config");
    }

    @Override
    public void insertDefaults(YamlConfiguration configuration) {
        configuration.set("useProxy", false);
        saveFile();
    }

    @Override
    public void cacheData(YamlConfiguration configuration) {
        this.useProxy = configuration.getBoolean("useProxy");
    }
}
