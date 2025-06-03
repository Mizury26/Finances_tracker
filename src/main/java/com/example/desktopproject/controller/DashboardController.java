package com.example.desktopproject.controller;

import com.example.desktopproject.charts.LineChart;
import com.example.desktopproject.charts.PieChart;
import com.example.desktopproject.db.ExpenseDAO;
import com.example.desktopproject.model.Expense;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class DashboardController {
    private static final Logger logger = Logger.getLogger(DashboardController.class);

    @FXML
    private StackPane pieChartContainer;

    @FXML
    private StackPane lineChartContainer;

    @FXML
    private ComboBox<Month> monthSelector;

    @FXML
    private Label noDataLabel;

    private List<Expense> currentMonthExpenses;
    private List<Expense> last12MonthExpenses;
    private PieChart pieChart;
    private LineChart lineChart;

    private int currentMonth;
    private int currentYear;


    @FXML
    public void initialize() {
        logger.debug("Initialisation du dashboard");

        LocalDate now = LocalDate.now();
        currentMonth = now.getMonthValue();
        currentYear = now.getYear();

        // Initialisation du graphique en camembert
        pieChart = new PieChart("Répartition des dépenses");
        lineChart = new LineChart("Évolution des dépenses");

        // Chargement des données
        loadExpenses();

        // Configuration du sélecteur de mois
        setupMonthSelector();

        // Ajout du graphique au conteneur
        pieChartContainer.getChildren().add(pieChart.getChartNode());
        lineChartContainer.getChildren().add(lineChart.getChartNode());

        // Affichage du graphique
        updateChartsForSelectedMonth();

        logger.debug("Dashboard initialisé avec succès");
    }

    private void loadExpenses() {
        currentMonthExpenses = ExpenseDAO.getByMonth(currentMonth, currentYear);
        last12MonthExpenses = ExpenseDAO.get12MonthsBefore(currentMonth, currentYear);
    }

    private void setupMonthSelector() {
        if (monthSelector != null) {
            // Configurer le convertisseur pour afficher les noms de mois en français
            monthSelector.setConverter(new StringConverter<Month>() {
                @Override
                public String toString(Month month) {
                    return month.getDisplayName(TextStyle.FULL, Locale.FRANCE);
                }

                @Override
                public Month fromString(String string) {
                    for (Month month : Month.values()) {
                        if (month.getDisplayName(TextStyle.FULL, Locale.FRANCE).equals(string)) {
                            return month;
                        }
                    }
                    return null;
                }
            });

            // Ajouter tous les mois
            monthSelector.getItems().addAll(Month.values());

            // Sélectionner le mois courant
            monthSelector.setValue(LocalDate.now().getMonth());

            // Action lors du changement de mois
            monthSelector.setOnAction(event -> updateChartsForSelectedMonth());
        }
    }

    private void updateChartsForSelectedMonth() {
        if (monthSelector != null && monthSelector.getValue() != null) {
            Month selectedMonth = monthSelector.getValue();
            currentMonth = selectedMonth.getValue();

            loadExpenses();

            YearMonth yearMonth = YearMonth.of(currentYear, selectedMonth);

            boolean hasData = !currentMonthExpenses.isEmpty();
            noDataLabel.setVisible(!hasData);
            pieChartContainer.setVisible(hasData);
            lineChartContainer.setVisible(hasData);

            // Mettre à jour le titre avec le nom en français
            String monthName = selectedMonth.getDisplayName(TextStyle.FULL, Locale.FRANCE);
            pieChart.setTitle("Répartition des dépenses - " + monthName);

            if (hasData) {
                pieChart.createChart(currentMonthExpenses);
                lineChart.createChart(last12MonthExpenses, yearMonth);
            } else {
                // Effacer les graphiques précédents si nécessaire
                pieChart.clear();
                lineChart.clear();
            }
        }
    }

}