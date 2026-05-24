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
import upv.ipc.sportlib.SportActivityApp;
import upv.ipc.sportlib.User;
import java.util.HashMap;
import java.time.LocalDate;
/**
 *
 * @author jose
 */
public class MapaDemoApp extends Application {
  public static Scene scene;

  public static HashMap<String, Parent> router = new HashMap<String, Parent>();

  public static void setRoot(Parent root) {

    scene.setRoot(root);
  }

  public static void setRoot(String key) {
    Parent root = router.get(key);
    if (root != null) {
      scene.setRoot(root);
    } else {
      System.out.printf("Requested root does not exist.");
    }
  }

  @Override
  public void start(Stage stage) throws Exception {
    Parent root;

    FXMLLoader loader;

      
        SportActivityApp app = SportActivityApp.getInstance();
        
        
        boolean registered = app.registerUser("dianaherasg", "dianaheras@gmail.com", "passPER21!", LocalDate.of(2000, 3, 14),(String) null);
        System.out.println("Register: " + registered);
        
        boolean login1 = app.login("dianaherasg", "passPER21!");
        System.out.println("Login 1: " + login1);
        app.logout();
        
        boolean login2 = app.login("dianaherasg", "passPER21!");
        System.out.println("Login 2: " + login2);
        app.logout();
         
        boolean login3 = app.login("dianaherasg", "passPER21!");
        System.out.println("Login 3: " + login3);
        System.out.println("Actual user: " + app.getCurrentUser());
        
        if(app.getCurrentUser() != null) {
            app.getSessionsByUser(app.getCurrentUser()).forEach(s ->
            System.out.println("Session: " + s.getStartTime() + " -> " + s.getEndTime()));
        }
        
        

       // loader = new FXMLLoader(getClass().getResource("../view/Register.fxml"));
        //root = loader.load();
        //router.put("Register", root);

        //loader = new FXMLLoader(getClass().getResource("../view/Dashboard.fxml"));
        //root = loader.load();
       // router.put("Dashboard", root);

        //loader = new FXMLLoader(getClass().getResource("../view/Activities.fxml"));
        //root = loader.load();
        //router.put("Activities", root);

        //loader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
       // root = loader.load();
       // router.put("Login", root);

        //loader = new FXMLLoader(getClass().getResource("../view/Maps.fxml"));
        //root = loader.load();
        //router.put("Maps", root);

        loader = new FXMLLoader(getClass().getResource("../view/Profile.fxml"));
        root = loader.load();
        router.put("Profile", root);

        
        
        
        scene = new Scene(router.get("Profile"), 800, 600);
        
        
         
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

}
