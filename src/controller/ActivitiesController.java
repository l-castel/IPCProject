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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mapademo.MapaDemoApp;
import mapademo.Navigable;
import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.SportActivityApp;
import upv.ipc.sportlib.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ActivitiesController implements Navigable
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
    private Label nicknameLabel;
    @javafx.fxml.FXML
    private Label logoutNav;
    @javafx.fxml.FXML
    private ListView activitiesListView;
    @javafx.fxml.FXML
    private Label profileNav;
    @javafx.fxml.FXML
    private ImageView avatarImage;

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



    }
    @javafx.fxml.FXML
    public void handleAddActivity(ActionEvent actionEvent) {
        Stage stage = (Stage) addActivityButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        Activity act = sportsApp.importActivity(selectedFile);

        if(act == null){

        } else{
            int id = activitiesListView.getSelectionModel().getSelectedIndex();
            data.add(act);

        }


    }

    @javafx.fxml.FXML
    public void handleProfile(ActionEvent event) {
        MapaDemoApp.setRoot("Profile");
    }

    @javafx.fxml.FXML
    public void handleLogout(ActionEvent event) {
        sportsApp.logout();
        MapaDemoApp.setRoot("Login");
    }

    @javafx.fxml.FXML
    public void handleDashboard(ActionEvent event) {
        MapaDemoApp.setRoot("Dashboard");
    }

    @javafx.fxml.FXML
    public void handleMaps(ActionEvent event) {
        MapaDemoApp.setRoot("Maps");
    }

    @javafx.fxml.FXML
    public void handleActivities(ActionEvent event) {
        MapaDemoApp.setRoot("Activities");
    }


    @Override
    public void onNavigate() {
        User user = sportsApp.getCurrentUser();
        if (user != null) {
            nicknameLabel.setText(user.getNickName());
            avatarImage.setImage(user.getAvatar());
        }
        data.clear();
        if(activitiesListView.getItems().size() == 0) {
            for (Activity act : sportsApp.getUserActivities()) {
                data.add(act);
            }
        }
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
            setGraphic(card);
        }
    }




}