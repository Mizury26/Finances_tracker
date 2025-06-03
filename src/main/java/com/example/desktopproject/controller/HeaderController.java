package com.example.desktopproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import org.apache.log4j.Logger;

public class HeaderController {
    private static final Logger logger = Logger.getLogger(HeaderController.class);


    @FXML
    public void changeScreen(ActionEvent event) {
        Object source = event.getSource();
        String screen = null;

        if (source instanceof Button button) {
            screen = (String) button.getUserData();
        }

        if (screen == null) {
            logger.warn("Aucune vue spécifiée.");
            return;
        }

        logger.info("Changement d'écran vers : " + screen);

        Scene scene = ((Button) source).getScene();
        MainLayoutController mainController = (MainLayoutController) scene.getUserData();

        switch (screen) {
            case "dashboard":
                mainController.loadView("dashboard-view.fxml");
                logger.debug("Navigation vers le tableau de bord");
                break;
            case "expenses":
                mainController.loadView("expenses-tab-view.fxml");
                logger.debug("Navigation vers les dépenses");
                break;
            case "incomes":
                mainController.loadView("incomes-tab-view.fxml");
                logger.debug("Navigation vers les revnue");
                break;
        }
    }
}