/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Patient extends Person {
    private String diagnosis;
    private String status; // e.g., "Waiting", "In Treatment"
    private String ic;
    
    public Patient(int id, String name, int age, String diagnosis, String status) {
        super(id, name, age); // Send name/age to Parent
        this.diagnosis = diagnosis;
        this.status = status;
    }

    public String getDiagnosis() { return diagnosis; }
    public String getStatus() { return status; }
}
