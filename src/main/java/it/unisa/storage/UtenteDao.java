package it.unisa.storage;

import it.unisa.utils.PasswordUtils;

import java.sql.*;


public class UtenteDao {

    public boolean createUser(Utente user) {
        String sql = "INSERT INTO utenti (nome, cognome, telefono, email, password_hash) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getNome());
            stmt.setString(2, user.getCognome());
            stmt.setString(3, user.getTelefono());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPasswordHash());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public Utente getUserByEmail(String email) {
        String sql = "SELECT * FROM utenti WHERE email = ?";
        Utente user = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new Utente();
                user.setId(rs.getInt("id"));
                user.setNome(rs.getString("nome"));
                user.setCognome(rs.getString("cognome"));
                user.setTelefono(rs.getString("telefono"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setDataRegistrazione(rs.getTimestamp("data_registrazione"));
                user.setRuolo(rs.getString("ruolo"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // Verifica se email esiste
    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM utenti WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // Registra nuovo utente
    public boolean registraUtente(Utente utente, String passwordHash) {
        String query = "INSERT INTO utenti (nome, cognome, email, password_hash, telefono, ruolo) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, utente.getNome());
            stmt.setString(2, utente.getCognome());
            stmt.setString(3, utente.getEmail());
            stmt.setString(4, passwordHash);
            stmt.setString(5, utente.getTelefono());
            stmt.setString(6, utente.getRuolo());

            int affected = stmt.executeUpdate();

            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        utente.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Autenticazione utente
    public Utente authenticateUser(String email, String password) {
        String hashedPassword = PasswordUtils.hashPassword(password);
        String query = "SELECT * FROM utenti WHERE email = ? AND password_hash = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUtente(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Ottieni utente per ID
    public Utente getUtenteById(int id) {
        String query = "SELECT * FROM utenti WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUtente(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Utente mapResultSetToUtente(ResultSet rs) throws SQLException {
        Utente utente = new Utente();
        utente.setId(rs.getInt("id"));
        utente.setNome(rs.getString("nome"));
        utente.setCognome(rs.getString("cognome"));
        utente.setEmail(rs.getString("email"));
        utente.setTelefono(rs.getString("telefono"));
        utente.setRuolo(rs.getString("ruolo"));
        utente.setDataRegistrazione(rs.getTimestamp("data_registrazione"));
        return utente;
    }

    public boolean updateUser(Utente user) {
        String sql = "UPDATE utenti SET nome = ?, cognome = ?, telefono = ? WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getNome());
            stmt.setString(2, user.getCognome());
            stmt.setString(3, user.getTelefono());
            stmt.setString(4, user.getEmail());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}