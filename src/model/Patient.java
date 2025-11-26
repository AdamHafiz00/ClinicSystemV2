/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Patient extends Person {
    private String icNumber; // <--- NEW FIELD


    // UPDATED CONSTRUCTOR (Now accepts icNumber)
    public Patient(int id, String name, int age, String gender, String contact_info, String icNumber) {
        super(id, name, age,gender,contact_info);
        this.icNumber = icNumber;

    }

    // Getters
    public String getIcNumber() { return icNumber; }

}
//public Person(int id, String name, int age, String gender, String contact_info) {