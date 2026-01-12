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
    // Streamlined to only include patient demographic data.
    public int addPatient(model.Patient p) {
        // FIX: Removed 'diagnosis' and 'status' from the INSERT list.
        String sql = "INSERT INTO patients (name, age, gender, contact_info, ic_number) VALUES (?, ?, ?, ?, ?)";

        try (java.sql.Connection conn = database.DatabaseConnection.getInstance().getConnection(); 
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p.getName());
            stmt.setInt(2, p.getAge());
            stmt.setString(3, p.getGender());
            stmt.setString(4, p.getContactInfo());
            stmt.setString(5, p.getIcNumber());
            // Removed bindings for diagnosis and status

            stmt.executeUpdate();

            java.sql.ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the generated patient_id
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error adding patient to database:");
            e.printStackTrace();
        }
        return -1; // Failure
    }

    // 2. REMOVED OLD updateDiagnosis method.
    // Rationale: This functionality is handled by the AppointmentDAO.updateDiagnosisAndStartTreatment(int appointmentId, String newDiagnosis)
    // and should target the 'appointments' table.
    
    // 3. GET ALL Method: Used to show the list in the Doctor's Table
    public List<Patient> getAllPatients() {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patients";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("contact_info"),
                        rs.getString("ic_number")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Method to find patients associated with a specific doctor (via appointments)
    public java.util.List<model.Patient> getPatientsByDoctorId(int doctorId) {
        java.util.List<model.Patient> list = new java.util.ArrayList<>();

        // SQL: We join 'patients' with 'appointments' to find the link
        String sql = "SELECT DISTINCT p.patient_id, p.name, p.age,p.gender,p.contact_info,p.ic_number "
                + "FROM patients p "
                + "JOIN appointments a ON p.patient_id = a.patient_id "
                // OPTIONAL: Add 'AND a.status = 'waiting'' here if you only want patients currently waiting for this doctor
                + "WHERE a.doctor_id = ?";

        try (java.sql.Connection conn = database.DatabaseConnection.getInstance().getConnection(); 
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            java.sql.ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new model.Patient(
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("contact_info"),
                        rs.getString("ic_number")
                ));
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Method to find a patient by their IC number
    public model.Patient getPatientByIC(String icNumber) {
        // Select all columns where ic_number matches
        String sql = "SELECT * FROM patients WHERE ic_number = ?";

        try (java.sql.Connection conn = database.DatabaseConnection.getInstance().getConnection(); 
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, icNumber);
            java.sql.ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // If found, create and return the Patient object
                return new model.Patient(
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("contact_info"),
                        rs.getString("ic_number")
                );
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no patient found
    }

    /**
     * Counts the total number of appointments for TODAY with a specific status 
     * (e.g., "Waiting", "In-treatment", "Complete").
     * NOTE: This method correctly queries the 'appointments' table.
     *
     * @param status The appointment status string to filter by.
     * @return The count of appointments matching the status, or 0 if an error occurs.
     */
    public int countPatientsByStatus(String status) {
        int count = 0;

        // SQL query to count appointments for TODAY with the given status
        String sql = "SELECT COUNT(*) FROM appointments "
                + "WHERE appointment_date >= DATE(NOW()) AND status = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Convert the controller's status (e.g., "Waiting") to the database's status (e.g., 'waiting')
            stmt.setString(1, status.toLowerCase());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching dashboard status count for '" + status + "':");
            e.printStackTrace();
        }
        return count;
    }
}