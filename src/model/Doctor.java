/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Doctor extends Person implements User{

    private String specialization;
    private String loginId;  // <--- NEW FIELD
    private String password; // <--- NEW FIELD
    private String role;
    
    public Doctor(int id, String name, int age, String gender, String contactInfo, String specialization,String role, String loginId, String password) {
        super(id, name, age, gender, contactInfo);
        this.specialization = specialization;
        this.loginId = loginId;
        this.password = password;
        this.role = role;
    }


    // --- Getters and Setters (Ensure these exist) ---
    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getSpecialization(){
        return specialization;
    }
    
    public String getRole(){
        return role;
    }
    // This tells the ComboBox to display the Name instead of the memory address
    @Override
    public String toString() {
        return this.getName(); // Or return this.name if it's accessible
    }

    // ... other existing getters/setters ...
}
