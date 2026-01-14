package it.unisa.storage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class SegnalazioneDAO {

    // Inserisci nuova segnalazione con id_utente
    public boolean inserisciSegnalazione(Segnalazione segnalazione, Integer idUtente) throws SQLException {
        String query = "INSERT INTO segnalazioni (titolo, descrizione, url, autore, immagine_path, id_utente, stato) " +
                "VALUES (?, ?, ?, ?, ?, ?, 'in_verifica')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, segnalazione.getTitolo());
            stmt.setString(2, segnalazione.getDescrizione());
            stmt.setString(3, segnalazione.getUrl());
            stmt.setString(4, segnalazione.getAutore());
            stmt.setString(5, segnalazione.getImmaginePath());
            if (idUtente != null) {
                stmt.setInt(6, idUtente);
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            int affected = stmt.executeUpdate();

            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        segnalazione.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    // Vecchio metodo per retrocompatibilità
    public boolean inserisciSegnalazione(Segnalazione segnalazione) throws SQLException {
        return inserisciSegnalazione(segnalazione, null);
    }

    // Ottieni tutte le segnalazioni in attesa di verifica
    public List<Segnalazione> getSegnalazioniInVerifica() throws SQLException {
        List<Segnalazione> segnalazioni = new ArrayList<>();
        String query = "SELECT * FROM segnalazioni WHERE stato = 'in_verifica' " +
                "ORDER BY data_segnalazione DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                segnalazioni.add(mapResultSetToSegnalazione(rs));
            }
        }
        return segnalazioni;
    }

    // Ottieni segnalazioni per utente
    public List<Segnalazione> getSegnalazioniByUtente(int idUtente) throws SQLException {
        List<Segnalazione> segnalazioni = new ArrayList<>();
        String query = "SELECT * FROM segnalazioni WHERE id_utente = ? " +
                "ORDER BY data_segnalazione DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUtente);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    segnalazioni.add(mapResultSetToSegnalazione(rs));
                }
            }
        }
        return segnalazioni;
    }

    // Aggiorna stato segnalazione (Verificata o Non Attendibile)
    public boolean aggiornaStatoSegnalazione(int idSegnalazione, String nuovoStato,
                                             int idGestore, Integer idNotizia) throws SQLException {
        String query = "UPDATE segnalazioni SET stato = ?, id_gestore_verificatore = ?, data_verifica = NOW(), id_notizia = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nuovoStato);
            stmt.setInt(2, idGestore);
            if (idNotizia != null) {
                stmt.setInt(3, idNotizia);
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setInt(4, idSegnalazione);

            return stmt.executeUpdate() > 0;
        }
    }

    // Ottieni tutte le segnalazioni
    public List<Segnalazione> getTutteSegnalazioni() throws SQLException {
        List<Segnalazione> segnalazioni = new ArrayList<>();
        String query = "SELECT * FROM segnalazioni ORDER BY data_segnalazione DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                segnalazioni.add(mapResultSetToSegnalazione(rs));
            }
        }
        return segnalazioni;
    }

    // Ottieni segnalazione per ID
    public Segnalazione getSegnalazioneById(int id) throws SQLException {
        String query = "SELECT * FROM segnalazioni WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSegnalazione(rs);
                }
            }
        }
        return null;
    }

    // Ottieni statistiche segnalazioni
    public int countSegnalazioniByStato(String stato) throws SQLException {
        String query = "SELECT COUNT(*) FROM segnalazioni WHERE stato = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, stato);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    // Ottieni segnalazione per ID notizia
    public Segnalazione getSegnalazioneByIdNotizia(int idNotizia) throws SQLException {
        String query = "SELECT * FROM segnalazioni WHERE id_notizia = ? LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idNotizia);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSegnalazione(rs);
                }
            }
        }
        return null;
    }

    private Segnalazione mapResultSetToSegnalazione(ResultSet rs) throws SQLException {
        Segnalazione s = new Segnalazione();
        s.setId(rs.getInt("id"));
        s.setNumeroSegnalazione(rs.getString("numero_segnalazione"));
        s.setTitolo(rs.getString("titolo"));
        s.setDescrizione(rs.getString("descrizione"));
        s.setUrl(rs.getString("url"));
        s.setAutore(rs.getString("autore"));
        s.setImmaginePath(rs.getString("immagine_path"));
        s.setDataSegnalazione(rs.getTimestamp("data_segnalazione"));
        s.setStato(rs.getString("stato"));

        // Campi aggiuntivi
        int idUtente = rs.getInt("id_utente");
        if (!rs.wasNull()) {
            s.setIdUtente(idUtente);
        }

        int idNotizia = rs.getInt("id_notizia");
        if (!rs.wasNull()) {
            s.setIdNotizia(idNotizia);
        }

        int idGestore = rs.getInt("id_gestore_verificatore");
        if (!rs.wasNull()) {
            s.setIdGestore(idGestore);
        }

        Timestamp dataVerifica = rs.getTimestamp("data_verifica");
        if (dataVerifica != null) {
            s.setDataVerifica(dataVerifica);
        }

        return s;
    }
}