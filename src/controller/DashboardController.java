/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.ResourceBundle;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author marti
 */
public class DashboardController implements Initializable {

    @FXML
    private Circle avatarCircle;
    @FXML
    private Label labelName;
    @FXML
    private Label labelSurname;
    @FXML
    private Button btnProfile;
    @FXML
    private Button btnActivities;
    @FXML
    private Button btnMaps;
    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnAddAnnotations;
    @FXML
    private Button btnSelectMap;
    @FXML
    private ScrollPane mapScrollPane;
    @FXML
    private Label lblDuration;
    @FXML
    private Label lblDistance;
    @FXML
    private Label lblAvgPace;
    @FXML
    private Label lblAvgSpeed;
    @FXML
    private Label lblMaxAltitude;
    @FXML
    private Label lblMinAltitude;
    @FXML
    private Label lblMaxElevation;
    @FXML
    private Label lblMinElevation;
    @FXML
    private Button btnSpeedRoute;
    @FXML
    private Button btnElevationProfile;
    @FXML
    private Canvas elevationCanvas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleProfile(ActionEvent event) {
    }

    @FXML
    private void handleActivities(ActionEvent event) {
    }

    @FXML
    private void handleMaps(ActionEvent event) {
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
    }


    @FXML
    private void addAnnotationsClick(MouseEvent event) {
    }


    @FXML
    private void handleSelectMap(ActionEvent event) {
    }

    @FXML
    private void handleSpeedOverRoute(ActionEvent event) {
    }

    @FXML
    private void handleElevationProfile(ActionEvent event) {
    }

    @FXML
    private void profileClick(MouseEvent event) {
    }

    @FXML
    private void activitiesClick(MouseEvent event) {
    }

    @FXML
    private void mapClick(MouseEvent event) {
    }

    @FXML
    private void logOutClick(MouseEvent event) {
    }

    @FXML
    private void handleLogOut(ActionEvent event) {
    }


    @FXML
    private void selectMapClick(MouseEvent event) {
    }

    @FXML
    private void speedOverRouteClick(MouseEvent event) {
    }

    @FXML
    private void elevationProfileClick(MouseEvent event) {
    }

    @FXML
    private void diagAddAnnotations(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/controller/AnnotationsDialog.fxml"));
        
            Parent root = loader.load();
        
            Stage dialog = new Stage();
            dialog.setTitle("");
            dialog.setScene(new Scene(root));
        
            dialog.initModality(Modality.APPLICATION_MODAL);
            
            Stage owner =(Stage) btnAddAnnotations.getScene().getWindow();
            dialog.initOwner(owner);
            
            dialog.setResizable(false);
            dialog.showAndWait();
        
        }catch (IOException e){
            e.printStackTrace();
        }
    }   
}
