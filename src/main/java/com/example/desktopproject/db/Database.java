package com.example.desktopproject.db;

import org.apache.log4j.Logger;
import org.sqlite.JDBC;

import java.sql.*;
import java.util.List;

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
        List<String> createTables = List.of(
                """
                CREATE TABLE IF NOT EXISTS Expense(
                    date TEXT NOT NULL,
                    housing REAL NOT NULL,
                    food REAL NOT NULL,
                    goingOut REAL NOT NULL,
                    transportation REAL NOT NULL,
                    travel REAL NOT NULL,
                    tax REAL NOT NULL,
                    others REAL NOT NULL
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS Income(
                    date TEXT NOT NULL,
                    salary REAL NOT NULL,
                    helper REAL NOT NULL,
                    selfEnterprise REAL NOT NULL,
                    passiveIncome REAL NOT NULL,
                    other REAL NOT NULL
                );
                """
        );

        try (Connection connection = Database.connect()) {
            if (connection == null) {
                logger.error("Impossible de se connecter à la base de données pour créer les tables");
                return false;
            }

            for (String createTableSQL : createTables) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute(createTableSQL);
                    logger.debug("Exécution de la requête SQL : " + createTableSQL.trim().substring(0, 40) + "...");
                } catch (SQLException e) {
                    logger.error("Erreur lors de l'exécution de la requête SQL : " + createTableSQL, e);
                    return false;
                }
            }

            logger.info("Toutes les tables ont été vérifiées/créées avec succès");
            return true;
        } catch (SQLException exception) {
            logger.error("Erreur lors de la création des tables", exception);
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
