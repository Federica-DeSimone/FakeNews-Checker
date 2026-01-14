package integration.gestore_verifiche;

import static org.mockito.Mockito.*;
import it.unisa.applicationLogic.GestoreVerificheManagement.RegistraGestoreServlet;
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

@DisplayName("RegistraGestoreServlet Integration Test")
class RegistraGestoreServletIntegrationTest {
    private RegistraGestoreServlet servlet;
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
        servlet = new RegistraGestoreServlet();
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
    @DisplayName("GET - Mostra form registrazione gestore")
    void testRegistraGestoreGetForm() throws ServletException, IOException, SQLException {
        Gestore admin = new Gestore();
        admin.setNome("Admin");
        admin.setCognome("Test");
        admin.setEmail("admin2@test.com");
        admin.setRuolo(Gestore.TipoGestore.GESTORE_VERIFICHE);
        gestoreDAO.registraGestore(admin, "pass", null);

        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("gestoreId")).thenReturn(admin.getId());
        when(sessionMock.getAttribute("gestoreRuolo")).thenReturn("GESTORE_VERIFICHE");
        when(requestMock.getRequestDispatcher("/Interface/GestoreVerificheGUI/registraGestore.jsp"))
                .thenReturn(dispatcherMock);

        servlet.doGet(requestMock, responseMock);

        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("✓ Form registrazione gestore mostrato");
    }

    @Test
    @DisplayName("POST - Registra nuovo gestore riuscita")
    void testRegistraGestoreSuccess() throws ServletException, IOException, SQLException {
        Gestore admin = new Gestore();
        admin.setNome("Admin");
        admin.setCognome("Test");
        admin.setEmail("admin3@test.com");
        admin.setRuolo(Gestore.TipoGestore.GESTORE_VERIFICHE);
        gestoreDAO.registraGestore(admin, "pass", null);

        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("gestoreId")).thenReturn(admin.getId());
        when(sessionMock.getAttribute("gestoreRuolo")).thenReturn("GESTORE_VERIFICHE");
        when(requestMock.getParameter("nome")).thenReturn("Nuovo");
        when(requestMock.getParameter("cognome")).thenReturn("Gestore");
        when(requestMock.getParameter("email")).thenReturn("nuovo@test.com");
        when(requestMock.getParameter("password")).thenReturn("Password123!");
        when(requestMock.getParameter("telefono")).thenReturn("1234567890");
        when(requestMock.getParameter("ruolo")).thenReturn("admin-verifiche");
        when(requestMock.getContextPath()).thenReturn("");

        servlet.doPost(requestMock, responseMock);

        verify(responseMock).sendRedirect(contains("successoRegistrazioneGestore.jsp"));
        System.out.println("✓ Gestore registrato con successo");
    }

    @Test
    @DisplayName("POST - Email gestore già registrata")
    void testRegistraGestoreEmailExists() throws ServletException, IOException, SQLException {
        Gestore admin = new Gestore();
        admin.setNome("Admin");
        admin.setCognome("Test");
        admin.setEmail("admin4@test.com");
        admin.setRuolo(Gestore.TipoGestore.GESTORE_VERIFICHE);
        gestoreDAO.registraGestore(admin, "pass", null);

        Gestore existing = new Gestore();
        existing.setNome("Existing");
        existing.setCognome("User");
        existing.setEmail("existing@test.com");
        existing.setRuolo(Gestore.TipoGestore.GESTORE_VERIFICHE);
        gestoreDAO.registraGestore(existing, "pass", null);

        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("gestoreId")).thenReturn(admin.getId());
        when(sessionMock.getAttribute("gestoreRuolo")).thenReturn("GESTORE_VERIFICHE");
        when(requestMock.getParameter("nome")).thenReturn("Nuovo");
        when(requestMock.getParameter("cognome")).thenReturn("Gestore");
        when(requestMock.getParameter("email")).thenReturn("existing@test.com");
        when(requestMock.getParameter("password")).thenReturn("Password123!");
        when(requestMock.getParameter("telefono")).thenReturn("1234567890");
        when(requestMock.getParameter("ruolo")).thenReturn("admin-verifiche");
        when(requestMock.getRequestDispatcher("/Interface/GestoreVerificheGUI/registraGestore.jsp"))
                .thenReturn(dispatcherMock);

        servlet.doPost(requestMock, responseMock);

        verify(requestMock).setAttribute(eq("errore"), contains("Email già registrata"));
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("✓ Email duplicata rigettata");
    }

    @Test
    @DisplayName("POST - Campi obbligatori vuoti")
    void testRegistraGestoreEmptyFields() throws ServletException, IOException, SQLException {
        Gestore admin = new Gestore();
        admin.setNome("Admin");
        admin.setCognome("Test");
        admin.setEmail("admin5@test.com");
        admin.setRuolo(Gestore.TipoGestore.GESTORE_VERIFICHE);
        gestoreDAO.registraGestore(admin, "pass", null);

        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("gestoreId")).thenReturn(admin.getId());
        when(sessionMock.getAttribute("gestoreRuolo")).thenReturn("GESTORE_VERIFICHE");
        when(requestMock.getParameter("nome")).thenReturn(null);
        when(requestMock.getParameter("cognome")).thenReturn("Gestore");
        when(requestMock.getParameter("email")).thenReturn("nuovo@test.com");
        when(requestMock.getParameter("password")).thenReturn("Password123!");
        when(requestMock.getParameter("telefono")).thenReturn("1234567890");
        when(requestMock.getParameter("ruolo")).thenReturn("admin-verifiche");
        when(requestMock.getRequestDispatcher("/Interface/GestoreVerificheGUI/registraGestore.jsp"))
                .thenReturn(dispatcherMock);

        servlet.doPost(requestMock, responseMock);

        verify(requestMock).setAttribute(eq("errore"), anyString());
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("✓ Campi vuoti rigettati");
    }
}