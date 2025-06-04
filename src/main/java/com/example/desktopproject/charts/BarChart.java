package com.example.desktopproject.charts;

import com.example.desktopproject.model.Expense;
import com.example.desktopproject.model.Income;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.apache.log4j.Logger;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class BarChart {
    private static final Logger logger = Logger.getLogger(BarChart.class);

    private final javafx.scene.chart.BarChart<String, Number> barChart;
    private final CategoryAxis xAxis;
    private final NumberAxis yAxis;

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

        xAxis.setCategories(FXCollections.observableArrayList(
                months.stream()
                        .map(month -> month.getDisplayName(TextStyle.SHORT, Locale.FRANCE))
                        .collect(Collectors.toList())
        ));

        // Séries pour les dépenses et les revenus
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Dépenses");

        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Revenus");

        // Ajouter les données aux séries
        Locale locale = Locale.FRANCE;
        for (Month month : months) {
            String monthName = month.getDisplayName(TextStyle.SHORT, locale);
            float expenseTotal = expenseTotals.getOrDefault(month, 0f);
            float incomeTotal = incomeTotals.getOrDefault(month, 0f);

            expenseSeries.getData().add(new XYChart.Data<>(monthName, expenseTotal));
            incomeSeries.getData().add(new XYChart.Data<>(monthName, incomeTotal));
        }

        // Ajouter les séries au graphique
        barChart.getData().addAll(expenseSeries, incomeSeries);

        if (!barChart.getData().isEmpty()) {
            Platform.runLater(() -> {
                for (Node n : barChart.lookupAll(".series0.chart-bar")) {
                    n.setStyle("-fx-bar-fill: #e8e8e8;");

                    n.setOnMouseEntered(event -> n.setStyle("-fx-bar-fill: #c0c0c0;")); // survol
                    n.setOnMouseExited(event -> n.setStyle("-fx-bar-fill: #e8e8e8;")); // sortie
                }
                for (Node n : barChart.lookupAll(".series1.chart-bar")) {
                    n.setStyle("-fx-bar-fill: #2a9d90;");
                    n.setOnMouseEntered(event -> n.setStyle("-fx-bar-fill: #1f7d70;"));
                    n.setOnMouseExited(event -> n.setStyle("-fx-bar-fill: #2a9d90;"));
                }
            });
        }

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


    public void clear() {
        barChart.getData().clear();
        logger.debug("Graphique à barres réinitialisé");
    }

    public Node getChartNode() {
        return barChart;
    }

    public void setTitle(String title) {
        barChart.setTitleSide(Side.TOP);

        // Appliquer un style CSS personnalisé pour la couleur du titre
        barChart.lookupAll(".chart-title").forEach(node -> {
            node.setStyle("-fx-text-fill: black; -fx-font-weight: 700; -fx-font-size: 12px; -fx-padding: 5 10 0 0; -fx-text-alignment: right; -fx-alignment: top-right");
        });
        barChart.setTitle(title);
    }
}