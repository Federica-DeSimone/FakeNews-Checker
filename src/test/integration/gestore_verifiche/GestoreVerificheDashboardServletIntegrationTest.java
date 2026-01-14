package integration.gestore_verifiche;

import static org.mockito.Mockito.*;
import it.unisa.applicationLogic.GestoreVerificheManagement.GestoreVerificheDashboardServlet;
import it.unisa.storage.*;
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

@DisplayName("GestoreVerificheDashboardServlet Integration Test")
class GestoreVerificheDashboardServletIntegrationTest {
    private GestoreVerificheDashboardServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private GestoreDAO gestoreDAO;
    private SegnalazioneDAO segnalazioneDAO;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        // Crea il database di test
        DatabaseSetupForTest.getConnection();
        System.out.println("=== Database di test inizializzato ===");
    }


    @BeforeEach
    void setUp() throws SQLException {
        servlet = new GestoreVerificheDashboardServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);
        gestoreDAO = new GestoreDAO();
        segnalazioneDAO = new SegnalazioneDAO();

        try (Connection conn = DatabaseSetupForTest.getConnection()) {
            conn.createStatement().execute("DELETE FROM segnalazioni;");
            conn.createStatement().execute("DELETE FROM gestori;");
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
            conn.createStatement().execute("DELETE FROM gestori;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Dashboard Gestore Verifiche: carica statistiche")
    void testDashboardLoadSuccess() throws ServletException, IOException, SQLException {
        Gestore gestore = new Gestore();
        gestore.setNome("Mario");
        gestore.setCognome("Rossi");
        gestore.setEmail("mario@test.com");
        gestore.setRuolo(Gestore.TipoGestore.GESTORE_VERIFICHE);
        gestoreDAO.registraGestore(gestore, "pass", null);

        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("gestoreId")).thenReturn(gestore.getId());
        when(sessionMock.getAttribute("gestoreRuolo")).thenReturn("GESTORE_VERIFICHE");
        when(requestMock.getRequestDispatcher("/Interface/GestoreVerificheGUI/dashboard.jsp"))
                .thenReturn(dispatcherMock);

        servlet.doGet(requestMock, responseMock);

        verify(requestMock).setAttribute(eq("inVerifica"), anyInt());
        verify(requestMock).setAttribute(eq("verificate"), anyInt());
        verify(requestMock).setAttribute(eq("nonAttendibili"), anyInt());
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("✓ Dashboard Gestore Verifiche caricato");
    }

    @Test
    @DisplayName("Dashboard Gestore Verifiche: accesso negato")
    void testDashboardUnauthorized() throws ServletException, IOException {
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("gestoreId")).thenReturn(null);
        when(requestMock.getContextPath()).thenReturn("");

        servlet.doGet(requestMock, responseMock);

        verify(responseMock).sendRedirect(contains("/Interface/AutenticazioneGUI/Login.jsp"));
        System.out.println("✓ Accesso negato (non autenticato)");
    }
}