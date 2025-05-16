package com.mycompany.kvk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlserver://172.16.16.60:1433;"
                                   + "databaseName=ION_Data;"
                                   + "encrypt=true;"
                                   + "trustServerCertificate=true";
    private static final String USER = "kvk";
    private static final String PASSWORD = "KvK-DataAccess1";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database successfully!");
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
        }
        return connection;
    }
}
