package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {
    private Connection connection;

    public Doctor(Connection connection) {
        this.connection = connection;
    }

    public void viewDoctor() {
        String query = "SELECT * FROM doctors";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctors:");
            System.out.println("+------------+-----------------------------+-------------------+");
            System.out.println("| Doctor  Id | Name                        | Specialization    |");
            System.out.println("+------------+-----------------------------+-------------------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                System.out.printf("|%-12d|%-29s|%-19s|\n", id, name, specialization);
                System.out.println("+------------+-----------------------------+-------------------+");
            }

            resultSet.close(); // Close the ResultSet
            preparedStatement.close(); // Close the PreparedStatement
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id) {
        String query = "SELECT * FROM doctors WHERE id = ?";
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
