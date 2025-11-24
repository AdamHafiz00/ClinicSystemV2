/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Doctor extends Person {
    private String specialization;

    public Doctor(int id, String name, int age, String gender, String contact_info, String specialization) {
        super(id, name, age,gender,contact_info); // Doctors don't strictly need age, so we pass 0
        this.specialization = specialization;
    }

    public String getSpecialization() { return specialization; }

    // This is important for the Dropdown box later!
    // It tells the GUI to display the Doctor's Name, not the memory address.
    @Override
    public String toString() {
        return this.getName() + " (" + this.specialization + ")";
    }
}
//public Person(int id, String name, int age, String gender, String contact_info) {