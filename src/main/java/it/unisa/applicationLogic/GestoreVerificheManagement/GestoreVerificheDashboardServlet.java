package it.unisa.applicationLogic.GestoreVerificheManagement;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import it.unisa.storage.Segnalazione;
import it.unisa.storage.SegnalazioneDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/gestoreVerifiche/dashboard")
public class GestoreVerificheDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private SegnalazioneDAO segnalazioneDAO;

    @Override
    public void init() throws ServletException {
        segnalazioneDAO = new SegnalazioneDAO();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verifica autenticazione
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("gestoreId") == null) {
            response.sendRedirect(request.getContextPath() + "/Interface/AutenticazioneGUI/Login.jsp");
            return;
        }

        String ruolo = (String) session.getAttribute("gestoreRuolo");
        if (!"GESTORE_VERIFICHE".equals(ruolo)) {
            response.sendRedirect(request.getContextPath() + "/Interface/AutenticazioneGUI/Login.jsp");
            return;
        }

        try {
            // Ottieni tutte le segnalazioni
            List<Segnalazione> segnalazioni = segnalazioneDAO.getTutteSegnalazioni();

            // Statistiche
            int inVerifica = segnalazioneDAO.countSegnalazioniByStato("in_verifica");
            int verificate = segnalazioneDAO.countSegnalazioniByStato("verificata");
            int nonAttendibili = segnalazioneDAO.countSegnalazioniByStato("non_attendibile");

            request.setAttribute("segnalazioni", segnalazioni);
            request.setAttribute("inVerifica", inVerifica);
            request.setAttribute("verificate", verificate);
            request.setAttribute("nonAttendibili", nonAttendibili);

            request.getRequestDispatcher("/Interface/GestoreVerificheGUI/dashboard.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errore", "Errore nel caricamento dei dati");
            request.getRequestDispatcher("/Interface/GestoreVerificheGUI/errore.jsp")
                    .forward(request, response);
        }
    }
}