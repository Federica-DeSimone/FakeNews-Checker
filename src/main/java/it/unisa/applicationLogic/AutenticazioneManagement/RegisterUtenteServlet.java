package it.unisa.applicationLogic.AutenticazioneManagement;

import it.unisa.storage.UtenteDao;
import it.unisa.utils.PasswordUtils;



import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import it.unisa.storage.Utente;


@WebServlet("/register")
public class RegisterUtenteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDao utenteDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        utenteDAO = new UtenteDao();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String telefono = request.getParameter("telefono");

        // Validazione
        if (nome == null || nome.trim().isEmpty() ||
                cognome == null || cognome.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {

            request.setAttribute("errore", "Tutti i campi obbligatori devono essere compilati");
            request.getRequestDispatcher("/Interface/AutenticazioneGUI/Register.jsp")
                    .forward(request, response);
            return;
        }

        // Verifica se email già esiste
        if (utenteDAO.emailExists(email)) {
            request.setAttribute("errore", "Email già registrata");
            request.getRequestDispatcher("/Interface/AutenticazioneGUI/Register.jsp")
                    .forward(request, response);
            return;
        }

        // Crea nuovo utente
        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome(nome);
        nuovoUtente.setCognome(cognome);
        nuovoUtente.setEmail(email);
        nuovoUtente.setTelefono(telefono);
        nuovoUtente.setRuolo("UTENTE");

        // Hash password
        String passwordHash = PasswordUtils.hashPassword(password);

        try {
            // Registra utente
            boolean success = utenteDAO.registraUtente(nuovoUtente, passwordHash);

            if (success) {
                // IMPORTANTE: Dopo registrazione, fai login automatico
                // Recupera l'utente completo dal database
                Utente utenteCompleto = utenteDAO.getUserByEmail(email);

                if (utenteCompleto != null) {
                    // Crea sessione con TUTTI gli attributi necessari
                    HttpSession session = request.getSession();
                    session.setAttribute("user", utenteCompleto);
                    session.setAttribute("userEmail", utenteCompleto.getEmail());
                    session.setAttribute("userName", utenteCompleto.getNome() + " " + utenteCompleto.getCognome());
                    session.setAttribute("userRole", utenteCompleto.getRuolo());
                    session.setAttribute("userType", "UTENTE");

                    System.out.println("DEBUG - Registrazione completata. Utente ID: " + utenteCompleto.getId());

                    // Redirect alla home
                    response.sendRedirect(request.getContextPath() + "/Interface/Home.jsp");
                } else {
                    request.setAttribute("errore", "Errore nel recupero dei dati utente");
                    request.getRequestDispatcher("/Interface/AutenticazioneGUI/Register.jsp")
                            .forward(request, response);
                }
            } else {
                request.setAttribute("errore", "Errore nella registrazione");
                request.getRequestDispatcher("/Interface/AutenticazioneGUI/Register.jsp")
                        .forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errore", "Errore: " + e.getMessage());
            request.getRequestDispatcher("/Interface/AutenticazioneGUI/Register.jsp")
                    .forward(request, response);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/Interface/AutenticazioneGUI/Register.jsp")
                .forward(request, response);
    }
}