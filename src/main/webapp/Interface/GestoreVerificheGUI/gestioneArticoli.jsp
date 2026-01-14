<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.storage.Notizia" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Articoli - FakeNews Checker</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/gestore.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <div class="min-h-screen bg-gray">
        <!-- Header -->
        <jsp:include page="/Interface/HeaderGestore.jsp" />

        <!-- Main Content -->
        <div class="container">
            <!-- Titolo -->
            <div class="page-header">
                <h1 class="page-title">Gestione Articoli</h1>
                <p class="page-subtitle">Tutti gli articoli pubblicati nel sistema</p>
            </div>

            <% if ("true".equals(request.getParameter("successo"))) { %>
            <div style="background-color: #d1fae5; border: 1px solid #10b981; color: #065f46; padding: 1rem; border-radius: 0.5rem; margin-bottom: 1.5rem;">
                ✅ Articolo inserito con successo!
            </div>
            <% } %>

            <!-- Bottone Inserisci -->
            <div style="margin-bottom: 2rem; text-align: right;">
                <a href="<%= request.getContextPath() %>/gestoreVerifiche/inserisciArticolo" class="btn btn-primary" style="text-decoration: none;">
                    <svg style="width: 18px; height: 18px; margin-right: 0.5rem;" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <line x1="12" y1="5" x2="12" y2="19"></line>
                        <line x1="5" y1="12" x2="19" y2="12"></line>
                    </svg>
                    Inserisci Nuovo Articolo
                </a>
            </div>

            <!-- Tabella Articoli -->
            <div class="section-card">
                <div class="table-container">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Titolo</th>
                                <th>Autore</th>
                                <th>Data Pubblicazione</th>
                                <th>Stato</th>
                                <th>Azioni</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<Notizia> notizie = (List<Notizia>) request.getAttribute("notizie");
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                
                                if (notizie != null && !notizie.isEmpty()) {
                                    for (Notizia n : notizie) {
                            %>
                            <tr>
                                <td><%= n.getId() %></td>
                                <td class="table-title"><%= n.getTitolo() %></td>
                                <td><%= n.getAutore() != null ? n.getAutore() : "-" %></td>
                                <td><%= sdf.format(n.getDataPubblicazione()) %></td>
                                <td>
                                    <% if ("verificata".equals(n.getStato())) { %>
                                        <span class="badge badge-success">Verificata</span>
                                    <% } else if ("in_verifica".equals(n.getStato())) { %>
                                        <span class="badge badge-blue">In Verifica</span>
                                    <% } else { %>
                                        <span class="badge" style="background-color: #e5e7eb; color: #6b7280;">Segnalata</span>
                                    <% } %>
                                </td>
                                <td>
                                    <a href="<%= request.getContextPath() %>/dettaglioNotizia?id=<%= n.getId() %>" 
                                       target="_blank"
                                       style="color: #4169e1; text-decoration: none; font-size: 0.875rem;">
                                        👁️ Visualizza
                                    </a>
                                </td>
                            </tr>
                            <%
                                    }
                                } else {
                            %>
                            <tr>
                                <td colspan="6" style="text-align: center; padding: 2rem; color: #6b7280;">
                                    Nessun articolo presente nel sistema
                                </td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</body>
</html>