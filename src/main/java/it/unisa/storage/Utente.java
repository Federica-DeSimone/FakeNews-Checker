package it.unisa.storage;


import java.sql.Timestamp;

public class Utente {
    private int id;
    private String nome;
    private String cognome;
    private String telefono;
    private String email;
    private String passwordHash;
    private Timestamp dataRegistrazione;
    private String ruolo;
    
    // Costruttori
    public Utente() {}
    
    public Utente(String nome, String cognome, String telefono, String email, String passwordHash) {
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.email = email;
        this.passwordHash = passwordHash;
        this.ruolo = "UTENTE";
    }
    
    // Getter e Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public Timestamp getDataRegistrazione() { return dataRegistrazione; }
    public void setDataRegistrazione(Timestamp dataRegistrazione) { this.dataRegistrazione = dataRegistrazione; }
    
    public String getRuolo() { return ruolo; }
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }
}