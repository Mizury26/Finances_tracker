package com.example.desktopproject.db;

import java.io.File;
import org.apache.log4j.Logger;

public class DataBasePath {
    private static final Logger logger = Logger.getLogger(DataBasePath.class);

    public static String getDatabasePath() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");
        String appName = "FinancesTracker";
        String dbPath;

        if (os.contains("win")) {
            // Windows : AppData/Local/FinancesTracker
            dbPath = userHome + "\\AppData\\Local\\" + appName + "\\database.db";
        } else if (os.contains("mac")) {
            // macOS : ~/Library/Application Support/FinancesTracker
            dbPath = userHome + "/Library/Application Support/" + appName + "/database.db";
        } else {
            // Linux : ~/.local/share/FinancesTracker
            dbPath = userHome + "/.local/share/" + appName + "/database.db";
        }

        logger.debug("Chemin de base de données défini: " + dbPath);
        return dbPath;
    }

    public static void ensureDatabaseDirectoryExists() {
        File dbFile = new File(getDatabasePath());
        File parentDir = dbFile.getParentFile();

        if (!parentDir.exists()) {
            boolean dirCreated = parentDir.mkdirs();
            if (dirCreated) {
                logger.info("Répertoire de base de données créé: " + parentDir.getAbsolutePath());
            } else {
                logger.error("Impossible de créer le répertoire de base de données: " + parentDir.getAbsolutePath());
            }
        } else {
            logger.debug("Répertoire de base de données existant: " + parentDir.getAbsolutePath());
        }
    }
}
