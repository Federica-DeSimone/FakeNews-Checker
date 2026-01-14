package it.unisa.applicationLogic.AutenticazioneManagement;

import java.io.IOException;
import java.sql.*;

import it.unisa.storage.Gestore;
import it.unisa.storage.GestoreDAO;
import it.unisa.storage.Utente;
import it.unisa.storage.UtenteDao;
import it.unisa.utils.PasswordUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private GestoreDAO gestoreDAO;
    private UtenteDao utenteDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        gestoreDAO = new GestoreDAO();
        utenteDAO = new UtenteDao();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {

            response.sendRedirect(request.getContextPath() + "/Interface/AutenticazioneGUI/LoginFailed.jsp");
            return;
        }

        try {
            // PRIMA: Prova login come GESTORE
            String passwordHashGestore = PasswordUtils.hashPassword(password);
            Gestore gestore = gestoreDAO.login(email, passwordHashGestore);

            if (gestore != null) {
                // Login gestore riuscito
                HttpSession session = request.getSession();
                session.setAttribute("gestoreId", gestore.getId());
                session.setAttribute("gestoreNome", gestore.getNomeCompleto());
                session.setAttribute("gestoreEmail", gestore.getEmail());
                session.setAttribute("gestoreRuolo", gestore.getRuolo().toString());
                session.setAttribute("userType", "GESTORE");

                // Redirect in base al ruolo
                if (gestore.getRuolo() == Gestore.TipoGestore.GESTORE_VERIFICHE) {
                    response.sendRedirect(request.getContextPath() + "/gestoreVerifiche/dashboard");
                } else if (gestore.getRuolo() == Gestore.TipoGestore.GESTORE_TECNICO) {
                    response.sendRedirect(request.getContextPath() + "/gestoreTecnico/dashboard");
                }
                return;
            }

            // SECONDO: Prova login come UTENTE NORMALE
            Utente utente = utenteDAO.authenticateUser(email, password);

            if (utente != null) {
                // Login utente riuscito
                HttpSession session = request.getSession();
                session.setAttribute("user", utente);
                session.setAttribute("userEmail", utente.getEmail());
                session.setAttribute("userName", utente.getNome() + " " + utente.getCognome());
                session.setAttribute("userRole", utente.getRuolo());
                session.setAttribute("userType", "UTENTE");

                // CONTROLLA SE C'È UN REDIRECT SALVATO
                String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");

                if (redirectAfterLogin != null) {
                    // Rimuovi l'attributo dalla sessione
                    session.removeAttribute("redirectAfterLogin");
                    // Redirect alla pagina salvata
                    response.sendRedirect(redirectAfterLogin);
                } else {
                    // Redirect normale alla Home
                    response.sendRedirect(request.getContextPath() + "/Interface/Home.jsp");
                }
                return;
            }

            // NESSUN UTENTE TROVATO - credenziali errate
            response.sendRedirect(request.getContextPath() + "/Interface/AutenticazioneGUI/LoginFailed.jsp");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/Interface/AutenticazioneGUI/LoginFailed.jsp");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/Interface/AutenticazioneGUI/Login.jsp")
                .forward(request, response);
    }
}