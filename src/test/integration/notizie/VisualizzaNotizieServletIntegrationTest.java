package integration.notizie;

import static org.mockito.Mockito.*;
import it.unisa.applicationLogic.NotizieManagement.VisualizzaNotizieServlet;
import it.unisa.storage.Notizia;
import it.unisa.storage.NotiziaDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import unit.test_DAO.DatabaseSetupForTest;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@DisplayName("VisualizzaNotizieServlet Integration Test")
class VisualizzaNotizieServletIntegrationTest {
    private VisualizzaNotizieServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private RequestDispatcher dispatcherMock;
    private NotiziaDAO notiziaDAO;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        // Crea il database di test
        DatabaseSetupForTest.getConnection();
        System.out.println("=== Database di test inizializzato ===");
    }


    @BeforeEach
    void setUp() throws SQLException {
        servlet = new VisualizzaNotizieServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        dispatcherMock = mock(RequestDispatcher.class);
        notiziaDAO = new NotiziaDAO();

        try (Connection conn = DatabaseSetupForTest.getConnection()) {
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
            conn.createStatement().execute("DELETE FROM notizie;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Visualizza Notizie: carica lista notizie pubbliche")
    void testVisualizzaNotizieSuccess() throws ServletException, IOException, SQLException {
        Notizia notizia = new Notizia();
        notizia.setTitolo("Titolo");
        notizia.setDescrizione("Descrizione");
        notizia.setAutore("Autore");
        notizia.setStato("verificata");
        notizia.setImmagine("img.jpg");
        notiziaDAO.inserisciNotizia(notizia);

        when(requestMock.getRequestDispatcher("/Interface/NavigazioneNotiziaGUI/visualizzaNotizie.jsp"))
                .thenReturn(dispatcherMock);

        servlet.doGet(requestMock, responseMock);

        verify(requestMock).setAttribute(eq("notizie"), any());
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("✓ Lista notizie caricata");
    }
}