package integration.notizie;

import static org.mockito.Mockito.*;

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

@DisplayName("CercaNotizieServlet Integration Test")
class CercaNotizieServletIntegrationTest {
    private CercaNotizieServlet servlet;
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
        servlet = new CercaNotizieServlet();
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
    @DisplayName("Cerca Notizie: ricerca con keyword")
    void testCercaNotizieSuccess() throws ServletException, IOException, SQLException {
        Notizia notizia = new Notizia();
        notizia.setTitolo("Articolo Speciale");
        notizia.setDescrizione("Descrizione speciale");
        notizia.setAutore("Autore");
        notizia.setStato("verificata");
        notizia.setImmagine("img.jpg");
        notiziaDAO.inserisciNotizia(notizia);

        when(requestMock.getParameter("keyword")).thenReturn("Speciale");
        when(requestMock.getParameter("ajax")).thenReturn(null);
        when(requestMock.getRequestDispatcher("/Interface/NavigazioneNotiziaGUI/visualizzaNotizie.jsp"))
                .thenReturn(dispatcherMock);

        servlet.doGet(requestMock, responseMock);

        verify(requestMock).setAttribute(eq("notizie"), any());
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("✓ Ricerca notizie completata");
    }

    @Test
    @DisplayName("Cerca Notizie: keyword vuoto")
    void testCercaNotizieEmpty() throws ServletException, IOException {
        when(requestMock.getParameter("keyword")).thenReturn("");
        when(requestMock.getParameter("ajax")).thenReturn(null);
        when(requestMock.getRequestDispatcher("/Interface/NavigazioneNotiziaGUI/visualizzaNotizie.jsp"))
                .thenReturn(dispatcherMock);

        servlet.doGet(requestMock, responseMock);

        verify(requestMock).setAttribute(eq("error"), anyString());
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("✓ Ricerca vuota gestita");
    }
}