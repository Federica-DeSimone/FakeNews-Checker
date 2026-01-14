package it.unisa.applicationLogic.UtenteManagement;

import it.unisa.storage.Utente;
import it.unisa.storage.UtenteDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/update-profile")
public class UpdateProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDao userDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UtenteDao();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/Interface/Login.jsp");
            return;
        }
        
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            response.sendRedirect(request.getContextPath() + "/Interface/Login.jsp");
            return;
        }
        
        // Recupera i dati dal form
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String telefono = request.getParameter("telefono");
        
        // Validazione
        if (nome == null || nome.trim().isEmpty() || cognome == null || cognome.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Nome e cognome sono obbligatori");
            response.sendRedirect(request.getContextPath() + "/Interface/UserProfile.jsp");
            return;
        }
        
        try {
            // Recupera l'utente corrente
            Utente currentUser = userDAO.getUserByEmail(userEmail);
            if (currentUser == null) {
                session.setAttribute("errorMessage", "Utente non trovato");
                response.sendRedirect(request.getContextPath() + "/Interface/Login.jsp");
                return;
            }
            
            // Aggiorna i dati
            currentUser.setNome(nome.trim());
            currentUser.setCognome(cognome.trim());
            currentUser.setTelefono(telefono != null ? telefono.trim() : null);
            
            // Salva nel database (devi implementare updateUser in UserDAO)
            boolean success = userDAO.updateUser(currentUser);
            
            if (success) {
                // Aggiorna la sessione
                session.setAttribute("userName", currentUser.getNome() + " " + currentUser.getCognome());
                session.setAttribute("user", currentUser);
                session.setAttribute("successMessage", "Profilo aggiornato con successo");
            } else {
                session.setAttribute("errorMessage", "Errore durante l'aggiornamento del profilo");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Errore durante l'aggiornamento del profilo");
        }
        
        response.sendRedirect(request.getContextPath() + "/Interface/UserProfile.jsp");
    }
}