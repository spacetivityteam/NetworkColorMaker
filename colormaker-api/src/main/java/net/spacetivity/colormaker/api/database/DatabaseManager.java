package net.spacetivity.colormaker.api.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;

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
        dataSource.setJdbcUrl("jdbc:mariadb://10.20.20.2:3306/color_manager");
        dataSource.setUsername("admin");
        dataSource.setPassword("UQUznpR4dSq1y7iduonh");
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
