/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Adam
 */
/**
 * Model class for users stored in the 'users' table (Admin or Receptionist).
 * Implements the User interface for login functionality.
 */
public class StaffUser implements User {
    
    private int loginIdPk;
    private String username;
    private String password;
    private String role;
        private String loginId; // Stores A001, R001, etc.

    // Constructor to match the fields in the 'users' table
    public StaffUser(int loginIdPk, String username, String password, String loginId,String role) {
        this.loginIdPk = loginIdPk;
        this.username = username;
        this.password = password;
         this.loginId = loginId;
         this.role = role;
    }

    // --- Implementation of the User interface ---

    @Override
    public String getLoginId() {
        return loginId; // Use the actual username from the table
    }

    @Override
    public String getPassword() {
        return password;
    }



    public String getUsername()
    {return username;
    }
    
    public String getRole(){
        return role;
    }
}