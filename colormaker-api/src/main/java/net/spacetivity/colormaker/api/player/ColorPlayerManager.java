package net.spacetivity.colormaker.api.player;

import net.spacetivity.colormaker.database.DatabasePattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ColorPlayerManager extends DatabasePattern<ColorPlayer> {

    public ColorPlayerManager(String tableName) {
        super(tableName);
    }

    @Override
    public ColorPlayer getSync(String uniqueId) {
        String queryStatement = "SELECT * FROM " + tableName + " WHERE uniqueId=?";
        ColorPlayer colorPlayer = null;

        try (Connection connection = databaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryStatement);
            statement.setString(1, uniqueId);
            ResultSet resultSet = statement.executeQuery();
            boolean hasNext = resultSet.next();
            resultSet.close();
            colorPlayer = hasNext ? convertResultSet(resultSet) : null;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return colorPlayer;
    }

    @Override
    public List<ColorPlayer> getAllSync() {
        String queryStatement = "SELECT * FROM " + tableName;
        List<ColorPlayer> colorPlayers = new ArrayList<>();

        try (Connection connection = databaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryStatement);
            ResultSet resultSet = statement.executeQuery();
            statement.close();

            while (resultSet.next())
                colorPlayers.add(convertResultSet(resultSet));

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return colorPlayers;
    }

    @Override
    public ColorPlayer convertResultSet(ResultSet resultSet) {
        ColorPlayer colorPlayer = null;

        try {
            UUID uniqueId = UUID.fromString(resultSet.getString("uniqueId"));
            String primaryColor = resultSet.getString("primaryColor");
            String secondaryColor = resultSet.getString("secondaryColor");
            colorPlayer = ColorPlayer.from(uniqueId, primaryColor, secondaryColor);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return colorPlayer;
    }

    @Override
    public void insertObject(ColorPlayer colorPlayer) {
        String queryStatement = createQueryStatement(tableName, "uniqueId", "primaryColor", "secondaryColor");
        try (Connection connection = databaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryStatement);
            statement.setString(1, colorPlayer.getUniqueId().toString());
            statement.setString(2, colorPlayer.getPrimaryColor());
            statement.setString(3, colorPlayer.getSecondaryColor());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
