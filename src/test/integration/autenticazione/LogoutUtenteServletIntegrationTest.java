package integration.autenticazione;

import static org.mockito.Mockito.*;
import it.unisa.applicationLogic.AutenticazioneManagement.LogoutUtenteServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.*;
import unit.test_DAO.DatabaseSetupForTest;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@DisplayName("LogoutUtenteServlet Integration Test")
class LogoutUtenteServletIntegrationTest {
    private LogoutUtenteServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        // Crea il database di test
        DatabaseSetupForTest.getConnection();
        System.out.println("=== Database di test inizializzato ===");
    }


    @BeforeEach
    void setUp() {
        servlet = new LogoutUtenteServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
    }

    @Test
    @DisplayName("Logout Utente: sessione invalidata e redirect a Home")
    void testLogoutUtenteSuccess() throws ServletException, IOException {
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(requestMock.getContextPath()).thenReturn("");

        servlet.doGet(requestMock, responseMock);

        verify(sessionMock).invalidate();
        verify(responseMock).sendRedirect(contains("/Interface/Home.jsp"));
        System.out.println("✓ Logout Utente eseguito con successo");
    }

    @Test
    @DisplayName("Logout Utente: nessuna sessione (già logged out)")
    void testLogoutUtenteNoSession() throws ServletException, IOException {
        when(requestMock.getSession(false)).thenReturn(null);
        when(requestMock.getContextPath()).thenReturn("");

        servlet.doGet(requestMock, responseMock);

        verify(responseMock).sendRedirect(contains("/Interface/Home.jsp"));
        System.out.println("✓ Logout Utente (nessuna sessione)");
    }
}