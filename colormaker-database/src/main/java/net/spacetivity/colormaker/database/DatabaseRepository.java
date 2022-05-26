package net.spacetivity.colormaker.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.spacetivity.colormaker.database.file.ConfigFileManager;
import net.spacetivity.colormaker.database.file.FileUtils;
import net.spacetivity.colormaker.database.redis.RedissonManager;
import net.spacetivity.colormaker.database.sql.DatabaseManager;

@Getter
public class DatabaseRepository {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Getter
    private static DatabaseRepository instance;

    private final FileUtils fileUtils;
    private final ConfigFileManager configFileManager;

    private final DatabaseManager databaseManager;
    private RedissonManager redissonManager;

    public DatabaseRepository(String dataFolderPath) {
        instance = this;
        this.fileUtils = new FileUtils();
        this.configFileManager = new ConfigFileManager(dataFolderPath);
        this.configFileManager.createSQLFile();
        this.databaseManager = new DatabaseManager();
    }


    public void enableRedis() {
        this.configFileManager.createRedisFile();
        this.redissonManager = new RedissonManager();
    }
}
