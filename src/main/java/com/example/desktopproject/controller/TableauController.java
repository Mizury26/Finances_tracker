package com.example.desktopproject.controller;

import com.example.desktopproject.component.ToggleCustomButton;
import com.example.desktopproject.db.ExpenseDAO;
import com.example.desktopproject.model.ChangeType;
import com.example.desktopproject.model.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import org.apache.log4j.Logger;
import com.example.desktopproject.API.ApiCall;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

public class TableauController {
    private static final Logger logger = Logger.getLogger(TableauController.class);

    @FXML
    private ToggleCustomButton toggleCustomButton;

    @FXML
    private TableView<Expense> tableView;

    @FXML
    private TableColumn<Expense, String> totalColumn;

    @FXML
    private TableColumn<Expense, String> housingColumn;

    @FXML
    private TableColumn<Expense, String> foodColumn;

    @FXML
    private TableColumn<Expense, String> goingOutColumn;

    @FXML
    private TableColumn<Expense, String> transportationColumn;

    @FXML
    private TableColumn<Expense, String> travelColumn;

    @FXML
    private TableColumn<Expense, String> taxColumn;

    @FXML
    private TableColumn<Expense, String> othersColumn;

    private ObservableList<Expense> expenses = FXCollections.observableArrayList();
    private DecimalFormat formatter = new DecimalFormat("#,##0.00");
    private double exchangeRate = 1.0; // Taux de change EUR -> USD

    @FXML
    public void initialize() {
        logger.debug("Initialisation du TableauController");

        // Récupération du taux de change une seule fois
        ChangeType rates = ApiCall.fetchData();
        if (rates != null) {
            exchangeRate = rates.getRate("EUR"); // ou la méthode appropriée pour récupérer le taux USD
        }

        // Configuration des colonnes monétaires
        setupCurrencyColumns();

        List<Expense> dbExpenses = ExpenseDAO.fetchAllDataFromDB();
        expenses.addAll(dbExpenses);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(expenses);

        logger.info("Tableau de dépenses initialisé avec " + expenses.size() + " entrées");

        toggleCustomButton.setLeftValue("€");
        toggleCustomButton.setRightValue("$");
        toggleCustomButton.setOnAction(event -> convertCurrency());
    }

    private void setupCurrencyColumns() {
        // Toutes les colonnes monétaires
        totalColumn.setCellValueFactory(cellData -> {
            Expense expense = cellData.getValue();
            return new SimpleStringProperty(formatter.format(expense.getTotal()) + " €");
        });

        housingColumn.setCellValueFactory(cellData -> {
            Expense expense = cellData.getValue();
            return new SimpleStringProperty(formatter.format(expense.getHousing()) + " €");
        });

        foodColumn.setCellValueFactory(cellData -> {
            Expense expense = cellData.getValue();
            return new SimpleStringProperty(formatter.format(expense.getFood()) + " €");
        });

        goingOutColumn.setCellValueFactory(cellData -> {
            Expense expense = cellData.getValue();
            return new SimpleStringProperty(formatter.format(expense.getGoingOut()) + " €");
        });

        transportationColumn.setCellValueFactory(cellData -> {
            Expense expense = cellData.getValue();
            return new SimpleStringProperty(formatter.format(expense.getTransportation()) + " €");
        });

        travelColumn.setCellValueFactory(cellData -> {
            Expense expense = cellData.getValue();
            return new SimpleStringProperty(formatter.format(expense.getTravel()) + " €");
        });

        taxColumn.setCellValueFactory(cellData -> {
            Expense expense = cellData.getValue();
            return new SimpleStringProperty(formatter.format(expense.getTax()) + " €");
        });

        othersColumn.setCellValueFactory(cellData -> {
            Expense expense = cellData.getValue();
            return new SimpleStringProperty(formatter.format(expense.getOthers()) + " €");
        });
    }

    @FXML
    public void addExpense() {
        logger.debug("Ouverture du dialogue d'ajout de dépense");
        Dialog<Expense> dialog = new DialogController();
        Optional<Expense> result = dialog.showAndWait();

        if (result.isPresent()) {
            Expense expense = result.get();
            expenses.add(expense);
            ExpenseDAO.insertExpense(expense);
            logger.info("Dépense ajoutée: " + expense.getDate() + " - " + expense.getTotal() + "€");
        } else {
            logger.debug("Ajout de dépense annulé");
        }
    }

    @FXML
    public void combinedAction(ActionEvent event) {
        convertCurrency();
    }

    private void convertCurrency() {
        if (toggleCustomButton.getLabel().equals("$")) {
            logger.info("Conversion des dépenses en dollars");
            // Conversion vers dollars pour toutes les colonnes
            totalColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
                double converted = expense.getTotal() * exchangeRate;
                return new SimpleStringProperty(formatter.format(converted) + " $");
            });

            housingColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
                double converted = expense.getHousing() * exchangeRate;
                return new SimpleStringProperty(formatter.format(converted) + " $");
            });

            foodColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
                double converted = expense.getFood() * exchangeRate;
                return new SimpleStringProperty(formatter.format(converted) + " $");
            });

            goingOutColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
                double converted = expense.getGoingOut() * exchangeRate;
                return new SimpleStringProperty(formatter.format(converted) + " $");
            });

            transportationColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
                double converted = expense.getTransportation() * exchangeRate;
                return new SimpleStringProperty(formatter.format(converted) + " $");
            });

            travelColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
                double converted = expense.getTravel() * exchangeRate;
                return new SimpleStringProperty(formatter.format(converted) + " $");
            });

            taxColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
                double converted = expense.getTax() * exchangeRate;
                return new SimpleStringProperty(formatter.format(converted) + " $");
            });

            othersColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
                double converted = expense.getOthers() * exchangeRate;
                return new SimpleStringProperty(formatter.format(converted) + " $");
            });
        } else {
            logger.info("Affichage des dépenses en euros");
            // Retour aux euros (valeurs originales)
            totalColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
                return new SimpleStringProperty(formatter.format(expense.getTotal()) + " €");
            });

            housingColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
                return new SimpleStringProperty(formatter.format(expense.getHousing()) + " €");
            });

            foodColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
                return new SimpleStringProperty(formatter.format(expense.getFood()) + " €");
            });

            goingOutColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
                return new SimpleStringProperty(formatter.format(expense.getGoingOut()) + " €");
            });

            transportationColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
                return new SimpleStringProperty(formatter.format(expense.getTransportation()) + " €");
            });

            travelColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
                return new SimpleStringProperty(formatter.format(expense.getTravel()) + " €");
            });

            taxColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
                return new SimpleStringProperty(formatter.format(expense.getTax()) + " €");
            });

            othersColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
                return new SimpleStringProperty(formatter.format(expense.getOthers()) + " €");
            });
        }

        // Rafraîchir le tableau
        tableView.refresh();
    }
}