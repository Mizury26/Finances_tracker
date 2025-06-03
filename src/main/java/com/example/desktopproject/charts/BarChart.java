package com.example.desktopproject.charts;

import com.example.desktopproject.model.Expense;
import com.example.desktopproject.model.Income;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class BarChart {
    private static final Logger logger = Logger.getLogger(BarChart.class);

    private final javafx.scene.chart.BarChart<String, Number> barChart;
    private final CategoryAxis xAxis;
    private final NumberAxis yAxis;
    private final DecimalFormat formatter = new DecimalFormat("#,##0.00");

    public BarChart(String title) {
        // Configuration des axes
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        xAxis.setLabel("Mois");
        yAxis.setLabel("Montant (€)");

        // Création du graphique
        barChart = new javafx.scene.chart.BarChart<>(xAxis, yAxis);
        barChart.setTitle(title);
        barChart.setAnimated(true);

        // Style du graphique pour une meilleure lisibilité
        barChart.setLegendVisible(true);
        barChart.setCategoryGap(20);
        barChart.setBarGap(3);
    }


    public void createChart(List<Expense> expenses, List<Income> incomes, Month selectedMonth) {
        if (expenses == null || incomes == null) {
            logger.error("Les données fournies pour le BarChart sont invalides (null)");
            return;
        }

        // Effacer les données existantes
        barChart.getData().clear();

        // Grouper les dépenses et revenus par mois
        Map<Month, Float> expenseTotals = calculateMonthlyTotals(expenses, Expense.class);
        Map<Month, Float> incomeTotals = calculateMonthlyTotals(incomes, Income.class);

        // Obtenir les 12 mois précédents à partir du mois sélectionné
        List<Month> months = getLast12MonthsFrom(selectedMonth);

        // Séries pour les dépenses et les revenus
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Dépenses");

        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Revenus");

        // Ajouter les données aux séries
        Locale locale = Locale.FRANCE;
        for (Month month : months) {
            String monthName = month.getDisplayName(TextStyle.FULL, locale);
            float expenseTotal = expenseTotals.getOrDefault(month, 0f);
            float incomeTotal = incomeTotals.getOrDefault(month, 0f);

            expenseSeries.getData().add(new XYChart.Data<>(monthName, expenseTotal));
            incomeSeries.getData().add(new XYChart.Data<>(monthName, incomeTotal));
        }

        // Ajouter les séries au graphique
        barChart.getData().addAll(expenseSeries, incomeSeries);

        // Ajouter des tooltips
        addTooltips(expenseSeries, incomeSeries);

        logger.debug("Graphique à barres créé avec succès pour les 12 mois précédant " +
                selectedMonth.getDisplayName(TextStyle.FULL, locale));
    }

    /**
     * Retourne une liste des 12 mois précédents à partir du mois sélectionné, dans l'ordre chronologique
     *
     * @param selectedMonth Le mois sélectionné
     * @return Liste des 12 mois précédents, incluant le mois sélectionné
     */
    private List<Month> getLast12MonthsFrom(Month selectedMonth) {
        List<Month> months = new ArrayList<>();

        // Ajouter les 11 mois précédents et le mois sélectionné
        for (int i = 11; i >= 0; i--) {
            Month month = selectedMonth.minus(i);
            months.add(month);
        }

        return months;
    }


    private <T> Map<Month, Float> calculateMonthlyTotals(List<T> items, Class<? extends T> clazz) {
        if (items == null || items.isEmpty()) {
            return new HashMap<>();
        }

        try {
            if (clazz == Expense.class) {
                return items.stream()
                        .map(item -> (Expense) item)
                        .collect(Collectors.groupingBy(
                                expense -> expense.getDate().getMonth(),
                                Collectors.collectingAndThen(
                                        Collectors.summingDouble(expense -> expense.getStrictTotal()),
                                        result -> result.floatValue()
                                )
                        ));
            } else if (clazz == Income.class) {
                return items.stream()
                        .map(item -> (Income) item)
                        .collect(Collectors.groupingBy(
                                income -> income.getDate().getMonth(),
                                Collectors.collectingAndThen(
                                        Collectors.summingDouble(income -> income.getStrictTotal()),
                                        result -> result.floatValue()
                                )
                        ));
            }
        } catch (Exception e) {
            logger.error("Erreur lors du calcul des totaux mensuels", e);
        }
        return new HashMap<>();
    }

    private void addTooltips(XYChart.Series<String, Number> expenseSeries, XYChart.Series<String, Number> incomeSeries) {
        for (XYChart.Data<String, Number> data : expenseSeries.getData()) {
            String formattedValue = formatter.format(data.getYValue());
            Tooltip tooltip = new Tooltip(data.getXValue() + "\nDépenses : " + formattedValue + " €");
            tooltip.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
            Tooltip.install(data.getNode(), tooltip);

            // Effet visuel au survol
            data.getNode().setOnMouseEntered(event -> data.getNode().setStyle("-fx-opacity: 0.8;"));
            data.getNode().setOnMouseExited(event -> data.getNode().setStyle("-fx-opacity: 1;"));
        }

        for (XYChart.Data<String, Number> data : incomeSeries.getData()) {
            String formattedValue = formatter.format(data.getYValue());
            Tooltip tooltip = new Tooltip(data.getXValue() + "\nRevenus : " + formattedValue + " €");
            tooltip.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
            Tooltip.install(data.getNode(), tooltip);

            // Effet visuel au survol
            data.getNode().setOnMouseEntered(event -> data.getNode().setStyle("-fx-opacity: 0.8;"));
            data.getNode().setOnMouseExited(event -> data.getNode().setStyle("-fx-opacity: 1;"));
        }
    }

    public void clear() {
        barChart.getData().clear();
        logger.debug("Graphique à barres réinitialisé");
    }

    public Node getChartNode() {
        return barChart;
    }

    public void setTitle(String title) {
        barChart.setTitle(title);
    }
}