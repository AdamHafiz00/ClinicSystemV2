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
    public int addPatient(model.Patient p) {
        // FIX: Add ic_number to the SQL
        String sql = "INSERT INTO patients (name, age, gender, contact_info, ic_number, diagnosis, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (java.sql.Connection conn = database.DatabaseConnection.getInstance().getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p.getName());
            stmt.setInt(2, p.getAge());
            stmt.setString(3, p.getGender());
            stmt.setString(4, p.getContactInfo());
            stmt.setString(5, p.getIcNumber());
            stmt.setString(6, p.getDiagnosis());
            stmt.setString(7, p.getStatus());

            stmt.executeUpdate();

            java.sql.ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Success
            }
        } catch (java.sql.SQLException e) {
            // This will print the error if it's a Duplicate Entry
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
                        rs.getString("gender"),
                        rs.getString("contact_info"),
                        rs.getString("ic_number"),
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
                        rs.getString("gender"),
                        rs.getString("contact_info"),
                        rs.getString("ic_number"),
                        rs.getString("diagnosis"),
                        rs.getString("status")
                ));
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    // File: src/dao/PatientDAO.java

// Method to find a patient by their IC number
    public model.Patient getPatientByIC(String icNumber) {
        // Select all columns where ic_number matches
        String sql = "SELECT * FROM patients WHERE ic_number = ?";

        try (java.sql.Connection conn = database.DatabaseConnection.getInstance().getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, icNumber);
            java.sql.ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // If found, create and return the Patient object
                // NOTE: Adjust the constructor arguments below to match your exact Patient.java class!
                return new model.Patient(
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("contact_info"),
                        rs.getString("ic_number"),
                        rs.getString("diagnosis"),
                        rs.getString("status")
                );
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no patient found
    }
    /**
     * Counts the total number of patients with a specific status 
     * (e.g., "Waiting", "In-treatment", "Complete").
     * * @param status The patient status string to filter by.
     * @return The count of patients matching the status, or 0 if an error occurs.
     */
    public int countPatientsByStatus(String status) {
        int count = 0;
        
        // SQL to count rows where the status column matches the provided status string
        String sql = "SELECT COUNT(*) AS count FROM patients WHERE status = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve the count from the column alias 'count'
                    count = rs.getInt("count");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting patients by status '" + status + "':");
            e.printStackTrace();
            // Return 0 on error
        }
        return count;
    }
}
