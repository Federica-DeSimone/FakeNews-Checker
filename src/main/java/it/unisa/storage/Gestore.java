package it.unisa.storage;

import java.sql.Timestamp;

public class Gestore {
    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private String passwordHash;
    private Timestamp dataRegistrazione;
    private TipoGestore ruolo;
    
    public enum TipoGestore {
        GESTORE_VERIFICHE,
        GESTORE_TECNICO
    }
    
    // Costruttori
    public Gestore() {}
    
    public Gestore(int id, String nome, String cognome, String email, 
                   String telefono, TipoGestore ruolo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
        this.ruolo = ruolo;
    }
    
    // Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCognome() {
        return cognome;
    }
    
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public Timestamp getDataRegistrazione() {
        return dataRegistrazione;
    }
    
    public void setDataRegistrazione(Timestamp dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }
    
    public TipoGestore getRuolo() {
        return ruolo;
    }
    
    public void setRuolo(TipoGestore ruolo) {
        this.ruolo = ruolo;
    }
    
    public String getNomeCompleto() {
        return nome + " " + cognome;
    }
}