package com.example.desktopproject.controller;

import com.example.desktopproject.charts.PieChart;
import com.example.desktopproject.db.ExpenseDAO;
import com.example.desktopproject.model.Expense;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DashboardController {
    private static final Logger logger = Logger.getLogger(DashboardController.class);

    @FXML
    private StackPane pieChartContainer;

    @FXML
    private ComboBox<String> monthSelector;

    private List<Expense> allExpenses;
    private PieChart pieChart;

    @FXML
    public void initialize() {
        logger.debug("Initialisation du dashboard");

        // Initialisation du graphique en camembert
        pieChart = new PieChart("Répartition des dépenses");

        // Chargement des données
        loadAllExpenses();

        // Configuration du sélecteur de mois
        setupMonthSelector();

        // Affichage du graphique
        updatePieChartForSelectedMonth();

        // Ajout du graphique au conteneur
        pieChartContainer.getChildren().add(pieChart.getChartNode());

        logger.debug("Dashboard initialisé avec succès");
    }

    private void loadAllExpenses() {
        allExpenses = ExpenseDAO.fetchAllDataFromDB();
        logger.debug("Chargement de " + allExpenses.size() + " dépenses au total");
    }

    private void setupMonthSelector() {
        if (monthSelector != null) {
            // Remplir le sélecteur avec les mois disponibles dans les données
            Locale locale = Locale.FRANCE;

            // Version simple: tous les mois de l'année
            for (Month month : Month.values()) {
                String monthName = month.getDisplayName(TextStyle.FULL, locale);
                monthSelector.getItems().add(monthName);
            }

            // Sélectionner le mois courant
            String currentMonth = LocalDate.now().getMonth()
                    .getDisplayName(TextStyle.FULL, locale);
            monthSelector.setValue(currentMonth);

            // Action lors du changement de mois
            monthSelector.setOnAction(event -> updatePieChartForSelectedMonth());
        }
    }

    private void updatePieChartForSelectedMonth() {
        if (monthSelector != null && monthSelector.getValue() != null) {
            String selectedMonth = monthSelector.getValue();

            // Filtrer les dépenses pour le mois sélectionné
            List<Expense> monthlyExpenses = filterExpensesByMonth(selectedMonth);

            // Mettre à jour le titre
            pieChart.setTitle("Répartition des dépenses - " + selectedMonth);

            // Charger les données et créer le graphique
            pieChart.loadData(monthlyExpenses);
            pieChart.createChart();
        }
    }

    private List<Expense> filterExpensesByMonth(String monthName) {
        return allExpenses.stream()
                .filter(expense -> {
                    String expenseMonth = expense.getDate().getMonth()
                            .getDisplayName(TextStyle.FULL, Locale.FRANCE);
                    return expenseMonth.equalsIgnoreCase(monthName);
                })
                .collect(Collectors.toList());
    }
}