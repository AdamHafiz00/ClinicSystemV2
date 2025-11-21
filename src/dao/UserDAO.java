/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.DatabaseConnection;
import java.sql.*;

public class UserDAO {
    // This checks the Admin/Receptionist table
    public boolean login(String loginId, String password) {
        String sql = "SELECT * FROM users WHERE login_id = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, loginId);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if a match is found
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}