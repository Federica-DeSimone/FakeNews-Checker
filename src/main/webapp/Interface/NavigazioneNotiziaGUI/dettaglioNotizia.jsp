<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.unisa.storage.Notizia" %>
<%@ page import="it.unisa.storage.Segnalazione" %>
<%@ page import="it.unisa.storage.SegnalazioneDAO" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dettaglio Notizia - FakeNews Checker</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
	 <style>
    /* 1. Forza la centratura della dialog box */
    .search-dialog {
        display: none; /* Gestito dal tuo JS */
        position: fixed !important;
        top: 0 !important;
        left: 0 !important;
        width: 100% !important;
        height: 100% !important;
        background: rgba(0, 0, 0, 0.7) !important;
        z-index: 9999 !important;
        align-items: center !important;
        justify-content: center !important;
    }

    /* 2. Sistema il contenitore bianco (quello della foto) */
    .dialog-box {
        display: flex !important;
        flex-direction: column !important;
        align-items: center !important;
        width: 90% !important;
        max-width: 500px !important;
        padding: 30px !important;
        box-sizing: border-box !important;
        position: relative !important;
    top: -120px !important; /* Lo sposta esattamente di 30px verso l'alto rispetto alla sua posizione */

    }

    /* 3. CORREZIONE ICONA (Il problema principale della foto) */
    .input-wrapper {
        position: relative !important;
        width: 100% !important;
        margin: 20px 0 !important;
    }

    .search-icon {
        position: absolute !important;
        left: 15px !important;
        top: 50% !important;
        transform: translateY(-50%) !important;
        z-index: 10 !important;
        margin: 0 !important; /* Rimuove spostamenti indesiderati */
    }

    .search-input {
        width: 100% !important; /* Forza la larghezza piena nel box */
        padding-left: 45px !important; /* Sposta il testo per non coprire l'icona */
        padding-right: 15px !important;
        height: 50px !important;
        box-sizing: border-box !important; /* Impedisce all'input di uscire dai bordi */
        border: 2px solid #3b82f6 !important;
        border-radius: 12px !important;
    }

    /* 4. Sistema i pulsanti sotto l'input */
    .search-confirm, .dialog-close {
        width: 100% !important;
        margin-top: 10px !important;
    }
</style>
</head>
<body>
    <div class="min-h-screen">
        <!-- Navigation Bar -->
        <jsp:include page="/Interface/Header.jsp" />

        <!-- Dettaglio Notizia -->
        <div class="detail-container">
            <%
                Notizia notizia = (Notizia) request.getAttribute("notizia");
                if (notizia != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                    // Ottieni l'URL fonte dalla segnalazione se la notizia è verificata
                    String urlFonte = null;
                    if ("verificata".equals(notizia.getStato())) {
                        try {
                            SegnalazioneDAO segDAO = new SegnalazioneDAO();
                            Segnalazione seg = segDAO.getSegnalazioneByIdNotizia(notizia.getId());
                            if (seg != null) {
                                urlFonte = seg.getUrl();
                            }
                        } catch (Exception e) {
                            // Ignora errori
                        }
                    }
            %>
            <div class="detail-card">
                <!-- Status Badge -->
                <div class="detail-status">
                    <%
                        String stato = notizia.getStato();
                        if ("verificata".equals(stato)) {
                    %>
                        <div class="status-badge status-verified">
                            <svg class="status-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                                <polyline points="22 4 12 14.01 9 11.01"></polyline>
                            </svg>
                            <span>Verificata</span>
                        </div>
                    <%
                        } else if ("in_verifica".equals(stato)) {
                    %>
                        <div class="status-badge status-verifying">
                            <svg class="status-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <circle cx="12" cy="12" r="10"></circle>
                                <line x1="12" y1="8" x2="12" y2="12"></line>
                                <line x1="12" y1="16" x2="12.01" y2="16"></line>
                            </svg>
                            <span>In Verifica</span>
                        </div>
                    <%
                        } else {
                    %>
                        <a href="<%= request.getContextPath() %>/inviaSegnalazione?idNotizia=<%= notizia.getId() %>&titolo=<%= java.net.URLEncoder.encode(notizia.getTitolo(), "UTF-8") %>"
                           class="status-badge status-reported">
                            <span>Segnala come sospetto</span>
                        </a>
                    <%
                        }
                    %>
                </div>

                <!-- Image -->
                <div class="detail-image">
                    <img src="<%= request.getContextPath() %>/images/<%= notizia.getImmagine() %>"
                         alt="<%= notizia.getTitolo() %>"
                         onerror="this.parentElement.innerHTML='<span class=\'image-placeholder-large\'>Foto non disponibile</span>'">
                </div>

                <!-- Title -->
                <h1 class="detail-title"><%= notizia.getTitolo() %></h1>

                <!-- Description -->
                <p class="detail-description"><%= notizia.getDescrizione() %></p>

                <!-- Metadata -->
                <div class="detail-metadata">
                    <% if (notizia.getAutore() != null && !notizia.getAutore().isEmpty()) { %>
                        <p><strong>Autore:</strong> <%= notizia.getAutore() %></p>
                    <% } %>
                    <p><strong>Data pubblicazione:</strong> <%= sdf.format(notizia.getDataPubblicazione()) %></p>

                    <!-- URL Fonte (se verificata) -->
                    <% if (urlFonte != null && !urlFonte.isEmpty()) { %>
                        <p>
                            <strong>Fonte:</strong>
                            <a href="<%= urlFonte %>" target="_blank" rel="noopener noreferrer"
                               style="color: #4169e1; text-decoration: none; word-break: break-all;">
                                <%= urlFonte %>
                            </a>
                        </p>
                    <% } %>
                </div>

                <!-- Back Button -->
                <div class="detail-actions">
                    <a href="<%= request.getContextPath() %>/visualizzaNotizie" class="btn-back">
                        ← Torna alle notizie
                    </a>
                </div>
            </div>
            <%
                } else {
            %>
            <div class="error-message">
                <p>Notizia non trovata.</p>
                <a href="<%= request.getContextPath() %>/visualizzaNotizie" class="btn-back">
                    Torna alle notizie
                </a>
            </div>
            <%
                }
            %>
        </div>
    </div>
</body>
</html>