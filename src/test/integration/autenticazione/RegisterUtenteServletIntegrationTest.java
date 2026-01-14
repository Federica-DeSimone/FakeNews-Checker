package integration.autenticazione;

import static org.mockito.Mockito.*;
import it.unisa.applicationLogic.AutenticazioneManagement.RegisterUtenteServlet;
import it.unisa.storage.Utente;
import it.unisa.storage.UtenteDao;
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

@DisplayName("RegisterUtenteServlet Integration Test - TC05")
class RegisterUtenteServletIntegrationTest {
    private RegisterUtenteServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private UtenteDao utenteDao;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        // Crea il database di test
        DatabaseSetupForTest.getConnection();
        System.out.println("=== Database di test inizializzato ===");
    }


    @BeforeEach
    void setUp() {
        servlet = new RegisterUtenteServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);
        utenteDao = new UtenteDao();

        try (Connection conn = DatabaseSetupForTest.getConnection()) {
            conn.createStatement().execute("DELETE FROM segnalazioni;");
            conn.createStatement().execute("DELETE FROM utenti;");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            servlet.init();
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        try (Connection conn = DatabaseSetupForTest.getConnection()) {
            conn.createStatement().execute("DELETE FROM segnalazioni;");
            conn.createStatement().execute("DELETE FROM utenti;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ===== TC05.10: Registrazione Riuscita =====
    @Test
    @DisplayName("TC05.10: Registrazione effettuata - utente registrato e autenticato")
    void testRegisterTC05_10_Success() throws ServletException, IOException {
        // MOCK HTTP Request con DATI VALIDI
        when(requestMock.getParameter("nome")).thenReturn("Andrea");
        when(requestMock.getParameter("cognome")).thenReturn("Verdi");
        when(requestMock.getParameter("email")).thenReturn("test@example.com");
        when(requestMock.getParameter("password")).thenReturn("ValidPassword123!");
        when(requestMock.getParameter("telefono")).thenReturn("1234567890");
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(requestMock.getContextPath()).thenReturn("");

        // ESEGUI: Chiama doPost
        servlet.doPost(requestMock, responseMock);

        // VERIFICA: Sessione impostata (login automatico dopo registrazione)
        verify(sessionMock).setAttribute(eq("user"), any(Utente.class));
        verify(sessionMock).setAttribute(eq("userType"), eq("UTENTE"));
        verify(sessionMock).setAttribute(eq("userEmail"), eq("test@example.com"));

        // VERIFICA: Redirect a Home
        verify(responseMock).sendRedirect(contains("/Interface/Home.jsp"));

        System.out.println("✓ TC05.10 PASSED: Registrazione effettuata e utente autenticato");
    }

    // ===== TC05.2: Email già registrata =====
    @Test
    @DisplayName("TC05.2: Email già registrata")
    void testRegisterTC05_2_EmailExists() throws ServletException, IOException {
        // SETUP: Registra primo utente
        Utente utente = new Utente();
        utente.setNome("Andrea");
        utente.setCognome("Verdi");
        utente.setEmail("andreaverdi@gmail.com");
        utente.setRuolo("UTENTE");
        utenteDao.registraUtente(utente, "Password123!");

        // MOCK: Tenta di registrarsi con STESSA email
        when(requestMock.getParameter("nome")).thenReturn("Luigi");
        when(requestMock.getParameter("cognome")).thenReturn("Rossi");
        when(requestMock.getParameter("email")).thenReturn("andreaverdi@gmail.com");
        when(requestMock.getParameter("password")).thenReturn("Password123!");
        when(requestMock.getParameter("telefono")).thenReturn("1234567890");
        when(requestMock.getRequestDispatcher("/Interface/AutenticazioneGUI/Register.jsp"))
                .thenReturn(dispatcherMock);

        // ESEGUI
        servlet.doPost(requestMock, responseMock);

        // VERIFICA: Errore email duplicata
        verify(requestMock).setAttribute(eq("errore"), contains("Email già registrata"));
        verify(dispatcherMock).forward(requestMock, responseMock);

        System.out.println("✓ TC05.2 PASSED: Email duplicata rigettata");
    }

    // ===== TC05.3: Email non inserita =====
    @Test
    @DisplayName("TC05.3: Email non inserita")
    void testRegisterTC05_3_NoEmail() throws ServletException, IOException {
        // MOCK: Email NULL (non inserita)
        when(requestMock.getParameter("nome")).thenReturn("Andrea");
        when(requestMock.getParameter("cognome")).thenReturn("Verdi");
        when(requestMock.getParameter("email")).thenReturn(null);  // ← EMAIL NULL
        when(requestMock.getParameter("password")).thenReturn("Password123!");
        when(requestMock.getParameter("telefono")).thenReturn("1234567890");
        when(requestMock.getRequestDispatcher("/Interface/AutenticazioneGUI/Register.jsp"))
                .thenReturn(dispatcherMock);

        // ESEGUI
        servlet.doPost(requestMock, responseMock);

        // VERIFICA: Errore campi obbligatori
        verify(requestMock).setAttribute(eq("errore"), anyString());
        verify(dispatcherMock).forward(requestMock, responseMock);

        System.out.println("✓ TC05.3 PASSED: Email obbligatorio");
    }

    // ===== Test: Nome vuoto =====
    @Test

    void testRegisterEmptyName() throws ServletException, IOException {
        when(requestMock.getParameter("nome")).thenReturn(null);
        when(requestMock.getParameter("cognome")).thenReturn("Verdi");
        when(requestMock.getParameter("email")).thenReturn("test@test.com");
        when(requestMock.getParameter("password")).thenReturn("Pass123!");
        when(requestMock.getParameter("telefono")).thenReturn("123");
        when(requestMock.getRequestDispatcher("/Interface/AutenticazioneGUI/Register.jsp"))
                .thenReturn(dispatcherMock);

        servlet.doPost(requestMock, responseMock);

        verify(requestMock).setAttribute(eq("errore"), anyString());
    }

    // ===== Test: GET (Mostra form) =====
    @Test
    @DisplayName("Registrazione: GET mostra il form")
    void testRegisterGetShowsForm() throws ServletException, IOException {
        when(requestMock.getRequestDispatcher("/Interface/AutenticazioneGUI/Register.jsp"))
                .thenReturn(dispatcherMock);

        servlet.doGet(requestMock, responseMock);

        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("✓ Form registrazione mostrato");
    }
}