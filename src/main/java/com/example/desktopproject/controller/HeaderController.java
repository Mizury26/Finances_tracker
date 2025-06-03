package com.example.desktopproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import org.apache.log4j.Logger;

public class HeaderController {
    private static final Logger logger = Logger.getLogger(HeaderController.class);


    @FXML
    public void changeScreen(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String screen = (String) menuItem.getUserData();
        logger.info("Changement d'écran vers : " + screen);

        Scene scene = menuItem.getParentPopup().getOwnerWindow().getScene();
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
                logger.debug("Navigation vers les dépenses");
                break;
            default:
                mainController.loadView("dashboard-view.fxml");
                break;
        }
    }
}