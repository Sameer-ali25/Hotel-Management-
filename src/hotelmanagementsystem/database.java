/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;


public class database {
        public static Connection connectDb() {
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/hoteldata","root","");
            return connect;
        }catch(Exception e) {e.printStackTrace();}
        return null;
    }
        
                
    }

