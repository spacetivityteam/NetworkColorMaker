package net.spacetivity.colormaker.database.spigot;

import net.spacetivity.colormaker.database.DatabaseRepository;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class SpigotInitializer extends JavaPlugin {

    private DatabaseRepository repository;

    @Override
    public void onEnable() {
        this.repository = new DatabaseRepository();
    }

    @Override
    public void onDisable() {
        try {
            this.repository.getDatabaseManager().getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
