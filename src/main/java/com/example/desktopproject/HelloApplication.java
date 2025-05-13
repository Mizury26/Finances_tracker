package com.example.desktopproject;

import com.example.desktopproject.db.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Check if the database is OK
        if (!Database.isOK()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de connexion à la base de données");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tableau-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Finance Tracker");
        stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("/images/logoDesktop.png")));
        stage.show();
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}