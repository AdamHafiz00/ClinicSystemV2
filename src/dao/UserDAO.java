/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.DatabaseConnection;
import model.User;
import model.Doctor;
import model.StaffUser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * Attempts to find a user (StaffUser or Doctor) by their login ID and password.
     * @param loginId The identifier (Doctor ID or Staff Login ID). <--- PARAMETER NAME CHANGE
     * @param password The raw password.
     * @return A concrete User object on success, or null if credentials fail.
     */
    public User getUserByCredentials(String loginId, String password) { // <--- PARAMETER NAME CHANGE
        User user = null;

        // --- 1. Check Doctors Table (Doctor ID is used as login ID) ---
        try {
            
            // Query uses doctor_id column, which corresponds to the loginId for doctors
            String sqlDoctor = "SELECT * FROM doctors WHERE login_id = ? AND password = ?";

            // ... (rest of the Doctor logic remains the same) ...

            try (Connection conn = DatabaseConnection.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sqlDoctor)) {

                stmt.setString(1, loginId);
                stmt.setString(2, password);

                // ... (Doctor result set logic) ...
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        user = new Doctor(
                            rs.getInt("doctor_id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getString("contact_info"),
                            rs.getString("specialization"),
                            rs.getString("login_id"),
                            rs.getString("password")
                        );
                        return user; 
                    }
                }
            }
        } catch (NumberFormatException e) {
            // Login ID wasn't a number, proceed to check staff
        } catch (SQLException e) {
            System.err.println("Error checking Doctor credentials:");
            e.printStackTrace();
        }

        // --- 2. Check USERS Table (for Admin or Receptionist) ---
        // Query uses the actual login_id column
        String sqlStaff = "SELECT * FROM users WHERE login_id = ? AND password = ?"; // <--- SQL QUERY CHANGE
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlStaff)) {

            stmt.setString(1, loginId); // <--- Use the string loginId parameter
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new StaffUser(
                        rs.getInt("login_id_pk"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("login_id")
                    );
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking StaffUser credentials:");
            e.printStackTrace();
        }
        
        return null; 
    }
}