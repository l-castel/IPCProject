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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author laura
 */
public class MapsController implements Initializable {

    @FXML
    private Circle avatarCircle;
    @FXML
    private ImageView avatarImage;
    @FXML
    private Label labelName;
    @FXML
    private Label labelSurname;
    @FXML
    private Button profileButton;
    @FXML
    private Button activitiesButton;
    @FXML
    private Button mapsButton;
    @FXML
    private Button dashboardButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button addButton;
    @FXML
    private Button addButton1;
    @FXML
    private Button addButton11;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void goToProfile(ActionEvent event) {
    }

    @FXML
    private void goToActivities(ActionEvent event) {
    }

    @FXML
    private void goToMaps(ActionEvent event) {
    }

    @FXML
    private void goToDashboard(ActionEvent event) {
    }

    @FXML
    private void logOut(ActionEvent event) {
    }

    @FXML
    private void addMap(ActionEvent event) {
    }
    
}
