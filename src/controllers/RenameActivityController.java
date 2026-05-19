package controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.SportActivityApp;

public class RenameActivityController
{
    @javafx.fxml.FXML
    private Button submitButton;
    @javafx.fxml.FXML
    private TextField newNameField;

    Activity act;

    SportActivityApp sportsApp;

    @javafx.fxml.FXML
    public void initialize() {
        sportsApp = SportActivityApp.getInstance();
    }

    @javafx.fxml.FXML
    public void handleSubmit(ActionEvent actionEvent) {
        String newName = newNameField.textProperty().getValue();
        sportsApp.renameActivity(act, newName);
        Stage curr = (Stage) submitButton.getScene().getWindow();
        curr.close();
    }

    public void initAct(Activity selected){
        act = selected;
    }
}