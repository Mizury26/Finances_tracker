package com.example.desktopproject.db;

import java.io.File;

public class DataBasePath {
    public static String getDatabasePath() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");
        String appName = "FinanceTracker";

        if (os.contains("win")) {
            // Windows : AppData/Local/FinanceTracker
            return userHome + "\\AppData\\Local\\" + appName + "\\database.db";
        } else if (os.contains("mac")) {
            // macOS : ~/Library/Application Support/FinanceTracker
            return userHome + "/Library/Application Support/" + appName + "/database.db";
        } else {
            // Linux : ~/.local/share/FinanceTracker
            return userHome + "/.local/share/" + appName + "/database.db";
        }
    }

    public static void ensureDatabaseDirectoryExists() {
        File dbFile = new File(getDatabasePath());
        dbFile.getParentFile().mkdirs();
    }
}
