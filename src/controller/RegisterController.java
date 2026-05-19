/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


/**
 * FXML Controller class
 *
 * @author laura
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField emailField;
    @FXML
    private Label emailError;
    @FXML
    private TextField phoneField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label passwordError;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label confirmError;
    @FXML
    private DatePicker birthdateField;
    @FXML
    private ImageView imageView;
    @FXML
    private Button buttonSelectAvatar;
    @FXML
    private Button registerButton;
    @FXML
    private Button cancelButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void selectAvatar(ActionEvent event) {
    }

    @FXML
    private void register(ActionEvent event) {
    }

    @FXML
    private void cancel(ActionEvent event) {
    }
    
}
