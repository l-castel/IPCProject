/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapademo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.HashMap;

/**
 *
 * @author jose
 */

public class MapaDemoApp extends Application {
    public static Scene scene;



    public static HashMap<String, Parent> router = new HashMap<String, Parent>();
    public static HashMap<String, Object> controllers= new HashMap<>();

    public static void setRoot(Parent root){

        scene.setRoot(root);
    }

    public static void setRoot(String key){
        Parent root = router.get(key);
        if(root != null){
            ((Navigable) controllers.get(key)).onNavigate();
            scene.setRoot(root);
        } else{
            System.out.printf("Requested root does not exist.");
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root;

        FXMLLoader loader;

        loader = new FXMLLoader(getClass().getResource("../view/Register.fxml"));
        root = loader.load();
        router.put("Register", root);
        controllers.put("Register", loader.getController());

        loader = new FXMLLoader(getClass().getResource("../view/Dashboard.fxml"));
        root = loader.load();
        router.put("Dashboard", root);
        controllers.put("Dashboard", loader.getController());

        loader = new FXMLLoader(getClass().getResource("../view/Annotationsdialog.fxml"));
        root = loader.load();
        router.put("AnnotationsDialog", root);

        loader = new FXMLLoader(getClass().getResource("../view/Activities.fxml"));
        root = loader.load();
        router.put("Activities", root);
        controllers.put("Activities", loader.getController());

        loader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
        root = loader.load();
        router.put("Login", root);
        controllers.put("Login", loader.getController());

        //loader = new FXMLLoader(getClass().getResource("../view/Maps.fxml"));
        //root = loader.load();
        //router.put("Maps", root);
        //controllers.put("Maps", loader.getController());


        scene = new Scene(router.get("Login"), 1920, 1080);
        


        // 3. Attach scene to stage and configure
        stage.setTitle("ProyectoIPC");
        stage.setScene(scene);

        // 4. Display the window
        stage.show();



    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
