package net.spacetivity.colormaker.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import net.spacetivity.colormaker.api.color.NetworkColorManager;
import net.spacetivity.colormaker.api.database.DatabaseManager;

import java.sql.SQLException;

@Getter
public class ColorRepository {

    @Getter(AccessLevel.MODULE)
    private static ColorRepository instance;
    public static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().create();

    // Boolean entscheidet ob bei color updates per redis an den proxy ein color update gesendet werden soll damit dann
    // auch im proxy cache die colors geupdated werden.
    private boolean useProxy;

    private DatabaseManager databaseManager;
    private NetworkColorManager networkColorManager;

    public void onEnable(boolean useProxy) {
        instance = this;
        this.useProxy = useProxy;
        this.databaseManager = new DatabaseManager();
        this.networkColorManager = new NetworkColorManager("colors");
    }

    public void onDisable() {
        try {
            this.databaseManager.getDataSource().close();
            this.databaseManager.getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
