package integration.autenticazione;

import static org.mockito.Mockito.*;

import it.unisa.applicationLogic.AutenticazioneManagement.LoginServlet;
import it.unisa.storage.Gestore;
import it.unisa.storage.GestoreDAO;
import it.unisa.storage.Utente;
import it.unisa.storage.UtenteDao;
import it.unisa.utils.PasswordUtils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.*;

import unit.test_DAO.DatabaseSetupForTest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Integration Test – LoginServlet
 * Testa l'autenticazione di utenti e gestori secondo la specifica TC01
 */
@DisplayName("LoginServlet Integration Test")
class LoginServletIntegrationTest {

    private LoginServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;

    private UtenteDao utenteDao;
    private GestoreDAO gestoreDAO;

    /* ===================== DATABASE ===================== */

    @BeforeAll
    static void setupDatabase() throws SQLException {
        DatabaseSetupForTest.getConnection();
        System.out.println("✓ Database di test inizializzato");
    }

    private void cleanDatabase() {
        try (Connection conn = DatabaseSetupForTest.getConnection()) {
            conn.setAutoCommit(false);

            conn.createStatement().execute("DELETE FROM segnalazioni");
            conn.createStatement().execute("DELETE FROM utenti");
            conn.createStatement().execute("DELETE FROM gestori");

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Errore pulizia database di test", e);
        }
    }

    private String uniqueEmail(String prefix) {
        return prefix + "_" + System.nanoTime() + "@test.com";
    }

    /* ===================== SETUP ===================== */

    @BeforeEach
    void setUp() {
        cleanDatabase();

        servlet = new LoginServlet();

        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);

        utenteDao = new UtenteDao();
        gestoreDAO = new GestoreDAO();

        try {
            servlet.init();
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }

    /* ===================== TC01: LOGIN UTENTE ===================== */

    @Nested

    class TestLoginUtente {

        @Test
        @DisplayName("TC01.1: Login riuscito")
        void testLoginUserSuccess() throws ServletException, IOException, SQLException {

            String email = uniqueEmail("user");

            Utente utente = new Utente();
            utente.setNome("Test");
            utente.setCognome("User");
            utente.setEmail(email);
            utente.setRuolo("UTENTE");

            utenteDao.registraUtente(
                    utente,
                    PasswordUtils.hashPassword("Password123!")
            );
            System.out.println("Utente di test: Email=" + email + ", Password=Testing11");

            when(requestMock.getParameter("email")).thenReturn(email);
            when(requestMock.getParameter("password")).thenReturn("Password123!");
            when(requestMock.getSession()).thenReturn(sessionMock);
            when(requestMock.getContextPath()).thenReturn("");

            servlet.doPost(requestMock, responseMock);

            verify(sessionMock).setAttribute(eq("user"), any(Utente.class));
            verify(sessionMock).setAttribute("userEmail", email);
            verify(sessionMock).setAttribute("userType", "UTENTE");

            verify(responseMock).sendRedirect(contains("/Interface/Home.jsp"));
        }

        @Test
        @DisplayName("TC01.3: Login fallito - password errata")
        void testLoginUserWrongPassword() throws ServletException, IOException, SQLException {

            String email = uniqueEmail("user");

            Utente utente = new Utente();
            utente.setNome("Test");
            utente.setCognome("User");
            utente.setEmail(email);
            utente.setRuolo("UTENTE");

            utenteDao.registraUtente(
                    utente,
                    PasswordUtils.hashPassword("Password123!")
            );
            System.out.println("Utente di test: Email=" + email + ", Password:Errata=12345678");
            when(requestMock.getParameter("email")).thenReturn(email);
            when(requestMock.getParameter("password")).thenReturn("WrongPassword");
            when(requestMock.getContextPath()).thenReturn("");

            servlet.doPost(requestMock, responseMock);

            verify(responseMock).sendRedirect(contains("LoginFailed.jsp"));
        }

        @Test

        void testLoginGetShowsForm() throws ServletException, IOException {

            when(requestMock.getRequestDispatcher("/Interface/AutenticazioneGUI/Login.jsp"))
                    .thenReturn(dispatcherMock);

            servlet.doGet(requestMock, responseMock);

            verify(dispatcherMock).forward(requestMock, responseMock);
        }
    }

    /* ===================== TC02: LOGIN GESTORE ===================== */

    @Nested

    class TestLoginGestore {

