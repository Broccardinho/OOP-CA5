package org.example.database;
import org.example.Exceptions.DaoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws DaoException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/f1_2024";
        String username = "root";
        String password = "";
        Connection connection = null;

        try {
            Class.forName(driver);
            System.out.println("MySQL JDBC Driver loaded successfully.");

            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection established successfully.");
        } catch (ClassNotFoundException e) {
            throw new DaoException("Failed to find driver class: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new DaoException("Connection failed: " + e.getMessage(), e);
        }
        return connection;
    }

    public void freeConnection(Connection connection) throws DaoException {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connection closed successfully.");
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to free connection: " + e.getMessage(), e);
        }
    }
}