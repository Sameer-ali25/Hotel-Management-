/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementsystem;


public class roomData {
    private int roomNumber;
    private String roomType;
    private String status;
    private double price;
    
    public roomData(int roomNumber,String roomType,String status,double price){
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.status = status;
        this.price = price; 
    }
    
    public int getRoomNumber(){
        return roomNumber;
    }
    public String getRoomType(){
        return roomType;
    }
    
    public String getStatus(){
        return status;
    }
    
    public double getPrice(){
        return price;
    }
    
    
}

