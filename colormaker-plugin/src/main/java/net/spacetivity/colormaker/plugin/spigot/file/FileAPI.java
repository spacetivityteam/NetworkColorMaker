package net.spacetivity.colormaker.plugin.spigot.file;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public abstract class FileAPI {

    private final File file;
    protected YamlConfiguration configuration;

    public FileAPI(JavaPlugin plugin, String fileName) {
        this.configuration = new YamlConfiguration();
        this.file = new File(plugin.getDataFolder(), fileName + ".yml");

        if (!Files.exists(file.toPath())) {
            try {
                Files.createDirectories(file.getParentFile().toPath());
                Files.createFile(file.toPath());
                insertDefaults(configuration);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.loadFile();
    }

    public abstract void insertDefaults(YamlConfiguration configuration);

    public abstract void cacheData(YamlConfiguration configuration);

    protected String getString(YamlConfiguration configuration, String string) {
        return Objects.requireNonNull(configuration.getString(string)).replace("&", "§");
    }

    protected void saveFile() {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFile() {
        try {
            this.configuration.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        this.cacheData(this.configuration);
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }
}
