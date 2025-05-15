package com.example.desktopproject.controller;

import com.example.desktopproject.db.ExpenseDAO;
import com.example.desktopproject.model.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class TableauController {
    private static final Logger logger = Logger.getLogger(TableauController.class);

    @FXML
    private TableView<Expense> tableView;

    private ObservableList<Expense> expenses = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        logger.debug("Initialisation du TableauController");
        List<Expense> dbExpenses = ExpenseDAO.fetchAllDataFromDB();
        expenses.addAll(dbExpenses);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(expenses);
        logger.info("Tableau de dépenses initialisé avec " + expenses.size() + " entrées");
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
}