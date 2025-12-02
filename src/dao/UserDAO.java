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

    // Inside dao.UserDAO.java
    public User getUserByCredentials(String loginId, String password) {
        User user = null;

        // --- 1. Check DOCTORS Table First ---
        // Since doctors have their own login info in their own table now
        String sqlDoctor = "SELECT * FROM doctors WHERE login_id = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmtDoc = conn.prepareStatement(sqlDoctor)) {

            stmtDoc.setString(1, loginId);
            stmtDoc.setString(2, password);

            try (ResultSet rsDoc = stmtDoc.executeQuery()) {
                if (rsDoc.next()) {
                    // Found a Doctor!
                    return new Doctor(
                            rsDoc.getInt("doctor_id"),
                            rsDoc.getString("name"),
                            rsDoc.getInt("age"),
                            rsDoc.getString("gender"),
                            rsDoc.getString("contact_info"),
                            rsDoc.getString("specialization"),
                            rsDoc.getString("role"),
                            rsDoc.getString("login_id"),
                            rsDoc.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // --- 2. Check USERS Table (for Admin/Receptionist) ---
        // If not found in doctors, check the users table
        String sqlStaff = "SELECT * FROM users WHERE login_id = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmtStaff = conn.prepareStatement(sqlStaff)) {

            stmtStaff.setString(1, loginId);
            stmtStaff.setString(2, password);

            try (ResultSet rsStaff = stmtStaff.executeQuery()) {
                if (rsStaff.next()) {
                    // Found Staff!
                    return new StaffUser(
                            rsStaff.getInt("login_id_pk"),
                            rsStaff.getString("username"),
                            rsStaff.getString("password"),
                            rsStaff.getString("login_id"),
                            rsStaff.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Not found in either table
    }
}
