/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.DatabaseConnection;
import model.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    // This method goes to the database, grabs all rows from 'doctors', 
    // and turns them into a List of Java 'Doctor' objects.
    public List<Doctor> getAllDoctors() {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM doctors";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("doctor_id");
                String name = rs.getString("name");
                String specialization = rs.getString("specialization");

                // Create a Doctor object and add to list
                list.add(new Doctor(id, name, specialization));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public model.Doctor login(String loginId, String password) {
        String sql = "SELECT * FROM doctors WHERE login_id = ? AND password = ?";

        try (java.sql.Connection conn = database.DatabaseConnection.getInstance().getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loginId);
            stmt.setString(2, password);

            java.sql.ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new model.Doctor(
                        rs.getInt("doctor_id"),
                        rs.getString("name"),
                        rs.getString("specialization")
                );
            }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
public void addDoctorWithCredentials(String name, String specialization, String password) throws java.sql.SQLException {
    java.sql.Connection conn = null;
    java.sql.PreparedStatement stmt = null;
    java.sql.ResultSet rs = null;

    try {
        conn = database.DatabaseConnection.getInstance().getConnection();
        
        // 1. Insert the new doctor without the Login ID yet (since it depends on the auto-generated ID)
        String insertSql = "INSERT INTO doctors (name, specialization, password, is_available) VALUES (?, ?, ?, ?)";
        stmt = conn.prepareStatement(insertSql, java.sql.Statement.RETURN_GENERATED_KEYS);
        
        stmt.setString(1, name);
        stmt.setString(2, specialization);
        stmt.setString(3, password);
        stmt.setBoolean(4, true); 
        
        stmt.executeUpdate();
        
        // 2. Get the new doctor_id (e.g., 4)
        int newDoctorId = -1;
        rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            newDoctorId = rs.getInt(1);
        }
        
        // 3. Generate the Login ID (e.g., D004)
        String newLoginId = "D" + String.format("%03d", newDoctorId);
        
        // 4. Update the record with the generated Login ID
        String updateSql = "UPDATE doctors SET login_id = ? WHERE doctor_id = ?";
        stmt = conn.prepareStatement(updateSql);
        stmt.setString(1, newLoginId);
        stmt.setInt(2, newDoctorId);
        stmt.executeUpdate();
        
    } finally {
        // IMPORTANT: Close resources to prevent leaks (conn is handled by Singleton, but stmt/rs need closing)
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        // NOTE: We rely on the Singleton to manage the single Connection lifecycle, so no conn.close() here.
    }
}
}
