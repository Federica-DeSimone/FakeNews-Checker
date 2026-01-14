package integration.gestore_verifiche;

import static org.mockito.Mockito.*;
import it.unisa.applicationLogic.GestoreVerificheManagement.InserisciArticoloServlet;
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

@DisplayName("InserisciArticoloServlet Integration Test")
class InserisciArticoloServletIntegrationTest {
    private InserisciArticoloServlet servlet;
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
        servlet = new InserisciArticoloServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);
        gestoreDAO = new GestoreDAO();

        try (Connection conn = DatabaseSetupForTest.getConnection()) {
            conn.createStatement().execute("DELETE FROM gestori;");
            conn.createStatement().execute("DELETE FROM notizie;");
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
            conn.createStatement().execute("DELETE FROM notizie;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("GET - Mostra form inserimento articolo")
    void testInserisciArticoloGetForm() throws ServletException, IOException, SQLException {
        Gestore gestore = new Gestore();
        gestore.setNome("Carlo");
        gestore.setCognome("Rossi");
        gestore.setEmail("carlo@test.com");
        gestore.setRuolo(Gestore.TipoGestore.GESTORE_VERIFICHE);
        gestoreDAO.registraGestore(gestore, "pass", null);

        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("gestoreId")).thenReturn(gestore.getId());
        when(sessionMock.getAttribute("gestoreRuolo")).thenReturn("GESTORE_VERIFICHE");
        when(requestMock.getRequestDispatcher("/Interface/GestoreVerificheGUI/inserisciArticolo.jsp"))
                .thenReturn(dispatcherMock);

        servlet.doGet(requestMock, responseMock);

        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("✓ Form inserimento articolo mostrato");
    }

    @Test
    @DisplayName("POST - Inserimento articolo riuscito")
    void testInserisciArticoloSuccess() throws ServletException, IOException, SQLException {
        Gestore gestore = new Gestore();
        gestore.setNome("Lucia");
        gestore.setCognome("Rossi");
        gestore.setEmail("lucia@test.com");
        gestore.setRuolo(Gestore.TipoGestore.GESTORE_VERIFICHE);
        gestoreDAO.registraGestore(gestore, "pass", null);

        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("gestoreId")).thenReturn(gestore.getId());
        when(sessionMock.getAttribute("gestoreRuolo")).thenReturn("GESTORE_VERIFICHE");
        when(requestMock.getParameter("titolo")).thenReturn("Titolo Articolo");
        when(requestMock.getParameter("descrizione")).thenReturn("Descrizione articolo");
        when(requestMock.getParameter("autore")).thenReturn("Autore");
        when(requestMock.getParameter("stato")).thenReturn("verificata");
        when(requestMock.getPart("foto")).thenReturn(null);
        when(requestMock.getContextPath()).thenReturn("");

        servlet.doPost(requestMock, responseMock);

        verify(responseMock).sendRedirect(contains("gestioneArticoli"));
        System.out.println("✓ Articolo inserito con successo");
    }
}