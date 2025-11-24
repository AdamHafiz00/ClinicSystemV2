/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AppointmentDAO {

    /**
     * Inserts a new appointment record into the database.
     */
    public boolean addAppointment(int patientId, int doctorId, String dateText, String timeSlot) {
        String status = "Booked";
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, time_slot, status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
     * Retrieves all time slots already booked for a specific doctor on a specific date.
     */
    public Set<String> getBookedTimeSlots(int doctorId, String dateText) {
        Set<String> busySlots = new HashSet<>();
        String sql = "SELECT time_slot FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND status = 'Booked'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
     * Deletes all appointments linked to a specific doctor ID.
     * Used as a prerequisite for deleting the doctor record itself.
     * * @param doctorId The ID of the doctor whose appointments need to be cleared.
     * @return True if the operation succeeded, false otherwise.
     */
    public boolean deleteAppointmentsByDoctorId(int doctorId) {
        String sql = "DELETE FROM appointments WHERE doctor_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
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
}