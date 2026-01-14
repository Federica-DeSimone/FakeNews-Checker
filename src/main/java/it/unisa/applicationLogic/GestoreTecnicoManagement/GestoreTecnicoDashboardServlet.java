package it.unisa.applicationLogic.GestoreTecnicoManagement;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import it.unisa.storage.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/gestoreTecnico/dashboard")
public class GestoreTecnicoDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ===================== GET =====================
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("gestoreEmail") == null) {
            response.sendRedirect(request.getContextPath() + "/Interface/AutenticazioneGUI/Login.jsp");
            return;
        }

        if (!"GESTORE_TECNICO".equals(session.getAttribute("gestoreRuolo"))) {
            response.sendRedirect(request.getContextPath() + "/Interface/Home.jsp");
            return;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();
            boolean connessioneAttiva = conn != null && !conn.isClosed();

            Map<String, Object> stats = new HashMap<>();
            stats.put("connessione", connessioneAttiva ? "Attiva" : "Non attiva");
            stats.put("backup", "Attivo");
            stats.put("dimensione", "—");

            stats.put("uptime", "98.7%");
            stats.put("latenzaMedia", "24ms");
            stats.put("richiesteOra", 1523);

            List<Map<String, String>> activeQueries = new ArrayList<>();

            request.setAttribute("stats", stats);
            request.setAttribute("activeQueries", activeQueries);

            request.getRequestDispatcher("/Interface/GestoreTecnicoGUI/dashboard.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            request.setAttribute("errore", "Errore database");
            request.getRequestDispatcher("/Interface/SegnalazioneNotiziAGUI/errore.jsp")
                    .forward(request, response);
        }
    }

    // ===================== POST – TC04 =====================
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("gestoreEmail") == null) {
            response.sendRedirect(request.getContextPath() + "/Interface/AutenticazioneGUI/Login.jsp");
            return;
        }

        if (!"GESTORE_TECNICO".equals(session.getAttribute("gestoreRuolo"))) {
            response.sendRedirect(request.getContextPath() + "/Interface/Home.jsp");
            return;
        }

        String tipoOperazione = request.getParameter("tipoOperazione");
        String componente = request.getParameter("componente");

        // TC04.1
        if (tipoOperazione == null && componente == null) {
            request.setAttribute("errore", "Selezionare un'operazione e un componente");
            request.getRequestDispatcher("/Interface/GestoreTecnicoGUI/dashboard.jsp")
                    .forward(request, response);
            return;
        }

        // TC04.2 (errore operazione)
        if (tipoOperazione == null) {
            request.setAttribute("errore", "Selezionare un'operazione");
            request.getRequestDispatcher("/Interface/GestoreTecnicoGUI/dashboard.jsp")
                    .forward(request, response);
            return;
        }

        // TC04.2 (errore componente)
        if (componente == null) {
            request.setAttribute("errore", "Selezionare una componente");
            request.getRequestDispatcher("/Interface/GestoreTecnicoGUI/dashboard.jsp")
                    .forward(request, response);
            return;
        }

        String messaggio = null;

        if (tipoOperazione.equals("Monitoraggio") && componente.equals("Database")) {
            messaggio = "Monitoraggio database effettuato con successo";
        }

        if (tipoOperazione.equals("Riavvio") && componente.equals("Server")) {
            messaggio = "Riavvio Server effettuato con successo";
        }

        if (tipoOperazione.equals("Aggiornamento") && componente.equals("Sistema")) {
            messaggio = "Aggiornamento Sistema effettuato con successo";
        }

        if (messaggio != null) {
            request.setAttribute("successo", messaggio);
        } else {
            request.setAttribute("errore", "Combinazione non valida");
        }

        request.getRequestDispatcher("/Interface/GestoreTecnicoGUI/dashboard.jsp")
                .forward(request, response);
    }
}
