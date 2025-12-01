/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Doctor extends Person implements User{
    private String specialization;
    private String loginId;   // <-- Add these back temporarily to hold auth data 
    private String password;  //     or move them to the Person class if appropriate.
    private String role;      //     Required to implement getRole()


    public Doctor(int id, String name, int age, String gender, String contact_info, String specialization) {
        
        super(id, name, age,gender,contact_info); // Doctors don't strictly need age, so we pass 0
        this.specialization = specialization;

    }
// <<< NEW Constructor for LOGIN (Must hold the credentials retrieved from USERS table) >>>
    public Doctor(int id, String name, int age, String gender, String contact_info, String specialization, String loginId, String password, String role) {
        super(id, name, age, gender, contact_info);
        this.specialization = specialization;
        this.loginId = loginId;
        this.password = password;
        this.role = role;
    }
    

    public String getSpecialization() { return specialization; }

    @Override
    public String getLoginId() {
        return this.loginId; // Return the loginId stored in this instance
    }
    
    @Override
    public String getPassword() {
        return this.password; // Return the password stored in this instance
    }
    
    @Override
    public String getRole() {
        return this.role; // Return the role ("Doctor")
    }
    
    // This is important for the Dropdown box later!
    // It tells the GUI to display the Doctor's Name, not the memory address.
    @Override
    public String toString() {
       return this.getName();
    }
    

    
}
//public Person(int id, String name, int age, String gender, String contact_info) {