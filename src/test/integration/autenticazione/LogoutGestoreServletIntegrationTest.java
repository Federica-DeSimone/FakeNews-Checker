package integration.autenticazione;

import static org.mockito.Mockito.*;
import it.unisa.applicationLogic.AutenticazioneManagement.LogoutGestoreServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.*;
import unit.test_DAO.DatabaseSetupForTest;
import java.io.IOException;
import java.sql.SQLException;

@DisplayName("LogoutGestoreServlet Integration Test")
class LogoutGestoreServletIntegrationTest {
    private LogoutGestoreServlet servlet;
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
        servlet = new LogoutGestoreServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
    }

    @Test
    @DisplayName("Logout Gestore: sessione invalidata")
    void testLogoutGestoreSuccess() throws ServletException, IOException {
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(requestMock.getContextPath()).thenReturn("");

        servlet.doGet(requestMock, responseMock);

        verify(sessionMock).removeAttribute("gestoreId");
        verify(sessionMock).removeAttribute("gestoreNome");
        verify(sessionMock).removeAttribute("gestoreEmail");
        verify(sessionMock).removeAttribute("gestoreRuolo");
        verify(sessionMock).invalidate();
        verify(responseMock).sendRedirect(contains("/Interface/AutenticazioneGUI/LoginGestore.jsp"));

        System.out.println("✓ Logout Gestore eseguito");
    }
}