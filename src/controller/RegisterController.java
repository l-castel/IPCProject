/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.ImageCursor;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.util.converter.LocalDateStringConverter;
import upv.ipc.sportlib.SportActivityApp;
import upv.ipc.sportlib.User;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import java.time.LocalDate;
import java.io.IOException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.util.converter.LocalDateStringConverter;

/**
 * FXML Controller class
 *
 * @author laura
 */

private BooleanProperty validNickname;
private BooleanProperty validEmail;
private BooleanProperty validPassword;
private BooleanProperty validConfirm;
private BooleanProperty validDate;

private ChangeListener<String> nicknameListener;
private ChangeListener<String> emailListener;
private ChangeListener<String> passwordListener;
private ChangeListener<String> confirmListener;

private String avatarPath ="";

private final SportActivityApp app = SportActivityApp.getInstance();

public class RegisterController implements Initializable {

    @FXML
    private TextField nickname;
    @FXML
    private DatePicker birthdateField;
    @FXML
    private Label birthdateError;
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
    private PasswordField confirmPasswordField;
    @FXML
    private Label confirmError;
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
        validNickname = new SimpleBooleanProperty(false);
        validEmail = new SimpleBooleanProperty(false);
        validPassword = new SimpleBooleanProperty(false);
        validConfirm = new SimpleBooleanProperty(false);
        validDate = new SimpleBooleanProperty(false);
        
        nickname.fontProperty().addListener((obv,oldValue, newValue)->{
            if(!newValue){
                checkNickname();
                if(!validNickname.get() && nicknameListener == null){
                    nicknameListener = (a,b,c)-> checkNickname();
                    nickname.textProperty().addListener(nicknameListener);
                }
            }
        });
        emailField.focusedProperty().addListener((obv,oldValue, newValue)->{
            if(!newValue){
                checkEmail();
                if(!validEmail.get() && emailListener == null){
                    emailListener = (a,b,c)-> checkEmail();
                    emailField.textProperty().addListener(emailListener);
                }
            }
        });
        passwordField.focusedProperty().addListener((obv,oldValue, newValue)->{
            if(!newValue){
                checkPassword();
                if(!validEmail.get() && passwordListener == null){
                    passwordListener = (a,b,c)-> checkPassword();
                    passwordField.textProperty().addListener(passwordListener);
                }
            }
        });
        confirmPasswordField.focusedProperty().addListener((obv,oldValue, newValue)->{
            if(!newValue){
                checkConfirm();
                if(!validEmail.get() && confirmListener == null){
                    confirmListener = (a,b,c)-> checkConfirm();
                    confirmPasswordField.textProperty().addListener(confirmListener);
                }
            }
        });
        birthdateField.focusedProperty().addListener((obv,oldValue, newValue)->{
            if(!newValue){ checkDate();}
        });
        birthdateField.valueProperty().addListener((observable, oldVal, newVal)->checkDate());
        
        BooleanBinding validFields = Bindings.and(validNickname, validDate)
                .and(validPassword)
                .and(validConfirm)
                .and(validDate);
        registerButton.disableProperty().bind(Bindings.not(validFields));
        
        cancelButton.setOnAction((event)->{
            cancelButton.getScene().getWindow().hide();
        });
        
        LocalDateStringConverter localDateStringConvert = new LocalDateStringConverter(){
            @Override
            public LocalDate fromString(String value){
                try{
                    return super.fromString(value);
                }catch(Exception e){
                    System.out.println("Exception in fromString");
                    return LocalDate.now();
                }
            }
            @Override
            public String toString(LocalDate value){
              return super.toString(value);
            }
        };
        birthdateField.setConverter(localDateStringConvert);
       
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
