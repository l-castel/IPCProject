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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.StrokeLineCap;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Node;
import mapademo.MapaDemoApp;

import mapademo.Navigable;
import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.Annotation;
import upv.ipc.sportlib.AnnotationType;
import upv.ipc.sportlib.GeoPoint;
import upv.ipc.sportlib.MapProjection;
import upv.ipc.sportlib.MapRegion;
import upv.ipc.sportlib.SportActivityApp;
import upv.ipc.sportlib.User;
import upv.ipc.sportlib.TrackPoint;
/**
 * FXML Controller class
 *
 * @author marti
 */

public class DashboardController implements Initializable, Navigable {
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
    private ImageView avatarImage;
    
    @FXML
    private Label nicknameLabel;
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
    private Button btnSpeedRoute;
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
    
    private boolean speedMode = false;
    private final List<Node> routeNodes = new ArrayList<>();
    private Circle elevationHighlightCircle;
    private Text mapTooltipText;
    
    private List<Double> elevationCumDistances;
    private List<TrackPoint> elevationTrackPoints;
    private double elevationMinElev;
    private double elevationElevRange;
    
    @FXML
    private Button btnSelectActivity;


    /**
     * Initializes the controller class*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        btnAddAnnotations.setOnAction(this::diagAddAnnotations);
        btnSelectActivity.setOnAction(this::handleSelectActivity);
        
        elevationCanvas.setOnMouseMoved(this::handleElevationMouseMoved);
        elevationCanvas.setOnMouseExited(this::handleElevationMouseExited);

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
        routeNodes.clear();
        elevationHighlightCircle = null;
        mapTooltipText = null;
        
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
        
        mapPane.setOnMouseMoved(this::handleMapMouseMoved);
        mapPane.setOnMouseExited(this::handleMapMouseExited);

        
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
        drawElevationProfile();
        focusOnRoute(activity);
        updateCumulativeStats();
    }

    // Generated with the help of AI
    private void focusOnRoute(Activity activity){
        if(activity == null || projection == null || mapPane == null) return;
        
        List<TrackPoint> points = activity.getTrackPoints();
        if(points == null || points.isEmpty()) return;
        
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = 0, maxY = 0;
        
        for(TrackPoint tp : points){
            Point2D p = projection.project(tp);
            if(p.getX() < minX) minX = p.getX();
            if(p.getY() < minY) minY = p.getY();
            if(p.getX() > maxX) maxX = p.getX();
            if(p.getY() > maxY) maxY = p.getY();
        }
        
        double routeCenterX = (minX + maxX) / 2;
        double routeCenterY = (minY + maxY) / 2;
        
        Platform.runLater(() -> {
            double scale = zoomSlider.getValue();
            double contentW = mapPane.getPrefWidth() * scale;
            double contentH = mapPane.getPrefHeight() * scale;
            double viewportW = mapScrollPane.getViewportBounds().getWidth();
            double viewportH = mapScrollPane.getViewportBounds().getHeight();
            
            if(viewportW <= 0 || viewportH <= 0) return;
            
            if(contentW > viewportW){
                double hvalue = (routeCenterX * scale - viewportW / 2) / (contentW - viewportW);
                mapScrollPane.setHvalue(Math.max(0, Math.min(1, hvalue)));
            } else {
                mapScrollPane.setHvalue(0.5);
            }
            
            if(contentH > viewportH){
                double vvalue = (routeCenterY * scale - viewportH / 2) / (contentH - viewportH);
                mapScrollPane.setVvalue(Math.max(0, Math.min(1, vvalue)));
            } else {
                mapScrollPane.setVvalue(0.5);
            }
        });
    }
    private void drawRoute(Activity activity){
        clearRouteNodes();
        if(speedMode){
            drawSpeedRoute(activity);
        } else {
            drawNormalRoute(activity);
        }
        drawStartEndPoints(activity);
    }
    
    private void clearRouteNodes(){
        for(Node node : routeNodes){
            mapPane.getChildren().remove(node);
        }
        routeNodes.clear();
    }
    
    private void drawNormalRoute(Activity activity){
        Polyline route = new Polyline();
        route.setStroke(Color.web("#E74C3C"));
        route.setStrokeWidth(4);
        route.setOpacity(0.90);
        
        for(TrackPoint trackPoint : activity.getTrackPoints()){
            Point2D point = projection.project(trackPoint);
            route.getPoints().addAll(point.getX(), point.getY());
        }
        mapPane.getChildren().add(route);
        routeNodes.add(route);
    }
    // Generated with the help of AI
    private void drawSpeedRoute(Activity activity){
        List<TrackPoint> points = activity.getTrackPoints();
        if(points == null || points.size() < 2) return;
        
        double minSpeed = Double.MAX_VALUE;
        double maxSpeed = 0;
        List<Double> speeds = new ArrayList<>();
        
        for(int i = 0; i < points.size() - 1; i++){
            double speed = points.get(i).speedTo(points.get(i + 1));
            if(Double.isNaN(speed) || Double.isInfinite(speed) || speed < 0){
                speed = 0;
            }
            speeds.add(speed);
            if(speed < minSpeed) minSpeed = speed;
            if(speed > maxSpeed) maxSpeed = speed;
        }
        
        if(speeds.isEmpty()){
            drawNormalRoute(activity);
            return;
        }
        
        if(minSpeed == maxSpeed){
            for(int i = 0; i < points.size() - 1; i++){
                Point2D p1 = projection.project(points.get(i));
                Point2D p2 = projection.project(points.get(i + 1));
                Line segment = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                segment.setStrokeWidth(4);
                segment.setOpacity(0.90);
                segment.setStroke(Color.web("#F1C40F"));
                segment.setStrokeLineCap(StrokeLineCap.ROUND);
                mapPane.getChildren().add(segment);
                routeNodes.add(segment);
            }
            return;
        }
        
        for(int i = 0; i < points.size() - 1; i++){
            Point2D p1 = projection.project(points.get(i));
            Point2D p2 = projection.project(points.get(i + 1));
            
            Line segment = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            segment.setStrokeWidth(4);
            segment.setOpacity(0.90);
            segment.setStroke(getSpeedColor(speeds.get(i), minSpeed, maxSpeed));
            segment.setStrokeLineCap(StrokeLineCap.ROUND);
            
            mapPane.getChildren().add(segment);
            routeNodes.add(segment);
        }
    }
    
    private Color getSpeedColor(double speed, double minSpeed, double maxSpeed){
        double ratio = (speed - minSpeed) / (maxSpeed - minSpeed);
        if(ratio < 0) ratio = 0;
        if(ratio > 1) ratio = 1;
        double hue = ratio * 120;
        return Color.hsb(hue, 0.85, 0.9);
    }
    
    private void drawStartEndPoints(Activity activity){
        Point2D start = projection.project(activity.getStartPoint());
        Point2D end = projection.project(activity.getEndPoint());
        
        Circle startCircle = new Circle(start.getX(), start.getY(), 8);
        startCircle.setFill(Color.GREEN);
        
        Circle endCircle = new Circle(end.getX(), end.getY(), 8);
        endCircle.setFill(Color.RED);
        
        Tooltip.install(startCircle, new Tooltip("Start"));
        Tooltip.install(endCircle, new Tooltip("End"));
        
        mapPane.getChildren().addAll(startCircle, endCircle);
        routeNodes.add(startCircle);
        routeNodes.add(endCircle);
    }
    
    private void drawActivityAnnotations(Activity activity){
        for (Annotation annotation : activity.getAnnotations()){
            drawAnnotation(annotation);
        }
    }
    private void updateStatistics(Activity activity){
        lblDistance.setText(String.format("%.2f km",activity.getTotalDistance()/ 1000.0));
        lblDuration.setText(String.format("%d min", activity.getDuration().toMinutes()));
        lblAvgSpeed.setText(String.format("%.2f km/h",activity.getAverageSpeed()));
        lblAvgPace.setText(String.format("%.2f min/km",activity.getAveragePace()));
        lblMaxAltitude.setText(String.format("%.0f m", activity.getMaxElevation()));
        lblMinAltitude.setText(String.format(" %.0f m", activity.getMinElevation()));
        lblMaxElevation.setText(String.format(" %.0f m", activity.getMaxElevation()));
        lblMinElevation.setText(String.format("%.0f m", activity.getMinElevation()));
        
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
        switch(type.toLowerCase()) {
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

    // Generated by AI
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
    private void handleLogout(ActionEvent event) {
        app.logout();
        MapaDemoApp.setRoot("Login");
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
        speedMode = !speedMode;
        if(speedMode){
            btnSpeedRoute.setStyle(
                "-fx-background-color: #1a1a1a; -fx-text-fill: #c8f020;" +
                "-fx-font-size: 13px; -fx-font-weight: bold;" +
                "-fx-background-radius: 10px; -fx-cursor: hand;"
            );
        } else {
            btnSpeedRoute.setStyle(
                "-fx-background-color: #1a1a1a; -fx-text-fill: white;" +
                "-fx-font-size: 13px; -fx-font-weight: bold;" +
                "-fx-background-radius: 10px; -fx-cursor: hand;"
            );
        }
        if(currentActivity != null){
            drawRoute(currentActivity);
        }
    }

    @Deprecated
    private void handleElevationProfile(ActionEvent event) {
        drawElevationProfile();
    }

    // Generated with the help of AI
    
    private void drawElevationProfile(){
        GraphicsContext gc = elevationCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, elevationCanvas.getWidth(), elevationCanvas.getHeight());
        
        if(currentActivity == null) return;
        
        List<TrackPoint> points = currentActivity.getTrackPoints();
        if(points == null || points.size() < 2) return;
        
        List<Double> cumDistances = new ArrayList<>();
        cumDistances.add(0.0);
        double totalDist = 0;
        double minElev = points.get(0).getElevation();
        double maxElev = points.get(0).getElevation();
        
        for(int i = 1; i < points.size(); i++){
            double dist = points.get(i - 1).distanceTo(points.get(i));
            totalDist += dist;
            cumDistances.add(totalDist);
            double elev = points.get(i).getElevation();
            if(elev < minElev) minElev = elev;
            if(elev > maxElev) maxElev = elev;
        }
        
        double left = 40, right = 10, top = 10, bottom = 25;
        double drawW = elevationCanvas.getWidth() - left - right;
        double drawH = elevationCanvas.getHeight() - top - bottom;
        
        double elevRange = maxElev - minElev;
        if(elevRange == 0) elevRange = 1;
        
        this.elevationCumDistances = cumDistances;
        this.elevationTrackPoints = points;
        this.elevationMinElev = minElev;
        this.elevationElevRange = elevRange;
        
        gc.setStroke(Color.web("#bbbbbb"));
        gc.setLineWidth(1);
        gc.strokeLine(left, top + drawH, left + drawW, top + drawH);
        gc.strokeLine(left, top, left, top + drawH);
        
        gc.setFill(Color.web("#555555"));
        gc.setFont(new Font(10));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(String.format("%.1f km", totalDist / 1000.0), left + drawW, top + drawH + 14);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText(String.format("%.0f", maxElev), left - 4, top + 8);
        gc.fillText(String.format("%.0f", minElev), left - 4, top + drawH);
        
        gc.save();
        gc.translate(12, top + drawH / 2);
        gc.rotate(-90);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Altitude (m)", 0, 0);
        gc.restore();
        
        gc.setFill(Color.web("#2ECC7144"));
        gc.beginPath();
        gc.moveTo(left, top + drawH);
        for(int i = 0; i < points.size(); i++){
            double x = left + (cumDistances.get(i) / totalDist) * drawW;
            double y = top + drawH - ((points.get(i).getElevation() - minElev) / elevRange) * drawH;
            gc.lineTo(x, y);
        }
        gc.lineTo(left + (cumDistances.get(cumDistances.size() - 1) / totalDist) * drawW, top + drawH);
        gc.closePath();
        gc.fill();
        
        gc.setStroke(Color.web("#27AE60"));
        gc.setLineWidth(2);
        gc.beginPath();
        for(int i = 0; i < points.size(); i++){
            double x = left + (cumDistances.get(i) / totalDist) * drawW;
            double y = top + drawH - ((points.get(i).getElevation() - minElev) / elevRange) * drawH;
            if(i == 0) gc.moveTo(x, y);
            else gc.lineTo(x, y);
        }
        gc.stroke();
    }

    // Generated with the help of AI
    private void handleElevationMouseMoved(MouseEvent e){
        if(elevationTrackPoints == null || elevationTrackPoints.isEmpty()) return;
        
        double left = 40;
        double top = 10;
        double drawW = elevationCanvas.getWidth() - left - 10;
        double drawH = elevationCanvas.getHeight() - top - 25;
        
        double mouseX = e.getX();
        if(mouseX < left || mouseX > left + drawW) return;
        
        drawElevationProfile();
        
        double totalDist = elevationCumDistances.get(elevationCumDistances.size() - 1);
        double targetDist = ((mouseX - left) / drawW) * totalDist;
        
        int closestIdx = 0;
        double minDiff = Math.abs(elevationCumDistances.get(0) - targetDist);
        for(int i = 1; i < elevationCumDistances.size(); i++){
            double diff = Math.abs(elevationCumDistances.get(i) - targetDist);
            if(diff < minDiff){
                minDiff = diff;
                closestIdx = i;
            }
        }
        
        TrackPoint tp = elevationTrackPoints.get(closestIdx);
        double x = left + (elevationCumDistances.get(closestIdx) / totalDist) * drawW;
        double y = top + drawH - ((tp.getElevation() - elevationMinElev) / elevationElevRange) * drawH;
        
        GraphicsContext gc = elevationCanvas.getGraphicsContext2D();
        gc.setStroke(Color.web("#E74C3C"));
        gc.setLineWidth(1);
        gc.setLineDashes(4, 4);
        gc.strokeLine(x, top, x, top + drawH);
        gc.strokeLine(left, y, left + drawW, y);
        gc.setLineDashes();
        
        gc.setFill(Color.web("#E74C3C"));
        gc.fillOval(x - 4, y - 4, 8, 8);
        
        gc.setFill(Color.web("#333333"));
        gc.setFont(new Font(10));
        gc.setTextAlign(TextAlignment.LEFT);
        String tooltip = String.format("Dist: %.1f km  Alt: %.0f m", elevationCumDistances.get(closestIdx) / 1000.0, tp.getElevation());
        double textX = x + 8;
        double textY = y - 8;
        if(textX + 80 > elevationCanvas.getWidth()) textX = x - 85;
        if(textY < 15) textY = y + 15;
        gc.fillText(tooltip, textX, textY);
        
        Point2D mapPoint = projection.project(tp);
        if(elevationHighlightCircle == null){
            elevationHighlightCircle = new Circle(7);
            elevationHighlightCircle.setFill(Color.web("#E74C3C"));
            elevationHighlightCircle.setStroke(Color.WHITE);
            elevationHighlightCircle.setStrokeWidth(2);
            mapPane.getChildren().add(elevationHighlightCircle);
        }
        elevationHighlightCircle.setCenterX(mapPoint.getX());
        elevationHighlightCircle.setCenterY(mapPoint.getY());
        elevationHighlightCircle.setVisible(true);
        elevationHighlightCircle.toFront();
    }

    // Generated with the help of AI
    private void handleMapMouseMoved(MouseEvent e){
        if(currentActivity == null || waitingMapClick || projection == null) return;
        
        List<TrackPoint> points = currentActivity.getTrackPoints();
        if(points == null || points.size() < 2) return;
        
        Point2D mouse = new Point2D(e.getX(), e.getY());
        double minDist = Double.MAX_VALUE;
        int closestIdx = -1;
        
        for(int i = 0; i < points.size(); i++){
            Point2D p = projection.project(points.get(i));
            double d = mouse.distance(p);
            if(d < minDist){
                minDist = d;
                closestIdx = i;
            }
        }
        
        if(minDist > 18 || closestIdx < 0){
            if(mapTooltipText != null) mapTooltipText.setVisible(false);
            return;
        }
        
        TrackPoint tp = points.get(closestIdx);
        double speed;
        if(closestIdx < points.size() - 1){
            speed = points.get(closestIdx).speedTo(points.get(closestIdx + 1));
        } else {
            speed = points.get(closestIdx - 1).speedTo(points.get(closestIdx));
        }
        if(Double.isNaN(speed) || Double.isInfinite(speed) || speed < 0) speed = 0;
        
        if(mapTooltipText == null){
            mapTooltipText = new Text();
            mapTooltipText.setFill(Color.WHITE);
            mapTooltipText.setStroke(Color.web("#1a1a1a"));
            mapTooltipText.setStrokeWidth(1.5);
            mapTooltipText.setFont(new Font(12));
            mapPane.getChildren().add(mapTooltipText);
        }
        
        mapTooltipText.setText(String.format("%.0f m  %.1f km/h", tp.getElevation(), speed));
        mapTooltipText.setX(e.getX() + 10);
        mapTooltipText.setY(e.getY() - 10);
        mapTooltipText.setVisible(true);
        mapTooltipText.toFront();
        
        Point2D mapPoint = projection.project(tp);
        if(elevationHighlightCircle == null){
            elevationHighlightCircle = new Circle(7);
            elevationHighlightCircle.setFill(Color.web("#E74C3C"));
            elevationHighlightCircle.setStroke(Color.WHITE);
            elevationHighlightCircle.setStrokeWidth(2);
            mapPane.getChildren().add(elevationHighlightCircle);
        }
        elevationHighlightCircle.setCenterX(mapPoint.getX());
        elevationHighlightCircle.setCenterY(mapPoint.getY());
        elevationHighlightCircle.setVisible(true);
        elevationHighlightCircle.toFront();
    }
    
    private void handleMapMouseExited(MouseEvent e){
        if(mapTooltipText != null){
            mapTooltipText.setVisible(false);
        }
        if(elevationHighlightCircle != null){
            elevationHighlightCircle.setVisible(false);
        }
    }
    
    private void handleElevationMouseExited(MouseEvent e){
        if(currentActivity != null){
            drawElevationProfile();
        }
        if(elevationHighlightCircle != null){
            elevationHighlightCircle.setVisible(false);
        }
    }

    @FXML
    private void profileClick(MouseEvent event) {
    }

    @FXML
    private void activitiesClick(MouseEvent event) {
    }

    @FXML
    private void selectMapClick(MouseEvent event) {
    }

    @FXML
    private void speedOverRouteClick(MouseEvent event) {
    }

    @Deprecated
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

    @Override
    public void onNavigate() {
        User user = app.getCurrentUser();
        if (user != null) {
            nicknameLabel.setText(user.getNickName());
            avatarImage.setImage(user.getAvatar());
        }
    }
}
