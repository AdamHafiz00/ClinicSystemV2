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

    public User getUserByCredentials(String loginId, String password) {
        
        // --- 1. AUTHENTICATE against the centralized USERS table ---
        String sqlAuth = "SELECT login_id_pk, username, password, role, login_id "
                       + "FROM users WHERE login_id = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmtAuth = conn.prepareStatement(sqlAuth)) {

            stmtAuth.setString(1, loginId);
            stmtAuth.setString(2, password);

            try (ResultSet rsAuth = stmtAuth.executeQuery()) {
                if (rsAuth.next()) {
                    
                    // Valid credentials found. Extract USERS table data.
                    int userIdPk = rsAuth.getInt("login_id_pk");
                    String username = rsAuth.getString("username");
                    String userPassword = rsAuth.getString("password");
                    String role = rsAuth.getString("role");
                    String userLoginId = rsAuth.getString("login_id");

                    // --- 2. BRANCH based on ROLE ---
                    if (role.equals("Admin") || role.equals("Receptionist")) {
                        // StaffUser data is complete from the USERS table
                        return new StaffUser(userIdPk, username, userPassword, role, userLoginId);
                    
                    } else if (role.equals("Doctor")) {
                        
                        // --- 3. DOCTOR: Fetch specific details using the Primary Key (userIdPk) ---
                        
                        // Assuming you added a FK column named 'user_login_pk_fk' to the doctors table.
                        String sqlDoctorDetails = "SELECT doctor_id, name, specialization, age, gender, contact_info "
                                                + "FROM doctors WHERE user_login_pk = ?"; 
                        
                        try (PreparedStatement stmtDoctor = conn.prepareStatement(sqlDoctorDetails)) {
                            stmtDoctor.setInt(1, userIdPk); // Use the integer Primary Key for lookup
                            
                            try (ResultSet rsDoc = stmtDoctor.executeQuery()) {
                                if (rsDoc.next()) {
                                    // Found all Doctor details!
                                    return new Doctor(
                                        rsDoc.getInt("doctor_id"),
                                        rsDoc.getString("name"),
                                        rsDoc.getInt("age"),
                                        rsDoc.getString("gender"),
                                        rsDoc.getString("contact_info"),
                                        rsDoc.getString("specialization"),
                                       // Pass the authentication/user data (from users table, used by the User interface methods)
                                        userLoginId,                        // Login ID
                                        userPassword,                       // Password
                                        role                                // Role ("Doctor")
                                    );
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during user login check:");
            e.printStackTrace();
        }
        
        return null; 
    }
}