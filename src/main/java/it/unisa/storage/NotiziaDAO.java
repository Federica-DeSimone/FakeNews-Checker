package it.unisa.storage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class NotiziaDAO {

    // Inserisci nuova notizia
    public boolean inserisciNotizia(Notizia notizia) throws SQLException {
        String query = "INSERT INTO notizie (titolo, descrizione, immagine, autore, stato) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, notizia.getTitolo());
            stmt.setString(2, notizia.getDescrizione());
            stmt.setString(3, notizia.getImmagine());
            stmt.setString(4, notizia.getAutore());
            stmt.setString(5, notizia.getStato());

            int affected = stmt.executeUpdate();

            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        notizia.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    // Elimina notizia
    public boolean eliminaNotizia(int id) throws SQLException {
        String query = "DELETE FROM notizie WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        }
    }


    // Ottieni tutte le notizie
    public List<Notizia> getTutteNotizie() throws SQLException {
        List<Notizia> notizie = new ArrayList<>();
        String query = "SELECT * FROM notizie ORDER BY data_pubblicazione DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                notizie.add(mapResultSetToNotizia(rs));
            }
        }
        return notizie;
    }

    // Ottieni notizia per ID
    public Notizia getNotiziaById(int id) throws SQLException {
        String query = "SELECT * FROM notizie WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNotizia(rs);
                }
            }
        }
        return null;
    }

    // Cerca notizie per keyword
    public List<Notizia> cercaNotizie(String keyword) {
        List<Notizia> notizie = new ArrayList<>();
        String sql = "SELECT * FROM NOTIZIE WHERE (titolo LIKE ? OR descrizione LIKE ? OR autore LIKE ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Notizia notizia = new Notizia();
                notizia.setId(rs.getInt("id"));
                notizia.setTitolo(rs.getString("titolo"));
                notizia.setDescrizione(rs.getString("descrizione"));
                notizia.setImmagine(rs.getString("immagine"));
                notizia.setStato(rs.getString("stato"));
                notizia.setDataPubblicazione(rs.getTimestamp("data_pubblicazione"));
                notizia.setAutore(rs.getString("autore"));
                notizie.add(notizia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notizie;
    }

    // Aggiorna notizia completa
    public boolean aggiornaNotizia(Notizia notizia) throws SQLException {
        String query = "UPDATE notizie SET titolo = ?, descrizione = ?, immagine = ?, autore = ?, stato = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, notizia.getTitolo());
            stmt.setString(2, notizia.getDescrizione());
            stmt.setString(3, notizia.getImmagine());
            stmt.setString(4, notizia.getAutore());
            stmt.setString(5, notizia.getStato());
            stmt.setInt(6, notizia.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    // Aggiorna stato notizia
    public boolean aggiornaStatoNotizia(int id, String nuovoStato) throws SQLException {
        String query = "UPDATE notizie SET stato = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nuovoStato);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        }
    }





    private Notizia mapResultSetToNotizia(ResultSet rs) throws SQLException {
        Notizia n = new Notizia();
        n.setId(rs.getInt("id"));
        n.setTitolo(rs.getString("titolo"));
        n.setDescrizione(rs.getString("descrizione"));
        n.setImmagine(rs.getString("immagine"));
        n.setStato(rs.getString("stato"));
        n.setDataPubblicazione(rs.getTimestamp("data_pubblicazione"));
        n.setAutore(rs.getString("autore"));
        return n;
    }
}