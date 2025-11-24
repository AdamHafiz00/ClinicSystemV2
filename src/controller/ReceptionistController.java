package controller;

import dao.DoctorDAO;
import dao.PatientDAO;
import dao.AppointmentDAO; // Added import
import model.Patient;
import model.Doctor;
import java.util.List;
import java.util.Set; // Added import

public class ReceptionistController {
    
    private final PatientDAO patientDAO = new PatientDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO(); // Initialize AppointmentDAO

    // --- Data Retrieval Methods ---
    
    public List<Doctor> loadDoctorsForAppointment() {
        return doctorDAO.getAllDoctors();
    }
    
    public Set<String> getBusySlots(int doctorId, String dateText) {
        // Calls the new method in AppointmentDAO
        return appointmentDAO.getBookedTimeSlots(doctorId, dateText);
    }
    
    // --- Booking Logic Method ---

    /**
     * Attempts to find an existing patient or create a new one, then books an appointment.
     */
    public boolean bookAppointment(String icNumber, String name, int age, String gender, String contact_info, String diagnosis, Doctor selectedDoctor, String dateText, String timeSlot) {
        int patientId = -1;
        Patient existingPatient = patientDAO.getPatientByIC(icNumber);
        
        // 1. Check if Patient Exists
        if (existingPatient != null) {
            patientId = existingPatient.getId();
        } else {
            // 2. Patient is new, so add to DB
            // NOTE: Must match the Patient constructor arguments
            Patient newPatient = new Patient(-1, name, age, gender, contact_info, icNumber, diagnosis, "Pending");
            patientId = patientDAO.addPatient(newPatient);
            
            if (patientId == -1) {
                // Failed to save new patient
                return false; 
            }
        }
        
        // 3. Book the Appointment
        if (patientId > 0 && selectedDoctor != null) {
            // Calls the updated method in AppointmentDAO with the dateText
            boolean bookingSuccess = appointmentDAO.addAppointment(patientId, selectedDoctor.getId(), dateText, timeSlot);
            return bookingSuccess;
        }
        
        return false;
    }
}