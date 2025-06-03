package com.example.desktopproject.db;

import com.example.desktopproject.model.Expense;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    private static final Logger logger = Logger.getLogger(ExpenseDAO.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Modifier la signature pour retourner une List<Expense>
    public static List<Expense> fetchAllDataFromDB() {
        String query = "SELECT * FROM Expense";
        List<Expense> expenses = new ArrayList<>();
        logger.debug("Récupération de toutes les dépenses depuis la BDD");

        try (Connection connection = Database.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                try {
                    LocalDate date = LocalDate.parse(resultSet.getString("date"));
                    Float housing = resultSet.getFloat("housing");
                    Float food = resultSet.getFloat("food");
                    Float goingOut = resultSet.getFloat("goingOut");
                    Float transportation = resultSet.getFloat("transportation");
                    Float travel = resultSet.getFloat("travel");
                    Float tax = resultSet.getFloat("tax");
                    Float others = resultSet.getFloat("others");

                    Expense expense = new Expense(date, housing, food, goingOut,
                            transportation, travel, tax, others);
                    expenses.add(expense);
                    logger.trace("Dépense chargée: " + date + " - " + expense.getTotal() + "€");
                } catch (Exception e) {
                    logger.error("Erreur lors du traitement d'une ligne de dépense", e);
                }
            }
            logger.info(expenses.size() + " dépenses récupérées de la base de données");
        } catch (SQLException e) {
            logger.error("Erreur SQL lors de la récupération des dépenses", e);
        }
        return expenses;
    }

    public static void insertExpense(Expense expense) {
        String query = "INSERT INTO Expense (date, housing, food, goingOut, transportation, travel, tax, others) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        logger.debug("Tentative d'insertion d'une nouvelle dépense pour la date: " + expense.getDate());

        try (Connection connection = Database.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Définir les valeurs dans l'ordre des colonnes
            preparedStatement.setString(1, expense.getDate().toString());
            preparedStatement.setFloat(2, expense.getHousing());
            preparedStatement.setFloat(3, expense.getFood());
            preparedStatement.setFloat(4, expense.getGoingOut());
            preparedStatement.setFloat(5, expense.getTransportation());
            preparedStatement.setFloat(6, expense.getTravel());
            preparedStatement.setFloat(7, expense.getTax());
            preparedStatement.setFloat(8, expense.getOthers());

            int rowsInserted = preparedStatement.executeUpdate();
            logger.info(rowsInserted + " dépense(s) insérée(s) en base de données pour la date: " + expense.getDate());

        } catch (SQLException e) {
            logger.error("Erreur lors de l'insertion d'une dépense", e);
        }
    }

    public static List<Expense> get12MonthsBefore(int month, int year) {
        // Crée la date du dernier jour du mois donné
        LocalDate endDate = LocalDate.of(year, month, 1).withDayOfMonth(1).plusMonths(1).minusDays(1);

        // Crée la date du premier jour 11 mois avant
        LocalDate startDate = endDate.minusMonths(11).withDayOfMonth(1);

        // Formater les dates en chaînes (dd/MM/yyyy)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String startDateStr = startDate.format(formatter);
        String endDateStr = endDate.format(formatter);

        String sql = "SELECT date, housing, food, goingOut, transportation, travel, tax, others FROM Expense " +
                "WHERE date BETWEEN ? AND ? ORDER BY date";

        List<Expense> expenses = new ArrayList<>();


        try (Connection connection = Database.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, startDateStr);
            preparedStatement.setString(2, endDateStr);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                try {
                    LocalDate date = LocalDate.parse(resultSet.getString("date"));
                    Float housing = resultSet.getFloat("housing");
                    Float food = resultSet.getFloat("food");
                    Float goingOut = resultSet.getFloat("goingOut");
                    Float transportation = resultSet.getFloat("transportation");
                    Float travel = resultSet.getFloat("travel");
                    Float tax = resultSet.getFloat("tax");
                    Float others = resultSet.getFloat("others");

                    Expense expense = new Expense(date, housing, food, goingOut,
                            transportation, travel, tax, others);
                    expenses.add(expense);
                    logger.trace("Dépense chargée: " + date + " - " + expense.getTotal() + "€");
                } catch (Exception e) {
                    logger.error("Erreur lors du traitement d'une ligne de dépense", e);
                }

            }

        } catch (SQLException e) {
            logger.error("Erreur lors de l'insertion d'une dépense", e);
        }

        return expenses;
    }


    public static List<Expense> getByMonth(int month, int year) {
        String datePattern = year + "-" + String.format("%02d", month) + "-__";

        String sql = "SELECT date, housing, food, goingOut, transportation, travel, tax, others FROM Expense " +
                "WHERE date LIKE ?";

        List<Expense> expenses = new ArrayList<>();

        try (Connection connection = Database.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, datePattern);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                try {
                    LocalDate date = LocalDate.parse(resultSet.getString("date"));
                    Float housing = resultSet.getFloat("housing");
                    Float food = resultSet.getFloat("food");
                    Float goingOut = resultSet.getFloat("goingOut");
                    Float transportation = resultSet.getFloat("transportation");
                    Float travel = resultSet.getFloat("travel");
                    Float tax = resultSet.getFloat("tax");
                    Float others = resultSet.getFloat("others");

                    Expense expense = new Expense(date, housing, food, goingOut,
                            transportation, travel, tax, others);
                    expenses.add(expense);
                    logger.trace("Dépense chargée: " + date + " - " + expense.getTotal() + "€");
                } catch (Exception e) {
                    logger.error("Erreur lors du traitement d'une ligne de dépense", e);
                }

            }

        } catch (SQLException e) {
            logger.error("Erreur lors de l'insertion d'une dépense", e);
        }

        return expenses;
    }
}
