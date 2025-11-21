/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // 1. The single instance of this class (Singleton)
    private static DatabaseConnection instance;
    private Connection connection;

    // 2. Database Configuration
    // CHANGE THIS if your database name is different!
    private final String URL = "jdbc:mysql://localhost:3306/clinic_db_v2";
    private final String USER = "root";
    private final String PASSWORD = ""; // Leave empty for XAMPP default

    // 3. Private Constructor (So no one else can create a new connection manually)
    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Connection Failed: " + e.getMessage());
        }
    }

    // 4. Global Access Point
    public static DatabaseConnection getInstance() {
        try {
            // Check if instance is null OR if the connection is CLOSED
            if (instance == null || instance.getConnection() == null || instance.getConnection().isClosed()) {
                instance = new DatabaseConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    // 5. Simple Main method to TEST if it works right now
    public static void main(String[] args) {
        DatabaseConnection.getInstance();
    }
}
