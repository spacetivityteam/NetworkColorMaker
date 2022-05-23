package net.spacetivity.colormaker.database.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import net.spacetivity.colormaker.database.DatabaseRepository;

import java.sql.SQLException;

public class BungeeInitializer extends Plugin {

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
