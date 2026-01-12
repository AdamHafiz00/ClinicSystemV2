/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author Adam
 */
import com.toedter.calendar.JDateChooser;
import controller.ReceptionistController;
import java.text.SimpleDateFormat;
import java.util.Date;
import exception.InvalidPatientDataException; //impoort the custom exception handler
import javax.swing.JOptionPane;

public class ReceptionForm extends javax.swing.JFrame {

    private final ReceptionistController controller = new ReceptionistController();

    /**
     * Creates new form ReceptionForm
     */
    public ReceptionForm() {
        initComponents();
        this.setLocationRelativeTo(null);
        loadDoctors();
        setupListeners();
        jDateChooser1.setMinSelectableDate(new java.util.Date());
    }

    private void setupListeners() {
        // 1. Listener for Doctor Dropdown Change
        comboDoctor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Call the method every time a different doctor is selected
                updateAvailableSlots();
            }
        });

        // 2. Listener for Date Chooser Change
        // We listen for the "date" property change, which fires when the user picks a date.
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    // Call the method every time the date value changes
                    updateAvailableSlots();
                }
            }
        });

        // Initial call to load slots when the form first opens
        // (This is important in case the initial doctor/date selection is valid)
        updateAvailableSlots();
    }

    // Helper method to fill the dropdown
    private void loadDoctors() {
        // Replace previous DAO calls with controller call
        java.util.List<model.Doctor> doctors = controller.loadDoctorsForAppointment();

        comboDoctor.removeAllItems(); // Recommended to clear before adding
        for (model.Doctor d : doctors) {
            comboDoctor.addItem(d);
        }
    }

    public String convertDateChooserToString(com.toedter.calendar.JDateChooser dateChooser) {
        // 1. Get the Date object
        Date date = dateChooser.getDate();

        // 2. Check for null before formatting (as previously discussed)
        if (date == null) {
            // You can return null or an empty string, depending on your database needs.
            // Returning null is safer if the database column allows nulls.
            return null;
        }

        // 3. Define the desired format (YYYY-MM-DD is standard for SQL)
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        // 4. Format and return the string
        return formatter.format(date);
    }
        // Call Custom Exception Handler to validate Input
    private void validateInputs() throws InvalidPatientDataException {
        // 1. Check for Empty Fields
        if (txtICNumber.getText().trim().isEmpty()) {
            throw new InvalidPatientDataException("IC Number is required.");
        }
        if (txtName.getText().trim().isEmpty()) {
            throw new InvalidPatientDataException("Patient Name cannot be empty.");
        }
        if (txtAge.getText().trim().isEmpty()) {
            throw new InvalidPatientDataException("Age is required.");
        }
        if (txtContact_info.getText().trim().isEmpty()) {
            throw new InvalidPatientDataException("Contact Info is required.");
        }

        // 2. Check Date Selection
        if (jDateChooser1.getDate() == null) {
            throw new InvalidPatientDataException("Please select an Appointment Date.");
        }

        // 3. Check Doctor Selection
        if (comboDoctor.getSelectedItem() == null) {
            throw new InvalidPatientDataException("Please select a Doctor.");
        }

        // 4. Check IC Format (Must be 12 digits)
        String ic = txtICNumber.getText().trim();
        if (!ic.matches("\\d{12}")) {
            throw new InvalidPatientDataException("IC Number must be exactly 12 digits (e.g., 990101105566).");
        }

        // 5. Check Age Validity
        try {
            int age = Integer.parseInt(txtAge.getText().trim());
            if (age <= 0 || age > 120) {
                throw new InvalidPatientDataException("Age must be between 1 and 120.");
            }
        } catch (NumberFormatException e) {
            throw new InvalidPatientDataException("Age must be a valid number.");
        }
        // 1. Get the selected date
        Date selectedDate = jDateChooser1.getDate();
        if (selectedDate == null) {
            throw new InvalidPatientDataException("Please select an Appointment Date.");
        }

        // 2. Get 'Today' but reset time to 00:00:00 (Midnight)
        // This ensures that if the user picks 'Today', it is NOT counted as 'Past'.
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        Date todayZeroTime = cal.getTime();

        // 3. Compare: If selected date is BEFORE today (00:00:00), throw error
        if (selectedDate.before(todayZeroTime)) {
            throw new InvalidPatientDataException("Invalid Date. You cannot book an appointment in the past.");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtAge = new javax.swing.JTextField();
        comboDoctor = new javax.swing.JComboBox<>();
        comboTimeSlot = new javax.swing.JComboBox<>();
        btnBook = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtICNumber = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbGender = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtContact_info = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        backBtn = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Name : ");

        jLabel2.setText("Age : ");

        jLabel3.setText("Select Doctor");

        jLabel4.setText("Time Slot : ");

        txtName.setToolTipText("");
        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        comboDoctor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboDoctorActionPerformed(evt);
            }
        });

        comboTimeSlot.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "09:00-10:00", "10:00-11:00", "11:00-12:00" }));

        btnBook.setText("Book Appointment");
        btnBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBookActionPerformed(evt);
            }
        });

        jLabel5.setText("IC Number : ");

        txtICNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtICNumberFocusLost(evt);
            }
        });

        jLabel6.setText("Gender :");

        cbGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));

        jLabel7.setText("Contact Info : ");

        jLabel8.setText("Appointment Date : ");

        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/CPMS logo (resized).jpg"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backBtn)
                        .addGap(134, 134, 134)
                        .addComponent(btnBook))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel8)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtICNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtContact_info, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtAge, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(comboTimeSlot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel9)
                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(117, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtICNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(cbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtContact_info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(comboDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(comboTimeSlot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBook)
                    .addComponent(backBtn))
                .addGap(100, 100, 100))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void btnBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBookActionPerformed
        try {
            // 1. RUN CUSTOM VALIDATION FIRST
            validateInputs();

            // 2. Only if validation passes, retrieve the data
            String name = txtName.getText().trim();
            String ageStr = txtAge.getText().trim();
            String gender = cbGender.getSelectedItem().toString();
            String contact_info = txtContact_info.getText().trim();
            String icNumber = txtICNumber.getText().trim();

            // Get date and slot
            String appointmentDate = convertDateChooserToString(jDateChooser1);
            int parsed_age = Integer.parseInt(ageStr); // Safe because validateInputs checked it
            model.Doctor selectedDoc = (model.Doctor) comboDoctor.getSelectedItem();

            // Safety check for time slot (incase validation passed but slot combo is empty)
            if (comboTimeSlot.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "No time slots available for this doctor on this date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String timeSlot = comboTimeSlot.getSelectedItem().toString();

            // 3. Pass data to Controller
            boolean success = controller.bookAppointment(
                    icNumber,
                    name,
                    parsed_age,
                    gender,
                    contact_info,
                    "Checkup",
                    selectedDoc,
                    appointmentDate,
                    timeSlot
            );

            // 4. Handle Success
            if (success) {
                JOptionPane.showMessageDialog(this, "Success! Appointment Booked.");

                // Clear fields for next patient
                txtName.setText("");
                txtAge.setText("");
                txtICNumber.setText("");
                txtContact_info.setText("");

                // Reset fields to enabled (in case they were locked by auto-fill)
                txtName.setEnabled(true);
                txtAge.setEnabled(true);
                txtContact_info.setEnabled(true);
                cbGender.setEnabled(true);

                // Refresh slots
                updateAvailableSlots();

            } else {
                JOptionPane.showMessageDialog(this, "Error saving appointment. Check if IC is unique or Database connection.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (InvalidPatientDataException ex) {
            // 5. CATCH CUSTOM VALIDATION ERRORS HERE
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            // 6. Catch unexpected errors
            JOptionPane.showMessageDialog(this, "A general error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnBookActionPerformed

    private void txtICNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtICNumberFocusLost
        String ic = txtICNumber.getText().trim();
        if (ic.isEmpty()) {
            return; // Don't check if empty
        }
        dao.PatientDAO dao = new dao.PatientDAO();
        model.Patient p = dao.getPatientByIC(ic); // Call Phase 2 method

        if (p != null) {
            if (!txtName.getText().equals(p.getName())) {
             
            // Found! Auto-fill fields
            javax.swing.JOptionPane.showMessageDialog(this, "Patient Found: " + p.getName());
            txtName.setText(p.getName());
            txtAge.setText(String.valueOf(p.getAge()));
            cbGender.setSelectedItem(p.getGender());
            txtContact_info.setText(p.getContactInfo());
            }
            // Lock fields so staff don't accidentally change them
            txtName.setEnabled(false);
            txtAge.setEnabled(false);
            txtContact_info.setEnabled(false);
            cbGender.setEnabled(false);
        } else {
            // Not found? Clear fields for new entry
            txtName.setEnabled(true);
            txtAge.setEnabled(true);
        }
    }//GEN-LAST:event_txtICNumberFocusLost

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        new MainMenu().setVisible(true);
        dispose();


    }//GEN-LAST:event_backBtnActionPerformed

    private void comboDoctorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboDoctorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboDoctorActionPerformed
// Define your clinic hours here
    private final String[] ALL_SLOTS = {"09:00-10:00", "10:00-11:00", "11:00-12:00", "14:00-15:00", "15:00-16:00"};

    private void updateAvailableSlots() {
        try {
            if (comboDoctor.getSelectedItem() == null || convertDateChooserToString(jDateChooser1) == null) {
                return;
            }

            model.Doctor selectedDoc = (model.Doctor) comboDoctor.getSelectedItem();
            String dateText = convertDateChooserToString(jDateChooser1);

            // 3. Get BUSY slots from DB (Call the Controller!)
            java.util.Set<String> busySlots = controller.getBusySlots(selectedDoc.getId(), dateText);

            // 4. Reset and Fill Dropdown
            comboTimeSlot.removeAllItems();

            for (String slot : ALL_SLOTS) {
                if (!busySlots.contains(slot)) {
                    comboTimeSlot.addItem(slot);
                }
            }

        } catch (Exception e) {
            // Handle exceptions if needed
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReceptionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReceptionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReceptionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReceptionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReceptionForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JButton btnBook;
    private javax.swing.JComboBox<String> cbGender;
    private javax.swing.JComboBox<Object> comboDoctor;
    private javax.swing.JComboBox<String> comboTimeSlot;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtAge;
    private javax.swing.JTextField txtContact_info;
    private javax.swing.JTextField txtICNumber;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
