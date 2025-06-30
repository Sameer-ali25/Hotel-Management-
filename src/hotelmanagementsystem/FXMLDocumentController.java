/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementsystem;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Button close;

    @FXML
    private Button loginbtn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField password;

    @FXML
    private StackPane stack_form;

    @FXML
    private TextField username;
    
    
    private Connection connect;
     private PreparedStatement prepare;
     private ResultSet result;
     
     private double x = 0;
     private double y = 0;
     
     public void login(){
         String user = username.getText();
         String pass = password.getText();
         String sql = "SELECT * FROM admin WHERE username = ? and password = ? ";
         
         connect = database.connectDb();
         
         try{
             prepare = connect.prepareStatement(sql);
             prepare.setString(1, user);
             prepare.setString(2, pass);
             
             result = prepare.executeQuery();
             
             Alert alert;
             
              if (user.isEmpty() || pass.isEmpty()){
                 alert = new Alert (AlertType.ERROR);
                 alert.setTitle("Error Message");
                 alert.setHeaderText(null);
                 alert.setContentText("Please Fill all Blank Fields");
                 alert.showAndWait();
             }
             
              if (result.next()) {
                 alert = new Alert (AlertType.INFORMATION);
                 alert.setTitle("Information Message");
                 alert.setHeaderText(null);
                 alert.setContentText("Successfully Login");
                 alert.showAndWait();
                 
                 loginbtn.getScene().getWindow().hide();
                        
                 
                 Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                 
                 Stage stage = new Stage();
                 Scene scene = new Scene (root);
                 
                  root.setOnMousePressed((MouseEvent event) -> {
                      x = event.getSceneX();
                      y = event.getSceneY(); 
                  });
                   root.setOnMouseDragged((MouseEvent event)-> {
                     
                     stage.setX(event.getScreenX() -x);
                     stage.setY(event.getScreenY() -y);
                 });
                  
                  stage.initStyle(StageStyle.TRANSPARENT);
                 
                 stage.setScene(scene);
                 stage.show();
                 
              }else{
                 alert = new Alert (AlertType.ERROR);
                 alert.setTitle("Error Message");
                 alert.setHeaderText(null);
                 alert.setContentText("Wrong username/Password");
                 alert.showAndWait();
              }                                       
         }catch (Exception e) {e.printStackTrace();}
         
         
     }    
    
    public void exit(){
         System.exit(0);
     }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
