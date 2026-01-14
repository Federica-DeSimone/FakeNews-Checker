package it.unisa.applicationLogic.GestoreVerificheManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import it.unisa.storage.Notizia;
import it.unisa.storage.NotiziaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/gestoreVerifiche/gestioneArticoli")
public class GestioneArticoliServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private NotiziaDAO notiziaDAO;

    @Override
    public void init() throws ServletException {
        notiziaDAO = new NotiziaDAO();
    }

    /* ===================== GET ===================== */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("gestoreId") == null) {
            response.sendRedirect(request.getContextPath() +
                    "/Interface/AutenticazioneGUI/Login.jsp");
            return;
        }

        String ruolo = (String) session.getAttribute("gestoreRuolo");
        if (!"GESTORE_VERIFICHE".equals(ruolo)) {
            response.sendRedirect(request.getContextPath() +
                    "/Interface/AutenticazioneGUI/Login.jsp");
            return;
        }

        try {
            List<Notizia> notizie = notiziaDAO.getTutteNotizie();
            request.setAttribute("notizie", notizie);

            request.getRequestDispatcher(
                            "/Interface/GestoreVerificheGUI/gestioneArticoli.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            request.setAttribute("errore", "Errore nel caricamento degli articoli");
            request.getRequestDispatcher(
                            "/Interface/GestoreVerificheGUI/errore.jsp")
                    .forward(request, response);
        }
    }

    /* ===================== POST (TC03) ===================== */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("gestoreId") == null) {
            response.sendRedirect(request.getContextPath() +
                    "/Interface/AutenticazioneGUI/Login.jsp");
            return;
        }

        String ruolo = (String) session.getAttribute("gestoreRuolo");
        if (!"GESTORE_VERIFICHE".equals(ruolo)) {
            response.sendRedirect(request.getContextPath() +
                    "/Interface/AutenticazioneGUI/Login.jsp");
            return;
        }

        String stato = request.getParameter("stato");
        String idNotiziaStr = request.getParameter("idNotizia");

        /* ===== TC03.1 ===== */
        if (stato == null || stato.isBlank()) {
            request.setAttribute("messaggio", "Impostare uno stato");
            doGet(request, response);
            return;
        }

        try {
            int idNotizia = Integer.parseInt(idNotiziaStr);
            notiziaDAO.aggiornaStatoNotizia(idNotizia, stato);

            /* ===== TC03.2–TC03.4 ===== */
            switch (stato) {
                case "Verificata":
                    request.setAttribute(
                            "messaggio",
                            "Stato impostato a Verificato, articolo pubblicato");
                    break;

                case "Non attendibile":
                    request.setAttribute(
                            "messaggio",
                            "Stato Non Attendibile, articolo rimosso");
                    break;

                case "In verifica":
                    request.setAttribute(
                            "messaggio",
                            "Stato impostato a In Verifica, articolo da verificare");
                    break;
            }

            doGet(request, response);

        } catch (Exception e) {
            request.setAttribute("errore", "Errore aggiornamento stato");
            request.getRequestDispatcher(
                            "/Interface/GestoreVerificheGUI/errore.jsp")
                    .forward(request, response);
        }
    }
}
