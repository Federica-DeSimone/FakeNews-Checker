package it.unisa.applicationLogic.NotizieManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;
import it.unisa.storage.Notizia;
import it.unisa.storage.NotiziaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/cercaNotizie")
public class CercaNotizieServlet extends HttpServlet {
    private NotiziaDAO notiziaDAO;

    @Override
    public void init() throws ServletException {
        notiziaDAO = new NotiziaDAO();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        boolean ajax = "true".equals(request.getParameter("ajax"));

        if (keyword == null || keyword.trim().isEmpty()) {
            if (ajax) {
                sendJsonResponse(response, new String[]{});
            } else {
                request.setAttribute("error", "Inserisci un termine di ricerca");
                request.getRequestDispatcher("/Interface/NavigazioneNotiziaGUI/visualizzaNotizie.jsp")
                        .forward(request, response);
            }
            return;
        }

        List<Notizia> notizie = notiziaDAO.cercaNotizie(keyword);

        if (ajax) {
            sendJsonResponse(response, notizie);
        } else {
            request.setAttribute("notizie", notizie);
            request.setAttribute("searchKeyword", keyword);
            request.getRequestDispatcher("/Interface/NavigazioneNotiziaGUI/visualizzaNotizie.jsp")
                    .forward(request, response);
        }
    }

    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(data));
    }
}