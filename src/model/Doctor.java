/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Doctor extends Person implements User {
    private String specialization;
    private String loginId;
    private String password;

    public Doctor(int id, String name, int age, String gender, String contact_info, String specialization,String loginId, String password) {
        super(id, name, age,gender,contact_info); // Doctors don't strictly need age, so we pass 0
        this.specialization = specialization;
        this.password = password;
        this.loginId = loginId;
    }

    public String getSpecialization() { return specialization; }

    // This is important for the Dropdown box later!
    // It tells the GUI to display the Doctor's Name, not the memory address.
    @Override
    public String toString() {
        return this.getName() + " (" + this.specialization + ")";
    }
    
    @Override
    public String getLoginId() {
       
        return loginId;
    }

    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getRole() {
        return "Doctor";
    }
}
//public Person(int id, String name, int age, String gender, String contact_info) {