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

    /**
     * Retrieves all doctors from the database, including their login
     * credentials.
     *
     * @return List of Doctor objects.
     */
    public List<Doctor> getAllDoctors() {
        List<Doctor> list = new ArrayList<>();
        // Updated Query: Fetch all columns including login_id and password
        String sql = "SELECT doctor_id, name, age, gender, contact_info, specialization, login_id, password, role FROM doctors";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Create Doctor object using the full constructor
                list.add(new Doctor(
                        rs.getInt("doctor_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("contact_info"),
                        rs.getString("specialization"),
                        rs.getString("role"),
                        rs.getString("login_id"), // Now fetching from doctors table
                        rs.getString("password") // Now fetching from doctors table
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Adds a new Doctor record (including login credentials) to the database.
     *
     * @param doctor The Doctor object containing the data to save.
     * @return True if insertion was successful, false otherwise.
     */
// Inside dao.DoctorDAO.java
    public boolean addDoctor(Doctor doctor) {
        Connection conn = null;
        ResultSet rs = null;
        String tempLoginId = "TEMP-" + System.currentTimeMillis(); // Temporary unique ID

        String sqlInsert = "INSERT INTO doctors (name, specialization, login_id, password, is_available, age, gender, contact_info,role) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlUpdate = "UPDATE doctors SET login_id = ? WHERE doctor_id = ?";

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false); // Start Transaction

            // 1. INSERT with Temporary ID
            int newId = -1;
            try (PreparedStatement stmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, doctor.getName());
                stmt.setString(2, doctor.getSpecialization());
                stmt.setString(3, tempLoginId);      // Placeholder
                stmt.setString(4, doctor.getPassword());
                stmt.setBoolean(5, true);
                stmt.setInt(6, doctor.getAge());
                stmt.setString(7, doctor.getGender());
                stmt.setString(8, doctor.getContactInfo());
                stmt.setString(9, "Doctor");
                

                stmt.executeUpdate();

                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    newId = rs.getInt(1);
                }
            }

            if (newId == -1) {
                conn.rollback();
                return false;
            }

            // 2. UPDATE with Final Format (e.g., "D" + 105 = "D105")
            String finalLoginId = "D" + newId;

            try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {
                updateStmt.setString(1, finalLoginId);
                updateStmt.setInt(2, newId);
                updateStmt.executeUpdate();
            }

            conn.commit(); // Save changes
            return true;

        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
            }
            if (rs != null) try {
                rs.close();
            } catch (SQLException ex) {
            }
        }
    }

    /**
     * Updates an existing Doctor record (Profile + Credentials).
     *
     * @param doctor The Doctor object containing the updated data.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateDoctor(Doctor doctor) {
        // Simplified SQL: Single UPDATE on doctors table
        String sql = "UPDATE doctors SET name=?, age=?, gender=?, contact_info=?, specialization=?, login_id=?, password=? WHERE doctor_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doctor.getName());
            stmt.setInt(2, doctor.getAge());
            stmt.setString(3, doctor.getGender());
            stmt.setString(4, doctor.getContactInfo());
            stmt.setString(5, doctor.getSpecialization());
            stmt.setString(6, doctor.getLoginId());  // Update Login ID
            stmt.setString(7, doctor.getPassword()); // Update Password
            stmt.setInt(8, doctor.getId());          // WHERE clause

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.err.println("Error updating doctor: Login ID already exists.");
            } else {
                System.err.println("Error updating doctor:");
            }
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a doctor record by their primary key.
     *
     * @param doctorId The ID of the doctor to delete.
     * @return True if the doctor was successfully deleted, false otherwise.
     */
    public boolean deleteDoctor(int doctorId) {
        String sql = "DELETE FROM doctors WHERE doctor_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting doctor ID " + doctorId + ".");
            e.printStackTrace();
            return false;
        }
    }
}
