package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() {
        System.out.println("Enter patient's name: ");
        String name = scanner.next();

        int age = -1;
        while (true) {
            try {
                System.out.println("Enter patient's age: ");
                age = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer for patient's age.");
                scanner.next(); // Clear the invalid input
            }
        }

        System.out.println("Enter patient's gender: ");
        String gender = scanner.next();

        try {
            String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient Added Successfully!!!");
            } else {
                System.out.println("Failed To Add Patient");
            }
            preparedStatement.close(); // Close the PreparedStatement
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatient() {
        String query = "SELECT * FROM patients";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients:");
            System.out.println("+------------+-----------------------------+--------+----------+");
            System.out.println("| Patient Id | Name                        | Age    | Gender   |");
            System.out.println("+------------+-----------------------------+--------+----------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12d|%-29s|%-8d|%-10s|\n", id, name, age, gender);
                System.out.println("+------------+-----------------------------+--------+----------+");
            }

            resultSet.close(); // Close the ResultSet
            preparedStatement.close(); // Close the PreparedStatement
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id) {
        String query = "SELECT * FROM patients WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean exists = resultSet.next();

            resultSet.close(); // Close the ResultSet
            preparedStatement.close(); // Close the PreparedStatement
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
