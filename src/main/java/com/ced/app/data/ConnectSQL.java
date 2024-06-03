/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ced.app.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectSQL {
    
    //Connect to Oracle,PostgreSQL,MySQL
    //dataInstance = "oracle" or "postgres" or "mysql"
    public static Connection getConnection(String dataInstance, String database, String username, String password) throws ClassNotFoundException, SQLException{
        Connection connect = null;
        
        String ORACLE = "oracle.jdbc.driver.OracleDriver";
        String POSTGRE = "org.postgresql.Driver";
        String MYSQL = "com.mysql.jdbc.Driver";
        
        if(dataInstance.toLowerCase().equals("postgres")){
            try {
                Class.forName(POSTGRE);
                connect = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, username, password);
            } catch (SQLException se) {
                throw se;
            }   
        }
        
        if(dataInstance.toLowerCase().equals("oracle")){
            try{
	        Class.forName(ORACLE);
		connect = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522/"+database,username,password);
            }
	    catch(SQLException se){
		throw se;
	    }
        }
        
        if(dataInstance.toLowerCase().equals("mysql")){
            try{
	        Class.forName(MYSQL);
		    connect = DriverManager.getConnection("jdbc:mysql://localhost/"+database,username,password);
            }
	    catch(SQLException se){
		throw se;
	    }        
        }
        
        return connect;
    }
    
}
