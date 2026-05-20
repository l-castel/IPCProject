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
public class ModifyMapController implements Initializable {

    @FXML
    private TextField name;
    private Button searchButton;
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
    private Button modifyButton;
    
    private MapRegion current;
    
    private File image;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
    }  
    public void initMap(MapRegion map){
            current = map;
            name.setText(map.getName());
            minLatitud.setText(String.valueOf(map.getLatMin()));
            maxLatitud.setText(String.valueOf(map.getLatMax()));
            minLongitud.setText(String.valueOf(map.getLonMin()));
            maxLongitud.setText(String.valueOf(map.getLonMax()));
    }


    @FXML
    private void GPXUpload(ActionEvent event) {
        FileChooser chose = new FileChooser();
        chose.setTitle("Select image");
        chose.getExtensionFilters().addAll(new FileChooser
                .ExtensionFilter("Images", "*.gpx"));
        Stage stage = (Stage)searchButton.getScene().getWindow();
        image = chose.showOpenDialog(stage);
    }

    @FXML
    private void modify(ActionEvent event) {
    }
    
}
