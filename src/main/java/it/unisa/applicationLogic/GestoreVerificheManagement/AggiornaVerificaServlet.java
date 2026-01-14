package it.unisa.applicationLogic.GestoreVerificheManagement;



import java.io.IOException;
import java.sql.SQLException;

import it.unisa.storage.Notizia;
import it.unisa.storage.NotiziaDAO;
import it.unisa.storage.Segnalazione;
import it.unisa.storage.SegnalazioneDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/gestoreVerifiche/aggiornaVerifica")
public class AggiornaVerificaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private SegnalazioneDAO segnalazioneDAO;
    private NotiziaDAO notiziaDAO;

    @Override
    public void init() throws ServletException {
        segnalazioneDAO = new SegnalazioneDAO();
        notiziaDAO = new NotiziaDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verifica autenticazione
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("gestoreId") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String ruolo = (String) session.getAttribute("gestoreRuolo");
        if (!"GESTORE_VERIFICHE".equals(ruolo)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Integer gestoreId = (Integer) session.getAttribute("gestoreId");
        String idSegnalazioneParam = request.getParameter("idSegnalazione");
        String azione = request.getParameter("azione"); // "verifica" o "rifiuta"

        if (idSegnalazioneParam == null || azione == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int idSegnalazione = Integer.parseInt(idSegnalazioneParam);
            Segnalazione segnalazione = segnalazioneDAO.getSegnalazioneById(idSegnalazione);

            if (segnalazione == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if ("verifica".equals(azione)) {
                // Se la segnalazione ha un id_notizia, aggiorna quella notizia CON I DATI DELLA SEGNALAZIONE
                if (segnalazione.getIdNotizia() != null) {
                    // Ottieni la notizia esistente
                    Notizia notiziaEsistente = notiziaDAO.getNotiziaById(segnalazione.getIdNotizia());

                    if (notiziaEsistente != null) {
                        // Aggiorna la notizia con i dati dalla segnalazione
                        notiziaEsistente.setDescrizione(segnalazione.getDescrizione());
                        notiziaEsistente.setStato("verificata");

                        // Aggiorna anche l'immagine se la segnalazione ne ha una
                        if (segnalazione.getImmaginePath() != null && !segnalazione.getImmaginePath().isEmpty()) {
                            String[] parts = segnalazione.getImmaginePath().split("/");
                            notiziaEsistente.setImmagine(parts[parts.length - 1]);
                        }

                        boolean notiziaAggiornata = notiziaDAO.aggiornaNotizia(notiziaEsistente);

                        if (notiziaAggiornata) {
                            // Aggiorna segnalazione come verificata
                            segnalazioneDAO.aggiornaStatoSegnalazione(
                                    idSegnalazione,
                                    "verificata",
                                    gestoreId,
                                    segnalazione.getIdNotizia()
                            );
                        }
                    }
                } else {
                    // Crea una nuova notizia verificata
                    Notizia notizia = new Notizia();
                    notizia.setTitolo(segnalazione.getTitolo());
                    notizia.setDescrizione(segnalazione.getDescrizione());
                    notizia.setAutore(segnalazione.getAutore());
                    notizia.setStato("verificata");

                    // Gestione immagine
                    String immagine = "default.jpg";
                    if (segnalazione.getImmaginePath() != null && !segnalazione.getImmaginePath().isEmpty()) {
                        String[] parts = segnalazione.getImmaginePath().split("/");
                        immagine = parts[parts.length - 1];
                    }
                    notizia.setImmagine(immagine);

                    // Inserisci notizia
                    boolean notiziaCreata = notiziaDAO.inserisciNotizia(notizia);

                    if (notiziaCreata) {
                        // Aggiorna segnalazione come verificata
                        segnalazioneDAO.aggiornaStatoSegnalazione(
                                idSegnalazione,
                                "verificata",
                                gestoreId,
                                notizia.getId()
                        );
                    }
                }

            } else if ("rifiuta".equals(azione)) {
                // Marca come non attendibile
                segnalazioneDAO.aggiornaStatoSegnalazione(
                        idSegnalazione,
                        "non_attendibile",
                        gestoreId,
                        null
                );

                // Se c'era una notizia collegata, riportala a "segnalata"
                if (segnalazione.getIdNotizia() != null) {
                    notiziaDAO.aggiornaStatoNotizia(segnalazione.getIdNotizia(), "segnalata");
                }
            }

            response.sendRedirect(request.getContextPath() + "/gestoreVerifiche/dashboard");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}