/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Patient extends Person {
    private String icNumber; // <--- NEW FIELD
    private String diagnosis;
    private String status;

    // UPDATED CONSTRUCTOR (Now accepts icNumber)
    public Patient(int id, String name, int age, String gender, String contact_info, String icNumber, String diagnosis, String status) {
        super(id, name, age,gender,contact_info);
        this.icNumber = icNumber;
        this.diagnosis = diagnosis;
        this.status = status;
    }

    // Getters
    public String getIcNumber() { return icNumber; }
    public String getDiagnosis() { return diagnosis; }
    public String getStatus() { return status; }
}
//public Person(int id, String name, int age, String gender, String contact_info) {