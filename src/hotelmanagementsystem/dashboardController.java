


package hotelmanagementsystem;

import java.net.URL;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class dashboardController implements Initializable {
    
     @FXML
    private AnchorPane main_form;

    @FXML
    private Button availableRooms_addBtn;

    @FXML
    private Button availableRooms_btn;

    @FXML
    private Button availableRooms_checkinBtn;

    @FXML
    private Button availableRooms_clearBtn;

    @FXML
    private TableColumn<roomData, String> availableRooms_col_price;

    @FXML
    private TableColumn<roomData, String> availableRooms_col_roomNumber;

    @FXML
    private TableColumn<roomData, String> availableRooms_col_roomType;

    @FXML
    private TableColumn<roomData, String> availableRooms_col_status;

    @FXML
    private Button availableRooms_deleteBtn;

    @FXML
    private AnchorPane availableRooms_form;

    @FXML
    private TextField availableRooms_price;

    @FXML
    private TextField availableRooms_roomNumber;

    @FXML
    private ComboBox<?> availableRooms_roomType;

    @FXML
    private TextField availableRooms_search;

    @FXML
    private ComboBox<?> availableRooms_status;

    @FXML
    private TableView<roomData> availableRooms_tableView;

    @FXML
    private Button availableRooms_updateBtn;

    @FXML
    private Button closeBtn;

    @FXML
    private Button customer_btn;

    @FXML
    private TableColumn<customerData, String> customer_checkedIn;

    @FXML
    private TableColumn<customerData, String> customer_checkedOut;

    @FXML
    private TableColumn<customerData, String> customer_customerNumber;

    @FXML
    private TableColumn<customerData, String> customer_firstName;

    @FXML
    private AnchorPane customer_form;

    @FXML
    private TableColumn<customerData, String> customer_lastName;

    @FXML
    private TableColumn<customerData, String> customer_phoneNumber;

    @FXML
    private TextField customer_search;

    @FXML
    private TableView<customerData> customer_tableView;

    @FXML
    private TableColumn<customerData, String> customer_totalPayment;

    @FXML
    private AreaChart<?, ?> dashboard_areaChart;

    @FXML
    private Label dashboard_bookTody;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Label dashboard_incomeToday;

    @FXML
    private Label dashboard_totalincome;

    
    @FXML
    private Button logout_btn;
   
    @FXML
    private Button minimizeBtn;

    @FXML
    private Label username;
    
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    
    private int count = 0;
    public void dashboardCountBookToday(){
        
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        
        String sql = "SELECT COUNT(id) FROM customer WHERE checkIn = '"+sqlDate+"'";
        
        connect = database.connectDb();
        
         count = 0;
        
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                count = result.getInt("COUNT(id)");
            }
            
            
        }catch(Exception e){e.printStackTrace();}       
    }
    
    
    public void dashboardDisplayBookToday(){
        
        dashboardCountBookToday();
        dashboard_bookTody.setText(String.valueOf(count));

    }
    
    private double sumToday = 0;
    public void dashboardSumIncomeToday(){
        
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        
        String sql = "SELECT SUM(total) FROM customer_receipt WHERE date = '"+sqlDate+"'";
        
        connect = database.connectDb();
        
        try{
            
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                sumToday = result.getDouble("SUM(total)");
            }
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void dashboardDisplayIncomeToday(){
        
        dashboard_incomeToday.setText(String.valueOf(sumToday));
    }
    private double overall = 0;
    public void dashboardSumTotalIncome(){
        String sql = "SELECT SUM(total) FROM customer_receipt";
        
        connect = database.connectDb();
        
        try{
            
           prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery(); 
            while(result.next()){
                overall = result.getDouble("SUM(total)");
            }
            
        }catch(Exception e){e.printStackTrace();}                
    }
 
    public void dashboardTotalIncome(){
    
     dashboardSumTotalIncome();
     
    dashboard_totalincome.setText(String.valueOf(overall));
    }
    
    public void dashboardChart(){
        
        dashboard_areaChart.getData().clear();
        
        String sql = "Select date, total FROM customer_receipt GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 8";
        
        connect = database.connectDb();
        
        XYChart.Series chart = new XYChart.Series();
                
        try{
            
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery(); 
            while(result.next()){
                
                chart.getData().add(new XYChart.Data(result.getString(1),result.getInt(2)));
            }
            
            dashboard_areaChart.getData().add(chart);
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    



    
    public void availableRoomsAdd(){
        String sql = "INSERT INTO room (roomNumber,type,status,price) VALUES(?,?,?,?)";
        
        connect = database.connectDb();
        
        try{
            String roomNumber = availableRooms_roomNumber.getText();
            String type = (String)availableRooms_roomType.getSelectionModel().getSelectedItem();
            String status = (String)availableRooms_status.getSelectionModel().getSelectedItem();
            String price  = availableRooms_price.getText();
            
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, roomNumber);
            prepare.setString(2, type);
            prepare.setString(3, status);
            prepare.setString(4, price);
            
            Alert alert;
            
            if (roomNumber == null || type == null || status == null || price == null){
                alert = new Alert (AlertType.ERROR);
                 alert.setTitle("Error Message");
                 alert.setHeaderText(null);
                 alert.setContentText("Please Fill All Blanks Fields");
                 alert.showAndWait();
                
                 
            }else{
                
                String check = "SELECT roomNumber FROM room WHERE roomNumber ='"+roomNumber+"'";
                
                prepare = connect.prepareStatement(check);
                result = prepare.executeQuery();
                if(result.next()) {
                     alert = new Alert (Alert.AlertType.ERROR);
                 alert.setTitle("Error Message");
                 alert.setHeaderText(null);
                 alert.setContentText("Room #"+roomNumber+"was already exist!");
                 alert.showAndWait();
                }else{
                    
                prepare = connect.prepareStatement(sql);
            prepare.setString(1, roomNumber);
            prepare.setString(2, type);
            prepare.setString(3, status);
            prepare.setString(4, price);
            
            
            prepare.executeUpdate();
            alert = new Alert (AlertType.INFORMATION);
                 alert.setTitle("Information Message");
                 alert.setHeaderText(null);
                 alert.setContentText("Successfully Added");
                 alert.showAndWait();
                 
                 availableRoomsShowData();
                 availableRoomsClear();
                }
            }
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
     public  ObservableList<roomData> availableRoomsListData(){        
        ObservableList<roomData> listData = FXCollections.observableArrayList();        
        String sql = "SELECT * FROM room";
        
        connect = database.connectDb();
           
        try{
            
            roomData roomD;            
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();           
            while(result.next()){
                roomD = new roomData(result.getInt("roomNumber"), 
                        result.getString("type"), 
                        result.getString("status"), 
                        result.getDouble("price"));
                
                listData.add(roomD);
            }            
        }catch(Exception e){e.printStackTrace();}
        return listData;
    }
     
    private ObservableList<roomData> roomDataList;
    
    public void availableRoomsShowData(){
        
        roomDataList = availableRoomsListData();
        
        availableRooms_col_roomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        availableRooms_col_roomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        availableRooms_col_status.setCellValueFactory(new PropertyValueFactory<>("Status"));
        availableRooms_col_price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        
        availableRooms_tableView.setItems(roomDataList);
        
    }
    
    public void availableRoomsSelectData(){
        
        roomData roomD = availableRooms_tableView.getSelectionModel().getSelectedItem();
        int num = availableRooms_tableView.getSelectionModel().getSelectedIndex();
        
        if((num -1) < -1){
            return;
        }
        availableRooms_roomNumber.setText(String.valueOf(roomD.getRoomNumber()));
        availableRooms_price.setText(String.valueOf(roomD.getPrice()));
    }
    
    public void availableRoomsUpdate(){
        String type1 = (String)availableRooms_roomType.getSelectionModel().getSelectedItem();
        String status1 = (String)availableRooms_status.getSelectionModel().getSelectedItem();
        String price1 = availableRooms_price.getText();
        String roomNum = availableRooms_roomNumber.getText();
        
        String sql = "UPDATE room SET type = '"+type1+"', status = '"+status1+"', price = '"+price1+"' WHERE roomNumber = '"+roomNum+"'" ;
        
        
        connect = database.connectDb();
        
        try{
            
            Alert alert;
            if(type1 == null || status1 == null|| price1 == null || roomNum == null){
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please Select the Room Data First");
            alert.showAndWait();
            }else{
                
                prepare = connect.prepareStatement(sql);
                prepare.executeUpdate();
                
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();   
                
                availableRoomsShowData();
                availableRoomsClear();
            }
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void availableRoomsDelete(){
        String type1 = (String)availableRooms_roomType.getSelectionModel().getSelectedItem();
        String status1 = (String)availableRooms_status.getSelectionModel().getSelectedItem();
        String price1 = availableRooms_price.getText();
        String roomNum = availableRooms_roomNumber.getText();
        
        String deleteData = "DELETE FROM room WHERE roomNumber = '"+roomNum+"'";
        
        connect = database.connectDb();
        try{
            
            Alert alert;
            
            if(type1 == null || status1 == null  || price1 == null || roomNum == null ){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select the data first!");
                alert.showAndWait();
            }else{
                statement = connect.createStatement();
                statement.executeUpdate(deleteData);
                
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete Room # " + roomNum);
                
                Optional<ButtonType> option = alert.showAndWait();
                
                if(option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();
                    statement.executeUpdate(deleteData);
                    
                    alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Deleted");
                alert.showAndWait();
                
                availableRoomsShowData();
                availableRoomsClear();
                
                }else{
                    return;
                }
                
            }
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void availableRoomsClear(){
        availableRooms_roomNumber.setText("");
        availableRooms_roomType.getSelectionModel().clearSelection();
        availableRooms_status.getSelectionModel().clearSelection();
        availableRooms_price.setText("");
    }
    
    public void availableRoomsCheckIn(){
        try{
            
            Parent root = FXMLLoader.load(getClass().getResource("checkIn.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setMinHeight(530+15);
            stage.setMinWidth(450+15);
            
            
                   
                   stage.initStyle(StageStyle.DECORATED);
                   stage.setScene(scene);
                   stage.show();
                   
        }catch(Exception e){e.printStackTrace();}
    }
    
    
    private String type[] = {"Single Room", "Double Room", "Triple Room", "Quad Room", "King Room"};
    
    public void availableRoomsroomType(){
        List <String> listData = new ArrayList<>();
        
        for(String data: type){
            listData.add(data);
        }
        
        ObservableList list = FXCollections.observableArrayList(listData);
        availableRooms_roomType.setItems(list);
    }
    
    private String status[] = {"Available", "Not Available", "Occupied"};
    
    public void availableRoomsstatus(){
        List<String> listData = new ArrayList<>();
        
        for(String data: status){
            listData.add(data);
        }
        
        ObservableList list = FXCollections.observableArrayList(listData);
        availableRooms_status.setItems(list);
    }
    
    public ObservableList<customerData> customerListData(){
        ObservableList<customerData> listData = FXCollections.observableArrayList();
        
        String sql = "SELECT * FROM customer";
        
        connect = database.connectDb();
        
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            customerData custD;
            
            while(result.next()){
                custD = new customerData(result.getInt("customer_id"),result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("phoneNumber"),
                        result.getDouble("total"),
                        result.getDate("checkIn"),
                        result.getDate("checkOut"));
                listData.add(custD);
                        
            }
        }catch(Exception e){e.printStackTrace();}
        
        return listData;
    }
    private ObservableList<customerData> listCustomerData;
    public void customerShowData(){
        listCustomerData = customerListData();
        
        customer_customerNumber.setCellValueFactory(new PropertyValueFactory<>("customerNum"));
        customer_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        customer_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        customer_phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customer_totalPayment.setCellValueFactory(new PropertyValueFactory<>("total"));
        customer_checkedIn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        customer_checkedOut.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
        
        customer_tableView.setItems(listCustomerData);
    }
    
//    public void customerSearch(){
//        FilteredList<customerData> filter = new FilteredList<>(listCustomerData, e -> true);
//        
//        customer_search.textProperty().addListener((observable, oldValue, newValue) ->{
//            filter.setPredicate(predicateCustomer -> {
//                
//                if(newValue == null && newValue.isEmpty()){
//                    return true;
//                }
//                
//                String searchKey = newValue.toLowerCase();
//                
//                if(predicateCustomer.getCustomerNum().toString().contains(searchKey)){
//                    return true;
//                }else if(predicateCustomer.getFirstName().toLowerCase().contains(searchKey)){
//                    return true;
//                }else if(predicateCustomer.getLastName().toLowerCase().contains(searchKey)){
//                    return true;
//                }else if(predicateCustomer.getTotal().toString().contains(searchKey)){
//                    return true;
//                }else if(predicateCustomer.getPhoneNumber().toLowerCase().contains(searchKey)){
//                    return true;
//                }else if(predicateCustomer.getCheckIn.toLowerCase().contains(searchKey)){
//                    return true;
//                }
//                
//                return false;
//            });
//        
//            });
//    }
    
    public void switchForm(ActionEvent event){
        
        if(event.getSource() == dashboard_btn){
            
            dashboard_form.setVisible(true);
            availableRooms_form.setVisible(false);
            customer_form.setVisible(false);
            
            dashboardDisplayBookToday();
            dashboardDisplayIncomeToday();
            dashboardTotalIncome();
            dashboardChart();
            
        }else if (event.getSource() == availableRooms_btn){
            
            dashboard_form.setVisible(false);
            availableRooms_form.setVisible(true);
            customer_form.setVisible(false);
            //availableRoomSearch();
            availableRoomsShowData();
        }else if (event.getSource() == customer_btn){
            
            dashboard_form.setVisible(false);
            availableRooms_form.setVisible(false);
            customer_form.setVisible(true);
            
            customerShowData();
        }
    }
    
    
    private double x = 0;
    private double y = 0;
    
    public void logout(){
        try{
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
                 alert.setHeaderText(null);
                 alert.setContentText("Are You Sure You Want To Logout");
                 alert.showAndWait();
            
            Optional<ButtonType> option = alert.showAndWait();
            
            if(option.get().equals(ButtonType.OK)){
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            
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
                 
                 logout_btn.getScene().getWindow().hide();
                 
            }else{
                return;
            }
        }catch(Exception e){e.printStackTrace();}
    }
    
    
    public void close(){
        System.exit(0);
    }
    
    public void minimize(){
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
     
        availableRoomsroomType();
        availableRoomsstatus();
        availableRoomsShowData();
        customerShowData();
        dashboardDisplayBookToday();
        dashboardDisplayIncomeToday();
        dashboardTotalIncome();
        dashboardChart();
    }
    
}
