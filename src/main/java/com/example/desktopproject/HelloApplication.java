package com.example.desktopproject;

import com.example.desktopproject.db.Database;
import com.example.desktopproject.utils.LogConfig;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;

public class HelloApplication extends Application {
    // Ne pas initialiser ici - utiliser une méthode getter
    private static Logger logger;

    // Getter pour logger qui s'assure que la config est initialisée
    private static Logger getLogger() {
        if (logger == null) {
            // Configure les logs si ce n'est pas déjà fait
            if (System.getProperty("finance.log.dir") == null) {
                LogConfig.configureLogDirectory();
            }
            logger = Logger.getLogger(HelloApplication.class);
        }
        return logger;
    }

    public static void main(String[] args) {
        // Configure les logs avant toute utilisation
        LogConfig.configureLogDirectory();

        getLogger().info("Lancement de l'application");
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        getLogger().info("Démarrage de l'application Finance Tracker");

        // Check if the database is OK
        getLogger().debug("Vérification de la connexion à la base de données");
        if (!Database.isOK()) {
            getLogger().error("Échec de la connexion à la base de données");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de connexion à la base de données");
            alert.showAndWait();
            return;
        }
        getLogger().debug("Connexion à la base de données réussie");

        try {
            getLogger().debug("Chargement du fichier FXML: tableau-view.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tableau-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Finance Tracker");

            getLogger().debug("Chargement de l'icône de l'application");
            stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("/images/logoDesktop.png")));

            stage.setScene(scene);
            stage.show();
            getLogger().info("Application démarrée avec succès");
        } catch (IOException e) {
            getLogger().error("Erreur lors du chargement de l'interface utilisateur", e);
            throw e;
        }
    }
}