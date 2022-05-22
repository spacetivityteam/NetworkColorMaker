package net.spacetivity.colormaker.api.color;

import net.spacetivity.colormaker.api.database.DatabasePattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NetworkColorManager extends DatabasePattern<NetworkColor> {

    public NetworkColorManager(String tableName) {
        super(tableName);
    }

    @Override
    public NetworkColor getSync(String colorName) {
        String queryStatement = "SELECT * FROM " + tableName + " WHERE colorName=?";
        NetworkColor networkColor = null;

        try (Connection connection = databaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryStatement);
            statement.setString(1, colorName);
            ResultSet resultSet = statement.executeQuery();
            boolean hasNext = resultSet.next();
            resultSet.close();
            networkColor = hasNext ? convertResultSet(resultSet) : null;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return networkColor;
    }

    @Override
    public List<NetworkColor> getAllSync() {
        String queryStatement = "SELECT * FROM " + tableName;
        List<NetworkColor> networkColors = new ArrayList<>();

        try (Connection connection = databaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryStatement);
            ResultSet resultSet = statement.executeQuery();
            statement.close();

            while (resultSet.next())
                networkColors.add(convertResultSet(resultSet));

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return networkColors;
    }

    @Override
    public NetworkColor convertResultSet(ResultSet resultSet) {
        NetworkColor networkColor = null;

        try {
            String colorName = resultSet.getString("colorName");
            String hexCode = resultSet.getString("hexCode");
            String permission = resultSet.getString("permission");
            boolean isPrimaryColor = resultSet.getBoolean("isPrimaryColor");
            boolean isSecondaryColor = resultSet.getBoolean("isSecondaryColor");
            networkColor = NetworkColor.from(colorName, hexCode, permission, isPrimaryColor, isSecondaryColor);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return networkColor;
    }

    @Override
    public void insertObject(NetworkColor networkColor) {
        String queryStatement = createQueryStatement(tableName, "colorName", "hexCode", "permission", "isPrimaryColor", "isSecondaryColor");
        try (Connection connection = databaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryStatement);
            statement.setString(1, networkColor.getColorName());
            statement.setString(2, networkColor.getHexCode());
            statement.setString(3, networkColor.getPermission());
            statement.setBoolean(4, networkColor.isPrimaryColor());
            statement.setBoolean(5, networkColor.isSecondaryColor());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
