/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polyline;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;

/**
 * FXML Controller class
 *
 * @author marti
 */
public class DashboardController implements Initializable {

    @FXML
    private Circle avatarCircle;
    @FXML
    private Label labelName;
    @FXML
    private Label labelSurname;
    @FXML
    private Button btnProfile;
    @FXML
    private Button btnActivities;
    @FXML
    private Button btnMaps;
    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnAddAnnotations;
    @FXML
    private Button btnSelectMap;
    @FXML
    private ScrollPane mapScrollPane;
    @FXML
    private Label lblDuration;
    @FXML
    private Label lblDistance;
    @FXML
    private Label lblAvgPace;
    @FXML
    private Label lblAvgSpeed;
    @FXML
    private Label lblMaxAltitude;
    @FXML
    private Label lblMinAltitude;
    @FXML
    private Label lblMaxElevation;
    @FXML
    private Label lblMinElevation;
    @FXML
    private Button btnSpeedRoute;
    @FXML
    private Button btnElevationProfile;
    @FXML
    private Canvas elevationCanvas;
    @FXML
    private StackPane mapContainer;
    @FXML
    private Pane annotationsPane;
    @FXML
    private ImageView mapImageView;
    @FXML
    private Button btnZoomOut;
    @FXML
    private Slider zoomSlider;
    @FXML
    private Button btnZoomIn;
    
