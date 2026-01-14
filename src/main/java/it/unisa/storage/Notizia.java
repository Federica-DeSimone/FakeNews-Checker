package it.unisa.storage;

import java.sql.Timestamp;


public class Notizia {
    private int id;
    private String titolo;
    private String descrizione;
    private String immagine;
    private String stato; // segnalata, verificata, in_verifica
    private Timestamp dataPubblicazione;
    private String autore;
    
    // Costruttore vuoto
    public Notizia() {
    }
    
    // Costruttore completo
    public Notizia(int id, String titolo, String descrizione, String immagine, 
                   String stato, Timestamp dataPubblicazione, String autore) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.immagine = immagine;
        this.stato = stato;
        this.dataPubblicazione = dataPubblicazione;
        this.autore = autore;
    }
    
    // Getter e Setter
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitolo() {
        return titolo;
    }
    
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public String getImmagine() {
        return immagine;
    }
    
    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }
    
    public String getStato() {
        return stato;
    }
    
    public void setStato(String stato) {
        this.stato = stato;
    }
    
    public Timestamp getDataPubblicazione() {
        return dataPubblicazione;
    }
    
    public void setDataPubblicazione(Timestamp dataPubblicazione) {
        this.dataPubblicazione = dataPubblicazione;
    }
    
    public String getAutore() {
        return autore;
    }
    
    public void setAutore(String autore) {
        this.autore = autore;
    }
}