package it.unisa.applicationLogic.SegnalazioneManagement;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import it.unisa.storage.Segnalazione;
import it.unisa.storage.SegnalazioneDAO;
import it.unisa.storage.Utente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/storicoSegnalazioni")
public class StoricoSegnalazioniServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private SegnalazioneDAO segnalazioneDAO;

    @Override
    public void init() throws ServletException {
        segnalazioneDAO = new SegnalazioneDAO();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verifica se l'utente è loggato
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // Utente non loggato - redirect al login
            response.sendRedirect(request.getContextPath() + "/Interface/AutenticazioneGUI/Login.jsp");
            return;
        }

        try {
            // Ottieni l'ID dell'utente loggato
            Utente utente = (Utente) session.getAttribute("user");
            int idUtente = utente.getId();

            // Recupera SOLO le segnalazioni dell'utente loggato
            List<Segnalazione> segnalazioni = segnalazioneDAO.getSegnalazioniByUtente(idUtente);
            request.setAttribute("segnalazioni", segnalazioni);

            request.getRequestDispatcher("/Interface/SegnalazioneNotiziaGUI/storicoSegnalazioni.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errore", "Errore nel caricamento dello storico: " + e.getMessage());
            request.getRequestDispatcher("/Interface/SegnalazioneNotiziaGUI/errore.jsp")
                    .forward(request, response);
        }
    }
}