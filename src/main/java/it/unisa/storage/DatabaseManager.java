package it.unisa.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestore centralizzato delle connessioni al database
 * Supporta sia MySQL (produzione) che H2 (test)
 */
public class DatabaseManager {

    // Credenziali MySQL da DatabaseConnection
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3307/fakenewschecker";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASSWORD = "060804FedeFrancy03052011!";

    // Configurazione H2 per i test
    // IMPORTANTE: DB_CLOSE_DELAY=-1 mantiene il DB aperto anche senza connessioni
    private static final String H2_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL;TRACE_LEVEL_SYSTEM_OUT=0";
    private static final String H2_USER = "sa";
    private static final String H2_PASSWORD = "";

    private static boolean isTestMode = false;
    private static Connection testConnection = null;

    /**
     * Attiva la modalità test
     */
    public static void setTestMode(Connection testConn) {
        isTestMode = true;
        testConnection = testConn;
    }

    /**
     * Disattiva la modalità test e torna a produzione
     */
    public static void resetToProduction() {
        isTestMode = false;
        testConnection = null;
    }

    /**
     * Restituisce la connessione appropriata
     * IMPORTANTE: Non chiudere la connessione di test!
     * I DAO la riutilizzano tra i test.
     */
    public static Connection getConnection() throws SQLException {
        if (isTestMode && testConnection != null) {
            return testConnection;
        }
        return getProductionConnection();
    }

    /**
     * Connessione MySQL per la produzione
     */
    private static Connection getProductionConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL non trovato", e);
        }
        return DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
    }

    /**
     * Connessione H2 per i test
     */
    public static Connection getTestConnection() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver H2 non trovato", e);
        }
        return DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWORD);
    }
}