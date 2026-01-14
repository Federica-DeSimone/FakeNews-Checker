package it.unisa.applicationLogic.UtenteManagement;


import java.io.IOException;

import it.unisa.storage.Utente;
import it.unisa.storage.UtenteDao;
import it.unisa.utils.PasswordUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDao userDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UtenteDao();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Validazione base
        if (nome == null || nome.trim().isEmpty() ||
            cognome == null || cognome.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            
            request.setAttribute("errorMessage", "Tutti i campi obbligatori devono essere compilati");
            request.getRequestDispatcher("/Interface/Register.jsp").forward(request, response); // CORRETTO
            return;
        }
        
        // Verifica se l'email esiste già
        if (userDAO.emailExists(email)) {
            request.setAttribute("errorMessage", "Email già registrata");
            request.getRequestDispatcher("/Interface/Register.jsp").forward(request, response); // CORRETTO
            return;
        }
        
        // Crea nuovo utente
        String hashedPassword = PasswordUtils.hashPassword(password);
        Utente newUser = new Utente(nome, cognome, telefono, email, hashedPassword);
        
        if (userDAO.createUser(newUser)) {
            // Registrazione successful
            HttpSession session = request.getSession();
            session.setAttribute("user", newUser);
            session.setAttribute("userEmail", newUser.getEmail());
            session.setAttribute("userName", newUser.getNome() + " " + newUser.getCognome());
            
            response.sendRedirect(request.getContextPath() + "/Interface/Home.jsp"); // CORRETTO
        } else {
            request.setAttribute("errorMessage", "Errore durante la registrazione. Riprova.");
            request.getRequestDispatcher("/Interface/Register.jsp").forward(request, response); // CORRETTO
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/Interface/Register.jsp").forward(request, response); // CORRETTO
    }
}