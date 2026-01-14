package unit.test_DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetupForTest {


    private static final String DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private static Connection connection;
    private static boolean schemaCreated = false;

    /**
     * Ottieni connessione al database di test
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.h2.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                // Crea schema solo la prima volta
                if (!schemaCreated) {
                    createSchema();
                    schemaCreated = true;
                }
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver H2 non trovato", e);
            }
        }
        return connection;
    }

    /**
     * Crea lo schema del database uguale a quello di produzione
     */
    private static void createSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {

            // ===== TABELLA UTENTI =====
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS utenti (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "nome VARCHAR(100) NOT NULL, " +
                            "cognome VARCHAR(100) NOT NULL, " +
                            "email VARCHAR(255) NOT NULL UNIQUE, " +
                            "password_hash VARCHAR(255) NOT NULL, " +
                            "telefono VARCHAR(20), " +
                            "ruolo VARCHAR(50) DEFAULT 'UTENTE', " +
                            "data_registrazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                            ")"
            );

            // ===== TABELLA GESTORI =====
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS gestori (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "nome VARCHAR(100) NOT NULL, " +
                            "cognome VARCHAR(100) NOT NULL, " +
                            "email VARCHAR(255) NOT NULL UNIQUE, " +
                            "password_hash VARCHAR(255) NOT NULL, " +
                            "telefono VARCHAR(20), " +
                            "ruolo VARCHAR(50) NOT NULL, " +
                            "creato_da INT, " +
                            "data_registrazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "FOREIGN KEY (creato_da) REFERENCES gestori(id) ON DELETE SET NULL" +
                            ")"
            );

            // ===== TABELLA NOTIZIE =====
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS notizie (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "titolo VARCHAR(255) NOT NULL, " +
                            "descrizione TEXT NOT NULL, " +
                            "immagine VARCHAR(255), " +
                            "autore VARCHAR(255), " +
                            "stato VARCHAR(50) DEFAULT 'segnalata', " +
                            "data_pubblicazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                            ")"
            );

            // ===== TABELLA SEGNALAZIONI =====
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS segnalazioni (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "numero_segnalazione VARCHAR(50) UNIQUE, " +
                            "titolo VARCHAR(255) NOT NULL, " +
                            "descrizione TEXT NOT NULL, " +
                            "url VARCHAR(500) NOT NULL, " +
                            "autore VARCHAR(255), " +
                            "immagine_path VARCHAR(500), " +
                            "stato VARCHAR(50) DEFAULT 'in_verifica', " +
                            "data_segnalazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "id_utente INT, " +
                            "id_notizia INT, " +
                            "id_gestore_verificatore INT, " +
                            "data_verifica TIMESTAMP, " +
                            "FOREIGN KEY (id_utente) REFERENCES utenti(id) ON DELETE SET NULL, " +
                            "FOREIGN KEY (id_notizia) REFERENCES notizie(id) ON DELETE SET NULL, " +
                            "FOREIGN KEY (id_gestore_verificatore) REFERENCES gestori(id) ON DELETE SET NULL" +
                            ")"
            );

            System.out.println("✓ Schema database di test creato con successo");
        }
    }

    /**
     * Pulisce TUTTI i dati dalle tabelle (rispettando foreign keys)
     */
    public static void cleanDatabase() throws SQLException {
        try (Statement stmt = getConnection().createStatement()) {
            // IMPORTANTE: Ordine inverso per rispettare le foreign key
            stmt.execute("SET REFERENTIAL_INTEGRITY FALSE"); // Disabilita temporaneamente
            stmt.execute("TRUNCATE TABLE segnalazioni");
            stmt.execute("TRUNCATE TABLE notizie");
            stmt.execute("TRUNCATE TABLE gestori");
            stmt.execute("TRUNCATE TABLE utenti");
            stmt.execute("SET REFERENTIAL_INTEGRITY TRUE"); // Riabilita

            System.out.println("✓ Database pulito");
        }
    }

    /**
     * Chiude la connessione (chiamato alla fine di tutti i test)
     */
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
            schemaCreated = false;
            System.out.println("✓ Connessione database di test chiusa");
        }
    }

    /**
     * Reset completo del database (schema + dati)
     */
    public static void resetDatabase() throws SQLException {
        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute("DROP ALL OBJECTS"); // Elimina tutto
            schemaCreated = false;
            createSchema(); // Ricrea lo schema
            System.out.println("✓ Database completamente resettato");
        }
    }
}