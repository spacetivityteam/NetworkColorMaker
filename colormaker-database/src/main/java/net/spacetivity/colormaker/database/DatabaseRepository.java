package net.spacetivity.colormaker.database;

import lombok.Getter;

@Getter
public class DatabaseRepository {

    @Getter
    private static DatabaseRepository instance;
    private final DatabaseManager databaseManager;

    public DatabaseRepository() {
        instance = this;
        this.databaseManager = new DatabaseManager();
    }
}
