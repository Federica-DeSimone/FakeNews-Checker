package integration.gestore_tecnico;

import static org.mockito.Mockito.*;

import it.unisa.applicationLogic.GestoreTecnicoManagement.GestoreTecnicoDashboardServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.*;

class GestoreTecnicoDashboardServletIntegrationTest {

    private GestoreTecnicoDashboardServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setup() {
        servlet = new GestoreTecnicoDashboardServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("gestoreEmail")).thenReturn("tecnico@test.com");
        when(session.getAttribute("gestoreRuolo")).thenReturn("GESTORE_TECNICO");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);


    }

    // ===== TC04.1 =====
    @Test
    void TC04_1() throws Exception {
        System.out.println("TC04.1 – TO1,C1");
        System.out.println("Input: TipoOperazione = null, Componente = null");

        when(request.getParameter("tipoOperazione")).thenReturn(null);
        when(request.getParameter("componente")).thenReturn(null);

        servlet.doPost(request, response);

        verify(request).setAttribute("errore",
                "Selezionare un'operazione e un componente");
        verify(dispatcher).forward(request, response);

        System.out.println("Oracolo: \"Selezionare un'operazione e un componente\"");
        System.out.println("✓ TC04.1 SUPERATO\n");
    }

    // ===== TC04.2 =====
    @Test
    void TC04_2() throws Exception {
        System.out.println("TC04.2 – TO2,C3");
        System.out.println("Input: TipoOperazione = Monitoraggio, Componente = Database");

        when(request.getParameter("tipoOperazione")).thenReturn("Monitoraggio");
        when(request.getParameter("componente")).thenReturn("Database");

        servlet.doPost(request, response);

        verify(request).setAttribute("successo",
                "Monitoraggio database effettuato con successo");
        verify(dispatcher).forward(request, response);

        System.out.println("Oracolo: \"Monitoraggio database effettuato con successo\"");
        System.out.println("✓ TC04.2 SUPERATO\n");
    }

    // ===== TC04.3 =====
    @Test
    void TC04_3() throws Exception {
        System.out.println("TC04.3 – TO3,C2");
        System.out.println("Input: TipoOperazione = Riavvio, Componente = Server");

        when(request.getParameter("tipoOperazione")).thenReturn("Riavvio");
        when(request.getParameter("componente")).thenReturn("Server");

        servlet.doPost(request, response);

        verify(request).setAttribute("successo",
                "Riavvio Server effettuato con successo");
        verify(dispatcher).forward(request, response);

        System.out.println("Oracolo: \"Riavvio Server effettuato con successo\"");
        System.out.println("✓ TC04.3 SUPERATO\n");
    }

    // ===== TC04.4 =====
    @Test
    void TC04_4() throws Exception {
        System.out.println("TC04.4 – TO4,C4");
        System.out.println("Input: TipoOperazione = Aggiornamento, Componente = Sistema");

        when(request.getParameter("tipoOperazione")).thenReturn("Aggiornamento");
        when(request.getParameter("componente")).thenReturn("Sistema");

        servlet.doPost(request, response);

        verify(request).setAttribute("successo",
                "Aggiornamento Sistema effettuato con successo");
        verify(dispatcher).forward(request, response);

        System.out.println("Oracolo: \"Aggiornamento Sistema effettuato con successo\"");
        System.out.println("✓ TC04.4 SUPERATO\n");
    }
}
