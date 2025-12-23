/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

/**
 * Custom exception thrown when patient input validation fails.
 */
public class InvalidPatientDataException extends Exception {
    
    // Constructor that accepts a custom error message
    public InvalidPatientDataException(String message) {
        super(message);
    }
}