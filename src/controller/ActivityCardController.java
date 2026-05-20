package controller;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.SportActivityApp;

import java.io.IOException;

public class ActivityCardController
{
    @javafx.fxml.FXML
    private Label distanceLabel;
    @javafx.fxml.FXML
    private Label deleteLabel;
    @javafx.fxml.FXML
    private ImageView editBtn;
    @javafx.fxml.FXML
    private Label activityNameLabel;
    @javafx.fxml.FXML
    private Label avgSpeedLabel;
    @javafx.fxml.FXML
    private Label elevationLabel;
    @javafx.fxml.FXML
    private ImageView deletBtn;
    @javafx.fxml.FXML
    private Label mapLabel;

    SportActivityApp sportsApp;

    ObservableList<Activity> data;

    ListView<Activity> listView;


    Activity act;

    @javafx.fxml.FXML
    public void initialize() {
        sportsApp = SportActivityApp.getInstance();

    }

    public void initCard(Activity act, ObservableList<Activity> data, ListView<Activity> listView){
        this.act = act;
        this.data = data;
        this.listView = listView;


        this.activityNameLabel.textProperty().setValue(act.getName());
        this.avgSpeedLabel.textProperty().setValue("Average speed: " + String.valueOf(Math.round(act.getAverageSpeed()) + "km/min"));
        this.distanceLabel.textProperty().setValue("Distance: " + String.valueOf(Math.round(act.getTotalDistance())) + "km");
        this.elevationLabel.textProperty().setValue("Elevation level: " + String.valueOf(Math.round(act.getElevationGain())) + "m");
        this.mapLabel.textProperty().setValue("Map: " + act.getSuggestedMap().getName());
    }


    @javafx.fxml.FXML
    public void handleEdit(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/RenameActivity.fxml"));
        Parent renRoot = loader.load();
        RenameActivityController renController = loader.getController();
        renController.initAct(this.act);
        Scene scene = new Scene(renRoot, 500, 300);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Rename Activity");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        data.set(listView.getSelectionModel().getSelectedIndex(), renController.getAct());
    }

    @javafx.fxml.FXML
    public void handleDelete(Event event) {
        sportsApp.removeActivity(act);
        data.remove(act);
    }

}