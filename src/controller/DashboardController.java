/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Insets;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseButton;
import mapademo.MapaDemoApp;

import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.Annotation;
import upv.ipc.sportlib.AnnotationType;
import upv.ipc.sportlib.GeoPoint;
import upv.ipc.sportlib.MapProjection;
import upv.ipc.sportlib.MapRegion;
import upv.ipc.sportlib.SportActivityApp;
import upv.ipc.sportlib.TrackPoint;
/**
 * FXML Controller class
 *
 * @author marti
 */

public class DashboardController implements Initializable {
    @FXML
    private Button btnAddAnnotations;
    @FXML
    private Button btnZoomOut;
    @FXML
    private Button btnZoomIn;
    @FXML
    private Slider zoomSlider;
    @FXML
    private ScrollPane mapScrollPane;
    
    @FXML
    private Circle avatarCircle;
    
    @FXML
    private Label labelName;
    @FXML
    private Label labelSurname;
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
    private Label lblTotTime;
    @FXML
    private Label lblTotDist;
    @FXML
    private Label lblTotGain;
    @FXML
    private Label lblTotLoss;
    
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
    
    private final SportActivityApp app = SportActivityApp.getInstance();
   
    private Pane mapPane;
    private Group zoomGroup;
    private MapProjection projection;
    
    private Activity currentActivity;
    public static MapRegion selectedMapRegion;
    
    private String pendingAnnotationText;
    private AnnotationType pendingAnnotationType;
    private Color pendingAnnotationColor = Color.BLACK;
    
    private boolean waitingMapClick= false;
    private final List<GeoPoint> pendingGeoPoints = new ArrayList<>();
    @FXML
    private Button btnSelectActivity;


