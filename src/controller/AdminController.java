package controller;

import dao.PatientDAO;
import dao.DoctorDAO;
import model.Doctor;
import dao.AppointmentDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class to manage business logic for the AdminPanel (Dashboard,
 * Doctor CRUD, Appointment Override).
 */
public class AdminController {

    private final PatientDAO patientDAO = new PatientDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    /**
     * Retrieves the core dashboard statistics by counting patients in different
     * statuses.
     *
     * @return A Map where keys are status names and values are the counts.
     */
    public Map<String, Integer> getDashboardStatistics() {
        Map<String, Integer> stats = new HashMap<>();

        // Define the statuses we need to count
        final String PENDING = "Waiting";
        final String IN_TREATMENT = "In-treatment";
        final String COMPLETE = "Complete";

        // Use the DAO method to fetch the counts (Assuming patientDAO has countPatientsByStatus)
        // Note: You must ensure patientDAO.countPatientsByStatus exists or remove this logic if not yet implemented.


        // Add the counts to the map
        stats.put("Waiting",  patientDAO.countPatientsByStatus(PENDING));
        stats.put("In-treatment",  patientDAO.countPatientsByStatus(IN_TREATMENT));
        stats.put("Complete", patientDAO.countPatientsByStatus(COMPLETE));

        return stats;
    }

    // --- Doctor Management Methods ---
    public List<Doctor> getAllDoctors() {
        // You must add 'throws Exception' if doctorDAO.getAllDoctors() throws an exception
        return doctorDAO.getAllDoctors();
    }

    public boolean addDoctor(Doctor doctor) {
        return doctorDAO.addDoctor(doctor) > 0;
    }

    public boolean updateDoctor(Doctor doctor) {
        return doctorDAO.updateDoctor(doctor);
    }

    public boolean deleteDoctor(int doctorId) {
        // 1. Delete all appointments linked to this doctor (Assuming this DAO method exists)
        boolean appointmentsCleared = appointmentDAO.deleteAppointmentsByDoctorId(doctorId);

        if (!appointmentsCleared) {
            return false;
        }

        // 2. Delete the doctor record
        return doctorDAO.deleteDoctor(doctorId);
    }

    /**
     * Retrieves full appointment details, filtered by doctor, date, and time.
     * FIX: Method name corrected to match DAO (getViewAppointments). FIX: Added
     * 'throws Exception' because the DAO method throws SQLException.
     */
    public List<Map<String, Object>> getViewAppointments(int doctorId, String date, String time) throws Exception {
        // Corrected method name to match the DAO: getViewAppointments
        return appointmentDAO.getViewAppointments(doctorId, date, time);
    }

    /**
     * Cancels (deletes) an appointment. FIX: Now works because
     * deleteAppointment(int id) is in the DAO (Step 1).
     */
    public boolean cancelAppointment(int appointmentId) {
        return appointmentDAO.deleteAppointment(appointmentId);
    }
    
    public java.util.List<java.util.Map<String, Object>> getInTreatmentAppointmentsForAdmin() throws Exception {
    // Assuming you have an instance of AppointmentDAO available
    dao.AppointmentDAO appointmentDAO = new dao.AppointmentDAO(); 
    
    // Calls the DAO method we prepared in the previous step
    return appointmentDAO.getInTreatmentAppointments(); 
}
}
