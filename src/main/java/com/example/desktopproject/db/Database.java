package com.example.desktopproject.db;

import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.apache.log4j.Logger;

public class Database {
    private static final Logger logger = Logger.getLogger(Database.class);

    /**
     * Location of database
     */
    private static final String location = DataBasePath.getDatabasePath();

    /**
     * Currently only table needed
     */
    private static final String requiredTable = "Expense";

    public static boolean isOK() {
        if (!checkDrivers())
            return false; // driver errors

        if (!checkConnection())
            return false; // can't connect to db

        return createTableIfNotExists(); // tables didn't exist
    }

    private static boolean checkDrivers() {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new JDBC());
            return true;
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            logger.error("Could not start SQLite Drivers", classNotFoundException);
            return false;
        }
    }

    private static boolean checkConnection() {
        try (Connection connection = connect()) {
            return connection != null;
        } catch (SQLException e) {
            logger.error("Could not connect to database", e);
            return false;
        }
    }

    private static boolean createTableIfNotExists() {
        String createTables = """
                     CREATE TABLE IF NOT EXISTS Expense(
                          date TEXT NOT NULL,
                          total REAL NOT NULL,
                          housing REAL NOT NULL,
                          food REAL NOT NULL,
                          goingOut REAL NOT NULL,
                          transportation REAL NOT NULL,
                          travel REAL NOT NULL,
                          tax REAL NOT NULL,
                          others REAL NOT NULL
                  );
                """;

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(createTables);
            statement.executeUpdate();
            logger.debug("Table Expense vérifiée/créée avec succès");
            return true;
        } catch (SQLException exception) {
            logger.error("Could not find tables in database", exception);
            return false;
        }
    }

    protected static Connection connect() {
        DataBasePath.ensureDatabaseDirectoryExists();
        String jdbcUrl = "jdbc:sqlite:" + location;
        Connection connection;
        try {
            connection = DriverManager.getConnection(jdbcUrl);
            logger.debug("Connexion à la base de données établie");
        } catch (SQLException exception) {
            logger.error("Could not connect to SQLite DB at " + location, exception);
            return null;
        }
        return connection;
    }
}
