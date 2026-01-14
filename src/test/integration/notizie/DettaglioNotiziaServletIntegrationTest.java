package integration.notizie;

import static org.mockito.Mockito.*;
import it.unisa.applicationLogic.NotizieManagement.DettaglioNotiziaServlet;
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

@DisplayName("DettaglioNotiziaServlet Integration Test")
class DettaglioNotiziaServletIntegrationTest {
    private DettaglioNotiziaServlet servlet;
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
        servlet = new DettaglioNotiziaServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        dispatcherMock = mock(RequestDispatcher.class);
        notiziaDAO = new NotiziaDAO();

        try (Connection conn = DatabaseSetupForTest.getConnection()) {
            conn.createStatement().execute("DELETE FROM segnalazioni;");
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
            conn.createStatement().execute("DELETE FROM segnalazioni;");
            conn.createStatement().execute("DELETE FROM notizie;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Dettaglio Notizia: carica dettagli articolo con ID valido")
    void testDettaglioNotiziaSuccess() throws ServletException, IOException, SQLException {
        Notizia notizia = new Notizia();
        notizia.setTitolo("Titolo Notizia");
        notizia.setDescrizione("Descrizione completa della notizia");
        notizia.setAutore("Autore");
        notizia.setStato("verificata");
        notizia.setImmagine("img.jpg");
        notiziaDAO.inserisciNotizia(notizia);

        when(requestMock.getParameter("id")).thenReturn(String.valueOf(notizia.getId()));
        when(requestMock.getRequestDispatcher("/Interface/NavigazioneNotiziaGUI/dettaglioNotizia.jsp"))
                .thenReturn(dispatcherMock);

        servlet.doGet(requestMock, responseMock);

        verify(requestMock).setAttribute(eq("notizia"), any(Notizia.class));
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("✓ Dettaglio notizia caricato con successo");
    }

    @Test
    @DisplayName("Dettaglio Notizia: ID non fornito")
    void testDettaglioNotiziaNoId() throws ServletException, IOException {
        when(requestMock.getParameter("id")).thenReturn(null);
        when(requestMock.getContextPath()).thenReturn("");

        servlet.doGet(requestMock, responseMock);

        verify(responseMock).sendRedirect(contains("visualizzaNotizie"));
        System.out.println("✓ Redirect quando ID mancante");
    }

    @Test
    @DisplayName("Dettaglio Notizia: ID non valido (non numerico)")
    void testDettaglioNotiziaInvalidId() throws ServletException, IOException {
        when(requestMock.getParameter("id")).thenReturn("abc");
        when(requestMock.getContextPath()).thenReturn("");

        servlet.doGet(requestMock, responseMock);

        verify(responseMock).sendRedirect(contains("visualizzaNotizie"));
        System.out.println("✓ Redirect quando ID non valido");
    }

    @Test
    @DisplayName("Dettaglio Notizia: Notizia non trovata")
    void testDettaglioNotiziaNotFound() throws ServletException, IOException {
        when(requestMock.getParameter("id")).thenReturn("99999");
        when(requestMock.getRequestDispatcher("/Interface/NavigazioneNotiziaGUI/errore.jsp"))
                .thenReturn(dispatcherMock);

        servlet.doGet(requestMock, responseMock);

        verify(requestMock).setAttribute(eq("errore"), contains("Notizia non trovata"));
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("✓ Gestione errore quando notizia non trovata");
    }

    @Test
    @DisplayName("Dettaglio Notizia: Mostra link per segnalazione")
    void testDettaglioNotiziaWithReportLink() throws ServletException, IOException, SQLException {
        Notizia notizia = new Notizia();
        notizia.setTitolo("Notizia da Segnalare");
        notizia.setDescrizione("Contenuto sospetto");
        notizia.setAutore("Autore");
        notizia.setStato("verificata");
        notizia.setImmagine("img.jpg");
        notiziaDAO.inserisciNotizia(notizia);

        when(requestMock.getParameter("id")).thenReturn(String.valueOf(notizia.getId()));
        when(requestMock.getRequestDispatcher("/Interface/NavigazioneNotiziaGUI/dettaglioNotizia.jsp"))
                .thenReturn(dispatcherMock);

        servlet.doGet(requestMock, responseMock);

        // Verifica che la notizia sia stata passata al JSP
        verify(requestMock).setAttribute(eq("notizia"), argThat(
                n -> n instanceof Notizia && ((Notizia) n).getTitolo().equals("Notizia da Segnalare")
        ));
        System.out.println("✓ Dettaglio notizia con link segnalazione");
    }
}