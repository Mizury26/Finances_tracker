package com.example.desktopproject.db;

import com.example.desktopproject.model.Expense;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    private static final Logger logger = Logger.getLogger(ExpenseDAO.class);

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
                    Float total = resultSet.getFloat("total");
                    Float housing = resultSet.getFloat("housing");
                    Float food = resultSet.getFloat("food");
                    Float goingOut = resultSet.getFloat("goingOut");
                    Float transportation = resultSet.getFloat("transportation");
                    Float travel = resultSet.getFloat("travel");
                    Float tax = resultSet.getFloat("tax");
                    Float others = resultSet.getFloat("others");

                    Expense expense = new Expense(date, total, housing, food, goingOut,
                            transportation, travel, tax, others);
                    expenses.add(expense);
                    logger.trace("Dépense chargée: " + date + " - " + total + "€");
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
        String query = "INSERT INTO Expense (date, total, housing, food, goingOut, transportation, travel, tax, others) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        logger.debug("Tentative d'insertion d'une nouvelle dépense pour la date: " + expense.getDate());

        try (Connection connection = Database.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Définir les valeurs dans l'ordre des colonnes
            preparedStatement.setString(1, expense.getDate().toString());
            preparedStatement.setFloat(2, expense.getTotal());
            preparedStatement.setFloat(3, expense.getHousing());
            preparedStatement.setFloat(4, expense.getFood());
            preparedStatement.setFloat(5, expense.getGoingOut());
            preparedStatement.setFloat(6, expense.getTransportation());
            preparedStatement.setFloat(7, expense.getTravel());
            preparedStatement.setFloat(8, expense.getTax());
            preparedStatement.setFloat(9, expense.getOthers());

            int rowsInserted = preparedStatement.executeUpdate();
            logger.info(rowsInserted + " dépense(s) insérée(s) en base de données pour la date: " + expense.getDate());

        } catch (SQLException e) {
            logger.error("Erreur lors de l'insertion d'une dépense", e);
        }
    }
}
