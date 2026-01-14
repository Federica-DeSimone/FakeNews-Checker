package integration.segnalazioni;

import static org.mockito.Mockito.*;
import it.unisa.applicationLogic.SegnalazioneManagement.InviaSegnalazioneServlet;
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

@DisplayName("InviaSegnalazioneServlet Integration Test - TC02")
class InviaSegnalazioneServletIntegrationTest {
    private InviaSegnalazioneServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private UtenteDao utenteDao;
    private Connection testConnection;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        // Crea il database di test
        DatabaseSetupForTest.getConnection();
        System.out.println("=== Database di test inizializzato ===");
    }


    @BeforeEach
    void setUp() throws SQLException {
        servlet = new InviaSegnalazioneServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);
        utenteDao = new UtenteDao();

        testConnection = DatabaseSetupForTest.getConnection();

        // Pulisci il database prima di ogni test
        testConnection.createStatement().execute("DELETE FROM segnalazioni;");
        testConnection.createStatement().execute("DELETE FROM notizie;");
        testConnection.createStatement().execute("DELETE FROM utenti;");

        servlet.init();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (testConnection != null && !testConnection.isClosed()) {
            testConnection.close();
        }
    }

    // Helper method per creare utente nel database
    private Utente createAndSaveUser(String email) throws SQLException {
        Utente utente = new Utente();
        utente.setNome("Test");
        utente.setCognome("User");
        utente.setEmail(email);
        utente.setRuolo("UTENTE");

        // Usa il connection di test per verificare/creare l'utente
        try (Connection conn = DatabaseSetupForTest.getConnection()) {
            // Prima controlla se esiste già
            var rs = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM utenti WHERE email = '" + email + "'"
            );
            rs.next();
            if (rs.getInt(1) == 0) {
                // Se non esiste, crealo
                utenteDao.registraUtente(utente, "password");
            }
        }

        return utente;
    }

    // ===== TC02.4: Segnalazione Effettuata (SUCCESSO) =====
    @Test
    @DisplayName("TC02.4: Segnalazione effettuata")
    void testSegnalazioneTC02_4_Success() throws ServletException, IOException, SQLException {
        // CREA e SALVA utente nel database con la stessa email usata nel test
        String testEmail = "user8@test.com";
        Utente utenteLoggato = createAndSaveUser(testEmail);

        // MOCK: Utente loggato
        when(sessionMock.getAttribute("userEmail")).thenReturn(testEmail);
        when(sessionMock.getAttribute("user")).thenReturn(utenteLoggato);
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        // MOCK: Dati della segnalazione VALIDI
        when(requestMock.getParameter("titolo")).thenReturn("Articolo sospetto");
        when(requestMock.getParameter("descrizione")).thenReturn("Fonti verificate");
        when(requestMock.getParameter("url")).thenReturn("https://articolo-verificato");
        when(requestMock.getParameter("autore")).thenReturn("Test User");
        when(requestMock.getParameter("idNotizia")).thenReturn(null);
        when(requestMock.getPart("foto")).thenReturn(null);
        when(requestMock.getContextPath()).thenReturn("");
        when(requestMock.getRequestDispatcher(anyString())).thenReturn(dispatcherMock);

        // ESEGUI: Invia segnalazione
        servlet.doPost(requestMock, responseMock);

        // VERIFICA: Redirect a pagina successo
        verify(responseMock).sendRedirect(contains("successoSegnalazione.jsp"));

        System.out.println("✓ TC02.4 PASSED: Segnalazione effettuata con successo");
    }

    // ===== TC02.1: Titolo non inserito =====
    @Test
    @DisplayName("TC02.1: Compilare il campo titolo")
    void testSegnalazioneTC02_1_NoTitolo() throws ServletException, IOException, SQLException {
        // CREA e SALVA utente nel database
        String testEmail = "user@test.com";
        Utente utenteLoggato = createAndSaveUser(testEmail);

        // MOCK: Utente loggato
        when(sessionMock.getAttribute("userEmail")).thenReturn(testEmail);
        when(sessionMock.getAttribute("user")).thenReturn(utenteLoggato);
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        // MOCK: Titolo NULL (non inserito)
        when(requestMock.getParameter("titolo")).thenReturn(null);  // ← TITOLO NULL
        when(requestMock.getParameter("descrizione")).thenReturn("Fonti verificate");
        when(requestMock.getParameter("url")).thenReturn("https://articolo-verificato");
        when(requestMock.getParameter("autore")).thenReturn("Test User");
        when(requestMock.getParameter("idNotizia")).thenReturn(null);
        when(requestMock.getPart("foto")).thenReturn(null);
        when(requestMock.getRequestDispatcher("/Interface/SegnalazioneNotiziaGUI/formSegnalazione.jsp"))
                .thenReturn(dispatcherMock);

        // ESEGUI
        servlet.doPost(requestMock, responseMock);

        // VERIFICA: Errore "Compila titolo"
        verify(requestMock).setAttribute(eq("errore"), contains("Compila tutti i campi"));
        verify(dispatcherMock).forward(requestMock, responseMock);

        System.out.println("✓ TC02.1 PASSED: Titolo obbligatorio");
    }

    // ===== TC02.2: URL non valido =====
    @Test
    @DisplayName("TC02.2: Inserire URL valido")
    void testSegnalazioneTC02_2_NoURL() throws ServletException, IOException, SQLException {
        // CREA e SALVA utente nel database
        String testEmail = "user3@test.com";
        Utente utenteLoggato = createAndSaveUser(testEmail);

        // MOCK: Utente loggato
        when(sessionMock.getAttribute("userEmail")).thenReturn(testEmail);
        when(sessionMock.getAttribute("user")).thenReturn(utenteLoggato);
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        // MOCK: URL NULL
        when(requestMock.getParameter("titolo")).thenReturn("Articolo sospetto");
        when(requestMock.getParameter("descrizione")).thenReturn("Fonti verificate");
        when(requestMock.getParameter("url")).thenReturn(null);  // ← URL NULL
        when(requestMock.getParameter("autore")).thenReturn("Test User");
        when(requestMock.getParameter("idNotizia")).thenReturn(null);
        when(requestMock.getPart("foto")).thenReturn(null);
        when(requestMock.getRequestDispatcher("/Interface/SegnalazioneNotiziaGUI/formSegnalazione.jsp"))
                .thenReturn(dispatcherMock);

        // ESEGUI
        servlet.doPost(requestMock, responseMock);

        // VERIFICA
        verify(requestMock).setAttribute(eq("errore"), contains("Compila tutti i campi"));
        verify(dispatcherMock).forward(requestMock, responseMock);

        System.out.println("✓ TC02.2 PASSED: URL obbligatorio");
    }

    // ===== TC02.3: Descrizione non inserita =====
    @Test
    @DisplayName("TC02.3: Compilare il campo descrizione")
    void testSegnalazioneTC02_3_NoDescrizione() throws ServletException, IOException, SQLException {
        // CREA e SALVA utente nel database
        String testEmail = "user4@test.com";
        Utente utenteLoggato = createAndSaveUser(testEmail);

        // MOCK: Utente loggato
        when(sessionMock.getAttribute("userEmail")).thenReturn(testEmail);
        when(sessionMock.getAttribute("user")).thenReturn(utenteLoggato);
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        // MOCK: Descrizione NULL
        when(requestMock.getParameter("titolo")).thenReturn("Articolo sospetto");
        when(requestMock.getParameter("descrizione")).thenReturn(null);  // ← DESCRIZIONE NULL
        when(requestMock.getParameter("url")).thenReturn("https://articolo-verificato");
        when(requestMock.getParameter("autore")).thenReturn("Test User");
        when(requestMock.getParameter("idNotizia")).thenReturn(null);
        when(requestMock.getPart("foto")).thenReturn(null);
        when(requestMock.getRequestDispatcher("/Interface/SegnalazioneNotiziaGUI/formSegnalazione.jsp"))
                .thenReturn(dispatcherMock);

        // ESEGUI
        servlet.doPost(requestMock, responseMock);

        // VERIFICA
        verify(requestMock).setAttribute(eq("errore"), contains("Compila tutti i campi"));
        verify(dispatcherMock).forward(requestMock, responseMock);

        System.out.println("✓ TC02.3 PASSED: Descrizione obbligatoria");
    }

    // ===== Test: Utente non autenticato =====
    @Test
    @DisplayName("Segnalazione: Utente non autenticato")
    void testSegnalazioneUnauthorized() throws ServletException, IOException {
        // MOCK: Nessuna sessione (non loggato)
        when(requestMock.getSession(false)).thenReturn(null);
        when(requestMock.getContextPath()).thenReturn("");

        // ESEGUI
        servlet.doPost(requestMock, responseMock);

        // VERIFICA: Redirect a login
        verify(responseMock).sendRedirect(contains("/Interface/AutenticazioneGUI/Login.jsp"));
    }

    // ===== Test: GET (Mostra form) =====
    @Test
    @DisplayName("Segnalazione: GET mostra il form")
    void testSegnalazioneGetShowsForm() throws ServletException, IOException, SQLException {
        // CREA e SALVA utente nel database
        String testEmail = "user5@test.com";
        Utente utenteLoggato = createAndSaveUser(testEmail);

        // MOCK: Utente loggato
        when(sessionMock.getAttribute("userEmail")).thenReturn(testEmail);
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(requestMock.getRequestDispatcher("/Interface/SegnalazioneNotiziaGUI/formSegnalazione.jsp"))
                .thenReturn(dispatcherMock);

        // ESEGUI
        servlet.doGet(requestMock, responseMock);

        // VERIFICA
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("✓ Form segnalazione mostrato");
    }
}