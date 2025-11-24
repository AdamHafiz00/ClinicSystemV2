/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Adam
 */
public interface User {
    
    // Abstract methods that must be implemented by any class implementing this interface
    
    // Assuming 'contactInfo' or a separate 'username' field will be used for login
    
    
    String getLoginId(); 
    
    String getPassword();
    
    // An optional method to determine the entity's role (helpful for routing after login)
    String getRole(); 
}
