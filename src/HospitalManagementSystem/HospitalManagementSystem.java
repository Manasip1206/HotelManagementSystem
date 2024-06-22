package HospitalManagementSystem;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Manasi@1209";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);

            while (true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. ADD PATIENTS");
                System.out.println("2. VIEW PATIENTS");
                System.out.println("3. VIEW DOCTORS");
                System.out.println("4. BOOK APPOINTMENT");
                System.out.println("5. EXIT");
                System.out.println("ENTER YOUR CHOICE");
                int choice = -1;
                while (true) {
                    try {
                        choice = scanner.nextInt();
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter an integer.");
                        scanner.next(); // Clear invalid input
                    }
                }

                switch (choice) {
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        doctor.viewDoctor();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("ENTER VALID CHOICE!!");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
        System.out.println("Enter patient's Id:");
        int patientId = -1;
        while (true) {
            try {
                patientId = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer for patient's Id.");
                scanner.next(); // Clear invalid input
            }
        }

        System.out.println("Enter doctor's Id:");
        int doctorId = -1;
        while (true) {
            try {
                doctorId = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer for doctor's Id.");
                scanner.next(); // Clear invalid input
            }
        }

        System.out.println("Enter date of appointment in format YYYY-MM-DD:");
        String appointmentDate = scanner.next();

        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
            if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {
                String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Appointment Booked Successfully!!!");
                    } else {
                        System.out.println("Failed to book appointment!");
                    }
                    preparedStatement.close(); // Close the PreparedStatement
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Doctor not available on this date");
            }
        } else {
            System.out.println("Either doctor or patient not available");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                resultSet.close(); // Close the ResultSet
                preparedStatement.close(); // Close the PreparedStatement
                return count == 0;
            }
            resultSet.close(); // Close the ResultSet
            preparedStatement.close(); // Close the PreparedStatement
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
