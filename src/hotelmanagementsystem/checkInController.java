/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementsystem;

import static com.mysql.cj.util.SaslPrep.prepare;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;



public class checkInController implements Initializable{
    @FXML
    private DatePicker checkIn_date;

    @FXML
    private AnchorPane checkIn_form;

    @FXML
    private DatePicker checkOut_date;

    @FXML
    private Button close;

    @FXML
    private Label customerNumber;

    @FXML
    private TextField emailAddress;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField phoneNumber;
    
    @FXML
    private Label total;

    @FXML
    private Label totalDays;
    
    @FXML
    private ComboBox<String> roomNumber;

    @FXML
    private ComboBox<?> roomType;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;
    
    public void customerCheckIn(){
        String insertCustomerData = "INSERT INTO customer (customer_id,total,roomType,roomNumber,firstName,lastName,phoneNumber,email,checkIn,checkOut)" + 
                "VALUES(?,?,?,?,?,?,?,?,?,?)" ;
        connect = database.connectDb();
        
        try{
            String customerNum = customerNumber.getText();
            String roomT = (String)roomType.getSelectionModel().getSelectedItem();
            String roomN = (String)roomNumber.getSelectionModel().getSelectedItem();
            String firstN = firstName.getText();
            String lastN = lastName.getText();
            String phoneNum = phoneNumber.getText();
            String email = emailAddress.getText();
            String checkInDate = String.valueOf(checkIn_date.getValue());
            String checkOutDate = String.valueOf(checkOut_date.getValue());
            
            Alert alert;
            if (customerNum == null || firstN == null || lastN == null || phoneNum == null || email == null
                    || checkInDate == null || checkOutDate == null){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(("Please fill all blanks fields"));
                alert.showAndWait();
            }else{
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(("Are You Sure?"));
//                prepare = connect.prepareStatement(insertCustomerData);
//                prepare.setString(1, customerNum);
//                prepare.setString(2, firstN);
//                prepare.setString(3, lastN);
//                prepare.setString(4, phoneNum);
//                prepare.setString(5, email);
//                prepare.setString(6, checkInDate);
//                prepare.setString(7, checkOutDate);
                
               // prepare.executeUpdate();
                
                
                
                Optional <ButtonType> option = alert.showAndWait();
                String totalC = String.valueOf(totalP);
                if (option.get().equals(ButtonType.OK)){
                prepare = connect.prepareStatement(insertCustomerData);
                prepare.setString(1, customerNum);
                prepare.setString(2, totalC);
                prepare.setString(3, roomT);
                prepare.setString(4, roomN);
                prepare.setString(5, firstN);
                prepare.setString(6, lastN);
                prepare.setString(7, phoneNum);
                prepare.setString(8, email);
                prepare.setString(9, checkInDate);
                prepare.setString(10, checkOutDate);
                
                prepare.executeUpdate();
                
                String date = String.valueOf(checkIn_date.getValue());
                
                String customerN = customerNumber.getText();
                String customerReceipt = "INSERT INTO customer_receipt (customer_num,total,date)" + "Values (?,?,?)";
                
                prepare = connect.prepareStatement(customerReceipt);
                prepare.setString(1, customerN);
                prepare.setString(2, totalC);
                prepare.setString(3, date);
                
                prepare.execute();
                
                String sqlEditStatus = "UPDATE room SET status ='occupied' WHERE roomNumber = '"+roomN+"'";
                
                statement = connect.createStatement();
                statement.executeUpdate(sqlEditStatus);
                
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText((null));
                alert.setContentText("Successfully checked-In");
                alert.showAndWait();
                
                reset();
                }else{
                    return;
                }
            }           
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void receipt(){
         
        HashMap hash = new HashMap();
        hash.put("hotelParameter", getData.customerNum + 1);
        
        try{
            if(totalP > 0){
            
            JasperDesign jDesign = JRXmlLoader.load("C:\\Users\\samee\\Desktop\\HotelManagementSystem(new)\\HotelManagementSystem\\src\\hotelmanagementsystem\\report.jrxml");
            JasperReport jReport = JasperCompileManager.compileReport(jDesign);
            JasperPrint jPrint = JasperFillManager.fillReport(jReport, hash, connect);
            
            JasperViewer.viewReport(jPrint, false);
            }else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText((null));
                alert.setContentText("Invalid Receipt");
                alert.showAndWait();
            }
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void reset(){
        firstName.setText("");
        lastName.setText("");
        phoneNumber.setText("");
        emailAddress.setText("");
        roomType.getSelectionModel().clearSelection();
        roomNumber.getSelectionModel().clearSelection();
        totalDays.setText("----");
        total.setText("$0.0");
    }
    
    
    public void totalDays(){
        Alert alert;
        
        if (checkOut_date.getValue().isAfter(checkIn_date.getValue())){
            getData.totalDays = checkOut_date.getValue().compareTo(checkIn_date.getValue());
          
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid check-out date");
            alert.showAndWait();
            
            checkOut_date.setValue(null);
        }
//    } else {
//        alert = new Alert(AlertType.ERROR);
//        alert.setTitle("Error Message");
//        alert.setHeaderText(null);
//        alert.setContentText("Please select both check-in and check-out dates");
//        alert.showAndWait();
//        getData.totalDays = 0; // Reset totalDays if dates are not selected
//    }
         
        displayTotal();
    }
    
    
    private float totalP = 0;
    
    public void displayTotal(){
        
        String totalId = String.valueOf(getData.totalDays);
        
        totalDays.setText(totalId);
        
        String selectItem =(String) roomNumber.getSelectionModel().getSelectedItem();
        
        String sql = "SELECT * FROM room WHERE roomNumber = '"+selectItem+"'";
        
        double priceData = 0;
        connect = database.connectDb();
        
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            if(result.next()){
                priceData = result.getDouble("price");
            }
             totalP = (float) ((priceData) * getData.totalDays);
            
            total.setText("$"+String.valueOf(totalP));
            
        }catch(Exception e){e.printStackTrace();}
    }
    
    public void roomTypeList(){
        String listType = "SELECT * FROM room WHERE status = 'Available' GROUP BY type ORDER BY roomNumber ASC";
        
        connect = database.connectDb();
        
        try{
            
            ObservableList listData = FXCollections.observableArrayList();
            prepare = connect.prepareStatement(listType);
            result = prepare.executeQuery();
            
            while(result.next()){
                listData.add(result.getString("type"));
            }
            
            roomType.setItems(listData);
            
            roomNumberList();
            
        }catch(Exception e){e.printStackTrace();}
    }
    
    public void roomNumberList(){
        
        String item = (String)roomType.getSelectionModel().getSelectedItem();
        String availableRoomNumber = "SELECT * FROM room WHERE type = '"+item+"'and status ='Available' ORDER BY roomNumber ASC";
        
        connect = database.connectDb();
        try{
            
            ObservableList listData = FXCollections.observableArrayList();
            prepare = connect.prepareStatement(availableRoomNumber);           
            result = prepare.executeQuery();
            
            while(result.next()){
                listData.add(result.getString("roomNumber"));
            }
            roomNumber.setItems(listData);
        }catch(Exception e){e.printStackTrace();}
    }
    
    
    public void CustomerNumber(){
        String customerNum = "SELECT customer_id FROM customer";
        
        connect = database.connectDb();
        
        try{
            prepare = connect.prepareStatement(customerNum);
            result = prepare.executeQuery();
            
            while(result.next()){
                getData.customerNum = result.getInt("customer_id");
            }
            
        }catch(Exception e){e.printStackTrace();}
    }
    
    public void displayCustomerNumber(){
        CustomerNumber();
        
        customerNumber.setText(String.valueOf(getData.customerNum+1));
    }
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayCustomerNumber();  
        roomTypeList();
        roomNumberList();
    }
    
}
