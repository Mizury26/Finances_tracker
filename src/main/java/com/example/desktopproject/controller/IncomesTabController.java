package com.example.desktopproject.controller;

import com.example.desktopproject.API.ApiCall;
import com.example.desktopproject.component.ToggleCustomButton;
import com.example.desktopproject.db.IncomeDAO;
import com.example.desktopproject.model.ChangeType;
import com.example.desktopproject.model.Income;
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

public class IncomesTabController {
    private static final Logger logger = Logger.getLogger(IncomesTabController.class);
    private final ObservableList<Income> incomes = FXCollections.observableArrayList();
    private final DecimalFormat formatter = new DecimalFormat("#,##0.00");
    @FXML
    private ToggleCustomButton toggleCustomButton;
    @FXML
    private TableView<Income> tableView;
    @FXML
    private TableColumn<Income, String> totalColumn;
    @FXML
    private TableColumn<Income, String> salaryColumn;
    @FXML
    private TableColumn<Income, String> helperColumn;
    @FXML
    private TableColumn<Income, String> selfEnterpriseColumn;
    @FXML
    private TableColumn<Income, String> passiveIncomeColumn;
    @FXML
    private TableColumn<Income, String> otherColumn;
    private double exchangeRate = 1.0; // Taux de change EUR -> USD

    @FXML
    public void initialize() {
        logger.debug("Initialisation du IncomesTabController");

        // Récupération du taux de change une seule fois
        ChangeType rates = ApiCall.fetchData();
        if (rates != null) {
            exchangeRate = rates.getRate("EUR"); // ou la méthode appropriée pour récupérer le taux USD
        }

        List<Income> dbIncomes = IncomeDAO.fetchAllDataFromDB();
        incomes.addAll(dbIncomes);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(incomes);

        logger.info("Tableau de dépenses initialisé avec " + incomes.size() + " entrées");

        toggleCustomButton.setLeftValue("€");
        toggleCustomButton.setRightValue("$");
        toggleCustomButton.setOnAction(this::combinedAction);
    }

    @FXML
    public void addIncomes() {
        logger.debug("Ouverture du dialogue d'ajout de dépense");
        Dialog<Income> dialog = new IncomeDialogController();
        Optional<Income> result = dialog.showAndWait();

        if (result.isPresent()) {
            Income income = result.get();
            incomes.add(income);
            IncomeDAO.insertIncome(income);
            logger.info("Revenus ajouté: " + income.getDate() + " - " + income.getTotal() + "€");
        } else {
            logger.debug("Ajout de revenus annulé");
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