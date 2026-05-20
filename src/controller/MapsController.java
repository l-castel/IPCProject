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
public class MapsController implements Initializable {

    @FXML
    private Circle avatarCircle;
    @FXML
    private ImageView avatarImage;
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
    private Button modifyButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ListView<MapRegion> mapsList;
    
    private final SportActivityApp app = SportActivityApp.getInstance();
    @FXML
    private Label nickname;
    
    ObservableList<MapRegion> map = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        User currentUser = app.getCurrentUser();
        
        if(currentUser!=null){
            nickname.setText(currentUser.getNickName());
            if(currentUser.getAvatar()!= null){
                avatarImage.setImage(currentUser.getAvatar());
            }
        }
        
        avatarImage.setClip(avatarCircle);
        
        map = mapsList.getItems();
        map.addAll(app.getMapRegions());
        mapsList.setCellFactory(param-> new ListCell<MapRegion>(){
            
            protected void updateItem(MapRegion item, boolean empty){
                super.updateItem(item, empty);
                if(empty || item == null) setText(null);
                else setText(item.getName());
            }
        });
        modifyButton.disableProperty().bind(
                Bindings.isNull(mapsList.getSelectionModel()
                        .selectedItemProperty()
                )
        );
        deleteButton.disableProperty().bind(
                Bindings.isNull(mapsList.getSelectionModel()
                        .selectedItemProperty()
                )
        );
    }    

    @FXML
    private void goToProfile(ActionEvent event) {
        MapaDemoApp.setRoot("Profile");
    }

    @FXML
    private void goToActivities(ActionEvent event) {
        MapaDemoApp.setRoot("Activities");
    }

    @FXML
    private void goToMaps(ActionEvent event) {
        MapaDemoApp.setRoot("Maps");
    }

    @FXML
    private void goToDashboard(ActionEvent event) {
        MapaDemoApp.setRoot("Dashboard");
    }

    @FXML
    private void logOut(ActionEvent event) {
        app.logout();
        MapaDemoApp.setRoot("Login");
    }

    @javafx.fxml.FXML
    private void addMap(ActionEvent event) {
        
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddMap.fxml"));
            Parent root = loader.load();
            
            AddMapController controller = loader.getController();;
            Stage stage = new Stage();
            stage.setTitle("Add map");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            //MapRegion newMap = controller.getMap();
            //if(newMap != null) map.add(0,newMap);
            mapsList.refresh();
        }catch(Exception e){
        }
    }

    @javafx.fxml.FXML
    private void moddifyMap(ActionEvent event) {
        
        MapRegion selectMap = mapsList.getSelectionModel()
                .getSelectedItem();
        if(selectMap == null) return;
        
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyMap.fxml"));
            Parent root = loader.load();
            
            ModifyMapController controller = loader.getController();
            controller.initMap(selectMap);
            Stage stage = new Stage();
            stage.setTitle("Modify map");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            mapsList.refresh();
        }catch(Exception e){
        }
    }

    @javafx.fxml.FXML
    private void deleteMap(ActionEvent event) {
        MapRegion selectMap = mapsList.getSelectionModel()
                .getSelectedItem();
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete map");
        alert.setHeaderText("Delete " +selectMap.getName()+"?");
        alert.setContentText("It wil be only deleted if it is not in use");
        Optional<ButtonType> presh = alert.showAndWait();
        
        if(presh.isPresent()&& presh.get() == ButtonType.OK){
            boolean delete = app.removeMapRegion(selectMap);
            if(delete){
                map.remove(selectMap);
            }
        }
    }
    
}
