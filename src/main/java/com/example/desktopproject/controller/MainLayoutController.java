package com.example.desktopproject.controller;

import com.example.desktopproject.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import org.apache.log4j.Logger;

import java.io.IOException;

public class MainLayoutController {
    private static final Logger logger = Logger.getLogger(ExpensesTabController.class);

    @FXML
    private StackPane contentArea;

    public void initialize() {
        loadView("dashboard-view.fxml");
    }

    public void loadView(String fxmlFile) {
        try {
            logger.debug("Chargement de la vue: " + fxmlFile);
            Parent view = FXMLLoader.load(HelloApplication.class.getResource(fxmlFile));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            logger.error("Erreur lors du chargement de la vue: " + fxmlFile, e);
        }
    }
}