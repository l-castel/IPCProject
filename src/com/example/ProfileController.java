/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class ProfileController implements Initializable {

    @FXML
    private Circle sidebarAvatarCircle;
    @FXML
    private TextField emailField;
    @FXML
    private Label emailError;
    @FXML
    private TextField phoneField;
    @FXML
    private Label phoneError;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label passwordError;
    @FXML
    private TextField dobDay;
    @FXML
    private TextField dobMonth;
    @FXML
    private TextField dobYear;
    @FXML
    private Label dobError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
