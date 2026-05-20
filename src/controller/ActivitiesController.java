package controller;

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

public class ActivitiesController
{

    private SportActivityApp sportsApp;

    ObservableList<Activity> data = null;

    private FileChooser fileChooser;
    @javafx.fxml.FXML
    private Button addActivityButton;
    @javafx.fxml.FXML
    private Label mapsNav;
    @javafx.fxml.FXML
    private Label activitiesNav;
    @javafx.fxml.FXML
    private Label dashboardNav;
    @javafx.fxml.FXML
    private Label userNameLabel;
    @javafx.fxml.FXML
    private Label userSurnameLabel;
    @javafx.fxml.FXML
    private Label logoutNav;
    @javafx.fxml.FXML
    private ListView activitiesListView;
    @javafx.fxml.FXML
    private Label profileNav;

    @javafx.fxml.FXML
    public void initialize() {
        sportsApp = SportActivityApp.getInstance();

        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Activity File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("GPX files", "*.gpx")
        );

        data = activitiesListView.getItems();

        activitiesListView.setCellFactory(c -> new ActivityCell(data, activitiesListView));

        for(Activity act : sportsApp.getUserActivities()){
            data.set(activitiesListView.getSelectionModel().getSelectedIndex(), act);
        }

    }
    @javafx.fxml.FXML
    public void handleAddActivity(ActionEvent actionEvent) {
        Stage stage = (Stage) addActivityButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        Activity act = sportsApp.importActivity(selectedFile);

        if(act == null){

        } else{
            int id = activitiesListView.getSelectionModel().getSelectedIndex();
            if(id == -1){
                data.add(0, act);
            } else data.add(id, act);

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

    @Deprecated
    public void handleEdit(Event event) {
    }

    @Deprecated
    public void handleDelete(Event event) {
    }
}

    class ActivityCell extends ListCell<Activity> {

        ObservableList<Activity> data;
        ListView<Activity> listView;

        public ActivityCell(ObservableList<Activity> data, ListView<Activity> listView){
            this.data = data;
            this.listView = listView;
        }

        @Override
        protected void updateItem(Activity item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {

            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ActivityCard.fxml"));

                VBox card = null;
                try {
                    card = loader.load();
                    ActivityCardController cardController = loader.getController();
                    cardController.initCard(item, data, listView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // populate labels via loader.getNamespace() or a mini-controller
                // set alternating background based on getIndex()
                setGraphic(card);
            }
        }


    }