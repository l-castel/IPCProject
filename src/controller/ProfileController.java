/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;

import javafx.event.Event;
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
import javafx.event.ActionEvent;

import mapademo.MapaDemoApp;
import mapademo.Navigable;
import upv.ipc.sportlib.SportActivityApp;
import upv.ipc.sportlib.User;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import java.io.File;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.ListCell;
import javafx.collections.FXCollections;
import java.time.Duration;
import upv.ipc.sportlib.Session;
import java.time.format.DateTimeFormatter;
import javafx.scene.layout.FlowPane;
import java.util.List;
import javafx.scene.image.ImageView;


/**
 * FXML Controller class
 *
 * @author PC
 */
public class ProfileController implements Initializable, Navigable {

    private String avatarPath;
    @FXML
    private TextField emailField;
    @FXML
    private Label emailError;


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

    @FXML
    private Label avatarIcon;

    private SportActivityApp app;
    @FXML
    private Circle CircleAvatar;
    @FXML
    private ImageView avatarImage;
    @FXML
    private Button SaveButton;

    private String originalEmail;
    private String originalPassword;
    private String originalDobDay;
    private String originalDobMonth;
    private String originalDobYear;
    private String originalAvatarPath;
    @FXML
    private Button ChangeAvatar;
    @FXML
    private Button EditButton;
    @FXML
    private FlowPane FlowPane;
    @FXML
    private Label NicknameLabel;
    @FXML
    private Label nicknameLabel;


    /**
     *
     * Initializes the controller class.
     */
    @Deprecated
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MouseScroll();
        setUpValidations();
        hideAllErrors();

        app = SportActivityApp.getInstance();

