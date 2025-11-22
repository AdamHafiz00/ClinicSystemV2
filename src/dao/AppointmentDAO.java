/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.DatabaseConnection;
import java.sql.*;

import java.util.HashSet;
import java.util.Set;

public class AppointmentDAO {
    
    
        public void bookAppointment(int patientId, int doctorId, String dateText, String timeSlot) throws SQLException {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appt_date, time_slot) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            // Convert String "2025-12-01" to SQL Date
            stmt.setDate(3, java.sql.Date.valueOf(dateText)); 
            stmt.setString(4, timeSlot);
            
            stmt.executeUpdate();
        }
    }
    
    

    // Method: Get a list of all BUSY slots for a specific doctor + date
   // METHOD 2: Check which slots are busy (For Smart Scheduling)
    public Set<String> getBookedTimeSlots(int doctorId, String dateText) {
        Set<String> bookedSlots = new HashSet<>();
        String sql = "SELECT time_slot FROM appointments WHERE doctor_id = ? AND appt_date = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, doctorId);
            stmt.setDate(2, java.sql.Date.valueOf(dateText)); 
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookedSlots.add(rs.getString("time_slot"));
            }
        } catch (Exception e) {
            // If date format is wrong (e.g. empty), just return empty list
            // e.printStackTrace(); 
        }
        return bookedSlots;
    }
    

        

   

}