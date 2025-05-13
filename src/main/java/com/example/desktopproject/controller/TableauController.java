
package com.example.desktopproject.controller;

import com.example.desktopproject.db.ExpenseDAO;
import com.example.desktopproject.model.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import java.util.List;
import java.util.Optional;

public class TableauController {
    @FXML
    private TableView<Expense> tableView;

    private ObservableList<Expense> expenses = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Exemple de données
        List<Expense> dbExpenses = ExpenseDAO.fetchAllDataFromDB();
        expenses.addAll(dbExpenses);
        tableView.setItems(expenses);
    }

    @FXML
    public void addExpense() {
        Dialog<Expense> dialog = new DialogController();
        Optional<Expense> result = dialog.showAndWait();

        if (result.isPresent()) {
            Expense expense = result.get();
            expenses.add(expense);
            ExpenseDAO.insertExpense(expense);
            System.out.println("Dépense ajoutée: " + expense.getDate() + " - " + expense.getTotal() + "€");
        }
    }
}