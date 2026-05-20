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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.scene.control.Button;

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
    @FXML
    private Button SaveButton;
    
    private String originalEmail;
    private String originalPhone;
    private String originalPassword;
    private String originalDobDay;
    private String originalDobMonth;
    private String originalDobYear;
    private String originalAvatarPath;
    @FXML
    private Button ChangeAvatar;
    @FXML
    private Button EditButton;
    
    
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
        loadUserData();
        
        setEditMode(false);
        
    }
        
        
        private void loadUserData() {
        User user = app.getCurrentUser();
        if(user == null) return;
        
        emailField.setText(user.getEmail() != null ? user.getEmail() : "");
        phoneField.setText("");
        passwordField.setText(user.getPassword() != null ? user.getPassword() : "");
        
        if(user.getBirthDate() != null) {
            LocalDate bd = user.getBirthDate();
            dobDay.setText(String.format("%02d", bd.getDayOfMonth()));
            dobMonth.setText(String.format("&02d", bd.getMonthValue()));
            dobYear.setText(String.valueOf(bd.getYear()));
        }
        
        avatarPath = user.getAvatarPath();
        
        
            
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
    
        private void setEditMode(boolean editing) {
            emailField.setEditable(editing);
            phoneField.setEditable(editing);
            passwordField.setEditable(editing);
            dobDay.setEditable(editing);
            dobMonth.setEditable(editing);
            dobYear.setEditable(editing);
            ChangeAvatar.setDisable(!editing);
            SaveButton.setDisable(!editing);
            
            String disabledStyle = "-fx-background-color: #c8f000; -fx.opacity: 0.6;";
            String enabledStyle = "-fx-background-color: #c8f000; -fx.opacity: 1.0;";
            String fieldStyle = editing ? enabledStyle : disabledStyle;
            
            emailField.setStyle(fieldStyle);
            phoneField.setStyle(fieldStyle);
            passwordField.setStyle(fieldStyle);
            
            if(!editing) hideAllErrors();
            
        }
        
        private void saveOriginalValues() {
            originalEmail = emailField.getText();
            originalPhone = phoneField.getText();
            originalPassword = passwordField.getText();
            originalDobDay = dobDay.getText();
            originalDobMonth = dobMonth.getText();
            originalDobYear = dobYear.getText();
            originalAvatarPath = avatarPath;
        }
        
        private void restoreOriginalValues() {
            emailField.setText(originalEmail);
            phoneField.setText(originalPhone);
            passwordField.setText(originalPassword);
            dobDay.setText(originalDobDay);
            dobMonth.setText(originalDobMonth);
            dobYear.setText(originalDobYear);
            avatarPath = originalAvatarPath;

            if (avatarPath != null && !avatarPath.isEmpty()) {
                Image img = new Image(new File(avatarPath).toURI().toString());
                ImagePattern pattern = new ImagePattern(img);
                sidebarAvatarCircle.setFill(pattern);
                CircleAvatar.setFill(pattern);
                avatarIcon.setVisible(false);
                sidebarAvatarIcon.setVisible(false);
                
            } else {
                sidebarAvatarCircle.setFill(javafx.scene.paint.Color.web("#c8f000"));
                CircleAvatar.setFill(javafx.scene.paint.Color("#c8f000"));
                
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
    
    boolean valid = User.checkEmail(emailField.getText().trim());
    emailError.setVisible(!valid);
    emailError.setManaged(!valid);
}

private void validatePassword() {
    String text = passwordField.getText();
    if(text.isEmpty()) {
    passwordError.setVisible(false);
    passwordError.setManaged(false);
    return;
  }
    boolean valid = User.checkPassword(text);
    passwordError.setVisible(!valid);
    passwordError.setManaged(!valid);
}

private void validatePhone() {
    String text = phoneField.getText().trim();
    boolean valid = text.isEmpty() || text.replaceAll("\\s", "").matches("\\+34\\d{9}");
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
        
        boolean valid = User.isOlderThan(birthDate, 12);
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
        
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        
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

    @FXML
    private void handleSave(ActionEvent event) {
        validateEmail();
        validatePassword();
        validatePhone();
        validateDob();
        
        if(emailError.isVisible() || passwordError.isVisible() || phoneError.isVisible() || dobError.isVisible()) {
            return;
        }
        LocalDate birthDate;
        
        try{
            int day = Integer.parseInt(dobDay.getText());
            int month = Integer.parseInt(dobMonth.getText());
            int year  = Integer.parseInt(dobYear.getText());
            
            birthDate = LocalDate.of(year, month, day);
        } catch(Exception e) {
            dobError.setVisible(false);
            dobError.setVisible(true);
            return;
        }
        
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        
        alert.setTitle("Save Changes");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure yo want to save the changes?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.isPresent() && result.get() == ButtonType.OK) {
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            
            if(!User.checkEmail(email)) {
                emailError.setVisible(true);
                emailError.setManaged(true);
                return;
            }
            
            if(!password.isEmpty() && !User.checkPassword(password)) {
                passwordError.setVisible(true);
                passwordError.setManaged(true);
                return;
            }
            if(!User.isOlderThan(birthDate, 12)) {
                dobError.setVisible(true);
                dobError.setManaged(true);
                return;
            }
            if (password.isEmpty()) {
                password = app.getCurrentUser().getPassword();
                
            }
            
            String finalAvatar = (avatarPath != null) ? avatarPath : app.getCurrentUser().getAvatarPath();
            
            app.updateCurrentUser(email, password, birthDate, finalAvatar);
            
            setEditMode(false);
        } else {
            
            restoreOriginalValues();
            setEditMode(false);
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        saveOriginalValues();
        setEditMode(true);
    }
        
        
    }
        
        
        
        