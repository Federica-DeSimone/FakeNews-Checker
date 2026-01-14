package it.unisa.storage;



import java.sql.Connection;





import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3307/fakenewschecker";
    private static final String USER = "root";
    private static final String PASSWORD = "060804FedeFrancy03052011!";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL non trovato", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
