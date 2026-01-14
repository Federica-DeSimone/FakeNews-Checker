package it.unisa.applicationLogic.GestoreVerificheManagement;



import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import it.unisa.storage.Gestore;
import it.unisa.storage.GestoreDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/gestoreVerifiche/elencoGestori")
public class ElencoGestoriServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private GestoreDAO gestoreDAO;
    
    @Override
    public void init() throws ServletException {
        gestoreDAO = new GestoreDAO();
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
            List<Gestore> gestori = gestoreDAO.getTuttiGestori();
            request.setAttribute("gestori", gestori);
            
            request.getRequestDispatcher("/Interface/GestoreVerificheGUI/elencoGestori.jsp")
                   .forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errore", "Errore nel caricamento dei gestori");
            request.getRequestDispatcher("/Interface/GestoreVerificheGUI/errore.jsp")
                   .forward(request, response);
        }
    }
}