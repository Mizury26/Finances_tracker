package com.example.desktopproject.charts;

import com.example.desktopproject.model.Expense;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import org.apache.log4j.Logger;

import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class LineChart {
    private static final Logger logger = Logger.getLogger(LineChart.class);

    private javafx.scene.chart.LineChart<String, Number> lineChart;
    private List<Expense> expenses;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private YearMonth selectedMonth;

    public void clear () {
        this.lineChart.getData().clear();
    }

    public LineChart(String title) {
        // Configuration des axes
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        xAxis.setLabel("Mois");
        yAxis.setLabel("Montant (€)");

        // Création du graphique
        this.lineChart = new javafx.scene.chart.LineChart<>(xAxis, yAxis);
        this.lineChart.setTitle(title);
        this.lineChart.setCreateSymbols(true);
        this.lineChart.setAnimated(true);
    }

    public void createChart(Object data, YearMonth selectedYearMonth) {
        this.selectedMonth = selectedYearMonth;
        lineChart.getData().clear();

        if (data instanceof List) {
            this.expenses = (List<Expense>) data;
            logger.debug("Données chargées pour le graphique linéaire: " + expenses.size() + " dépenses");
        } else {
            logger.error("Type de données incorrect pour le graphique linéaire");
            return;
        }

        if (expenses == null || expenses.isEmpty()) {
            logger.warn("Aucune donnée disponible pour créer le graphique linéaire");
            return;
        }

        // Obtenir la liste des 6 derniers mois à partir du mois sélectionné
        List<YearMonth> months = getLast6Months(selectedYearMonth);

        // Créer une série pour chaque catégorie de dépense
        XYChart.Series<String, Number> logementSeries = new XYChart.Series<>();
        logementSeries.setName("Logement");

        XYChart.Series<String, Number> nourritureSeries = new XYChart.Series<>();
        nourritureSeries.setName("Nourriture");

        XYChart.Series<String, Number> sortieSeries = new XYChart.Series<>();
        sortieSeries.setName("Sorties");

        XYChart.Series<String, Number> transportSeries = new XYChart.Series<>();
        transportSeries.setName("Transport");

        XYChart.Series<String, Number> voyageSeries = new XYChart.Series<>();
        voyageSeries.setName("Voyages");

        XYChart.Series<String, Number> impotSeries = new XYChart.Series<>();
        impotSeries.setName("Impôts");

        XYChart.Series<String, Number> autresSeries = new XYChart.Series<>();
        autresSeries.setName("Autres");

        // Pour chaque mois, calculer le total par catégorie
        for (YearMonth currentMonth : months) {
            String monthLabel = currentMonth.getMonth().getDisplayName(TextStyle.SHORT, Locale.FRANCE) + " " + currentMonth.getYear();

            // Filtrer les dépenses pour ce mois
            List<Expense> monthExpenses = expenses.stream()
                    .filter(e -> YearMonth.from(e.getDate()).equals(currentMonth))
                    .collect(Collectors.toList());

            // Calculer les totaux
            float totalLogement = monthExpenses.stream().map(Expense::getHousing).reduce(0f, Float::sum);
            float totalNourriture = monthExpenses.stream().map(Expense::getFood).reduce(0f, Float::sum);
            float totalSortie = monthExpenses.stream().map(Expense::getGoingOut).reduce(0f, Float::sum);
            float totalTransport = monthExpenses.stream().map(Expense::getTransportation).reduce(0f, Float::sum);
            float totalVoyage = monthExpenses.stream().map(Expense::getTravel).reduce(0f, Float::sum);
            float totalImpot = monthExpenses.stream().map(Expense::getTax).reduce(0f, Float::sum);
            float totalAutres = monthExpenses.stream().map(Expense::getOthers).reduce(0f, Float::sum);

            // Ajouter les données aux séries
            logementSeries.getData().add(new XYChart.Data<>(monthLabel, totalLogement));
            nourritureSeries.getData().add(new XYChart.Data<>(monthLabel, totalNourriture));
            sortieSeries.getData().add(new XYChart.Data<>(monthLabel, totalSortie));
            transportSeries.getData().add(new XYChart.Data<>(monthLabel, totalTransport));
            voyageSeries.getData().add(new XYChart.Data<>(monthLabel, totalVoyage));
            impotSeries.getData().add(new XYChart.Data<>(monthLabel, totalImpot));
            autresSeries.getData().add(new XYChart.Data<>(monthLabel, totalAutres));
        }

        // Ajouter les séries au graphique
        lineChart.getData().addAll(
                logementSeries,
                nourritureSeries,
                sortieSeries,
                transportSeries,
                voyageSeries,
                impotSeries,
                autresSeries
        );

        // Ajouter des tooltips
        addTooltips();

        logger.debug("Graphique linéaire créé avec succès");
    }

    private List<YearMonth> getLast6Months(YearMonth selectedMonth) {
        List<YearMonth> months = new ArrayList<>();
        YearMonth current = selectedMonth;

        // Ajouter les 6 derniers mois (y compris le mois sélectionné)
        for (int i = 0; i < 6; i++) {
            months.add(current);
            current = current.minusMonths(1);
        }

        // Inverser l'ordre pour avoir chronologiquement
        Collections.reverse(months);
        return months;
    }

    private void addTooltips() {
        for (XYChart.Series<String, Number> series : lineChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Tooltip tooltip = new Tooltip(
                        series.getName() + "\n" +
                                data.getXValue() + " : " +
                                data.getYValue() + " €"
                );
                Tooltip.install(data.getNode(), tooltip);

                // Ajouter des effets au survol
                data.getNode().setOnMouseEntered(event ->
                        data.getNode().setStyle("-fx-background-color: blue;"));
                data.getNode().setOnMouseExited(event ->
                        data.getNode().setStyle(null));
            }
        }
    }

    public Node getChartNode() {
        return lineChart;
    }

    public void setTitle(String title) {
        this.lineChart.setTitle(title);
    }
}