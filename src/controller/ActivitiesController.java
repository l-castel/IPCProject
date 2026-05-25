package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mapademo.MapaDemoApp;
import mapademo.Navigable;
import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.SportActivityApp;
import upv.ipc.sportlib.User;

import java.io.File;

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
    private ListView<Activity> activitiesListView;
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
        if (selectedFile == null) {
            return;
        }

        Activity act = sportsApp.importActivity(selectedFile);

        if(act != null){
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
        data.setAll(sportsApp.getUserActivities());
        activitiesListView.refresh();
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
            setText(null);
            setGraphic(null);
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ActivityCard.fxml"));
        try {
            Parent card = loader.load();
            ActivityCardController cardController = loader.getController();
            cardController.initCard(item, data, listView);
            setText(null);
            setGraphic(card);
        } catch (Exception e) {
            setText(item.getName());
            setGraphic(null);
            e.printStackTrace();
        }
    }




}
