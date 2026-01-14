package integration.gestore_verifiche;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import it.unisa.applicationLogic.GestoreVerificheManagement.GestioneArticoliServlet;
import it.unisa.storage.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import unit.test_DAO.DatabaseSetupForTest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@DisplayName("TC03 - Aggiornamento stato articolo")
class GestioneArticoliServletIntegrationTest {

    private GestioneArticoliServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private GestoreDAO gestoreDAO;
    private NotiziaDAO notiziaDAO;
    private Notizia notizia;

    /* ===================== DATABASE TEST ===================== */
    @BeforeAll
    static void setupDatabase() throws SQLException {
        DatabaseSetupForTest.getConnection();
    }

    /* ===================== SETUP ===================== */
    @BeforeEach
    void setUp() throws Exception {

        servlet = new GestioneArticoliServlet();

        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);

        gestoreDAO = new GestoreDAO();
        notiziaDAO = new NotiziaDAO();

        // Pulizia database di test
        try (Connection conn = DatabaseSetupForTest.getConnection()) {
            conn.createStatement().execute("DELETE FROM notizie;");
            conn.createStatement().execute("DELETE FROM gestori;");
        }

        /* ===== Gestore verifiche (EMAIL UNIVOCA) ===== */
        Gestore gestore = new Gestore();
        gestore.setNome("Admin");
        gestore.setCognome("Test");
        gestore.setEmail("admin_" + System.currentTimeMillis() + "@test.com");
        gestore.setRuolo(Gestore.TipoGestore.GESTORE_VERIFICHE);
        gestoreDAO.registraGestore(gestore, "pass", null);

        /* ===== Notizia ===== */
        notizia = new Notizia();
        notizia.setTitolo("Articolo Test");
        notizia.setDescrizione("Descrizione");
        notizia.setAutore("Autore");
        notizia.setStato("In verifica");
        notizia.setImmagine("img.jpg");
        notiziaDAO.inserisciNotizia(notizia);

        /* ===== Sessione mock ===== */
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("gestoreId")).thenReturn(gestore.getId());
        when(sessionMock.getAttribute("gestoreRuolo"))
                .thenReturn("GESTORE_VERIFICHE");

        when(requestMock.getRequestDispatcher(anyString()))
                .thenReturn(dispatcherMock);

        servlet.init();
    }

    /* ===================== TC03.1 ===================== */
    @Test
    @DisplayName("TC03.1 - Stato non impostato")
    void testStatoNonImpostato() throws ServletException, IOException {

        when(requestMock.getMethod()).thenReturn("POST");
        when(requestMock.getParameter("idNotizia"))
                .thenReturn(String.valueOf(notizia.getId()));
        when(requestMock.getParameter("stato"))
                .thenReturn(null);

        servlet.service(requestMock, responseMock);

        verify(requestMock)
                .setAttribute("messaggio", "Impostare uno stato");
        System.out.println("✓ TC03.1 PASSED: Stato obbligatorio");
    }

    /* ===================== TC03.2 ===================== */
    @Test
    @DisplayName("TC03.2 - Stato Verificato")
    void testStatoVerificato() throws Exception {

        when(requestMock.getMethod()).thenReturn("POST");
        when(requestMock.getParameter("idNotizia"))
                .thenReturn(String.valueOf(notizia.getId()));
        when(requestMock.getParameter("stato"))
                .thenReturn("Verificata");

        servlet.service(requestMock, responseMock);

        Notizia aggiornata =
                notiziaDAO.getNotiziaById(notizia.getId());

        assertEquals("Verificata", aggiornata.getStato());

        verify(requestMock).setAttribute(
                "messaggio",
                "Stato impostato a Verificato, articolo pubblicato"
        );
        System.out.println("✓ TC03.2 PASSED: Stato impostato a Verificato, articolo pubblicato");
    }

    /* ===================== TC03.3 ===================== */
    @Test
    @DisplayName("TC03.3 - Stato Non Attendibile")
    void testStatoNonAttendibile() throws Exception {

        when(requestMock.getMethod()).thenReturn("POST");
        when(requestMock.getParameter("idNotizia"))
                .thenReturn(String.valueOf(notizia.getId()));
        when(requestMock.getParameter("stato"))
                .thenReturn("Non attendibile");

        servlet.service(requestMock, responseMock);

        Notizia aggiornata =
                notiziaDAO.getNotiziaById(notizia.getId());

        assertEquals("Non attendibile", aggiornata.getStato());

        verify(requestMock).setAttribute(
                "messaggio",
                "Stato Non Attendibile, articolo rimosso"
        );
        System.out.println("✓ TC03.3 PASSED: Stato Non Attendibile, articolo rimosso");
    }

    /* ===================== TC03.4 ===================== */
    @Test
    @DisplayName("TC03.4 - Stato In Verifica")
    void testStatoInVerifica() throws Exception {

        when(requestMock.getMethod()).thenReturn("POST");
        when(requestMock.getParameter("idNotizia"))
                .thenReturn(String.valueOf(notizia.getId()));
        when(requestMock.getParameter("stato"))
                .thenReturn("In verifica");

        servlet.service(requestMock, responseMock);

        Notizia aggiornata =
                notiziaDAO.getNotiziaById(notizia.getId());

        assertEquals("In verifica", aggiornata.getStato());

        verify(requestMock).setAttribute(
                "messaggio",
                "Stato impostato a In Verifica, articolo da verificare"
        );
        System.out.println("✓ TC03.4 PASSED: Stato impostato a In Verifica, articolo da verificare");
    }
}
