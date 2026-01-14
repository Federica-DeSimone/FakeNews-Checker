package integration.segnalazioni;

import static org.mockito.Mockito.*;
import it.unisa.applicationLogic.SegnalazioneManagement.StoricoSegnalazioniServlet;
import it.unisa.storage.Segnalazione;
import it.unisa.storage.SegnalazioneDAO;
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

@DisplayName("StoricoSegnalazioniServlet Integration Test")
class StoricoSegnalazioniServletIntegrationTest {
    private StoricoSegnalazioniServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private SegnalazioneDAO segnalazioneDAO;
    private UtenteDao utenteDao;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        // Crea il database di test
        DatabaseSetupForTest.getConnection();
        System.out.println("=== Database di test inizializzato ===");
    }


    @BeforeEach
    void setUp() throws SQLException {
        servlet = new StoricoSegnalazioniServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);
        segnalazioneDAO = new SegnalazioneDAO();
        utenteDao = new UtenteDao();

        try (Connection conn = DatabaseSetupForTest.getConnection()) {
            conn.createStatement().execute("DELETE FROM segnalazioni;");
            conn.createStatement().execute("DELETE FROM utenti;");
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

    @Test
    @DisplayName("Storico Segnalazioni: carica storico utente")
    void testStoricoSegnalazioniSuccess() throws ServletException, IOException, SQLException {
        Utente utente = new Utente();
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        utente.setEmail("mario@test.com");
        utente.setRuolo("UTENTE");
        utenteDao.registraUtente(utente, "pass");

        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setTitolo("Fake News");
        segnalazione.setDescrizione("Segnalazione");
        segnalazione.setUrl("http://url.com");
        segnalazione.setAutore("Mario");
        segnalazione.setImmaginePath("img.jpg");
        segnalazioneDAO.inserisciSegnalazione(segnalazione, utente.getId());

        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("user")).thenReturn(utente);
        when(requestMock.getRequestDispatcher("/Interface/SegnalazioneNotiziaGUI/storicoSegnalazioni.jsp"))
                .thenReturn(dispatcherMock);

        servlet.doGet(requestMock, responseMock);

        verify(requestMock).setAttribute(eq("segnalazioni"), any());
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("✓ Storico segnalazioni caricato");
    }

    @Test
    @DisplayName("Storico Segnalazioni: utente non autenticato")
    void testStoricoSegnalazioniUnauthorized() throws ServletException, IOException {
        when(requestMock.getSession(false)).thenReturn(null);
        when(requestMock.getContextPath()).thenReturn("");

        servlet.doGet(requestMock, responseMock);

        verify(responseMock).sendRedirect(contains("/Interface/AutenticazioneGUI/Login.jsp"));
        System.out.println("✓ Accesso negato (non autenticato)");
    }
}