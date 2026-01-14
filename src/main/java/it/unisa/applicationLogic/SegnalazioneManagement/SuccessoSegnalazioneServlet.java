package it.unisa.applicationLogic.SegnalazioneManagement;

import java.io.IOException;




import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;




@WebServlet("/successoSegnalazione")
public class SuccessoSegnalazioneServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/Interface/SegnalazioneNotiziaGUI/successoSegnalazione.jsp")
                .forward(request, response);
    }
}