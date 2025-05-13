package com.example.desktopproject.db;

import com.example.desktopproject.model.Expense;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    // Modifier la signature pour retourner une List<Expense>
    public static List<Expense> fetchAllDataFromDB() {
        String query = "SELECT * FROM Expense";
        List<Expense> expenses = new ArrayList<>();

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
                } catch (Exception e) {
                    System.err.println("Erreur lors du traitement d'une ligne: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur SQL: " + e.getMessage());
        }
        return expenses;
    }

    public static void insertExpense(Expense expense) {
        String query = "INSERT INTO Expense (date, total, housing, food, goingOut, transportation, travel, tax, others) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            System.out.println(rowsInserted + " dépense(s) insérée(s) en base de données");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'insertion: " + e.getMessage());
        }
    }
}
