package net.spacetivity.colormaker.database;

import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Getter
public abstract class DatabasePattern<T> {

    protected final String tableName;
    protected final DatabaseManager databaseManager;
    protected final Executor executor;

    public DatabasePattern(String tableName) {
        this.tableName = tableName;
        this.databaseManager = DatabaseRepository.getInstance().getDatabaseManager();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public String createQueryStatement(String tableName, String... fields) {
        List<String> valueQuestionMarks = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) valueQuestionMarks.add("?");
        return createRawQueryStatement(tableName, fields, valueQuestionMarks.toArray(new String[0]));
    }

    public String createRawQueryStatement(String tableName, String[] fields, String[] values) {
        StringJoiner fieldsString = new StringJoiner(", ");
        StringJoiner valuesString = new StringJoiner(", ");

        for (String field : fields) fieldsString.add(field);
        for (String value : values) valuesString.add(value);

        return "INSERT INTO " + tableName + " (" +
                fieldsString + ") VALUES (" +
                valuesString + ")";
    }

    public boolean isExist(String keyField, Object key) {
        String queryStatement = MessageFormat.format("SELECT * FROM {0} WHERE {1}=?", tableName, keyField);
        boolean response = false;

        try (Connection connection = databaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryStatement);
            statement.setString(1, key.toString());
            ResultSet resultSet = statement.executeQuery();
            statement.close();
            response = resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return response;
    }

    public CompletableFuture<T> getAsync(String key) {
        CompletableFuture<T> asyncTask = new CompletableFuture<>();
        executor.execute(() -> asyncTask.complete(getSync(key)));
        return asyncTask;
    }

    public CompletableFuture<List<T>> getAllAsync() {
        CompletableFuture<List<T>> asyncTask = new CompletableFuture<>();
        executor.execute(() -> asyncTask.complete(getAllSync()));
        return asyncTask;
    }

    public T getSync(String key) {
        return null;
    }

    public List<T> getAllSync() {
        return null;
    }

    public abstract T convertResultSet(ResultSet resultSet);

    public abstract void insertObject(T object);

    public void updateField(String keyField, String key, String fieldToUpdate, Object newValue) {
        String queryStatement = "UPDATE " + tableName + " SET `" + fieldToUpdate + "`=? WHERE `" + keyField + "`=?";

        try (Connection connection = databaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryStatement);
            statement.setObject(1, newValue);
            statement.setString(2, key);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
