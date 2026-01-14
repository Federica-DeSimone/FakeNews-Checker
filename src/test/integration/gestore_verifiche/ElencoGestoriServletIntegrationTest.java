package integration.gestore_verifiche;

import static org.mockito.Mockito.*;
import it.unisa.applicationLogic.GestoreVerificheManagement.ElencoGestoriServlet;
import it.unisa.storage.Gestore;
import it.unisa.storage.GestoreDAO;
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

@DisplayName("ElencoGestoriServlet Integration Test")
class ElencoGestoriServletIntegrationTest {
    private ElencoGestoriServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private GestoreDAO gestoreDAO;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        // Crea il database di test
        DatabaseSetupForTest.getConnection();
        System.out.println("=== Database di test inizializzato ===");
    }


    @BeforeEach
    void setUp() throws SQLException {
        servlet = new ElencoGestoriServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);
        gestoreDAO = new GestoreDAO();

        try (Connection conn = DatabaseSetupForTest.getConnection()) {
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
            conn.createStatement().execute("DELETE FROM gestori;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Elenco Gestori: carica lista")
    void testElencoGestoriSuccess() throws ServletException, IOException, SQLException {
        Gestore gestore = new Gestore();
        gestore.setNome("Admin");
        gestore.setCognome("Test");
        gestore.setEmail("admin@test.com");
        gestore.setRuolo(Gestore.TipoGestore.GESTORE_VERIFICHE);
        gestoreDAO.registraGestore(gestore, "pass", null);

        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("gestoreId")).thenReturn(gestore.getId());
        when(sessionMock.getAttribute("gestoreRuolo")).thenReturn("GESTORE_VERIFICHE");
        when(requestMock.getRequestDispatcher("/Interface/GestoreVerificheGUI/elencoGestori.jsp"))
                .thenReturn(dispatcherMock);

        servlet.doGet(requestMock, responseMock);

        verify(requestMock).setAttribute(eq("gestori"), any());
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("✓ Elenco gestori caricato");
    }
}