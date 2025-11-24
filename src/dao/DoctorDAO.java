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
import java.util.HashSet;
import java.util.Set;

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
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                String contact_info = rs.getString("contact_info");
                String specialization = rs.getString("specialization");
                String loginId = rs.getString("login_id");
                String password = rs.getString("password");

                // Create a Doctor object and add to list
                list.add(new Doctor(id, name, age, gender, contact_info, specialization, loginId, password));
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
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("contact_info"),
                        rs.getString("specialization"),
                        rs.getString("login_id"),
                        rs.getString("password")
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
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            // NOTE: We rely on the Singleton to manage the single Connection lifecycle, so no conn.close() here.
        }
    }

    public Set<String> getBookedTimeSlots(int doctorId, String dateText) {
        Set<String> busySlots = new HashSet<>();
        // Note: The DATE formatting might need adjustment based on your specific database (e.g., MySQL vs. PostgreSQL)
        String sql = "SELECT time_slot FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND status = 'Booked'";

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
     * Adds a new Doctor record to the database. Assumes the 'doctors' table
     * columns are: doctor_id (PK), name, age, gender, contact_info,
     * specialization.
     *
     * * @param doctor The Doctor object containing the data to save.
     * @return The auto-generated ID of the new doctor, or -1 if the insertion
     * fails.
     */
    public int addDoctor(Doctor doctor) {
        int doctorId = -1;

        // SQL query to insert doctor details. Note: You must ensure your 'doctors' table 
        // includes columns for all inherited fields (name, age, gender, contact_info).
        String sql = "INSERT INTO doctors (name, age, gender, contact_info, specialization) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); // Use RETURN_GENERATED_KEYS to get the new doctor_id
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters based on the Doctor model (which inherits from Person)
            stmt.setString(1, doctor.getName());
            stmt.setInt(2, doctor.getAge());
            stmt.setString(3, doctor.getGender());
            stmt.setString(4, doctor.getContactInfo());
            stmt.setString(5, doctor.getSpecialization());
            stmt.setString(6, doctor.getPassword());
            stmt.setString(7, doctor.getLoginId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the auto-generated doctor_id
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        doctorId = rs.getInt(1);
                        System.out.println("New Doctor added with ID: " + doctorId);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding new doctor:");
            e.printStackTrace();
        }
        return doctorId;
    }
    
    /**
     * Updates an existing Doctor record in the database.
     * Assumes the Doctor object has a valid doctor_id set.
     * * @param doctor The Doctor object containing the updated data.
     * @return True if the update was successful (one row affected), false otherwise.
     */
    public boolean updateDoctor(Doctor doctor) {
        // NOTE: We update all editable fields except the auto-increment ID
        String sql = "UPDATE doctors SET name = ?, age = ?, gender = ?, contact_info = ?, specialization = ?, password = ?, login_id = ? WHERE doctor_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 1. Set the new values
            stmt.setString(1, doctor.getName());
            stmt.setInt(2, doctor.getAge());
            stmt.setString(3, doctor.getGender());
            stmt.setString(4, doctor.getContactInfo());
            stmt.setString(5, doctor.getSpecialization());
            stmt.setString(6, doctor.getPassword());
            stmt.setString(7, doctor.getLoginId());
            
            // 2. Set the WHERE clause condition (ID of the doctor to update)
            stmt.setInt(8, doctor.getId()); 

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Check for unique constraint violation (e.g., if login_id is duplicated)
            if (e.getSQLState().startsWith("23")) { 
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
     * * @param doctorId The ID of the doctor to delete.
     * @return True if the doctor was successfully deleted, false otherwise.
     */
    public boolean deleteDoctor(int doctorId) {
        String sql = "DELETE FROM doctors WHERE doctor_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, doctorId); 

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting doctor ID " + doctorId + ".");
            e.printStackTrace();
            return false;
        }
    }


}
