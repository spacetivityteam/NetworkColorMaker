package net.spacetivity.colormaker.database.sql;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import net.spacetivity.colormaker.database.DatabaseRepository;
import net.spacetivity.colormaker.database.file.SQLFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.StringJoiner;

@Getter
public final class DatabaseManager {

    private final HikariDataSource dataSource;

    public DatabaseManager() {

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        dataSource = new HikariDataSource();
        dataSource.addDataSourceProperty("cachePrepStmts", "true");
        dataSource.addDataSourceProperty("prepStmtCacheSize", "250");
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        DatabaseRepository instance = DatabaseRepository.getInstance();
        SQLFile sqlFile = instance.getFileUtils().readFile(instance.getConfigFileManager().getSqlFilePath().toFile(), SQLFile.class);

        dataSource.setJdbcUrl(MessageFormat.format("jdbc:mariadb://{0}:{1}/{2}", sqlFile.getHostname(), sqlFile.getPort(), sqlFile.getDatabase()));
        dataSource.setUsername(sqlFile.getUser());
        dataSource.setPassword(sqlFile.getPassword());
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void createTable(String tableName, final LinkedHashMap<String, String> fields) {
        String queryStatement = generateTableIfNotExist(tableName, fields);

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryStatement);
            statement.execute();
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public String generateTableIfNotExist(String tableName, final LinkedHashMap<String, String> fields) {
        StringJoiner fieldsString = new StringJoiner(", ");
        fields.forEach((key, value) -> fieldsString.add(key + " " + value.toUpperCase()));
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" + fieldsString + ");";
    }
}
