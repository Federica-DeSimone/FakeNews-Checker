package unit.test_DAO;

import static org.junit.jupiter.api.Assertions.*;
import it.unisa.storage.GestoreDAO;
import it.unisa.storage.Gestore;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

public class GestoreDAOTest {
    private GestoreDAO gestoreDAO;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        DatabaseSetupForTest.getConnection();
        System.out.println("=== Database di test inizializzato ===");
    }

    @BeforeEach
    void setUp() {
        gestoreDAO = new GestoreDAO();
        try (Connection conn = DatabaseSetupForTest.getConnection()) {
            conn.createStatement().execute("DELETE FROM segnalazioni;");
            conn.createStatement().execute("DELETE FROM notizie;");
            conn.createStatement().execute("DELETE FROM gestori;");
            conn.createStatement().execute("DELETE FROM utenti;");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test

    void testLogin() throws SQLException {
        Gestore gestore = new Gestore();
        gestore.setNome("Admin");
        gestore.setCognome("Test");
        gestore.setEmail("admin@test.com");
        gestore.setTelefono("123456789");
        gestore.setRuolo(Gestore.TipoGestore.GESTORE_VERIFICHE);

        gestoreDAO.registraGestore(gestore, "hashedpassword123", null);
        System.out.println("Gestore registrato: Email=" + gestore.getEmail());

        Gestore loggedIn = gestoreDAO.login("admin@test.com", "hashedpassword123");
        assertNotNull(loggedIn, "Login fallito");
        assertEquals("Admin", loggedIn.getNome(), "Nome non corrispondente");
        assertEquals(Gestore.TipoGestore.GESTORE_VERIFICHE, loggedIn.getRuolo(), "Ruolo non corrispondente");

        System.out.println("Login riuscito: Email=" + loggedIn.getEmail());
    }

    @Test

    void testRegistraGestore() throws SQLException {
        Gestore gestore = new Gestore();
        gestore.setNome("Tecnico");
        gestore.setCognome("Test");
        gestore.setEmail("tecnico@test.com");
        gestore.setTelefono("987654321");
        gestore.setRuolo(Gestore.TipoGestore.GESTORE_TECNICO);

        boolean result = gestoreDAO.registraGestore(gestore, "hashedpassword456", null);
        assertTrue(result, "Registrazione gestore fallita");
        assertTrue(gestore.getId() > 0, "ID non assegnato");

        System.out.println("Gestore registrato: Email=" + gestore.getEmail() + ", ID=" + gestore.getId());
    }

    @Test

    void testGetGestoreById() throws SQLException {
        Gestore gestore = new Gestore();
        gestore.setNome("Manager");
        gestore.setCognome("Test");
        gestore.setEmail("manager@test.com");
        gestore.setTelefono("555555555");
        gestore.setRuolo(Gestore.TipoGestore.GESTORE_VERIFICHE);

        gestoreDAO.registraGestore(gestore, "hashedpassword789", null);
        int id = gestore.getId();
        System.out.println("Gestore registrato: Email=" + gestore.getEmail() + ", ID=" + id);

        Gestore retrieved = gestoreDAO.getGestoreById(id);
        assertNotNull(retrieved, "Gestore non trovato");
        assertEquals("Manager", retrieved.getNome(), "Nome non corrispondente");
        assertEquals(id, retrieved.getId(), "ID non corrispondente");

        System.out.println("Gestore recuperato per ID: " + id);
    }

    @Test

    void testEmailEsiste() throws SQLException {
        Gestore gestore = new Gestore();
        gestore.setNome("Test");
        gestore.setCognome("Email");
        gestore.setEmail("test.email@test.com");
        gestore.setTelefono("111111111");
        gestore.setRuolo(Gestore.TipoGestore.GESTORE_TECNICO);

        gestoreDAO.registraGestore(gestore, "hashedpassword111", null);
        System.out.println("Gestore registrato per verifica email: Email=" + gestore.getEmail());

        boolean exists = gestoreDAO.emailEsiste("test.email@test.com");
        assertTrue(exists, "Email dovrebbe esistere");

        boolean notExists = gestoreDAO.emailEsiste("inesistente@test.com");
        assertFalse(notExists, "Email non dovrebbe esistere");

        System.out.println("Verifica email completata: Email esistente=" + exists + ", Email inesistente=" + notExists);
    }
}
