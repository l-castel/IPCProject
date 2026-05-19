package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mapademo.MapaDemoApp;
import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.SportActivityApp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ActivitiesController
{
    @javafx.fxml.FXML
    private Button addActivityButton;
    @javafx.fxml.FXML
    private Label logoutNav;
    @javafx.fxml.FXML
    private Label userNameLabel;
    @javafx.fxml.FXML
    private Label userSurnameLabel;
    @javafx.fxml.FXML
    private Label mapsNav;
    @javafx.fxml.FXML
    private Label activitiesNav;
    @javafx.fxml.FXML
    private Label dashboardNav;
    @javafx.fxml.FXML
    private Label profileNav;

    private SportActivityApp sportsApp;

    ObservableList<Activity> data = null;
    @javafx.fxml.FXML
    private ListView activitiesListView;

    private FileChooser fileChooser;

    @javafx.fxml.FXML
    public void initialize() {
        sportsApp = SportActivityApp.getInstance();

        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Activity File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("GPX files", "*.gpx")
        );

        data = activitiesListView.getItems();

        activitiesListView.setCellFactory(c -> new ActivityCell());

        for(Activity act : sportsApp.getUserActivities()){
            data.set(activitiesListView.getSelectionModel().getSelectedIndex(), act);
        }

    }



    @Deprecated
    public void handleEditActivity(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/RenameActivity.fxml"));
        Parent renRoot = loader.load();
        RenameActivityController renController = loader.getController();
        Activity act = (Activity) activitiesListView.getSelectionModel().getSelectedItem();
        renController.initAct(act);
        Scene scene = new Scene(renRoot, 500, 300);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Rename Activity");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @Deprecated
    public void handleDeleteActivity(Event event) {
        Activity sel = (Activity) activitiesListView.getSelectionModel().getSelectedItem();
        if(sel != null) {
            sportsApp.removeActivity(sel);
            data.remove(sel);
        }
    }

    @javafx.fxml.FXML
    public void handleAddActivity(ActionEvent actionEvent) {
        Stage stage = (Stage) addActivityButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        Activity act = sportsApp.importActivity(selectedFile);

        if(act == null){

        } else{
            data.set(activitiesListView.getSelectionModel().getSelectedIndex(), act);
        }


    }

    @javafx.fxml.FXML
    public void handleProfile(Event event) {
        MapaDemoApp.setRoot("Profle");
    }

    @javafx.fxml.FXML
    public void handleLogout(Event event) {
        sportsApp.logout();
        MapaDemoApp.setRoot("Login");
    }

    @javafx.fxml.FXML
    public void handleDashboard(Event event) {
        MapaDemoApp.setRoot("Dashboard");
    }

    @javafx.fxml.FXML
    public void handleMaps(Event event) {
        MapaDemoApp.setRoot("Maps");
    }

    @javafx.fxml.FXML
    public void handleActivities(Event event) {
        MapaDemoApp.setRoot("Activities");
    }
}

    class ActivityCell extends ListCell<Activity> {
        @Override
        protected void updateItem(Activity item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("activityCard.fxml"));
                VBox card = null;
                try {
                    card = loader.load();
                } catch (Exception e) {
                    System.out.println("Error loading Activity card");
                }
                // populate labels via loader.getNamespace() or a mini-controller
                // set alternating background based on getIndex()
                setGraphic(card);
            }
        }
    }