package com.example.desktopproject.utils;

import java.io.File;
import java.io.IOException;

/**
 * Classe utilitaire pour configurer les chemins des fichiers de log
 * selon le système d'exploitation
 */
public class LogConfig {

    private static final String LOG_FILENAME = "application.log";

    /**
     * Configure le chemin du fichier de log selon l'OS et le définit
     * comme propriété système pour que log4j puisse l'utiliser
     */
    public static void configureLogDirectory() {
        String logDirPath = getLogDirectoryPath();

        // Crée le répertoire de logs s'il n'existe pas
        File logDir = new File(logDirPath);
        if (!logDir.exists()) {
            boolean created = logDir.mkdirs();
            if (!created) {
                System.err.println("ERREUR: Impossible de créer le répertoire de logs: " + logDirPath);
            } else {
                System.out.println("Répertoire de logs créé: " + logDirPath);
            }
        }

        // Vérifie si le fichier de log peut être créé
        File logFile = new File(logDirPath, LOG_FILENAME);
        if (!logFile.exists()) {
            try {
                boolean fileCreated = logFile.createNewFile();
                if (fileCreated) {
                    System.out.println("Fichier de log créé: " + logFile.getAbsolutePath());
                } else {
                    System.err.println("ERREUR: Impossible de créer le fichier de log: " + logFile.getAbsolutePath());
                }
            } catch (IOException e) {
                System.err.println("ERREUR lors de la création du fichier de log: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // S'assurer que le fichier est accessible en écriture
        if (!logFile.canWrite()) {
            System.err.println("ATTENTION: Le fichier de log existe mais n'est pas accessible en écriture: "
                    + logFile.getAbsolutePath());
        }

        // Définit la propriété système pour que log4j puisse l'utiliser
        // On utilise le chemin absolu pour éviter tout problème de résolution
        System.setProperty("finance.log.dir", logDir.getAbsolutePath());
        System.out.println("Chemin de log configuré: " + logDir.getAbsolutePath());
    }

    /**
     * Détermine le chemin approprié pour les logs selon l'OS
     */
    public static String getLogDirectoryPath() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");
        String appName = "FinanceTracker";
        String logDir;

        if (os.contains("win")) {
            // Windows : AppData/Local/FinanceTracker/logs
            logDir = userHome + File.separator + "AppData" + File.separator + "Local"
                    + File.separator + appName + File.separator + "logs";
        } else if (os.contains("mac")) {
            // macOS : ~/Library/Logs/FinanceTracker
            logDir = userHome + File.separator + "Library" + File.separator + "Logs"
                    + File.separator + appName;
        } else {
            // Linux : ~/.local/share/FinanceTracker/logs
            logDir = userHome + File.separator + ".local" + File.separator + "share"
                    + File.separator + appName + File.separator + "logs";
        }

        return logDir;
    }
}