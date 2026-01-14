package it.unisa.applicationLogic.GestoreVerificheManagement;



import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import it.unisa.storage.Notizia;
import it.unisa.storage.NotiziaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/gestoreVerifiche/inserisciArticolo")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class InserisciArticoloServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private NotiziaDAO notiziaDAO;
    private static final String UPLOAD_DIR = "images";

    @Override
    public void init() throws ServletException {
        notiziaDAO = new NotiziaDAO();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        request.getRequestDispatcher("/Interface/GestoreVerificheGUI/inserisciArticolo.jsp")
                .forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        request.setCharacterEncoding("UTF-8");

        String titolo = request.getParameter("titolo");
        String descrizione = request.getParameter("descrizione");
        String autore = request.getParameter("autore");
        String stato = request.getParameter("stato");

        if (titolo == null || titolo.trim().isEmpty() ||
                descrizione == null || descrizione.trim().isEmpty() ||
                stato == null || stato.trim().isEmpty()) {

            request.setAttribute("errore", "Tutti i campi obbligatori devono essere compilati");
            request.getRequestDispatcher("/Interface/GestoreVerificheGUI/inserisciArticolo.jsp")
                    .forward(request, response);
            return;
        }

        // Gestione upload immagine
        String nomeImmagine = "default.jpg";
        Part filePart = request.getPart("foto");

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = getFileName(filePart);
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            String filePath = uploadPath + File.separator + uniqueFileName;
            filePart.write(filePath);

            nomeImmagine = uniqueFileName;
        }

        Notizia notizia = new Notizia();
        notizia.setTitolo(titolo);
        notizia.setDescrizione(descrizione);
        notizia.setAutore(autore);
        notizia.setStato(stato);
        notizia.setImmagine(nomeImmagine);

        try {
            boolean success = notiziaDAO.inserisciNotizia(notizia);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/gestoreVerifiche/gestioneArticoli?successo=true");
            } else {
                request.setAttribute("errore", "Errore nell'inserimento dell'articolo");
                request.getRequestDispatcher("/Interface/GestoreVerificheGUI/inserisciArticolo.jsp")
                        .forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errore", "Errore database: " + e.getMessage());
            request.getRequestDispatcher("/Interface/GestoreVerificheGUI/inserisciArticolo.jsp")
                    .forward(request, response);
        }
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }
}