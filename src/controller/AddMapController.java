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
import javafx.scene.control.ButtonType;
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
import upv.ipc.sportlib.MapRegion;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import mapademo.MapaDemoApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.util.Optional;



/**
 * FXML Controller class
 *
 * @author laura
 */
public class AddMapController implements Initializable {

    @FXML
    private TextField name;
    @FXML
    private TextField maxLatitud;
    @FXML
    private TextField minLatitud;
    @FXML
    private TextField maxLongitud;
    @FXML
    private TextField minLongitud;
    @FXML
    private Button gpxButton;
    @FXML
    private Button addButton;
    
    private final SportActivityApp app = SportActivityApp.getInstance();
    private File image;
    @FXML
    private Button cancelButton;
    @FXML
    private Label errorLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        addButton.disableProperty().bind(name.textProperty().isEmpty()
               .or(minLatitud.textProperty().isEmpty())
               .or(maxLatitud.textProperty().isEmpty())
               .or(maxLongitud.textProperty().isEmpty())
               .or(minLongitud.textProperty().isEmpty())
               );
        Runnable ocultar = ()-> errorLabel.setVisible(false);
        name.textProperty().addListener((ob, ov, nv)->ocultar.run());
        minLatitud.textProperty().addListener((ob, ov, nv)->ocultar.run());
        maxLatitud.textProperty().addListener((ob, ov, nv)->ocultar.run());
        minLongitud.textProperty().addListener((ob, ov, nv)->ocultar.run());
        maxLongitud.textProperty().addListener((ob, ov, nv)->ocultar.run());
    }    


    @FXML
    private void GPXUpload(ActionEvent event) {
        FileChooser chose = new FileChooser();
        chose.setTitle("Select image");
        chose.getExtensionFilters().addAll(new FileChooser
                .ExtensionFilter("Images", "*.jpg","*.jpeg","*.png"));
        Stage stage = (Stage)gpxButton.getScene().getWindow();
        image = chose.showOpenDialog(stage);
        
        if(image != null){
            gpxButton.setText(image.getName());
            errorLabel.setVisible(false);
        }
    }

    @FXML
    private void add(ActionEvent event) {
        
        if(image==null){
            showError("You must select a map image.");
            return;
        }
        if(!image.exists()){
            showError("The selected upload image does not exist.");
            image = null;
            return;
        }
        double minLat = 0, maxLat = 0, minLon = 0, maxLon = 0;
        try{
         minLat = Double.parseDouble(minLatitud.getText().trim());
         maxLat = Double.parseDouble(maxLatitud.getText().trim());
         minLon = Double.parseDouble(minLongitud.getText().trim());
         maxLon = Double.parseDouble(maxLongitud.getText().trim());
        }catch(NumberFormatException e){
            showError("Coordinates must be valid numbers.");
            return;
        }
        if(minLat > maxLat){
            showError("Min is bigger than max.");
            minLatitud.requestFocus();
            return;
        }
        
        if(minLon > maxLon){
            showError("Min is bigger than max.");
            minLongitud.requestFocus();
            return;
        }
        
        if(minLat < -90 || maxLat> 90){
            showError("Latitud must be between 90 and -90.");
            minLatitud.requestFocus();
            return;
        }
        
        if(minLon < -180 || maxLon> 180){
            showError("Longitud must be between 180 and -180.");
            minLongitud.requestFocus();
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add map");
        alert.setHeaderText("Are you sure you want to add this map?");
        alert.setContentText("Once added, the map cannot be modified. Please review the uploadesd information before confirming.");
        
        Optional<ButtonType> presh = alert.showAndWait();
        
        if(presh.isPresent()&& presh.get() == ButtonType.CANCEL){
            return;
        }
        MapRegion newMap = app.addMapRegion(name.getText().trim(), image, minLat, maxLat, minLon, maxLon);
        
        if(newMap != null){
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Map added");
            success.setHeaderText(null);
            success.setContentText("Map added successfully");
            success.showAndWait();
            
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();   
        }else{
            showError("Could not add the map. Try again.");
        }
    }
    private void showError(String message){
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    @FXML
    private void cancel(ActionEvent event) {
        ((Stage) addButton.getScene().getWindow()).close();
    }
    
}
