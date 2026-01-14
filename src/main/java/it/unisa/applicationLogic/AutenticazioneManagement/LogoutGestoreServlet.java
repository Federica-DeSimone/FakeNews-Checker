package it.unisa.applicationLogic.AutenticazioneManagement;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logoutGestore")
public class LogoutGestoreServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Rimuovi solo gli attributi del gestore
            session.removeAttribute("gestoreId");
            session.removeAttribute("gestoreNome");
            session.removeAttribute("gestoreEmail");
            session.removeAttribute("gestoreRuolo");
            session.removeAttribute("userType");
            
            // Invalida la sessione
            session.invalidate();
        }
        
        response.sendRedirect(request.getContextPath() + "/Interface/AutenticazioneGUI/LoginGestore.jsp");
    }
}