package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import mapademo.MapaDemoApp;
import mapademo.Navigable;
import upv.ipc.sportlib.SportActivityApp;

public class LoginController implements Navigable
{
    @javafx.fxml.FXML
    private TextField usernameField;
    @javafx.fxml.FXML
    private Button btnLogin;
    @javafx.fxml.FXML
    private Label feedbackLabel;
    @javafx.fxml.FXML
    private PasswordField passwordField;
    @javafx.fxml.FXML
    private Button btnRegister;

    private SportActivityApp sportsApp;

    @javafx.fxml.FXML
    public void initialize() {
        sportsApp = SportActivityApp.getInstance();
    }

    @javafx.fxml.FXML
    public void handleLogin(ActionEvent actionEvent) {
        String username = usernameField.textProperty().getValue();
        String password = passwordField.textProperty().getValue();
        boolean authed = sportsApp.login(username, password);

        if(authed) {MapaDemoApp.setRoot("Dashboard");}
        else feedbackLabel.textProperty().setValue("Credentials are incorrect, try again.");
    }

    @javafx.fxml.FXML
    public void handleRegister(ActionEvent actionEvent) {
        MapaDemoApp.setRoot("Register");
    }

    @Override
    public void onNavigate() {

    }
}