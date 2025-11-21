/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.DoctorDAO;
import dao.UserDAO;
import model.Doctor;

public class LoginController {

    // Returns a string indicating the role ("Doctor", "Admin", or "Login Failed")
    public String authenticate(String loginId, String password) {
        if (loginId == null || password == null || loginId.length() < 2) {
            return "Login Failed";
        }
        
        // Use the first letter of the ID to determine the role
        String prefix = loginId.substring(0, 1).toUpperCase();

        if (prefix.equals("D")) {
            // D = Doctor Login
            DoctorDAO docDao = new DoctorDAO();
            Doctor doctor = docDao.login(loginId, password);
            if (doctor != null) {
                // In a future step, you might save the doctor object here for the portal
                return "Doctor"; 
            }
        } else if (prefix.equals("A")) {
            // A = Admin/Receptionist Login
            UserDAO userDao = new UserDAO();
            if (userDao.login(loginId, password)) {
                return "Admin"; 
            }
        }
        
        return "Login Failed"; // Default for incorrect credentials or unrecognized prefix
    }
}
