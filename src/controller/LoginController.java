/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UserDAO;
import model.User; 

public class LoginController {

    private final UserDAO userDAO = new UserDAO();

    /**
     * Authenticates a user using their unique login ID and password.
     * @param loginId The provided login identifier (Doctor ID, A001, R001, etc.).
     * @param password The provided password.
     * @return The authenticated User object (StaffUser or Doctor), or null on failure.
     */
    public User authenticate(String loginId, String password) { // <--- PARAMETER NAME CHANGED
        // The core logic remains delegation to the DAO
        return userDAO.getUserByCredentials(loginId, password); 
    }
}
