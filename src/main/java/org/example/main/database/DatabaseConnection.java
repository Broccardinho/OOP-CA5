package org.example.main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/F1_2024"; // Updated database name
    private static final String USER = "root"; // Default MySQL username
    private static final String PASSWORD = ""; // Default MySQL password (empty if no password is set)


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

