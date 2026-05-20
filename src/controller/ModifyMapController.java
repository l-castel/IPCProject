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
    
    private final SportActivityApp app = SportActivityApp.getInstance();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       modifyButton.disableProperty().bind(name.textProperty().isEmpty()
               .or(minLatitud.textProperty().isEmpty())
               .or(maxLatitud.textProperty().isEmpty())
               .or(maxLongitud.textProperty().isEmpty())
               .or(minLongitud.textProperty().isEmpty())
               );
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
        Stage stage = (Stage)gpxButton.getScene().getWindow();
        image = chose.showOpenDialog(stage);
        
        if(image != null){
            gpxButton.setText(image.getName());
        }
    }

    @FXML
    private void modify(ActionEvent event) {
        double minLat = 0, maxLat = 0, minLon = 0, maxLon = 0;
        try{
         minLat = Double.parseDouble(minLatitud.getText().trim());
         maxLat = Double.parseDouble(maxLatitud.getText().trim());
         minLon = Double.parseDouble(minLongitud.getText().trim());
         maxLon = Double.parseDouble(maxLongitud.getText().trim());
        }catch(NumberFormatException e){
            
        }
        /*if(minLat >= maxLat){
            
        }
        if(minLon >= maxLon){
            
        }*/
        
        boolean coordenatesChange = minLat != current.getLatMin()
                ||maxLat!= current.getLatMax() 
                ||minLon != current.getLonMin() 
                ||maxLon != current.getLonMax();
        boolean imageChange = image != null;
        
        if(!coordenatesChange || !imageChange){
            ((Stage)modifyButton.getScene().getWindow()).close();
        }
        String name = current.getName();
        File img = imageChange ? image : new File(current.getImagePath());
        
        boolean remove = app.removeMapRegion(current);
        if(!remove){
            showError();
        }
        
        MapRegion update = app.addMapRegion(name, img, minLat, maxLat, minLon, maxLon);
        
        if(update != null){
            Stage stage = (Stage) modifyButton.getScene().getWindow();
        }
    }
    
    private void showError(){
        
    }
    
}
