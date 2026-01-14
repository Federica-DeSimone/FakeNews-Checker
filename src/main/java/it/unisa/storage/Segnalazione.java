package it.unisa.storage;

import java.sql.Timestamp;

public class Segnalazione {
    private int id;
    private String numeroSegnalazione;
    private String titolo;
    private String descrizione;
    private String url;
    private String autore;
    private String immaginePath;
    private Timestamp dataSegnalazione;
    private String stato; // in_verifica, verificata, non_attendibile
    private Integer idUtente;
    private Integer idNotizia;
    private Integer idGestore;
    private Timestamp dataVerifica;
    
    // Costruttori
    public Segnalazione() {}
    
    public Segnalazione(String titolo, String descrizione, String url, String autore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.url = url;
        this.autore = autore;
        this.stato = "in_verifica";
    }
    
    // Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNumeroSegnalazione() {
        return numeroSegnalazione;
    }
    
    public void setNumeroSegnalazione(String numeroSegnalazione) {
        this.numeroSegnalazione = numeroSegnalazione;
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
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getAutore() {
        return autore;
    }
    
    public void setAutore(String autore) {
        this.autore = autore;
    }
    
    public String getImmaginePath() {
        return immaginePath;
    }
    
    public void setImmaginePath(String immaginePath) {
        this.immaginePath = immaginePath;
    }
    
    public Timestamp getDataSegnalazione() {
        return dataSegnalazione;
    }
    
    public void setDataSegnalazione(Timestamp dataSegnalazione) {
        this.dataSegnalazione = dataSegnalazione;
    }
    
    public String getStato() {
        return stato;
    }
    
    public void setStato(String stato) {
        this.stato = stato;
    }
    
    public Integer getIdUtente() {
        return idUtente;
    }
    
    public void setIdUtente(Integer idUtente) {
        this.idUtente = idUtente;
    }
    
    public Integer getIdNotizia() {
        return idNotizia;
    }
    
    public void setIdNotizia(Integer idNotizia) {
        this.idNotizia = idNotizia;
    }
    
    public Integer getIdGestore() {
        return idGestore;
    }
    
    public void setIdGestore(Integer idGestore) {
        this.idGestore = idGestore;
    }
    
    public Timestamp getDataVerifica() {
        return dataVerifica;
    }
    
    public void setDataVerifica(Timestamp dataVerifica) {
        this.dataVerifica = dataVerifica;
    }
    
    public String getStatoDescrizione() {
        switch (stato) {
            case "in_verifica":
                return "In Verifica";
            case "verificata":
                return "Verificata";
            case "non_attendibile":
                return "Notizia non pubblicata perché non attendibile";
            default:
                return stato;
        }
    }
}