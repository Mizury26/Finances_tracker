package com.example.desktopproject.db;

import com.example.desktopproject.model.Income;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IncomeDAO {
    private static final Logger logger = Logger.getLogger(IncomeDAO.class);

    /**
     * Récupère toutes les données de revenus depuis la base de données.
     */
    public static List<Income> fetchAllDataFromDB() {
        String query = "SELECT * FROM Income";
        List<Income> incomes = new ArrayList<>();
        logger.debug("Récupération de tous les revenus depuis la BDD");

        try (Connection connection = Database.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                try {
                    LocalDate date = LocalDate.parse(resultSet.getString("date"));
                    Float salary = resultSet.getFloat("salary");
                    Float helper = resultSet.getFloat("helper");
                    Float selfEnterprise = resultSet.getFloat("selfEnterprise");
                    Float passiveIncome = resultSet.getFloat("passiveIncome");
                    Float other = resultSet.getFloat("other");

                    Income income = new Income(date, salary, helper, selfEnterprise, passiveIncome, other);
                    incomes.add(income);
                    logger.trace("Revenu chargé: " + date + " - Total: " + income.getTotal() + "€");
                } catch (Exception e) {
                    logger.error("Erreur lors du traitement d'une ligne de revenu", e);
                }
            }
            logger.info(incomes.size() + " revenus récupérés de la base de données");
        } catch (SQLException e) {
            logger.error("Erreur SQL lors de la récupération des revenus", e);
        }
        return incomes;
    }

    /**
     * Insère un revenu dans la base de données.
     */
    public static void insertIncome(Income income) {
        String query = "INSERT INTO Income (date, salary, helper, selfEnterprise, passiveIncome, other) VALUES (?, ?, ?, ?, ?, ?)";
        logger.debug("Tentative d'insertion d'un nouveau revenu pour la date: " + income.getDate());

        try (Connection connection = Database.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, income.getDate().toString());
            preparedStatement.setFloat(2, income.getSalary());
            preparedStatement.setFloat(3, income.getHelper());
            preparedStatement.setFloat(4, income.getSelfEnterprise());
            preparedStatement.setFloat(5, income.getPassiveIncome());
            preparedStatement.setFloat(6, income.getOther());

            int rowsInserted = preparedStatement.executeUpdate();
            logger.info(rowsInserted + " revenu(x) inséré(s) en base de données pour la date: " + income.getDate());

        } catch (SQLException e) {
            logger.error("Erreur lors de l'insertion d'un revenu", e);
        }
    }

    /**
     * Récupère les revenus pour un mois et une année donnés.
     */
    public static List<Income> getByMonth(int month, int year) {
        String query = "SELECT * FROM Income WHERE strftime('%m', date) = ? AND strftime('%Y', date) = ?";
        List<Income> incomes = new ArrayList<>();
        logger.debug("Récupération des revenus pour le mois: " + month + " et l'année: " + year);

        try (Connection connection = Database.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, String.format("%02d", month));
            preparedStatement.setString(2, String.valueOf(year));

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                try {
                    LocalDate date = LocalDate.parse(resultSet.getString("date"));
                    Float salary = resultSet.getFloat("salary");
                    Float helper = resultSet.getFloat("helper");
                    Float selfEnterprise = resultSet.getFloat("selfEnterprise");
                    Float passiveIncome = resultSet.getFloat("passiveIncome");
                    Float other = resultSet.getFloat("other");

                    Income income = new Income(date, salary, helper, selfEnterprise, passiveIncome, other);
                    incomes.add(income);
                    logger.trace("Revenu chargé: " + date + " - Total: " + income.getTotal() + "€");
                } catch (Exception e) {
                    logger.error("Erreur lors du traitement d'une ligne de revenu", e);
                }
            }
        } catch (SQLException e) {
            logger.error("Erreur SQL lors de la récupération des revenus", e);
        }
        return incomes;
    }

    /**
     * Récupère les revenus des 12 mois précédents à partir d'un mois et d'une année donnés.
     */
    public static List<Income> get12MonthsBefore(int month, int year) {
        LocalDate endDate = LocalDate.of(year, month, 1).withDayOfMonth(1).plusMonths(1).minusDays(1);
        LocalDate startDate = endDate.minusMonths(11).withDayOfMonth(1);

        String query = "SELECT * FROM Income WHERE date BETWEEN ? AND ?";
        List<Income> incomes = new ArrayList<>();
        logger.debug("Récupération des revenus des 12 mois précédents à partir de: " + startDate + " jusqu'à: " + endDate);

        try (Connection connection = Database.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, startDate.toString());
            preparedStatement.setString(2, endDate.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                try {
                    LocalDate date = LocalDate.parse(resultSet.getString("date"));
                    Float salary = resultSet.getFloat("salary");
                    Float helper = resultSet.getFloat("helper");
                    Float selfEnterprise = resultSet.getFloat("selfEnterprise");
                    Float passiveIncome = resultSet.getFloat("passiveIncome");
                    Float other = resultSet.getFloat("other");

                    Income income = new Income(date, salary, helper, selfEnterprise, passiveIncome, other);
                    incomes.add(income);
                    logger.trace("Revenu chargé: " + date + " - Total: " + income.getTotal() + "€");
                } catch (Exception e) {
                    logger.error("Erreur lors du traitement d'une ligne de revenu", e);
                }
            }
        } catch (SQLException e) {
            logger.error("Erreur SQL lors de la récupération des revenus", e);
        }
        return incomes;
    }
}