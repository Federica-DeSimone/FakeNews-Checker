package it.unisa.storage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestoreDAO {
    
    public Gestore login(String email, String passwordHash) throws SQLException {
        String query = "SELECT * FROM gestori WHERE email = ? AND password_hash = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email);
            stmt.setString(2, passwordHash);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGestore(rs);
                }
            }
        }
        return null;
    }
    
    public boolean registraGestore(Gestore gestore, String passwordHash, Integer creatoDa) throws SQLException {
        String query = "INSERT INTO gestori (nome, cognome, email, password_hash, telefono, ruolo, creato_da) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, gestore.getNome());
            stmt.setString(2, gestore.getCognome());
            stmt.setString(3, gestore.getEmail());
            stmt.setString(4, passwordHash);
            stmt.setString(5, gestore.getTelefono());
            stmt.setString(6, gestore.getRuolo().toString());
            if (creatoDa != null) {
                stmt.setInt(7, creatoDa);
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        gestore.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public List<Gestore> getTuttiGestori() throws SQLException {
        List<Gestore> gestori = new ArrayList<>();
        String query = "SELECT * FROM gestori ORDER BY data_registrazione DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                gestori.add(mapResultSetToGestore(rs));
            }
        }
        return gestori;
    }
    
    public Gestore getGestoreById(int id) throws SQLException {
        String query = "SELECT * FROM gestori WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGestore(rs);
                }
            }
        }
        return null;
    }
    
    public boolean emailEsiste(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM gestori WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    private Gestore mapResultSetToGestore(ResultSet rs) throws SQLException {
        Gestore gestore = new Gestore();
        gestore.setId(rs.getInt("id"));
        gestore.setNome(rs.getString("nome"));
        gestore.setCognome(rs.getString("cognome"));
        gestore.setEmail(rs.getString("email"));
        gestore.setTelefono(rs.getString("telefono"));
        gestore.setPasswordHash(rs.getString("password_hash"));
        gestore.setDataRegistrazione(rs.getTimestamp("data_registrazione"));
        
        String ruolo = rs.getString("ruolo");
        if ("GESTORE_VERIFICHE".equals(ruolo)) {
            gestore.setRuolo(Gestore.TipoGestore.GESTORE_VERIFICHE);
        } else if ("GESTORE_TECNICO".equals(ruolo)) {
            gestore.setRuolo(Gestore.TipoGestore.GESTORE_TECNICO);
        }
        
        return gestore;
    }
}