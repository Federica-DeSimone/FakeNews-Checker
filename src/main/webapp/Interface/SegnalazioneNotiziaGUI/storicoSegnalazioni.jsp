<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.storage.Segnalazione" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Storico Segnalazioni - FakeNews Checker</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<div class="min-h-screen">
    <!-- Include Header -->
    <jsp:include page="/Interface/Header.jsp" />

    <!-- Storico Segnalazioni -->
    <div class="news-container">
        <h1 class="page-title">Le mie Segnalazioni</h1>

        <div class="news-grid">
            <%
                List<Segnalazione> segnalazioni = (List<Segnalazione>) request.getAttribute("segnalazioni");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                if (segnalazioni != null && !segnalazioni.isEmpty()) {
                    for (Segnalazione seg : segnalazioni) {
            %>
            <div class="news-card">
                <!-- Image -->
                <div class="news-image">
                    <% if (seg.getImmaginePath() != null && !seg.getImmaginePath().isEmpty()) { %>
                    <img src="<%= request.getContextPath() %>/images/<%= seg.getImmaginePath() %>"
                         alt="<%= seg.getTitolo() %>"
                         onerror="this.parentElement.innerHTML='<span class=\'image-placeholder\'>Foto</span>'">

                    <% } else { %>
                    <span class="image-placeholder">Foto</span>
                    <% } %>
                </div>

                <!-- Content -->
                <div class="news-content">
                    <h3 class="news-title"><%= seg.getTitolo() %></h3>
                    <p class="news-description"><%= seg.getDescrizione() %></p>

                    <!-- Info aggiuntive -->
                    <div class="segnalazione-info">
                        <p class="segnalazione-url">
                            <strong>Fonte:</strong>
                            <a href="<%= seg.getUrl() %>" target="_blank" class="url-link">
                                <%= seg.getUrl().length() > 50 ? seg.getUrl().substring(0, 50) + "..." : seg.getUrl() %>
                            </a>
                        </p>
                        <% if (seg.getAutore() != null && !seg.getAutore().isEmpty()) { %>
                        <p class="segnalazione-autore"><strong>Autore:</strong> <%= seg.getAutore() %></p>
                        <% } %>
                        <p class="segnalazione-data"><strong>Data:</strong> <%= sdf.format(seg.getDataSegnalazione()) %></p>
                        <% if (seg.getNumeroSegnalazione() != null) { %>
                        <p class="segnalazione-numero"><strong>N. Segnalazione:</strong> <%= seg.getNumeroSegnalazione() %></p>
                        <% } %>
                    </div>

                    <!-- Status Badge -->
                    <div class="news-status">
                        <%
                            String stato = seg.getStato();
                            if ("in_verifica".equals(stato)) {
                        %>
                        <div class="status-badge status-verifying">
                            <svg class="status-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <circle cx="12" cy="12" r="10"></circle>
                                <polyline points="12 6 12 12 16 14"></polyline>
                            </svg>
                            <span>In Verifica</span>
                        </div>
                        <%
                        } else if ("verificata".equals(stato)) {
                        %>
                        <div class="status-badge status-verified">
                            <svg class="status-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                                <polyline points="22 4 12 14.01 9 11.01"></polyline>
                            </svg>
                            <span>Articolo Pubblicato</span>
                        </div>
                        <%
                        } else if ("non_attendibile".equals(stato)) {
                        %>
                        <div class="status-badge status-rejected">
                            <svg class="status-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <circle cx="12" cy="12" r="10"></circle>
                                <line x1="15" y1="9" x2="9" y2="15"></line>
                                <line x1="9" y1="9" x2="15" y2="15"></line>
                            </svg>
                            <span>Articolo non pubblicato perché non attendibile</span>
                        </div>
                        <%
                            }
                        %>
                    </div>

                    <!-- Link all'articolo se pubblicato -->
                    <% if ("verificata".equals(stato) && seg.getIdNotizia() != null) { %>
                    <div style="margin-top: 1rem;">
                        <a href="<%= request.getContextPath() %>/dettaglioNotizia?id=<%= seg.getIdNotizia() %>"
                           class="btn-view-article"
                           style="display: inline-block; padding: 0.5rem 1rem; background-color: #10b981; color: white; text-decoration: none; border-radius: 0.375rem; font-size: 0.875rem; transition: background-color 0.2s;">
                            📰 Visualizza Articolo Pubblicato
                        </a>
                    </div>
                    <% } %>
                </div>
            </div>
            <%
                }
            } else {
            %>
            <div class="no-news">
                <p>Non hai ancora effettuato segnalazioni.</p>
                <a href="<%= request.getContextPath() %>/visualizzaNotizie"
                   style="display: inline-block; margin-top: 1rem; padding: 0.75rem 1.5rem; background-color: #4169e1; color: white; text-decoration: none; border-radius: 0.5rem; font-weight: 500;">
                    Sfoglia le Notizie
                </a>
            </div>
            <%
                }
            %>
        </div>
    </div>
</div>
</body>
</html>