    private String pendingAnnotationText;
    private String pendingAnnotationType;
    private javafx.scene.paint.Color pendingAnnotationColor;
    private boolean waitingMapClick = false;
    private Polyline currentRoute;
    private boolean routeDrawingMode = false;
    private Pane mapPane;
    private Group zoomGroup;
    /**
     * Initializes the controller class*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        btnAddAnnotations.setOnAction(this::diagAddAnnotations);
        
        
        zoomSlider.setMin(0.5);
        zoomSlider.setMax(10.0);
        zoomSlider.setValue(1.0);
        
        zoomSlider.valueProperty().addListener((obs, oldValue, newValue)->{
            applyZoom(newValue.doubleValue());
        });
        btnZoomIn.setOnAction(event ->{
            zoomSlider.setValue(zoomSlider.getValue() + 0.1);
        });
        btnZoomOut.setOnAction(event ->{
            zoomSlider.setValue(zoomSlider.getValue() - 0.1);
        });
        buildMap(MapsController.selectedMapPath);
               
        
    
    }
    private void addPointToRoute(double x, double y){
        if(currentRoute == null){
            currentRoute = new Polyline();
            currentRoute.setStroke(javafx.scene.paint.Color.RED);
            currentRoute.setStrokeWidth(5);
            mapPane.getChildren().add(currentRoute);
        }
        currentRoute.getPoints().addAll(x,y);
        
        Circle point = new Circle (x,y,3);
        point.setFill(javafx.scene.paint.Color.WHITE);
        point.setStroke(javafx.scene.paint.Color.RED);
        point.setStrokeWidth(2);
        
        mapPane.getChildren().add(point);
        
    }
    
    private void zoom(double scaleValue){
        if(zoomGroup==null) return;
        
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
    }
    
    private void applyZoom(double zoomValue){
           zoom(zoomValue);
    }
    
    private void buildMap(String imagePath){
        Image img = new Image(getClass().getResourceAsStream(imagePath));
        double W = img.getWidth();
        double H = img.getHeight();

        // ── mapPane: lienzo del mapa ───────────────────────────────────
        // Usamos un Pane (y no un Group) para poder posicionar los nodos
        // hijos con coordenadas absolutas (setLayoutX / setLayoutY).
        mapPane = new Pane();
        mapPane.setPrefSize(W, H); // tamaño preferido = tamaño de la imagen
        mapPane.setMinSize(W, H);  // impedimos que el layout lo encoja
        mapPane.setMaxSize(W, H);  // impedimos que el layout lo agrande

        // Añadimos la imagen como fondo del Pane
        ImageView iv = new ImageView(img);
        iv.setFitWidth(W);
        iv.setFitHeight(H);
        iv.setPreserveRatio(true);
        
        mapPane.getChildren().add(iv);

        
        mapPane.setOnMouseClicked(e -> {
            double x = e.getX();
            double y = e.getY();
            
            if (waitingMapClick && e.getButton() == MouseButton.PRIMARY) {
                addAnnotationOnMap(x,y);
            } 
            if (routeDrawingMode && e.getButton() == MouseButton.PRIMARY) {
                addPointToRoute(x,y);
            } 
            if (routeDrawingMode && e.getClickCount() == 2) {
                routeDrawingMode = false;
            } 
            
        });

        
        zoomGroup = new Group(mapPane);
        Group contentGroup = new Group(zoomGroup);
        mapScrollPane.setContent(contentGroup);
        zoom(zoomSlider.getValue());
    }
   
    @FXML
    private void diagAddAnnotations(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AnnotationsDialog.fxml"));
        
            Parent root = loader.load();
        
            Stage dialog = new Stage();
            dialog.setTitle("Annotaions");
            dialog.setScene(new Scene(root,440,520));
        
            dialog.initModality(Modality.APPLICATION_MODAL);
           
            dialog.initOwner(btnAddAnnotations.getScene().getWindow());
            
            dialog.setResizable(false);
            dialog.showAndWait();
            
            AnnotationsController controller = loader.getController();
            
            if(controller.isConfirmed()){
                pendingAnnotationText= controller.getAnnotationText();
                pendingAnnotationType= controller.getSelectedType();
                pendingAnnotationColor= controller.getSelectedColor();
                
                waitingMapClick = true;
                mapScrollPane.setStyle("-fx-cursor: crosshair;");
                mapPane.setStyle("-fx-cursor: crosshair;");
                System.out.println("Click to place the annotation on the map");
            }
        
        }catch (IOException e){
            e.printStackTrace();
        }
    } 
    
    private void addAnnotationOnMap(double x, double y){
        String tooltipText= pendingAnnotationText;
        
        if(tooltipText == null || tooltipText.trim().isEmpty()){
            tooltipText = pendingAnnotationType;
        }
        if(pendingAnnotationType == null){
            pendingAnnotationType = "Text";
        }
        switch (pendingAnnotationType.toLowerCase()){
            case"circle":{
                javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle();
                
                circle.setCenterX(x);
                circle.setCenterY(y);
                circle.setRadius(18);
                circle.setFill(javafx.scene.paint.Color.color(pendingAnnotationColor.getRed(),pendingAnnotationColor.getGreen(),pendingAnnotationColor.getBlue(),0.25));
                circle.setStroke(pendingAnnotationColor);
                circle.setStrokeWidth(4);
                
                Tooltip.install(circle, new Tooltip(pendingAnnotationText));
                
                mapPane.getChildren().add(circle);
                break;
            }
            case"point":{
                javafx.scene.shape.Circle point = new javafx.scene.shape.Circle();
                
                point.setCenterX(x);
                point.setCenterY(y);
                point.setRadius(6);
                point.setFill(pendingAnnotationColor);
                point.setStroke(javafx.scene.paint.Color.BLACK);
                point.setStrokeWidth(4);
                
                Tooltip.install(point, new Tooltip(pendingAnnotationText));
                 
                mapPane.getChildren().add(point);
                break;
            }
            case"line":{
                javafx.scene.shape.Line line = new javafx.scene.shape.Line();
                
                line.setStartX(x-30);
                line.setStartY(y);
                line.setEndX(x+30);
                line.setEndY(y);
                line.setStroke(pendingAnnotationColor);
                line.setStrokeWidth(4);
                
                 Tooltip.install(line, new Tooltip(pendingAnnotationText));
                
                mapPane.getChildren().add(line);
                break;
            }
            default:{
                javafx.scene.text.Text annotation = new javafx.scene.text.Text(pendingAnnotationText);
                annotation.setFill(pendingAnnotationColor);
                annotation.setStyle("-fx-font-size: 16px;"+"-fx-font-weight: bold;"+"-fx-stroke: black;"+"-fx-stroke-width: 0.3;");
                annotation.setLayoutX(x);
                annotation.setLayoutY(y);
                
                mapPane.getChildren().add(annotation);
                break;
            }
        }
        waitingMapClick = false;
        mapScrollPane.setStyle("");
        mapPane.setStyle("");
    }
    
     @FXML
    private void handleProfile(ActionEvent event) {
    }

    @FXML
    private void handleActivities(ActionEvent event) {
    }

    @FXML
    private void handleMaps(ActionEvent event) {
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
    }


    @FXML
    private void addAnnotationsClick(MouseEvent event) {
    }


    @FXML
    private void handleSelectMap(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/view/Maps.fxml"));
            
            Parent root = loader.load();
            
            Stage stage = (Stage) btnSelectMap.getScene().getWindow();
            
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSpeedOverRoute(ActionEvent event) {
    }

    @FXML
    private void handleElevationProfile(ActionEvent event) {
    }

    @FXML
    private void profileClick(MouseEvent event) {
    }

    @FXML
    private void activitiesClick(MouseEvent event) {
    }

    @FXML
    private void mapClick(MouseEvent event) {
    }

    @FXML
    private void logOutClick(MouseEvent event) {
    }

    @FXML
    private void handleLogOut(ActionEvent event) {
    }


    @FXML
    private void selectMapClick(MouseEvent event) {
    }

    @FXML
    private void speedOverRouteClick(MouseEvent event) {
    }

    @FXML
    private void elevationProfileClick(MouseEvent event) {
    }
}
