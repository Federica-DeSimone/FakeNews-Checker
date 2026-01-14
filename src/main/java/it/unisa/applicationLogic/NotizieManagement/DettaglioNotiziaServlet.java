package it.unisa.applicationLogic.NotizieManagement;

import java.io.IOException;
import java.sql.SQLException;

import it.unisa.storage.Notizia;
import it.unisa.storage.NotiziaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/dettaglioNotizia")
public class DettaglioNotiziaServlet extends HttpServlet {

    private NotiziaDAO notiziaDAO;

    @Override
    public void init() throws ServletException {
        notiziaDAO = new NotiziaDAO();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("visualizzaNotizie");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Notizia notizia = notiziaDAO.getNotiziaById(id);

            if (notizia != null) {
                request.setAttribute("notizia", notizia);
                request.getRequestDispatcher("/Interface/NavigazioneNotiziaGUI/dettaglioNotizia.jsp")
                        .forward(request, response);
            } else {
                request.setAttribute("errore", "Notizia non trovata");
                request.getRequestDispatcher("/Interface/NavigazioneNotiziaGUI/errore.jsp")
                        .forward(request, response);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("visualizzaNotizie");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errore", "Errore nel caricamento della notizia");
            request.getRequestDispatcher("/Interface/NavigazioneNotiziaGUI/errore.jsp")
                    .forward(request, response);
        }
    }
}