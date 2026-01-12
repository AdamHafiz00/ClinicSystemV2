/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AppointmentDAO {

    /**
     * Inserts a new appointment record into the database.
     */
    public boolean addAppointment(int patientId, int doctorId, String dateText, String timeSlot) {
        String status = "waiting";
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, time_slot, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            stmt.setString(3, dateText); // Date from form (e.g., "YYYY-MM-DD")
            stmt.setString(4, timeSlot);
            stmt.setString(5, status);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error saving appointment:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all time slots already booked for a specific doctor on a
     * specific date.
     */
    public Set<String> getBookedTimeSlots(int doctorId, String dateText) {
        Set<String> busySlots = new HashSet<>();
        String sql = "SELECT time_slot FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND status IN ('waiting', 'in-treatment')";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            stmt.setString(2, dateText);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    busySlots.add(rs.getString("time_slot"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving booked slots:");
            e.printStackTrace();
        }
        return busySlots;
    }

    /**
     * Deletes all appointments linked to a specific doctor ID. Used as a
     * prerequisite for deleting the doctor record itself.
     *
     * * @param doctorId The ID of the doctor whose appointments need to be
     * cleared.
     * @return True if the operation succeeded, false otherwise.
     */
    public boolean deleteAppointmentsByDoctorId(int doctorId) {
        String sql = "DELETE FROM appointments WHERE doctor_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            // Execute the delete operation
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting appointments for doctor ID " + doctorId + ":");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves full appointment details including Patient and Doctor names.
     * Can be filtered by doctorId and date (pass -1 or null to ignore filter).
     */
    public List<Map<String, Object>> getViewAppointments(int doctorId, String date, String time) throws SQLException {
        List<Map<String, Object>> appointmentList = new ArrayList<>();

        // Start base query
        StringBuilder sql = new StringBuilder(
                "SELECT a.appointment_id AS id, a.appointment_date AS date, a.time_slot AS time, "
                + "d.name AS doctor, p.name AS patient, p.ic_number AS ic, a.status "
                + "FROM appointments a "
                + "JOIN doctors d ON a.doctor_id = d.doctor_id "
                + "JOIN patients p ON a.patient_id = p.patient_id "
                + "WHERE 1=1" // Placeholder to simplify dynamic WHERE clause
        );

        List<Object> parameters = new ArrayList<>();

        // Apply Doctor Filter
        if (doctorId != -1) {
            sql.append(" AND a.doctor_id = ?");
            parameters.add(doctorId);
        }

        // Apply Date Filter
        if (date != null && !date.trim().isEmpty()) {
            sql.append(" AND a.appointment_date = ?");
            parameters.add(date);
        }

        // Apply Time Filter <--- NEW LOGIC
        if (time != null && !time.trim().isEmpty()) {
            sql.append(" AND a.time_slot = ?");
            parameters.add(time);
        }

        // You can add an ORDER BY clause here for a better display, e.g.:
        sql.append(" ORDER BY a.appointment_date DESC, a.time_slot ASC");

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            // Bind parameters
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            // ... (rest of the database fetching logic remains the same) ...
            // (code to execute query and build the map list)
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", rs.getInt("id"));
                    row.put("date", rs.getDate("date").toString());
                    row.put("time", rs.getString("time"));
                    row.put("doctor", rs.getString("doctor"));
                    row.put("patient", rs.getString("patient"));
                    row.put("ic", rs.getString("ic"));
                    row.put("status", rs.getString("status"));
                    appointmentList.add(row);
                }
            }
        }
        return appointmentList;
    }

    /**
     * Deletes a single appointment record by its primary key (appointment_id).
     * Used for appointment cancellation by the Admin.
     *
     * * @param appointmentId The ID of the appointment to delete/cancel.
     * @return True if the operation succeeded, false otherwise.
     */
    public boolean deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, appointmentId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting appointment ID " + appointmentId + ":");
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDiagnosisAndStartTreatment(int appointmentId, String newStatus, String diagnosis) {
        String sql = "UPDATE appointments SET status = ?, diagnosis = ? WHERE appointment_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus.toLowerCase()); // e.g., 'complete'
            stmt.setString(2, diagnosis);
            stmt.setInt(3, appointmentId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating appointment ID " + appointmentId + " status and diagnosis:");
            e.printStackTrace();
            return false;
        }
    }

    public java.util.List<java.util.Map<String, Object>> getTodaysActiveAppointmentsByDoctor(int doctorId) throws SQLException {
        java.util.List<java.util.Map<String, Object>> appointmentList = new java.util.ArrayList<>();

        // Query to get Appointment ID, Patient Name/Age, Diagnosis, and Status
// Query to get Appointment ID, Patient Name/Age, Diagnosis, and Status
            String sql = "SELECT a.appointment_id, p.name AS patient_name, p.age AS patient_age, a.appointment_date AS date, "
                       + "a.diagnosis, a.status " 
                       + "FROM appointments a "   
                       + "JOIN patients p ON a.patient_id = p.patient_id "
                       + "WHERE a.doctor_id = ? "
                       + "AND a.appointment_date >= DATE(NOW()) " // Filter for today
                       + "AND a.status = 'waiting' " // Filter for active statuses
                       + "ORDER BY a.time_slot ASC";

        try (java.sql.Connection conn = database.DatabaseConnection.getInstance().getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);

            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    java.util.Map<String, Object> row = new java.util.HashMap<>();
                    // Map keys must match the aliases used in the SELECT statement
                    row.put("appointment_id", rs.getInt("appointment_id"));
                    row.put("patient_name", rs.getString("patient_name"));
                    row.put("patient_age", rs.getInt("patient_age"));
                    row.put("diagnosis", rs.getString("diagnosis"));
                    row.put("status", rs.getString("status"));
                    row.put("date", rs.getString("date"));
                    appointmentList.add(row);
                }
            }
        }
        return appointmentList;
    }

    public java.util.List<java.util.Map<String, Object>> getInTreatmentAppointments() throws java.sql.SQLException {
        java.util.List<java.util.Map<String, Object>> appointmentList = new java.util.ArrayList<>();

        // Query to get Appointment ID, Patient Name, Diagnosis, and Status
        String sql = "SELECT a.appointment_id, "
                   + "p.name AS patient_name, "
                   + "d.name AS doctor_name, "       
                   + "a.diagnosis, a.status, a.appointment_date "
                   + "FROM appointments a "
                   + "JOIN patients p ON a.patient_id = p.patient_id "
                   + "JOIN doctors d ON a.doctor_id = d.doctor_id " 
                   + "WHERE a.status IN ( 'in-treatment','waiting' ) "
                   + "AND a.appointment_date >= DATE(NOW()) "
                   + "ORDER BY a.appointment_date, a.time_slot";

        try (java.sql.Connection conn = database.DatabaseConnection.getInstance().getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql); java.sql.ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                java.util.Map<String, Object> row = new java.util.HashMap<>();
                row.put("appointment_id", rs.getInt("appointment_id"));
                row.put("patient_name", rs.getString("patient_name"));
                row.put("diagnosis", rs.getString("diagnosis"));
                row.put("status", rs.getString("status"));
                row.put("date", rs.getString("appointment_date"));
                row.put("doctor", rs.getString("doctor_name"));
                
                
                appointmentList.add(row);
            }
        }
        return appointmentList;
    }

    public boolean updateAppointmentStatus(int appointmentId, String newStatus) {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, appointmentId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating appointment status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
