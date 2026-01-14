package it.unisa.applicationLogic.UtenteManagement;

import java.io.IOException;

import it.unisa.storage.Utente;
import it.unisa.storage.UtenteDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDao userDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UtenteDao();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        Utente user = userDAO.authenticateUser(email, password);
        
        if (user != null) {
            // Login successful
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userName", user.getNome() + " " + user.getCognome());
            session.setAttribute("userRole", user.getRuolo());
            
            // Redirect in base al ruolo
            if ("ADMIN".equals(user.getRuolo())) {
                response.sendRedirect(request.getContextPath() + "/admin-news");
            } else {
                response.sendRedirect(request.getContextPath() + "/Interface/Home.jsp");
            }
        } else {
            // Login failed
            response.sendRedirect(request.getContextPath() + "/Interface/AutenticazioneGUI/LoginFailed.jsp");
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/Interface/AutenticazioneGUI/Login.jsp").forward(request, response);
    }
}