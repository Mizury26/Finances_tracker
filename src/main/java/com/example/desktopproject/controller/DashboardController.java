package com.example.desktopproject.controller;

import com.example.desktopproject.charts.BarChart;
import com.example.desktopproject.charts.LineChart;
import com.example.desktopproject.charts.PieChart;
import com.example.desktopproject.db.ExpenseDAO;
import com.example.desktopproject.db.IncomeDAO;
import com.example.desktopproject.model.Expense;
import com.example.desktopproject.model.Income;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardController {
    private static final Logger logger = Logger.getLogger(DashboardController.class);

    @FXML
    private StackPane pieChartContainer;

    @FXML
    private StackPane lineChartContainer;

    @FXML
    private StackPane barChartContainer;

    @FXML
    private ComboBox<Month> monthSelector;

    @FXML
    private Label noDataLabel;

    @FXML
    private Label welcomeMessage;

    @FXML
    private Label currentDate;

    private List<Expense> currentMonthExpenses;
    private List<Expense> last12MonthExpenses;
    private List<Income> last12MonthIncomes;
    private PieChart pieChart;
    private LineChart lineChart;
    private BarChart barChart;

    private int currentMonth;
    private int currentYear;


    @FXML
    public void initialize() {
        String userName = System.getProperty("user.name");
        welcomeMessage.setText("Bienvenue " + userName + " !");
        Locale locale = new Locale("fr", "FR");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());
        currentDate.setText(date);

        logger.debug("Initialisation du dashboard");

        LocalDate now = LocalDate.now();
        currentMonth = now.getMonthValue();
        currentYear = now.getYear();

        // Initialisation du graphique en camembert
        pieChart = new PieChart("Répartition des dépenses");
        lineChart = new LineChart("Évolution des dépenses");
        barChart = new BarChart("Dépenses VS revenus");

        // Chargement des données
        loadExpenses();

        // Configuration du sélecteur de mois
        setupMonthSelector();

        // Ajout du graphique au conteneur
        pieChartContainer.getChildren().add(pieChart.getChartNode());
        lineChartContainer.getChildren().add(lineChart.getChartNode());
        barChartContainer.getChildren().add(barChart.getChartNode());

        // Affichage du graphique
        updateChartsForSelectedMonth();

        logger.debug("Dashboard initialisé avec succès");
    }

    private void loadExpenses() {
        currentMonthExpenses = ExpenseDAO.getByMonth(currentMonth, currentYear);
        last12MonthExpenses = ExpenseDAO.get12MonthsBefore(currentMonth, currentYear);
        last12MonthIncomes = IncomeDAO.get12MonthsBefore(currentMonth, currentYear);
        System.out.println("Chargement des dépenses du mois " + currentMonth + " de l'année " + currentYear);
        System.out.println(last12MonthExpenses);

        logger.debug("Chargement de " + currentMonthExpenses.size() + " dépenses du mois et " +
                last12MonthExpenses.size() + " dépenses des 12 derniers mois");
        logger.debug("Chargement de " + last12MonthIncomes.size() + " revenus des 12 derniers mois");
    }

    private void setupMonthSelector() {
        if (monthSelector != null) {
            // Configurer le convertisseur pour afficher les noms de mois en français
            monthSelector.setConverter(new StringConverter<Month>() {
                @Override
                public String toString(Month month) {
                    String monthName = month.getDisplayName(TextStyle.FULL, Locale.FRANCE);
                    monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);

                    return monthName;
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


            String monthName = selectedMonth.getDisplayName(TextStyle.FULL, Locale.FRANCE);
            pieChart.setTitle(monthName + ", " + currentYear);
            barChart.setTitle(monthName + ", " + currentYear);
            lineChart.setTitle(monthName + ", " + currentYear);

            if (hasData) {
                pieChart.createChart(currentMonthExpenses);
            } else {
                // Effacer le graphique précédents si nécessaire
                pieChart.clear();
            }

            lineChart.createChart(last12MonthExpenses, yearMonth);

            // Mise à jour du graphique à barres
            if (!last12MonthIncomes.isEmpty()) {
                barChart.createChart(last12MonthExpenses, last12MonthIncomes, selectedMonth);
            } else {
                barChart.clear();
            }
        }
    }

}