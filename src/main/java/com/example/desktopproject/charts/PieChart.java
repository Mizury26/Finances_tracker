package com.example.desktopproject.charts;

import com.example.desktopproject.model.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import org.apache.log4j.Logger;

import java.util.List;

public class PieChart {
    private static final Logger logger = Logger.getLogger(PieChart.class);
    private final javafx.scene.chart.PieChart pieChart;
    public ObservableList<javafx.scene.chart.PieChart.Data> pieChartData = FXCollections.observableArrayList();
    private List<Expense> expenses;

    public PieChart(String title) {
        this.pieChart = new javafx.scene.chart.PieChart();
        this.pieChart.setTitle(title);
    }

    public void clear() {
        this.pieChartData.clear();
        this.pieChart.setData(pieChartData);
        logger.debug("Graphique en camembert réinitialisé");
    }

    public void createChart(Object data) {
        clear(); // Réinitialiser les données du graphique

        if (data instanceof List) {
            this.expenses = (List<Expense>) data;
            logger.debug("Données chargées pour le graphique en camembert: " + expenses.size() + " dépenses");
        } else {
            logger.error("Type de données incorrect pour le graphique en camembert");
        }

        if (expenses == null || expenses.isEmpty()) {
            logger.warn("Aucune donnée disponible pour créer le graphique en camembert");
            return;
        }

        // Calcul des totaux par catégorie
        float totalLogement = 0, totalNourriture = 0, totalSortie = 0;
        float totalTransport = 0, totalVoyage = 0, totalImpot = 0, totalAutres = 0;
        float grandTotal = 0;

        for (Expense expense : expenses) {
            totalLogement += expense.getStrictHousing();
            totalNourriture += expense.getStrictFood();
            totalSortie += expense.getStrictGoingOut();
            totalTransport += expense.getStrictTransportation();
            totalVoyage += expense.getStrictTravel();
            totalImpot += expense.getStrictTax();
            totalAutres += expense.getStrictOthers();
            grandTotal += expense.getStrictTotal();
        }


        // N'ajouter que les catégories avec des dépenses > 0
        if (totalLogement > 0)
            pieChartData.add(new javafx.scene.chart.PieChart.Data("Logement", totalLogement));
        if (totalNourriture > 0)
            pieChartData.add(new javafx.scene.chart.PieChart.Data("Nourriture", totalNourriture));
        if (totalSortie > 0)
            pieChartData.add(new javafx.scene.chart.PieChart.Data("Sorties", totalSortie));
        if (totalTransport > 0)
            pieChartData.add(new javafx.scene.chart.PieChart.Data("Transport", totalTransport));
        if (totalVoyage > 0)
            pieChartData.add(new javafx.scene.chart.PieChart.Data("Voyages", totalVoyage));
        if (totalImpot > 0)
            pieChartData.add(new javafx.scene.chart.PieChart.Data("Impôts", totalImpot));
        if (totalAutres > 0)
            pieChartData.add(new javafx.scene.chart.PieChart.Data("Autres", totalAutres));

        // Mettre à jour le graphique
        pieChart.setData(pieChartData);

        // Ajouter des tooltips
        final float finalGrandTotal = grandTotal;
        pieChart.getData().forEach(dataSec -> {
            String percentage = String.format("%.1f%%", (dataSec.getPieValue() / finalGrandTotal * 100));
            String text = dataSec.getName() + " : " + dataSec.getPieValue() + "€ (" + percentage + ")";
            Tooltip tooltip = new Tooltip(text);
            Tooltip.install(dataSec.getNode(), tooltip);
        });

        logger.debug("Graphique en camembert créé avec succès");
    }

    public Node getChartNode() {
        return pieChart;
    }

    // Méthode pour mettre à jour le titre
    public void setTitle(String title) {
        pieChart.setTitleSide(Side.TOP);

        // Appliquer un style CSS personnalisé pour la couleur du titre
        pieChart.lookupAll(".chart-title").forEach(node -> {
            node.setStyle("-fx-text-fill: black; -fx-font-weight: 700; -fx-font-size: 12px; -fx-padding: 5 10 0 0; -fx-text-alignment: right; -fx-alignment: top-right");
        });

        this.pieChart.setTitle(title);
    }
}