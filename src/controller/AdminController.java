/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.PatientDAO;
import dao.DoctorDAO; // Will be needed for CRUD Doctor operations later
import model.Doctor; // Will be needed for doctor management later
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class to manage business logic for the AdminPanel (Dashboard, Doctor CRUD, Appointment Override).
 */
public class AdminController {
    
    private final PatientDAO patientDAO = new PatientDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();
    
    /**
     * Retrieves the core dashboard statistics by counting patients in different statuses.
     * @return A Map where keys are status names and values are the counts.
     */
    public Map<String, Integer> getDashboardStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        // Define the statuses we need to count
        final String PENDING = "Waiting"; // Assuming 'Waiting' is the status for pending appointments
        final String IN_TREATMENT = "In-treatment"; 
        final String COMPLETE = "Complete";
        
        // Use the DAO method to fetch the counts
        int waitingCount = patientDAO.countPatientsByStatus(PENDING);
        int inTreatmentCount = patientDAO.countPatientsByStatus(IN_TREATMENT);
        int completeCount = patientDAO.countPatientsByStatus(COMPLETE);
        
        // Add the counts to the map
        stats.put("Waiting", waitingCount);
        stats.put("In-treatment", inTreatmentCount);
        stats.put("Complete", completeCount);
        
        return stats;
    }
    
    // --- Doctor Management Methods (To be implemented later) ---
    
    public List<Doctor> getAllDoctors() {
        return doctorDAO.getAllDoctors();
    }
    
 /**
     * Handles the business logic for adding a new doctor.
     * * @param doctor The Doctor object populated by the AdminPanel form.
     * @return True if the doctor was added successfully, false otherwise.
     */
    public boolean addDoctor(Doctor doctor) {
        // The DAO returns the new ID (> 0) on success
        return doctorDAO.addDoctor(doctor) > 0;
    }
    
    // public boolean updateDoctor(...) {...}
    // public boolean deleteDoctor(int doctorId) {...}
}
