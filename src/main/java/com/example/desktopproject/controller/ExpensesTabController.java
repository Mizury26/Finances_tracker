package com.example.desktopproject.controller;

import com.example.desktopproject.API.ApiCall;
import com.example.desktopproject.component.ToggleCustomButton;
import com.example.desktopproject.db.ExpenseDAO;
import com.example.desktopproject.model.ChangeType;
import com.example.desktopproject.model.Expense;
import com.example.desktopproject.model.Monetary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

public class ExpensesTabController {
    private static final Logger logger = Logger.getLogger(ExpensesTabController.class);
    private final ObservableList<Expense> expenses = FXCollections.observableArrayList();
    private final DecimalFormat formatter = new DecimalFormat("#,##0.00");
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
    private double exchangeRate = 1.0; // Taux de change EUR -> USD

    @FXML
    public void initialize() {
        logger.debug("Initialisation du TableauController");

        // Récupération du taux de change une seule fois
        ChangeType rates = ApiCall.fetchData();
        if (rates != null) {
            exchangeRate = rates.getRate("EUR"); // ou la méthode appropriée pour récupérer le taux USD
        }

        List<Expense> dbExpenses = ExpenseDAO.fetchAllDataFromDB();
        expenses.addAll(dbExpenses);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(expenses);

        logger.info("Tableau de dépenses initialisé avec " + expenses.size() + " entrées");

        toggleCustomButton.setLeftValue("€");
        toggleCustomButton.setRightValue("$");
        toggleCustomButton.setOnAction(this::combinedAction);
    }

    @FXML
    public void addExpense() {
        logger.debug("Ouverture du dialogue d'ajout de dépense");
        Dialog<Expense> dialog = new ExpenseDialogController();
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
        if (toggleCustomButton.getSelectedLabel().equals("$")) {
            Monetary.rate = (float) exchangeRate;
        } else {
            Monetary.rate = 1f;
        }

        Monetary.unit = toggleCustomButton.getSelectedLabel();
        tableView.refresh();
    }
}