    /**
     * Initializes the controller class*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        btnAddAnnotations.setOnAction(this::diagAddAnnotations);
        btnSelectActivity.setOnAction(this::handleSelectActivity);

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
        MapRegion region = resolveInitialRegion();
        
        if(region != null){
            buildMap(region);
        }
        
        updateCumulativeStats();
    
    }
    
    private void zoom(double scaleValue){
        if(zoomGroup==null) return;
        
        double scrollH = mapScrollPane.getHvalue();
        double scrollV = mapScrollPane.getVvalue();
        
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        
        mapScrollPane.setHvalue(scrollH);
        mapScrollPane.setVvalue(scrollV);
    }
    
    private void applyZoom(double zoomValue){
           zoom(zoomValue);
    }
    private MapRegion resolveInitialRegion(){
        if(selectedMapRegion != null){
            return selectedMapRegion;
        }
        for(MapRegion region : app.getMapRegions()){
            if(region.getName().toLowerCase().contains("calderona")){
                return region;
            }
        }
        if(!app.getMapRegions().isEmpty()){
            return app.getMapRegions().get(0);
        }
        return null;
    }   
    
    private void buildMap(MapRegion region){
        
        File imgFile = new File(region.getImagePath());
        Image img = new Image(imgFile.toURI().toString());
        double W = img.getWidth();
        double H = img.getHeight();

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

        projection = new MapProjection(region, W, H);
        
        mapPane.setOnMouseClicked(e -> {
            
            if (waitingMapClick && e.getButton() == MouseButton.PRIMARY) {
                handleAnnotaionMapClick(e.getX(),e.getY());
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
                pendingAnnotationType= parseAnnotationType(controller.getSelectedType());
                pendingAnnotationColor= controller.getSelectedColor();
                
                pendingGeoPoints.clear();
                
                waitingMapClick = true;
                mapScrollPane.setStyle("-fx-cursor: crosshair;");
                mapPane.setStyle("-fx-cursor: crosshair;");
                System.out.println("Click to place the annotation on the map");
            }
        
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void handleAnnotaionMapClick(double x, double y){
        GeoPoint geoPoint= projection.unproject(x,y);
        pendingGeoPoints.add(geoPoint);
        
        int requiredPoints= requiredPointsFor(pendingAnnotationType);
        
        if(pendingGeoPoints.size()<requiredPoints){
            System.out.println("Select a second point");
            return;
        }
        
        Annotation annotation = new Annotation( pendingAnnotationType, pendingAnnotationText, toHex(pendingAnnotationColor),4.0,new ArrayList<>(pendingGeoPoints));
        if(currentActivity != null){
            annotation = app.addAnnotation(currentActivity, annotation);
        }
        drawAnnotation(annotation);
        waitingMapClick = false;
        pendingGeoPoints.clear();
        mapPane.setStyle("");
        mapScrollPane.setStyle("");
    }
    
    private void drawAnnotation(Annotation annotation){
       List<GeoPoint> geoPoints= annotation.getGeoPoints();
        
        if(geoPoints.isEmpty()){
            return;
        }    
        String text = annotation.getText();
        Color color= Color.web(annotation.getColor());
        
        Point2D p1 = projection.project(geoPoints.get(0));
        
        
        switch (annotation.getType()){
            case CIRCLE:{
                
                if(geoPoints.size()<2){
                    return;
                }
                Point2D p2 = projection.project(geoPoints.get(1));
                
                double radius = p1.distance(p2);
                Circle circle = new Circle(p1.getX(),p1.getY(),radius);
                
                circle.setFill(Color.color(color.getRed(),color.getGreen(),color.getBlue(),0.25));
                circle.setStroke(color);
                circle.setStrokeWidth(annotation.getStrokeWidth());
                
                Tooltip.install(circle, new Tooltip(text));
                
                mapPane.getChildren().add(circle);
                break;
            }
            case POINT:{
                Circle point = new Circle(p1.getX(),p1.getY(),7);
                
                point.setFill(color);
                point.setStroke(color.BLACK);
                point.setStrokeWidth(1);
                
                Tooltip.install(point, new Tooltip(text));
                
                mapPane.getChildren().add(point);
                break;
            }
            case LINE:{
                if(geoPoints.size()<2){
                    return;
                }
                Point2D p2 = projection.project(geoPoints.get(1));
                
                Line line = new Line(p1.getX(),p1.getY(),p2.getX(),p2.getY());
                line.setStroke(color);
                line.setStrokeWidth(annotation.getStrokeWidth());
                
                 Tooltip.install(line, new Tooltip(text));
                
                mapPane.getChildren().add(line);
                break;
            }
            case TEXT:{
                Text label = new Text(p1.getX(),p1.getY(),text);
                label.setFill(color);
                label.setStyle("-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-stroke: black;" +
                        "-fx-stroke-width: 0.3;");
                
                Tooltip.install(label, new Tooltip(text));
                
                mapPane.getChildren().add(label);
                break;
            }
        }
    }
    private void showActivity(Activity activity){
        currentActivity = activity;
        
        MapRegion region = activity.getSuggestedMap();
        buildMap(region);
        
        drawRoute(activity);
        drawActivityAnnotations(activity);
        updateStatistics(activity);
        updateCumulativeStats();
    }
    private void drawRoute(Activity activity){
        Polyline route = new Polyline();
        
        route.setStroke(Color.web("#E74C3C"));
        route.setStrokeWidth(4);
        route.setOpacity(0.90);
        
        for(TrackPoint trackPoint :activity.getTrackPoints()){
            Point2D point = projection.project(trackPoint);
            route.getPoints().addAll(point.getX(), point.getY());
        }
        mapPane.getChildren().add(route);
        drawStartEndPoints(activity);
        
    }
    
    private void drawStartEndPoints(Activity activity){
        Point2D start = projection.project(activity.getStartPoint());
        Point2D end = projection.project(activity.getStartPoint());
        
        Circle startCircle = new Circle(start.getX(),start.getY(),8);
        startCircle.setFill(Color.GREEN);
        
        Circle endCircle = new Circle(end.getX(),end.getY(),8);
        endCircle.setFill(Color.RED);
        
        Tooltip.install(startCircle, new Tooltip("Start"));
        Tooltip.install(endCircle, new Tooltip("End"));
        
    }
    
    private void drawActivityAnnotations(Activity activity){
        for (Annotation annotation : activity.getAnnotations()){
            drawAnnotation(annotation);
        }
    }
    private void updateStatistics(Activity activity){
        lblDistance.setText(String.format("Distance: %.2f km",activity.getTotalDistance()/ 1000.0));
        lblDuration.setText("Duration:"+ activity.getDuration());
        lblAvgSpeed.setText(String.format("Average Speed: %.2f km/h",activity.getAverageSpeed()));
        lblAvgPace.setText(String.format("Average Pace: %.2f min/km",activity.getAveragePace()));
        lblMaxAltitude.setText(String.format("Max Altitude: %.0f m", activity.getMaxElevation()));
        lblMinAltitude.setText(String.format("Min Altitude: %.0f m", activity.getMinElevation()));
        lblMaxElevation.setText(String.format("Max Elevation: %.0f m", activity.getMaxElevation()));
        lblMinElevation.setText(String.format("Min Elevation: %.0f m", activity.getMinElevation()));
        
    }
    private void updateCumulativeStats(){
        List<Activity> activities = app.getUserActivities();
        
        if(activities == null || activities.isEmpty()){
            lblTotTime.setText("");
            lblTotDist.setText("");
            lblTotGain.setText("");
            lblTotLoss.setText("");
            return;
        }
        
        java.time.Duration totalDuration = java.time.Duration.ZERO;
        double totalDistance = 0.0;
        double totalAscent = 0.0;
        double totalDescent = 0.0;
        
        for(Activity act : activities){
            totalDuration = totalDuration.plus(act.getDuration());
            totalDistance += act.getTotalDistance();
            totalAscent += act.getElevationGain();
            totalDescent += act.getElevationLoss();
        }
        
        long hours = totalDuration.toHours();
        long minutes = totalDuration.toMinutesPart();
        long seconds = totalDuration.toSecondsPart();
        
        lblTotTime.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        lblTotDist.setText(String.format("%.2f km", totalDistance / 1000.0));
        lblTotGain.setText(String.format("%.0f m", totalAscent));
        lblTotLoss.setText(String.format("%.0f m", totalDescent));
    }
    private int requiredPointsFor(AnnotationType type){
        if(type == AnnotationType.LINE || type == AnnotationType.CIRCLE){
            return 2;
        }
        return 1;
    }
    private AnnotationType parseAnnotationType(String type){
        if (type == null){
            return AnnotationType.TEXT;
        }
        switch(type.toLowerCase()){
            case "point":
                return AnnotationType.POINT;
                
            case "circle":
                return AnnotationType.CIRCLE;
            
            case "line":
                return AnnotationType.LINE;
                
            case "text":
                return AnnotationType.TEXT;
        }
        return null;
    }
    
    private String toHex(Color color){
        int r = (int) Math.round(color.getRed()*255);
        int g = (int) Math.round(color.getGreen()*255);
        int b = (int) Math.round(color.getBlue()*255);
        
        return String.format("#%02X%02X%02X",r,g,b);
    }
    
     @FXML
    private void handleProfile(ActionEvent event) {
        MapaDemoApp.setRoot("Profile"); 
    }

    @FXML
    private void handleActivities(ActionEvent event) {
        MapaDemoApp.setRoot("Activities");
    }

    @FXML
    private void handleMaps(ActionEvent event) {
        MapaDemoApp.setRoot("Maps");
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
    }


    @FXML
    private void addAnnotationsClick(MouseEvent event) {
    }


    @Deprecated
    private void handleSelectMap(ActionEvent event) {
        List<MapRegion> maps = app.getMapRegions();
        
        if( maps == null || maps.isEmpty()){
            System.out.println("There are no available maps");
            return;
        }
        
        ComboBox<String> comboMaps = new ComboBox<>();
        
        for(MapRegion map : maps){
            comboMaps.getItems().add(map.getName());
        }
        
        Button cancel = new Button("Cancel");
        Button select = new Button("Select");
        
        Label title = new Label("MAPS");
        Label label = new Label("Map");
        
        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #302f36;"+"-fx-background-radius: 20;");
        
        title.setStyle("-fx-font-size:34;"+"-fx-font-weight:bold;"+"-fx-text-fill:white;");
        label.setStyle("-fx-font-size:22;"+"-fx-font-weight:bold;"+"-fx-text-fill:white;");
        
        comboMaps.setPrefSize(300,45);
        comboMaps.setStyle("-fx-font-size:16;"+"-fx-background-radius:8;"+"-fx-background-color:white;");
        
        cancel.setStyle("-fx-font-size:18;"+"-fx-background-radius:15;"+"-fx-background-color:#D9FF3F;"+"-fx-font-weight:bold");
        select.setStyle("-fx-font-size:18;"+"-fx-background-radius:15;"+"-fx-background-color:#D9FF3F;"+"-fx-font-weight:bold");
        
        HBox buttons = new HBox(20, cancel, select);
        
        root.getChildren().addAll(title,label,comboMaps,buttons);
        
        Stage dialog = new Stage();
        dialog.setTitle("Select map");
        dialog.setScene(new Scene(root,440,300));
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);
        
        cancel.setOnAction(e-> dialog.close());
        select.setOnAction(e->{
            String selectedName = comboMaps.getValue();
            
        
            if(selectedName != null){
                for(MapRegion map : maps){
                    if(map.getName().equals(selectedName)){
                        selectedMapRegion = map;
                        buildMap(map);
                        break;
                    }
                }
            }
            dialog.close();
        });
        dialog.showAndWait();
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

    @FXML
    private void handleSelectActivity(ActionEvent event) {
        System.out.println("Select Activity");
        
        List<Activity> activities = app.getUserActivities();
        
        if( activities == null || activities.isEmpty()){
            System.out.println("There are no activities available");
            return;
        }
        ListView<String> activitiesList = new ListView<>();
       
        for(Activity activity : activities){
            activitiesList.getItems().add(activity.getName());
        }
        activitiesList.setPrefSize(340,220);
        
        Button cancel = new Button("Cancel");
        Button select = new Button("Select");
        
        Label title = new Label("ACTIVITIES");
        Label label = new Label("Activity");
        
        VBox root = new VBox(18);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #302f36;"+"-fx-background-radius: 20;");
        
        title.setStyle("-fx-font-size:34;"+"-fx-font-weight:bold;"+"-fx-text-fill:white;");
        label.setStyle("-fx-font-size:22;"+"-fx-font-weight:bold;"+"-fx-text-fill:white;");
        
        HBox buttons = new HBox(20, cancel, select);
        
        root.getChildren().addAll(title,label,activitiesList,buttons);
        
        Stage dialog = new Stage();
        dialog.setTitle("Select Activities");
        dialog.setScene(new Scene(root,440,300));
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(btnSelectActivity.getScene().getWindow());
        dialog.setResizable(false);
        
        cancel.setOnAction(e-> dialog.close());
        select.setOnAction(e->{
            String selectedName = activitiesList.getSelectionModel().getSelectedItem();
            
        
            if(selectedName != null){
                for(Activity activity : activities){
                    if(activity.getName().equals(selectedName)){
                        currentActivity = activity;
                        showActivity(activity);
                        break;
                    }
                }
            }
            dialog.close();
        });
        dialog.showAndWait();
    }
}
