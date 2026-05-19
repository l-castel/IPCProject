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
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;
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
    
    private BooleanProperty validEmail;
    private BooleanProperty validPassword;
    private BooleanProperty validConfirm;
    private BooleanProperty validDate;
    
    private ChangeListener<String> emailListener;
    private ChangeListener<String> paswordListener;
    private ChangeListener<String> confirmListener;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        validEmail = new SimpleBooleanProperty();
        validPassword = new SimpleBooleanProperty();
        validConfirm = new SimpleBooleanProperty();
        validDate = new SimpleBooleanProperty();
        
        emailField.focusedProperty().addListener((observable, oldValue, newValue)->{
            if(!newValue){ //focus lost
                checkEmail();
                if (!validEmail.get()) {
                    if (emailListener == null) {
                        emailListener = (a, b, c) -> checkEmail();
                        emailField.textProperty().addListener(emailListener);
                    }
                }            
            }      
        });
        passwordField.focusedProperty().addListener((observable, oldValue, newValue)->{
            if(!newValue){ //focus lost
                checkPassword();
                if (!validPassword.get()) {
                    if (paswordListener == null) {
                        paswordListener = (a, b, c) -> checkEmail();
                        passwordField.textProperty().addListener(paswordListener);
                    }
                }            
            }      
        });
        
        confirmPasswordField.focusedProperty().addListener((observable, oldValue, newValue)->{
            if(!newValue){ //focus lost
                checkPasswordsMatch();
                if (!validConfirm.get()) {
                    if (confirmListener == null) {
                        confirmListener = (a, b, c) -> checkEmail();
                        confirmPasswordField.textProperty().addListener(confirmListener);
                    }
                }            
            }      
        });
        BooleanBinding validFields = Bindings.and(validEmail, validPassword)
                 .and(validConfirm);
        
        registerButton.disableProperty().bind(
                Bindings.not(validFields)
           ); 
        cancelButton.setOnAction( (event)->{
            cancelButton.getScene().getWindow().hide();
});
    }    

    @FXML
    private void selectAvatar(ActionEvent event) {
    }

    @FXML
    private void register(ActionEvent event) {
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        birthdateField.setValue(null);
        validEmail.setValue(Boolean.FALSE);
        validPassword.setValue(Boolean.FALSE);
        validConfirm.setValue(Boolean.FALSE);
        validDate.setValue(Boolean.FALSE);
        
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/view/Dashboard.FXML"));
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene (root);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
    }
    
    private void showError(boolean isValid, Node field, Node errorMessage){
        errorMessage.setVisible(!isValid);
        field.setStyle(((isValid) ? "" : "-fx-background-color: #FCE5E0"));
    }
    
    private void checkEmail(){
        String email = emailField.getText();
        boolean isValid = email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9]+\\.)+[a-zA-Z]{2,6}$");
        validEmail.set(isValid); //actualiza la property asociada
        showError(isValid, emailField, emailError); //muestra o esconde el mensaje de error
    }
    private void checkPassword() {
        String password = passwordField.getText();
        boolean isValid = password.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,15}$");
        validPassword.set(isValid); //actualiza la property asociada
        showError(isValid, passwordField, passwordError); //muestra o esconde el mensaje de error
    }
    private void checkPasswordsMatch() {
        boolean match = passwordField.getText().equals(confirmPasswordField.getText());
        validConfirm.set(match);
        showError(match, confirmPasswordField, confirmError);
    }
    /*private void checkDate(){
        LocalDate value = dateField.getValue();
        boolean isValid = value.isBefore(LocalDate.now().minus(16, YEARS));
        validDate.set(isValid);
        showError(isValid, dateField, dateError);
    }*/
}
