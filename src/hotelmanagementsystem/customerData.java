/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementsystem;

import java.sql.Date;

/**
 *
 * @author samee
 */

public class customerData {
    private int customerNum;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Double total;
    private Date checkIn;
    private Date checkOut;
    
    public customerData(int customerNum,String firstName,String lastName,String phoneNumber,Double total,Date checkIn,Date checkOut){
        this.customerNum = customerNum;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.total = total;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
    
    public int getCustomerNum(){
        return customerNum;
    }
    
    public String getFirstName(){
        return firstName;
    }
    
    public String getLastName(){
        return lastName;
    }
    
    public String getPhoneNumber(){
        return phoneNumber;
    }
    
    public Double getTotal(){
        return total;
    }
    
    public Date getCheckIn(){
        return checkIn;
    }
    
     public Date getCheckOut(){
        return checkOut;
    }   
}