        setEditMode(false);



    }


    private void loadUserData() {
        User user = app.getCurrentUser();
        if(user == null) return;

        NicknameLabel.setText(user.getNickName());
        nicknameLabel.setText(user.getNickName());

        emailField.setText(user.getEmail() != null ? user.getEmail() : "");

        passwordField.setText(user.getPassword() != null ? user.getPassword() : "");

        if(user.getBirthDate() != null) {
            LocalDate bd = user.getBirthDate();
            dobDay.setText(String.format("%02d", bd.getDayOfMonth()));
            dobMonth.setText(String.format("%02d", bd.getMonthValue()));
            dobYear.setText(String.valueOf(bd.getYear()));
        }

        avatarPath = user.getAvatarPath();

        Image avatar = user.getAvatar();
        if (avatar != null) {
            avatarImage.setImage(avatar);
            CircleAvatar.setFill(new ImagePattern(avatar));
            avatarIcon.setVisible(false);
        } else {
            avatarImage.setImage(null);
            CircleAvatar.setFill(Color.web("#c8f000"));
            avatarIcon.setVisible(true);
        }

        loadSessions();
    }

    private void setEditMode(boolean editing) {
        emailField.setEditable(editing);
        passwordField.setEditable(editing);
        dobDay.setEditable(editing);
        dobMonth.setEditable(editing);
        dobYear.setEditable(editing);
        ChangeAvatar.setDisable(!editing);
        SaveButton.setDisable(!editing);

        String disabledStyle = "-fx-background-color: #c8f000; -fx-opacity: 0.6;";
        String enabledStyle = "-fx-background-color: #c8f000; -fx-opacity: 1.0;";
        String fieldStyle = editing ? enabledStyle : disabledStyle;

        emailField.setStyle(fieldStyle);
        passwordField.setStyle(fieldStyle);

        if(!editing) hideAllErrors();

    }

    private void saveOriginalValues() {
        originalEmail = emailField.getText();
        originalPassword = passwordField.getText();
        originalDobDay = dobDay.getText();
        originalDobMonth = dobMonth.getText();
        originalDobYear = dobYear.getText();
        originalAvatarPath = avatarPath;
    }

    private void restoreOriginalValues() {
        emailField.setText(originalEmail);
        passwordField.setText(originalPassword);
        dobDay.setText(originalDobDay);
        dobMonth.setText(originalDobMonth);
        dobYear.setText(originalDobYear);
        avatarPath = originalAvatarPath;

        if (avatarPath != null && !avatarPath.isEmpty()) {
            Image img = new Image(new File(avatarPath).toURI().toString());
            avatarImage.setImage(img);
            CircleAvatar.setFill(new ImagePattern(img));
            avatarIcon.setVisible(false);
        } else {
            avatarImage.setImage(null);
            CircleAvatar.setFill(Color.web("#c8f000"));
            avatarIcon.setVisible(true);
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

        dobError.setVisible(false);
        dobError.setManaged(false);
    }

    private void setUpValidations() {

        emailField.setOnAction(e -> validateEmail());
        passwordField.setOnAction(e -> validatePassword());
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
            avatarImage.setImage(image);
            CircleAvatar.setFill(new ImagePattern(image));
            avatarIcon.setVisible(false);

        }

    }

    @FXML
    private void handleSave(ActionEvent event) {
        validateEmail();
        validatePassword();
        validateDob();

        if (emailError.isVisible() || passwordError.isVisible() || dobError.isVisible()) {
            return;
        }

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.setTitle("Save Changes");

        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #1a1a1a; -fx-padding: 30; -fx-background-radius: 10;");
        root.setAlignment(javafx.geometry.Pos.CENTER);
        root.setPrefWidth(350);

        Label title = new Label("Save Changes");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18; -fx-font-weight: bold;");

        Label message = new Label("Are you sure you want to save the changes?");
        message.setStyle("-fx-text-fill: #bbbbbb; -fx-font-size: 13;");
        message.setWrapText(true);
        message.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        javafx.scene.control.Separator sep = new javafx.scene.control.Separator();
        sep.setStyle("-fx-background-color: #333333;");

        HBox buttons = new HBox(15);
        buttons.setAlignment(javafx.geometry.Pos.CENTER);

        Button confirmBtn = new Button("Save");
        confirmBtn.setStyle("-fx-background-color: #c8f000; -fx-text-fill: #1a1a1a; "
                + "-fx-font-weight: bold; -fx-font-size: 13; "
                + "-fx-padding: 8 25 8 25; -fx-background-radius: 5;");

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #333333; -fx-text-fill: white; "
                + "-fx-font-size: 13; -fx-padding: 8 25 8 25; -fx-background-radius: 5;");

        buttons.getChildren().addAll(confirmBtn, cancelBtn);
        root.getChildren().addAll(title, message, sep, buttons);

        Scene dialogScene = new Scene(root);
        dialogScene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        dialogStage.setScene(dialogScene);

        confirmBtn.setOnAction(ev -> {
            dialogStage.close();

            User user = app.getCurrentUser();
            if (user == null) {
                return;
            }

            String email = emailField.getText().trim();
            String password = passwordField.getText();

            if (!email.isEmpty() && !User.checkEmail(email)) {
                emailError.setVisible(true);
                emailError.setManaged(true);
                return;
            }
            if (!password.isEmpty() && !User.checkPassword(password)) {
                passwordError.setVisible(true);
                passwordError.setManaged(true);
                return;
            }

            LocalDate birthDate = user.getBirthDate();
            String dayStr = dobDay.getText().trim();
            String monthStr = dobMonth.getText().trim();
            String yearStr = dobYear.getText().trim();

            if (!dayStr.isEmpty() && !monthStr.isEmpty() && !yearStr.isEmpty()) {
                try {
                    int day = Integer.parseInt(dayStr);
                    int month = Integer.parseInt(monthStr);
                    int year = Integer.parseInt(yearStr);
                    birthDate = LocalDate.of(year, month, day);
                } catch (Exception e) {
                    dobError.setVisible(true);
                    dobError.setManaged(true);
                    return;
                }

                if (!User.isOlderThan(birthDate, 12)) {
                    dobError.setVisible(true);
                    dobError.setManaged(true);
                    return;
                }
            }

            if (email.isEmpty()) {
                email = user.getEmail();
            }
            if (password.isEmpty()) {
                password = user.getPassword();
            }

            String finalAvatar = (avatarPath != null) ? avatarPath : user.getAvatarPath();

            if (app.updateCurrentUser(email, password, birthDate, finalAvatar)) {
                loadUserData();
                saveOriginalValues();
                setEditMode(false);
            }
        });

        cancelBtn.setOnAction(ev -> {
            dialogStage.close();
            restoreOriginalValues();
            setEditMode(false);
        });

        dialogStage.showAndWait();
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        saveOriginalValues();
        setEditMode(true);
    }

    private void loadSessions() {
        User user = app.getCurrentUser();
        if(user == null) return;

        FlowPane.getChildren().clear();
        FlowPane.setHgap(12);
        FlowPane.setVgap(12);
        FlowPane.setPrefWrapLength(760);
        FlowPane.setStyle("-fx-background-color: #c8f000; -fx-padding: 16; -fx-background-radius: 10;");


        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        List<Session> sessions = app.getSessionsByUser(user);

        for(int i = 0; i < sessions.size(); i++) {
            Session session = sessions.get(i);

            long totalSeconds = (long) session.getDuration().getSeconds();
            long hours = totalSeconds / 3600;
            long minutes = (totalSeconds % 3600) / 60;
            String duration = hours + "h " + minutes + "min ";

            VBox card = new VBox(6);
            card.setStyle("-fx-background-color: #d4f535; -fx-background-radius: 8; -fx-padding: 14;");
            card.setPrefWidth(220);

            Label titleLabel = new Label("Session " + (i + 1));
            titleLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #111111; ");

            Label durationLabel = new Label("Duration: " + duration);
            durationLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #333333;");

            Label startLabel = new Label("Start: " + session.getStartTime().format(fmt));
            startLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #333333; ");

            Label endLabel = new Label("End: " + session.getEndTime().format(fmt));
            endLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #333333; ");

            Label importedLabel = new Label("Imported activities: " + session.getImportedActivities());
            importedLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #333333; ");

            Label viewedLabel = new Label("Viewed activities: " + session.getViewedActivities());
            viewedLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #333333; ");

            Label annotationsLabel = new Label("Annotations created: " + session.getAnnotationsCreated());
            annotationsLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #333333; ");


            card.getChildren().addAll(titleLabel, durationLabel, startLabel, endLabel, importedLabel, viewedLabel, annotationsLabel);

            FlowPane.getChildren().add(card);
            FlowPane.setMaxWidth(Double.MAX_VALUE);


        }


    }

    @Override
    public void onNavigate() {
        loadUserData();
        loadSessions();
    }

    @FXML
    private void handleProfile(ActionEvent event) {
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        MapaDemoApp.setRoot("Dashboard");
    }

    @FXML
    private void handleMaps(ActionEvent event) {
        MapaDemoApp.setRoot("Maps");
    }

    @FXML
    private void handleActivities(ActionEvent event) {
        MapaDemoApp.setRoot("Activities");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        app.logout();
        MapaDemoApp.setRoot("Login");
    }
}
        
        
        