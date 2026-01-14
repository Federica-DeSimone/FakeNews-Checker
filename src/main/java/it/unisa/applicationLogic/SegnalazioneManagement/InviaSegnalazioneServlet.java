package it.unisa.applicationLogic.SegnalazioneManagement;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import it.unisa.storage.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;


@WebServlet("/inviaSegnalazione")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class InviaSegnalazioneServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private SegnalazioneDAO segnalazioneDAO;
    private NotiziaDAO notiziaDAO;

    private static final String UPLOAD_DIR = "uploads/segnalazioni";

    @Override
    public void init() {
        segnalazioneDAO = new SegnalazioneDAO();
        notiziaDAO = new NotiziaDAO();
    }

    /* =====================================================
       GET → MOSTRA FORM
       ===================================================== */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userEmail") == null) {
            response.sendRedirect(request.getContextPath() + "/Interface/AutenticazioneGUI/Login.jsp");
            return;
        }

        // Se arrivo da una notizia (es: /inviaSegnalazione?idNotizia=3)
        String idNotiziaParam = request.getParameter("idNotizia");

        if (idNotiziaParam != null && !idNotiziaParam.isBlank()) {
            try {
                int idNotizia = Integer.parseInt(idNotiziaParam);
                Notizia notizia = notiziaDAO.getNotiziaById(idNotizia);

                if (notizia != null) {
                    request.setAttribute("idNotizia", idNotizia);
                    request.setAttribute("titoloNotizia", notizia.getTitolo());
                    request.setAttribute("immagineNotizia", notizia.getImmagine());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        request.getRequestDispatcher("/Interface/SegnalazioneNotiziaGUI/formSegnalazione.jsp")
                .forward(request, response);
    }

    /* =====================================================
       POST → INVIA SEGNALAZIONE
       ===================================================== */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userEmail") == null) {
            response.sendRedirect(request.getContextPath() + "/Interface/AutenticazioneGUI/Login.jsp");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        Utente utente = (Utente) session.getAttribute("user");
        Integer idUtente = (utente != null) ? utente.getId() : null;


        String idNotiziaParam = request.getParameter("idNotizia");
        String titolo;
        String immaginePath = null;

        String descrizione = request.getParameter("descrizione");
        String url = request.getParameter("url");
        String autore = request.getParameter("autore");

        if (autore == null || autore.isBlank()) {
            String userName = (String) session.getAttribute("userName");
            autore = (userName != null) ? userName : (String) session.getAttribute("userEmail");
        }

        try {
            // ===== SEGNALAZIONE DA ARTICOLO =====
            if (idNotiziaParam != null && !idNotiziaParam.isBlank()) {

                int idNotizia = Integer.parseInt(idNotiziaParam);
                Notizia notizia = notiziaDAO.getNotiziaById(idNotizia);

                if (notizia == null) {
                    request.setAttribute("errore", "Notizia non valida");
                    request.getRequestDispatcher("/Interface/SegnalazioneNotiziaGUI/formSegnalazione.jsp")
                            .forward(request, response);
                    return;
                }

                titolo = notizia.getTitolo();
                immaginePath = notizia.getImmagine();

            }
            // ===== SEGNALAZIONE MANUALE =====
            else {

                titolo = request.getParameter("titolo");

                Part filePart = request.getPart("foto");
                if (filePart != null && filePart.getSize() > 0) {

                    String fileName = extractFileName(filePart);
                    String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;

                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdirs();

                    String uniqueName = System.currentTimeMillis() + "_" + fileName;
                    filePart.write(uploadPath + File.separator + uniqueName);

                    immaginePath = UPLOAD_DIR + "/" + uniqueName;
                }
            }

            // VALIDAZIONE
            if (titolo == null || titolo.isBlank()
                    || descrizione == null || descrizione.isBlank()
                    || url == null || url.isBlank()) {

                request.setAttribute("errore", "Compila tutti i campi obbligatori");
                request.getRequestDispatcher("/Interface/SegnalazioneNotiziaGUI/formSegnalazione.jsp")
                        .forward(request, response);
                return;
            }

            Segnalazione segnalazione = new Segnalazione();
            segnalazione.setTitolo(titolo);
            segnalazione.setDescrizione(descrizione);
            segnalazione.setUrl(url);
            segnalazione.setAutore(autore);
            segnalazione.setImmaginePath(immaginePath);

            boolean success = segnalazioneDAO.inserisciSegnalazione(segnalazione, idUtente);

            if (success) {
                response.sendRedirect(
                        request.getContextPath() +
                                "/Interface/SegnalazioneNotiziaGUI/successoSegnalazione.jsp"
                );
                ;
            } else {
                request.setAttribute("errore", "Errore durante l'invio della segnalazione");
                request.getRequestDispatcher("/Interface/SegnalazioneNotiziaGUI/formSegnalazione.jsp")
                        .forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            request.getRequestDispatcher("/Interface/SegnalazioneNotiziaGUI/formSegnalazione.jsp")
                    .forward(request, response);
        }
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String token : contentDisp.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 2, token.length() - 1);
            }
        }
        return "file";
    }
}
