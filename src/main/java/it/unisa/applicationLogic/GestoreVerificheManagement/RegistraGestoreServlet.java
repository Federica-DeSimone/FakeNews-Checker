package it.unisa.applicationLogic.GestoreVerificheManagement;

import it.unisa.storage.Gestore;
import it.unisa.storage.GestoreDAO;


import java.io.IOException;
import java.sql.SQLException;

import it.unisa.utils.PasswordUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/gestoreVerifiche/registraGestore")
public class RegistraGestoreServlet extends HttpServlet {
    
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
        
        request.getRequestDispatcher("/Interface/GestoreVerificheGUI/registraGestore.jsp")
               .forward(request, response);
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
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
        
        // ID del gestore che sta registrando
        Integer gestoreId = (Integer) session.getAttribute("gestoreId");
        
        // Leggi parametri
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String telefono = request.getParameter("telefono");
        String tipoRuolo = request.getParameter("ruolo");
        
        // Validazione
        if (nome == null || nome.trim().isEmpty() ||
            cognome == null || cognome.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            tipoRuolo == null || tipoRuolo.trim().isEmpty()) {
            
            request.setAttribute("errore", "Tutti i campi obbligatori devono essere compilati");
            request.getRequestDispatcher("/Interface/GestoreVerificheGUI/registraGestore.jsp")
                   .forward(request, response);
            return;
        }
        
        try {
            // Verifica se email già esiste
            if (gestoreDAO.emailEsiste(email)) {
                request.setAttribute("errore", "Email già registrata nel sistema");
                request.getRequestDispatcher("/Interface/GestoreVerificheGUI/registraGestore.jsp")
                       .forward(request, response);
                return;
            }
            
            // Crea nuovo gestore
            Gestore nuovoGestore = new Gestore();
            nuovoGestore.setNome(nome);
            nuovoGestore.setCognome(cognome);
            nuovoGestore.setEmail(email);
            nuovoGestore.setTelefono(telefono);
            
            // Imposta ruolo
            if ("admin-verifiche".equals(tipoRuolo)) {
                nuovoGestore.setRuolo(Gestore.TipoGestore.GESTORE_VERIFICHE);
            } else if ("admin-tecnico".equals(tipoRuolo)) {
                nuovoGestore.setRuolo(Gestore.TipoGestore.GESTORE_TECNICO);
            } else {
                request.setAttribute("errore", "Ruolo non valido");
                request.getRequestDispatcher("/Interface/GestoreVerificheGUI/registraGestore.jsp")
                       .forward(request, response);
                return;
            }
            
            // Hash password
            String passwordHash = PasswordUtils.hashPassword(password);
            
            // Registra gestore (con l'ID di chi lo sta registrando)
            boolean success = gestoreDAO.registraGestore(nuovoGestore, passwordHash, gestoreId);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/Interface/GestoreVerificheGUI/successoRegistrazioneGestore.jsp");
            } else {
                request.setAttribute("errore", "Errore nella registrazione del gestore");
                request.getRequestDispatcher("/Interface/GestoreVerificheGUI/registraGestore.jsp")
                       .forward(request, response);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errore", "Errore database: " + e.getMessage());
            request.getRequestDispatcher("/Interface/GestoreVerificheGUI/registraGestore.jsp")
                   .forward(request, response);
        }
    }
}