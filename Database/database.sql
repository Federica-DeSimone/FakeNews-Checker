CREATE DATABASE IF NOT EXISTS FAKENEWSCHECKER;
USE FAKENEWSCHECKER;

-- ===========================
-- TABELLA: GESTORI
-- ===========================
CREATE TABLE gestori (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    data_registrazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ruolo ENUM('GESTORE_VERIFICHE','GESTORE_TECNICO') NOT NULL,
    creato_da INT,
    PRIMARY KEY (id),
    UNIQUE KEY email (email),
    KEY ruolo (ruolo),
    KEY creato_da (creato_da)
);

-- ===========================
-- TABELLA: UTENTI
-- ===========================
CREATE TABLE utenti (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    data_registrazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ruolo ENUM('UTENTE') DEFAULT 'UTENTE',
    PRIMARY KEY (id),
    UNIQUE KEY email (email),
    KEY ruolo (ruolo)
);

-- ===========================
-- TABELLA: NOTIZIE
-- ===========================
CREATE TABLE notizie (
    id INT NOT NULL AUTO_INCREMENT,
    titolo VARCHAR(500) NOT NULL,
    descrizione TEXT NOT NULL,
    immagine VARCHAR(255) NOT NULL,
    stato VARCHAR(20),
    data_pubblicazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    autore VARCHAR(255),
    PRIMARY KEY (id),
    KEY stato (stato)
);

-- ===========================
-- TABELLA: SEGNALAZIONI
-- ===========================
CREATE TABLE segnalazioni (
    id INT NOT NULL AUTO_INCREMENT,
    numero_segnalazione VARCHAR(50),
    titolo VARCHAR(500) NOT NULL,
    descrizione TEXT NOT NULL,
    url VARCHAR(1000) NOT NULL,
    autore VARCHAR(255),
    id_utente INT,
    id_notizia INT,
    immagine_path VARCHAR(500),
    data_segnalazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_verifica TIMESTAMP NULL,
    stato ENUM('in_verifica','verificata','non_attendibile') DEFAULT 'in_verifica',
    id_gestore_verificatore INT,
    PRIMARY KEY (id),
    UNIQUE KEY numero_segnalazione (numero_segnalazione),
    KEY id_utente (id_utente),
    KEY id_notizia (id_notizia),
    KEY stato (stato),
    KEY id_gestore_verificatore (id_gestore_verificatore)
);

-- ===========================
-- VISTA: VISTA_STATISTICHE_SISTEMA
-- ===========================
CREATE OR REPLACE VIEW vista_statistiche_sistema AS
SELECT
    (SELECT COUNT(*) FROM utenti) AS totale_utenti,
    (SELECT COUNT(*) FROM gestori) AS totale_gestori,
    (SELECT COUNT(*) FROM notizie) AS totale_notizie,
    (SELECT COUNT(*) FROM segnalazioni) AS totale_segnalazioni,
    (SELECT COUNT(*) FROM segnalazioni WHERE stato = 'in_verifica') AS segnalazioni_pending,
    (SELECT COUNT(*) FROM segnalazioni WHERE stato = 'verificata') AS segnalazioni_verificate,
    (SELECT COUNT(*) FROM segnalazioni WHERE stato = 'non_attendibile') AS segnalazioni_rifiutate,
    (SELECT COUNT(*) FROM notizie WHERE stato = 'verificata') AS notizie_verificate;
