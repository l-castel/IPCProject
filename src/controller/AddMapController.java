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
import javafx.scene.control.TextField;


/**
 * FXML Controller class
 *
 * @author laura
 */
public class AddMapController implements Initializable {

    @FXML
    private TextField mapNameField;
    @FXML
    private Button searchFileBtn;
    @FXML
    private TextField maxLatField;
    @FXML
    private TextField minLatField;
    @FXML
    private TextField maxLonField;
    @FXML
    private TextField minLonField;
    @FXML
    private Button uploadGpxBtn;
    @FXML
    private Button confirmAddBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleSearchFile(ActionEvent event) {
    }

    @FXML
    private void handleUploadGpx(ActionEvent event) {
    }

    @FXML
    private void handleConfirmAdd(ActionEvent event) {
    }
    
}
