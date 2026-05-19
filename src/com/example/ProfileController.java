/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.input.ScrollEvent;

import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;

import upv.ipc.sportlib.SportActivityApp;
import upv.ipc.sportlib.User;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import java.io.File;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class ProfileController implements Initializable {

    @FXML
    private Circle sidebarAvatarCircle;
    
    private String avatarPath;
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
    @FXML
    private ScrollPane ScrollPane;
    
    private static final double SCROLL_SPEED = 0.003;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%&*()\\-+=]).{8,20}$");
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "\\+34\\d{9}");
    @FXML
    private Label avatarIcon;
    
    private SportActivityApp app;
    @FXML
    private Circle CircleAvatar;
    @FXML
    private Label sidebarAvatarIcon;
    
    
    
    /**
     * 
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MouseScroll();
        setUpValidations();
        hideAllErrors();
        
        app = SportActivityApp.getInstance();
        
        User user = app.getCurrentUser();
        
        if(user != null) {
            
            if(user.getAvatar() != null) {
                ImagePattern pattern = new ImagePattern(user.getAvatar());
                
                sidebarAvatarCircle.setFill(pattern);
                CircleAvatar.setFill(pattern);
                
                avatarIcon.setVisible(false);
                sidebarAvatarIcon.setVisible(false);
                
            } else {
                avatarIcon.setVisible(true);
                sidebarAvatarIcon.setVisible(true);
            }
           
            
        } 
        
        
    }    
    
    private void MouseScroll() {
        ScrollPane.setOnMouseEntered(event -> ScrollPane.requestFocus());

        ScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            double newValue = ScrollPane.getVvalue() - (event.getDeltaY() * SCROLL_SPEED);
            newValue = Math.max(0.0, Math.min(1.0, newValue));
            ScrollPane.setVvalue(newValue);
            event.consume();
        });
    }
    
    private void hideAllErrors() {
        emailError.setVisible(false);
        emailError.setManaged(false);
        
        passwordError.setVisible(false);
        passwordError.setManaged(false);
        
        phoneError.setVisible(false);
        phoneError.setManaged(false);
        
        dobError.setVisible(false);
        dobError.setManaged(false);
    }
    
    private void setUpValidations() {
        
        emailField.setOnAction(e -> validateEmail());
        passwordField.setOnAction(e -> validatePassword());
        phoneField.setOnAction(e -> validatePhone());
        dobDay.setOnAction(e -> validateDob());
        dobMonth.setOnAction(e -> validateDob());
        dobYear.setOnAction(e -> validateDob());
            

        dobDay.textProperty().addListener((obs, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) dobDay.setText(newValue.replaceAll("[^\\d]",""));
            if(newValue.length() > 2) dobDay.setText(newValue.substring(0, 2));
        });
        
        dobMonth.textProperty().addListener((obs, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) dobMonth.setText(newValue.replaceAll("[^\\d]", ""));
            if (newValue.length() > 2)   dobMonth.setText(newValue.substring(0,2));
        });
        
        dobYear.textProperty().addListener((obs, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) dobYear.setText(newValue.replaceAll("[^\\d]",""));
            if(newValue.length() > 4) dobYear.setText(newValue.substring(0, 4));
        });
    }
    
private void validateEmail() {
    String text = emailField.getText().trim();
    boolean valid = EMAIL_PATTERN.matcher(text).matches();
    emailError.setVisible(!valid);
    emailError.setManaged(!valid);
}

private void validatePassword() {
    String text = passwordField.getText();
    boolean valid = PASSWORD_PATTERN.matcher(text).matches();
    passwordError.setVisible(!valid);
    passwordError.setManaged(!valid);
}

private void validatePhone() {
    String text = phoneField.getText().trim();
    boolean valid = PHONE_PATTERN.matcher(text).matches();
    phoneError.setVisible(!valid);
    phoneError.setManaged(!valid);
    
}
private void validateDob() {
    String dayStr = dobDay.getText().trim();
    String monStr = dobMonth.getText().trim();
    String yearStr = dobYear.getText().trim();
    
    if(dayStr.isEmpty() || monStr.isEmpty() || yearStr.isEmpty()) {
        dobError.setVisible(false);
        dobError.setManaged(false);
        return;
    }
    
    try {
        int day = Integer.parseInt(dayStr);
        int month = Integer.parseInt(monStr);
        int year = Integer.parseInt(yearStr);
        
        LocalDate birthDate = LocalDate.of(year, month, day);
        LocalDate minDate = LocalDate.now().minusYears(12);
        
        boolean valid = !birthDate.isAfter(minDate);
        dobError.setVisible(!valid);
        dobError.setManaged(!valid);
        
        
    } catch (Exception e) {
        dobError.setVisible(true);
        dobError.setManaged(true);
    }
    
}

    @FXML
    private void handleChangeAvatar(ActionEvent event) {
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Avatar");
        
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        
        File file = fileChooser.showOpenDialog(null);
        
        if(file!=null) {
            
            avatarPath = file.getAbsolutePath();
            
            Image image = new Image(file.toURI().toString());
            ImagePattern pattern = new ImagePattern(image);
            
            sidebarAvatarCircle.setFill(pattern);
            CircleAvatar.setFill(pattern);
            
            avatarIcon.setVisible(false);
            sidebarAvatarIcon.setVisible(false);
            
        }
        
        }
        
        
    }
        
        
        
        