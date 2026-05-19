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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author marti
 */
public class AnnotationsController implements Initializable {

    @FXML
    private ComboBox<String> comboBoxType;
    @FXML
    private TextArea textAnnotation;
    @FXML
    private Circle circleBlack;
    @FXML
    private Circle circleRed;
    @FXML
    private Circle circleBlue;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnAddAnnotation;
    
    private String selectedType;
    private String annotationText;
    private Color selectedColor = Color.BLACK;
    private boolean confirmed = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        comboBoxType.getItems().addAll("Text","Point","Circle","Line");
        comboBoxType.setValue("Text");
        
        circleBlack.setOnMouseClicked(e-> selectedColor = Color.BLACK);
        circleBlue.setOnMouseClicked(e-> selectedColor = Color.BLUE);
        circleRed.setOnMouseClicked(e-> selectedColor = Color.RED);
    }
    @FXML
    private void handleCancel(ActionEvent event) {
        confirmed = false;
        closeWindow();
    }

    @FXML
    private void handleAddAnnotation(ActionEvent event) {
        selectedType = comboBoxType.getValue();
        annotationText = textAnnotation.getText();
        
        if (annotationText == null || annotationText.trim().isEmpty()){
            annotationText = selectedType;
        }
        confirmed = true;
        closeWindow();
    }
    
    private void closeWindow(){
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
    public boolean isConfirmed(){
        return confirmed;
    }
    public String getAnnotationText(){
        return annotationText;
    }
    public Color getSelectedColor(){
        return selectedColor;
    }
    public String getSelectedType(){
        return selectedType;
    }
    
}
