package com.example.desktopproject.controller;

import com.example.desktopproject.HelloApplication;
import com.example.desktopproject.model.Expense;
import com.example.desktopproject.model.Income;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IncomeDialogController extends Dialog<Income> {
    private static final Logger logger = Logger.getLogger(IncomeDialogController.class);

    @FXML
    private DatePicker periodeField;
    @FXML
    private TextField salaryField;
    @FXML
    private TextField helpersField;
    @FXML
    private TextField selfEnterpriseField;
    @FXML
    private TextField passiveIncomeField;
    @FXML
    private TextField othersField;
    @FXML
    private Label totalLabel;

    private List<TextField> moneyFields = new ArrayList<>();

    public IncomeDialogController() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("add-income-view.fxml"));

            loader.setController(this);
            DialogPane dialogPane = loader.load();

            setDialogPane(dialogPane);
            setTitle("Ajouter un revenu");
            Stage stage = (Stage) dialogPane.getScene().getWindow();
            stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("/images/logoDesktop.png")));

            logger.debug("IncomeDialogController initialisé avec succès");

            setResultConverter(buttonType -> {
                if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    logger.info("Création de nouveaux revenus");
                    return createIncome();
                }
                logger.debug("Annulation de la création de revenus");
                return null; // Retourne null si Annuler est cliqué
            });

        } catch (IOException e) {
            logger.error("Erreur lors de l'ouverture du dialogue", e);
            e.printStackTrace();
            // Afficher un message d'erreur à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de l'ouverture du dialogue");
            alert.showAndWait();
        }
    }

    @FXML
    public void initialize() {
        // Initialiser la liste des champs monétaires
        moneyFields = new ArrayList<>();
        moneyFields.add(salaryField);
        moneyFields.add(helpersField);
        moneyFields.add(selfEnterpriseField);
        moneyFields.add(passiveIncomeField);
        moneyFields.add(othersField);

        // Ajouter des écouteurs pour chaque champ
        for (TextField field : moneyFields) {
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                updateTotal();
            });
        }

        // Initialiser les champs avec "0"
        for (TextField field : moneyFields) {
            field.setText("0");
        }

        // Calculer le total initial
        updateTotal();
    }

    /**
     * Met à jour le total affiché
     */
    private void updateTotal() {
        float total = 0.0f;

        for (TextField field : moneyFields) {
            try {
                total += Float.parseFloat(field.getText());
            } catch (NumberFormatException e) {
                // Ignorer les valeurs non numériques
            }
        }

        totalLabel.setText(String.format("%.2f €", total));
    }

    /**
     * Vérifie si les entrées sont valides
     */
    public boolean isInputValid() {
        StringBuilder errorMessage = new StringBuilder();

        // Vérifier la période
        if (periodeField.getValue() == null) {
            errorMessage.append("Période non valide!\n");
        }

        // Vérifier tous les champs monétaires
        for (TextField field : moneyFields) {
            try {
                float value = Float.parseFloat(field.getText());
                if (value < 0) {
                    errorMessage.append("Les montants ne peuvent pas être négatifs!\n");
                    break;
                }
            } catch (NumberFormatException e) {
                errorMessage.append("Veuillez entrer des montants valides!\n");
                break;
            }
        }

        // S'il y a des erreurs, les afficher
        if (errorMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Champs invalides");
            alert.setHeaderText("Veuillez corriger les champs invalides");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return false;
        }

        return true;
    }

    public Income createIncome() {
        if (!isInputValid()) {
            logger.warn("Tentative de création d'une dépense avec des données invalides");
            return null;
        }

        LocalDate date = periodeField.getValue();
        float salary = parseFloatSafe(salaryField.getText());
        float helpers = parseFloatSafe(helpersField.getText());
        float selfEnterprise = parseFloatSafe(selfEnterpriseField.getText());
        float passiveIncome = parseFloatSafe(passiveIncomeField.getText());
        float others = parseFloatSafe(othersField.getText());

        float total = salary + helpers + selfEnterprise + passiveIncome + others;

        logger.info("Nouvelle dépense créée pour la date: " + date + " avec un total de: " + total);

        return new Income(date, salary, helpers, selfEnterprise,
                passiveIncome, others);
    }

    /**
     * Parse une String en float, retourne 0.0f en cas d'erreur
     */
    private float parseFloatSafe(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }
}