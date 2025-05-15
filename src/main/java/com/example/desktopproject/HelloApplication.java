package com.example.desktopproject;

import com.example.desktopproject.db.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;

public class HelloApplication extends Application {
    private static final Logger logger = Logger.getLogger(HelloApplication.class);

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Démarrage de l'application Finance Tracker");

        // Check if the database is OK
        logger.debug("Vérification de la connexion à la base de données");
        if (!Database.isOK()) {
            logger.error("Échec de la connexion à la base de données");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de connexion à la base de données");
            alert.showAndWait();
            return;
        }
        logger.debug("Connexion à la base de données réussie");

        try {
            logger.debug("Chargement du fichier FXML: tableau-view.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tableau-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Finance Tracker");

            logger.debug("Chargement de l'icône de l'application");
            stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("/images/logoDesktop.png")));

            stage.setScene(scene);
            stage.show();
            logger.info("Application démarrée avec succès");
        } catch (IOException e) {
            logger.error("Erreur lors du chargement de l'interface utilisateur", e);
            throw e;
        }
    }

    public static void main(String[] args) {
        logger.info("Lancement de l'application");
        launch();
    }
}