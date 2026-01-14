package it.unisa.applicationLogic.NotizieManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import it.unisa.storage.Notizia;
import it.unisa.storage.NotiziaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/visualizzaNotizie")
public class VisualizzaNotizieServlet extends HttpServlet {

    private NotiziaDAO notiziaDAO;

    @Override
    public void init() throws ServletException {
        notiziaDAO = new NotiziaDAO();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Notizia> notizie = notiziaDAO.getTutteNotizie();
            request.setAttribute("notizie", notizie);
            request.getRequestDispatcher("/Interface/NavigazioneNotiziaGUI/visualizzaNotizie.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errore", "Errore nel caricamento delle notizie");
            request.getRequestDispatcher("/Interface/NavigazioneNotiziaGUI/errore.jsp")
                    .forward(request, response);
        }
    }
}