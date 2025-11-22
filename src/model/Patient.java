/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Patient extends Person {
    private String diagnosis;
    private String status; // e.g., "Waiting", "In Treatment"
    private String ic_number;
    
    
    public Patient(int id, String name, int age,String ic_number, String diagnosis, String status) {
        super(id, name, age); // Send name/age to Parent
        this.diagnosis = diagnosis;
        this.status = status;
        this.ic_number = ic_number;
    }

    public String getDiagnosis() { return diagnosis; }
    public String getStatus() { return status; }
    public String getIcNumber(){
        return ic_number;
    }
}
