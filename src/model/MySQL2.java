/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import com.mysql.cj.jdbc.PreparedStatementWrapper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class MySQL2 {

    private static Connection connection;

    public static void createConnection() throws Exception {

        if (connection == null) {

            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myshopapp", "root", "shivoni@19");
        }

    }

    public static ResultSet executeSearch(String query) throws Exception {

        createConnection();

        return connection.createStatement().executeQuery(query);

    }

    public static Integer executeIUD(String query) throws Exception {

        createConnection();

        return connection.createStatement().executeUpdate(query);

    }
    
    
    public static Integer executePrepIUD(String query,List<Object> parameters) throws Exception{
        
        createConnection();
        
        PreparedStatement pstmt = connection.prepareStatement(query);
        
        for(int i =0; i<parameters.size();i++){
            pstmt.setObject(i +1, parameters.get(i));
        }
        
        return pstmt.executeUpdate();
        
    }

  

}
