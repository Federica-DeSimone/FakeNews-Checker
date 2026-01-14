package unit.test_DAO;

import static org.junit.jupiter.api.Assertions.*;
import it.unisa.storage.UtenteDao;
import it.unisa.storage.Utente;
import it.unisa.utils.PasswordUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class UtenteDAOTest {
    private static final Logger logger = Logger.getLogger(UtenteDAOTest.class.getName());
    private UtenteDao utenteDao;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        DatabaseSetupForTest.getConnection();
    }

    @BeforeEach
    void setUp() {
        utenteDao = new UtenteDao();
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
    void testCreateUser() {
        Utente utente = new Utente();
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        utente.setEmail("mario.rossi@test.com");
        utente.setTelefono("1234567890");
        utente.setPasswordHash("hashedpassword123");
        utente.setRuolo("UTENTE");

        boolean result = utenteDao.createUser(utente);
        assertTrue(result, "Creazione utente fallita");

        System.out.println("Utente creato: Email=" + utente.getEmail());
    }

    @Test
    void testGetUserByEmail() {
        Utente utente = new Utente();
        utente.setNome("Luigi");
        utente.setCognome("Verdi");
        utente.setEmail("luigi.verdi@test.com");
        utente.setTelefono("0987654321");
        utente.setPasswordHash("hashedpassword456");
        utente.setRuolo("UTENTE");

        utenteDao.createUser(utente);

        Utente retrieved = utenteDao.getUserByEmail("luigi.verdi@test.com");

        assertNotNull(retrieved, "Utente non trovato");
        assertEquals("Luigi", retrieved.getNome(), "Nome non corrispondente");
        assertEquals("Verdi", retrieved.getCognome(), "Cognome non corrispondente");
        assertEquals("luigi.verdi@test.com", retrieved.getEmail(), "Email non corrispondente");

        System.out.println("Utente recuperato: Email=" + retrieved.getEmail());
    }

    @Test
    void testEmailExists() {
        Utente utente = new Utente();
        utente.setNome("Giovanni");
        utente.setCognome("Bianchi");
        utente.setEmail("giovanni.bianchi@test.com");
        utente.setTelefono("555123456");
        utente.setPasswordHash("hashedpassword789");
        utente.setRuolo("UTENTE");

        utenteDao.createUser(utente);

        boolean exists = utenteDao.emailExists("giovanni.bianchi@test.com");
        assertTrue(exists, "Email dovrebbe esistere");

        boolean notExists = utenteDao.emailExists("inesistente@test.com");
        assertFalse(notExists, "Email non dovrebbe esistere");

        System.out.println("Verifica email: Email=" + utente.getEmail() + " esiste");
    }

    @Test
    void testAuthenticateUser() {
        Utente utente = new Utente();
        utente.setNome("Anna");
        utente.setCognome("Neri");
        utente.setEmail("anna.neri@test.com");
        utente.setTelefono("321654987");

        String password = "password123";
        String hashedPassword = PasswordUtils.hashPassword(password);
        utente.setPasswordHash(hashedPassword);
        utente.setRuolo("UTENTE");

        boolean creato = utenteDao.createUser(utente);
        assertTrue(creato, "Creazione utente per autenticazione fallita");

        Utente autenticato = utenteDao.authenticateUser("anna.neri@test.com", password);
        assertNotNull(autenticato, "Autenticazione fallita");
        assertEquals("Anna", autenticato.getNome(), "Nome non corrispondente");

        System.out.println("Utente autenticato: Email=" + utente.getEmail() + ", Password=" + password);
    }

    @Test
    void testAuthenticateUserWrongPassword() {
        Utente utente = new Utente();
        utente.setNome("Marco");
        utente.setCognome("Gialli");
        utente.setEmail("marco.gialli@test.com");
        utente.setTelefono("555444333");

        String password = "password123";
        String hashedPassword = PasswordUtils.hashPassword(password);
        utente.setPasswordHash(hashedPassword);
        utente.setRuolo("UTENTE");

        utenteDao.createUser(utente);

        String wrongPassword = "passwordSbagliata";
        Utente autenticato = utenteDao.authenticateUser("marco.gialli@test.com", wrongPassword);
        assertNull(autenticato, "Autenticazione con password errata dovrebbe fallire");

        System.out.println("Autenticazione con password errata: Email=" + utente.getEmail() + ", Password=" + wrongPassword);
    }

    @Test
    void testRegistraUtente() {
        Utente utente = new Utente();
        utente.setNome("Paolo");
        utente.setCognome("Blu");
        utente.setEmail("paolo.blu@test.com");
        utente.setTelefono("111222333");

        String password = "password456";

        boolean registrato = utenteDao.registraUtente(utente, password);
        assertTrue(registrato, "Registrazione utente fallita");
        assertTrue(utente.getId() > 0, "ID non assegnato");

        Utente recuperato = utenteDao.getUserByEmail("paolo.blu@test.com");
        assertNotNull(recuperato, "Utente registrato non trovato");
        assertEquals("Paolo", recuperato.getNome(), "Nome non corrispondente");

        System.out.println("Registrazione completata: Email=" + utente.getEmail());
    }
}