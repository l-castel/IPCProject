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
import mapademo.Navigable;
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
import java.time.temporal.ChronoUnit;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.net.URI;
import java.net.URL;
import mapademo.MapaDemoApp;

/**
 * FXML Controller class
 *
 * @author laura
 */



public class RegisterController implements Initializable, Navigable {

    @FXML
    private TextField nickname;
    @FXML
    private DatePicker birthdateField;
    @FXML
    private TextField emailField;
    @FXML
    private Label emailError;
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
    @FXML
    private Label nicknameError;
    @FXML
    private Label birthdateError;
    
    
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
        
        nickname.focusedProperty().addListener((obv,oldValue, newValue)->{
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
                if(!validPassword.get() && passwordListener == null){
                    passwordListener = (a,b,c)-> checkPassword();
                    passwordField.textProperty().addListener(passwordListener);
                }
            }
        });
        confirmPasswordField.focusedProperty().addListener((obv,oldValue, newValue)->{
            if(!newValue){
                checkConfirm();
                if(!validConfirm.get() && confirmListener == null){
                    confirmListener = (a,b,c)-> checkConfirm();
                    confirmPasswordField.textProperty().addListener(confirmListener);
                }
            }
        });
        birthdateField.focusedProperty().addListener((obv,oldValue, newValue)->{
            if(!newValue){ checkDate();}
        });
        birthdateField.valueProperty().addListener((observable, oldVal, newVal)->checkDate());
        
        BooleanBinding validFields = Bindings.and(validNickname, validEmail)
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
        
       imageView.setFitWidth(100);
       imageView.setFitHeight(100);
       imageView.setPreserveRatio(false);
       Circle clip = new Circle(50,50,50);
       imageView.setClip(clip);
       
       Image defaultAvatar = new Image(getClass().getResource("/resources/logo.png").toExternalForm());
       imageView.setImage(defaultAvatar);
    }

    public void checkNickname(){
        boolean isValid = User.checkNickName(nickname.getText().trim());
        validNickname.set(isValid);
        showError(isValid, nickname, nicknameError);
    }
    public void checkEmail(){
        boolean isValid = User.checkEmail(emailField.getText().trim());
        validEmail.set(isValid);
        showError(isValid, emailField, emailError);
    }
    public void checkPassword(){
        boolean isValid = User.checkPassword(passwordField.getText().trim());
        validPassword.set(isValid);
        showError(isValid, passwordField, passwordError);
    }
    public void checkConfirm(){
        boolean same = !passwordField.getText().isEmpty()
                && passwordField.getText().equals(confirmPasswordField.getText());
        validConfirm.set(same);
        showError(same, confirmPasswordField, confirmError);
    }
    
    public void checkDate(){
        LocalDate value = birthdateField.getValue();
        if(value == null){
            validDate.set(false);
            birthdateError.setVisible(true);
            return;
        }
        boolean isValid = User.isOlderThan(value, 12);
        validDate.set(isValid);
        showError(isValid, birthdateField, birthdateError);
    }

    @FXML
    private void selectAvatar(ActionEvent event) {
        FileChooser chosen = new FileChooser();
        chosen.setTitle("Select Avatar");
        chosen.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.jpeg"));
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        File file = chosen.showOpenDialog(stage);
        if(file != null){
            avatarPath = file.getAbsolutePath();
            imageView.setImage(new Image(file.toURI().toString()));
            Circle newClip = new Circle(50,50,50);
            imageView.setClip(newClip);
        }
    }

    @FXML
    private void register(ActionEvent event) {
        boolean ok = app.registerUser(nickname.getText().trim(), 
                emailField.getText().trim(), passwordField.getText(), 
                birthdateField.getValue(), avatarPath);
        
        nickname.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        birthdateField.setValue(null);
        avatarPath = "";
        
        validNickname.setValue(Boolean.FALSE);
        validEmail.setValue(Boolean.FALSE);
        validPassword.setValue(Boolean.FALSE);
        validConfirm.setValue(Boolean.FALSE);
        validDate.setValue(Boolean.FALSE);
        
        if(ok){
            goToLogin(event);
        }else{
            nicknameError.setText("Nickname already in use");
            showError(false, nickname, nicknameError);
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        cancelButton.getScene().getWindow().hide();
    }
    
    private void goToLogin(ActionEvent event){
        MapaDemoApp.setRoot("Login");
    }
    private void showError(boolean isValid, Node field, Node errorMessage){
        errorMessage.setVisible(!isValid);
        field.setStyle(((isValid) ? "" : "-fx-background-color: #FCE5E0"));
    }

    @Override
    public void onNavigate() {

    }
}
