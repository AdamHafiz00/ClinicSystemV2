/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.DatabaseConnection;
import java.sql.*;

public class AppointmentDAO {

    public void bookAppointment(int patientId, int doctorId, String date, String timeSlot) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appt_date, time_slot) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            stmt.setDate(3, java.sql.Date.valueOf(date)); // Format: YYYY-MM-DD
            stmt.setString(4, timeSlot);
            
            stmt.executeUpdate();
            System.out.println("Appointment booked successfully!");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}