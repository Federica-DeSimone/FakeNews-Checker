<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.storage.Notizia" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FakeNews Checker - Notizie</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">

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

        <!-- News Grid -->
        <div class="news-container">

            <!-- Banner risultati ricerca -->
            <%
                String keyword = (String) request.getAttribute("keyword");
                if (keyword != null && !keyword.isEmpty()) {
                    List<Notizia> notizieRisultato = (List<Notizia>) request.getAttribute("notizie");
            %>
            <div style="padding: 20px; text-align: center; background-color: #f0f9ff; margin-bottom: 20px; border-radius: 8px; max-width: 1200px; margin-left: auto; margin-right: auto;">
                <h2 style="color: #1e40af; margin: 0 0 10px 0; font-family: 'Poppins', sans-serif;">
                    🔍 Risultati di ricerca per: "<%= keyword %>"
                </h2>
                <p style="color: #64748b; margin: 0 0 15px 0; font-family: 'Poppins', sans-serif;">
                    <%= notizieRisultato != null ? "Trovate " + notizieRisultato.size() + " notizie" : "Nessuna notizia trovata" %>
                </p>
                <button onclick="window.location.href='<%= request.getContextPath() %>/visualizzaNotizie'"
                        style="padding: 10px 20px; background-color: #3b82f6; color: white; border: none; border-radius: 6px; cursor: pointer; font-size: 14px; font-family: 'Poppins', sans-serif; font-weight: 500;">
                    ← Mostra tutte le notizie
                </button>
            </div>
            <% } %>

            <div class="news-grid">
                <%
                    List<Notizia> notizie = (List<Notizia>) request.getAttribute("notizie");
                    if (notizie != null && !notizie.isEmpty()) {
                        for (Notizia notizia : notizie) {
                %>
                <div class="news-card" onclick="window.location.href='<%= request.getContextPath() %>/dettaglioNotizia?id=<%= notizia.getId() %>'">
                    <!-- Image -->
                    <div class="news-image">
                        <img src="<%= request.getContextPath() %>/images/<%= notizia.getImmagine() %>"
                             alt="<%= notizia.getTitolo() %>"
                             onerror="this.parentElement.innerHTML='<span class=\'image-placeholder\'>Foto</span>'">
                    </div>

                    <!-- Content -->
                    <div class="news-content">
                        <h3 class="news-title"><%= notizia.getTitolo() %></h3>
                        <p class="news-description"><%= notizia.getDescrizione() %></p>

                        <!-- Status Badge -->
                        <div class="news-status" onclick="event.stopPropagation();">
                            <%
                                String stato = notizia.getStato();
                                if ("segnalata".equals(stato)) {
                            %>
                                <a href="<%= request.getContextPath() %>/inviaSegnalazione?idNotizia=<%= notizia.getId() %>&titolo=<%= java.net.URLEncoder.encode(notizia.getTitolo(), "UTF-8") %>&immagine=<%= java.net.URLEncoder.encode(notizia.getImmagine(), "UTF-8") %>"
                                   class="status-badge status-reported">
                                    <span>Segnala come sospetto</span>
                                </a>
                            <%
                                } else if ("verificata".equals(stato)) {
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
                                }
                            %>
                        </div>
                    </div>
                </div>
                <%
                        }
                    } else {
                %>
                <div class="no-news">
                    <p>Nessuna notizia disponibile al momento.</p>
                </div>
                <%
                    }
                %>
            </div>
        </div>
    </div>

    <script src="<%= request.getContextPath() %>/scripts/menu.js"></script>
</body>
</html>