        @Test
        @DisplayName("Login Gestore Verifiche: Success")
        void testLoginGestoreVerificheSuccess() throws Exception {

            String email = uniqueEmail("gestore_verifiche");

            Gestore gestore = new Gestore();
            gestore.setNome("Mario");
            gestore.setCognome("Rossi");
            gestore.setEmail(email);
            gestore.setTelefono("123");
            gestore.setRuolo(Gestore.TipoGestore.GESTORE_VERIFICHE);

            gestoreDAO.registraGestore(
                    gestore,
                    PasswordUtils.hashPassword("Password123!"),
                    null
            );

            when(requestMock.getParameter("email")).thenReturn(email);
            when(requestMock.getParameter("password")).thenReturn("Password123!");
            when(requestMock.getSession()).thenReturn(sessionMock);
            when(requestMock.getContextPath()).thenReturn("");

            servlet.doPost(requestMock, responseMock);

            verify(sessionMock).setAttribute("userType", "GESTORE");
            verify(sessionMock).setAttribute("gestoreRuolo", "GESTORE_VERIFICHE");
            verify(responseMock).sendRedirect(contains("gestoreVerifiche/dashboard"));
            System.out.println("✓ Login Gestore Verifiche effettuato con successo");
        }

        @Test
        @DisplayName("Login Gestore Tecnico: Success")
        void testLoginGestoreTecnicoSuccess() throws Exception {

            String email = uniqueEmail("gestore_tecnico");

            Gestore gestore = new Gestore();
            gestore.setNome("Tecnico");
            gestore.setCognome("Admin");
            gestore.setEmail(email);
            gestore.setTelefono("456");
            gestore.setRuolo(Gestore.TipoGestore.GESTORE_TECNICO);

            gestoreDAO.registraGestore(
                    gestore,
                    PasswordUtils.hashPassword("Password123!"),
                    null
            );

            when(requestMock.getParameter("email")).thenReturn(email);
            when(requestMock.getParameter("password")).thenReturn("Password123!");
            when(requestMock.getSession()).thenReturn(sessionMock);
            when(requestMock.getContextPath()).thenReturn("");

            servlet.doPost(requestMock, responseMock);

            verify(sessionMock).setAttribute("userType", "GESTORE");
            verify(sessionMock).setAttribute("gestoreRuolo", "GESTORE_TECNICO");
            verify(responseMock).sendRedirect(contains("gestoreTecnico/dashboard"));
            System.out.println("✓ Login Gestore Tecnico effettuato con successo");
        }
    }

    /* ===================== EDGE CASES ===================== */





    @Test
    @DisplayName("Login: Email vuota")
    void testLoginEmptyEmail() throws ServletException, IOException {

        when(requestMock.getParameter("email")).thenReturn("");
        when(requestMock.getParameter("password")).thenReturn("Password123!");
        when(requestMock.getContextPath()).thenReturn("");

        servlet.doPost(requestMock, responseMock);

        verify(responseMock).sendRedirect(contains("LoginFailed.jsp"));
    }

    @Test

    void testLoginUnregisteredEmail() throws ServletException, IOException {

        when(requestMock.getParameter("email")).thenReturn("nonexistent@test.com");
        when(requestMock.getParameter("password")).thenReturn("Password123!");
        when(requestMock.getContextPath()).thenReturn("");

        servlet.doPost(requestMock, responseMock);

        verify(responseMock).sendRedirect(contains("LoginFailed.jsp"));
        System.out.println("✓ Login con email vuota gestito correttamente");
    }

    @Test
    @DisplayName("Login: Password vuota")
    void testLoginEmptyPassword() throws ServletException, IOException, SQLException {

        String email = uniqueEmail("user");

        Utente utente = new Utente();
        utente.setNome("Test");
        utente.setCognome("User");
        utente.setEmail(email);
        utente.setRuolo("UTENTE");

        utenteDao.registraUtente(
                utente,
                PasswordUtils.hashPassword("Password123!")
        );

        when(requestMock.getParameter("email")).thenReturn(email);
        when(requestMock.getParameter("password")).thenReturn("");
        when(requestMock.getContextPath()).thenReturn("");

        servlet.doPost(requestMock, responseMock);

        verify(responseMock).sendRedirect(contains("LoginFailed.jsp"));
        System.out.println("✓ Login con password vuota gestito correttamente");
    }
}
