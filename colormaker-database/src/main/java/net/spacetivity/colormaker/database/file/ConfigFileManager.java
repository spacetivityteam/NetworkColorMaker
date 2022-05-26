package net.spacetivity.colormaker.database.file;

import lombok.Getter;
import net.spacetivity.colormaker.database.DatabaseRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigFileManager {

    private final FileUtils fileUtils = DatabaseRepository.getInstance().getFileUtils();

    private final String dataFolderPath;

    @Getter
    private final Path sqlFilePath;

    @Getter
    private final Path redisFilePath;

    public ConfigFileManager(String dataFolderPath) {
        this.dataFolderPath = dataFolderPath;
        this.sqlFilePath = Paths.get(dataFolderPath + "/sql.json");
        this.redisFilePath = Paths.get(dataFolderPath + "/redis.json");
    }

    public void createSQLFile() {
        if (Files.exists(sqlFilePath)) return;

        try {
            Path path = Path.of(dataFolderPath);
            if (!Files.exists(path))
                Files.createDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = sqlFilePath.toFile();
        SQLFile sqlFile = new SQLFile();
        sqlFile.setHostname("127.0.0.1");
        sqlFile.setPort("3306");
        sqlFile.setDatabase("color_manager");
        sqlFile.setUser("admin");
        sqlFile.setPassword("-");

        fileUtils.saveFile(file, sqlFile);
    }

    public void createRedisFile() {
        if (Files.exists(redisFilePath)) return;

        try {
            Path path = Path.of(dataFolderPath);
            if (!Files.exists(path))
                Files.createDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = redisFilePath.toFile();
        RedisFile redisFile = new RedisFile();
        redisFile.setHostname("127.0.0.1");
        redisFile.setPort("6379");
        redisFile.setPassword("-");

        fileUtils.saveFile(file, redisFile);
    }
}
