/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package clinicsystemv2;

/**
 *
 * @author Adam
 */
public class ClinicSystemV2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // This method safely launches the GUI on the Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Launch the MainMenu as the first screen
            new view.MainMenu().setVisible(true);
        });
    }

}
