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
    
    private File newImage;
    
    private final SportActivityApp app = SportActivityApp.getInstance();
    @FXML
    private Button cancelButton;
    @FXML
    private Label gapsError;
    
    private File currentImage;
    
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
        Runnable ocultar = ()-> gapsError.setVisible(false);
        name.textProperty().addListener((ob, ov, nv)->ocultar.run());
        minLatitud.textProperty().addListener((ob, ov, nv)->ocultar.run());
        maxLatitud.textProperty().addListener((ob, ov, nv)->ocultar.run());
        minLongitud.textProperty().addListener((ob, ov, nv)->ocultar.run());
        maxLongitud.textProperty().addListener((ob, ov, nv)->ocultar.run());
    }  
    public void initMap(MapRegion map){
            current = map;
            name.setText(map.getName());
            minLatitud.setText(String.valueOf(map.getLatMin()));
            maxLatitud.setText(String.valueOf(map.getLatMax()));
            minLongitud.setText(String.valueOf(map.getLonMin()));
            maxLongitud.setText(String.valueOf(map.getLonMax()));
            
            String path = map.getImagePath();
            currentImage =(path != null && !path.isBlank())? new File(path):null;
    }


    @FXML
    private void GPXUpload(ActionEvent event) {
        FileChooser chose = new FileChooser();
        chose.setTitle("Select new map");
        chose.getExtensionFilters().addAll(new FileChooser
                .ExtensionFilter("Images", "*.jpg","*.jpeg","*.png"));
        Stage stage = (Stage)gpxButton.getScene().getWindow();
        newImage = chose.showOpenDialog(stage);
        
        if(newImage != null){
            gpxButton.setText(newImage.getName());
            gapsError.setVisible(false);
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
            showError("Coordinates must be valid numbers.");
            return;
        }
        if(minLat >= maxLat){
            showError("Min is bigger than max");
            minLatitud.requestFocus();
            return;
        }
        if(minLon >= maxLon){
            showError("Min is bigger than max");
            minLongitud.requestFocus();
            return;
        }
        
        boolean coordenatesChange = minLat != current.getLatMin()
                ||maxLat!= current.getLatMax() 
                ||minLon != current.getLonMin() 
                ||maxLon != current.getLonMax();
        
        boolean nameChange = false;
        
        
        String newName = name.getText().trim();
        String currentName = current.getName();
        if(!currentName.equals(newName)) nameChange=true;
        
        boolean imageChange = false;
        if(newImage==null){
            newImage = currentImage;
        }else{
            imageChange =true;
        }
        
        if(coordenatesChange == true || imageChange == true|| nameChange==true){
            
            app.removeMapRegion(current);
            MapRegion update = app.addMapRegion(newName, newImage, minLat, maxLat, minLon, maxLon);
            System.out.println(newName);
            System.out.println(newImage);
            System.out.println(minLat);
            System.out.println(maxLat);
            System.out.println(minLon);
            System.out.println(maxLon);
            System.out.println(update);
            
            if(update==null){
                Stage stage = (Stage) modifyButton.getScene().getWindow();
                stage.close();
            }else{
                
                 Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                 alert.setTitle("No update");
                 alert.setHeaderText("There is no update in the map infomation ");
                 
        
                Optional<ButtonType> presh = alert.showAndWait();
        
                if(presh.isPresent()&& presh.get() == ButtonType.OK){
                   app.addMapRegion(current.getName(), currentImage, current.getLonMax(), current.getLatMax(), current.getLonMin(), current.getLatMin());
                   Stage stage = (Stage) modifyButton.getScene().getWindow();
                stage.close();
                }
            }
            
        }
        

        /*boolean remove = app.removeMapRegion(current);
        if(!remove){
            showError("Could not update the map");
            return;
        }*/
        
        
        /*MapRegion update = app.addMapRegion(newName, newImage, minLat, maxLat, minLon, maxLon);
        
        if(update != null){
            ((Stage) modifyButton.getScene().getWindow()).close();
        }else{
            app.addMapRegion(current.getName(), new File(current.getImagePath()), current.getLatMin(), current.getLatMax(), current.getLonMin(), current.getLatMax());
            showError("Could not save changes. Original map restored");
        }*/
        
        
    }
    
    private void showError(String message){
        gapsError.setText(message);
        gapsError.setVisible(true);
    }

    @FXML
    private void cancel(ActionEvent event) {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }
    
}
