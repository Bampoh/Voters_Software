package com.example.voters_software;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


    public class Database {


        private static final String DB_URL = "jdbc:mysql://localhost:3306/voters_software";
        private static final String DB_USER = "";
        private static final String DB_PASS = "";

        private static Connection connection;

        public void XAMPPDatabaseConnector(){
            try{
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            }catch (SQLException e){

            }
        }


        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

        }
}
