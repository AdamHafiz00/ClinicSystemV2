/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.DatabaseConnection;
import model.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // 1. ADD Method: Saves a new patient and returns their new Database ID.
    // We need the ID back so we can link an Appointment to it immediately!
    public int addPatient(Patient p) {
        String sql = "INSERT INTO patients (name, age, diagnosis, status) VALUES (?, ?, ?, ?)";

        // 'RETURN_GENERATED_KEYS' is the magic flag to get the ID back
        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p.getName());
            stmt.setInt(2, p.getAge());
            stmt.setString(3, p.getDiagnosis()); // Usually "Checkup" initially
            stmt.setString(4, p.getStatus());    // "Waiting"

            stmt.executeUpdate();

            // Get the ID created by Auto-Increment
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Success: Return the new ID (e.g., 5)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Failure
    }

    // 2. UPDATE Method: Used by the Doctor Portal to change the diagnosis
    public void updateDiagnosis(int patientId, String newDiagnosis) {
        String sql = "UPDATE patients SET diagnosis = ?, status = 'In Treatment' WHERE patient_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newDiagnosis);
            stmt.setInt(2, patientId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. GET ALL Method: Used to show the list in the Doctor's Table
    public List<Patient> getAllPatients() {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patients";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("diagnosis"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    // Add this inside PatientDAO class

    public java.util.List<model.Patient> getPatientsByDoctorId(int doctorId) {
        java.util.List<model.Patient> list = new java.util.ArrayList<>();

        // FIXED SQL: We join 'patients' with 'appointments' to find the link
        String sql = "SELECT p.patient_id, p.name, p.age, p.diagnosis, p.status "
                + "FROM patients p "
                + "JOIN appointments a ON p.patient_id = a.patient_id "
                + "WHERE a.doctor_id = ?";

        try (java.sql.Connection conn = database.DatabaseConnection.getInstance().getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            java.sql.ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new model.Patient(
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("diagnosis"),
                        rs.getString("status")
                ));
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
