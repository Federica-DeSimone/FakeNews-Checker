<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.storage.Segnalazione" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Gestore Verifiche - FakeNews Checker</title>
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
                <h1 class="page-title">Gestione Verifiche</h1>
                <p class="page-subtitle">Valuta le segnalazioni degli utenti</p>
            </div>

            <!-- Statistiche -->
            <div class="stats-grid">
                <div class="stat-card stat-pending">
                    <div class="stat-icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <circle cx="12" cy="12" r="10"></circle>
                            <polyline points="12 6 12 12 16 14"></polyline>
                        </svg>
                    </div>
                    <div class="stat-content">
                        <p class="stat-label">In Attesa</p>
                        <p class="stat-value"><%= request.getAttribute("inVerifica") %></p>
                    </div>
                </div>

                <div class="stat-card stat-verified">
                    <div class="stat-icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                            <polyline points="22 4 12 14.01 9 11.01"></polyline>
                        </svg>
                    </div>
                    <div class="stat-content">
                        <p class="stat-label">Verificate</p>
                        <p class="stat-value"><%= request.getAttribute("verificate") %></p>
                    </div>
                </div>

                <div class="stat-card stat-rejected">
                    <div class="stat-icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <circle cx="12" cy="12" r="10"></circle>
                            <line x1="15" y1="9" x2="9" y2="15"></line>
                            <line x1="9" y1="9" x2="15" y2="15"></line>
                        </svg>
                    </div>
                    <div class="stat-content">
                        <p class="stat-label">Non Attendibili</p>
                        <p class="stat-value"><%= request.getAttribute("nonAttendibili") %></p>
                    </div>
                </div>
            </div>

            <!-- Segnalazioni in Attesa -->
            <div class="section-card">
                <h2 class="section-title">Segnalazioni in Attesa di Verifica</h2>

                <%
                    List<Segnalazione> segnalazioni = (List<Segnalazione>) request.getAttribute("segnalazioni");
                    List<Segnalazione> pending = new ArrayList<>();
                    if (segnalazioni != null) {
                        for (Segnalazione s : segnalazioni) {
                            if ("in_verifica".equals(s.getStato())) {
                                pending.add(s);
                            }
                        }
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                    if (pending.isEmpty()) {
                %>
                <div class="empty-state">
                    <svg class="empty-icon" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <circle cx="12" cy="12" r="10"></circle>
                        <polyline points="12 6 12 12 16 14"></polyline>
                    </svg>
                    <p>Nessuna segnalazione in attesa</p>
                </div>
                <%
                    } else {
                        for (Segnalazione seg : pending) {
                %>
                <div class="segnalazione-card">
                    <div class="segnalazione-content">
                        <div class="segnalazione-header">
                            <span class="badge badge-blue"><%= seg.getNumeroSegnalazione() %></span>
                            <span class="segnalazione-date"><%= sdf.format(seg.getDataSegnalazione()) %></span>
                        </div>

                        <h3 class="segnalazione-title"><%= seg.getTitolo() %></h3>
                        <p class="segnalazione-description"><%= seg.getDescrizione() %></p>

                        <div class="segnalazione-meta">
                            <p class="meta-item">
                                <strong>URL:</strong>
                                <a href="<%= seg.getUrl() %>" target="_blank" class="meta-link">
                                    <%= seg.getUrl().length() > 60 ? seg.getUrl().substring(0, 60) + "..." : seg.getUrl() %>
                                </a>
                            </p>
                            <% if (seg.getAutore() != null && !seg.getAutore().isEmpty()) { %>
                                <p class="meta-item"><strong>Autore:</strong> <%= seg.getAutore() %></p>
                            <% } %>
                        </div>
                    </div>

                    <div class="segnalazione-actions">
                        <form action="<%= request.getContextPath() %>/gestoreVerifiche/aggiornaVerifica" method="post" style="display: inline;">
                            <input type="hidden" name="idSegnalazione" value="<%= seg.getId() %>">
                            <input type="hidden" name="azione" value="verifica">
                            <button type="submit" class="btn btn-verify">
                                <svg class="btn-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                                    <polyline points="22 4 12 14.01 9 11.01"></polyline>
                                </svg>
                                Verifica
                            </button>
                        </form>
                        <form action="<%= request.getContextPath() %>/gestoreVerifiche/aggiornaVerifica" method="post" style="display: inline;">
                            <input type="hidden" name="idSegnalazione" value="<%= seg.getId() %>">
                            <input type="hidden" name="azione" value="rifiuta">
                            <button type="submit" class="btn btn-reject">
                                <svg class="btn-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <circle cx="12" cy="12" r="10"></circle>
                                    <line x1="15" y1="9" x2="9" y2="15"></line>
                                    <line x1="9" y1="9" x2="15" y2="15"></line>
                                </svg>
                                Non Attendibile
                            </button>
                        </form>
                    </div>
                </div>
                <%
                        }
                    }
                %>
            </div>

            <!-- Storico Decisioni -->
            <div class="section-card">
                <h2 class="section-title">Storico Decisioni</h2>

                <div class="table-container">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Numero</th>
                                <th>Titolo</th>
                                <th>Data</th>
                                <th>Stato</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (segnalazioni != null) {
                                    for (Segnalazione seg : segnalazioni) {
                                        if (!"in_verifica".equals(seg.getStato())) {
                            %>
                            <tr>
                                <td><%= seg.getNumeroSegnalazione() %></td>
                                <td class="table-title"><%= seg.getTitolo() %></td>
                                <td><%= sdf.format(seg.getDataSegnalazione()) %></td>
                                <td>
                                    <% if ("verificata".equals(seg.getStato())) { %>
                                        <span class="badge badge-success">
                                            <svg class="badge-icon" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                                                <polyline points="22 4 12 14.01 9 11.01"></polyline>
                                            </svg>
                                            Verificata
                                        </span>
                                    <% } else { %>
                                        <span class="badge badge-danger">
                                            <svg class="badge-icon" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                                <circle cx="12" cy="12" r="10"></circle>
                                                <line x1="15" y1="9" x2="9" y2="15"></line>
                                                <line x1="9" y1="9" x2="15" y2="15"></line>
                                            </svg>
                                            Non Attendibile
                                        </span>
                                    <% } %>
                                </td>
                            </tr>
                            <%
                                        }
                                    }
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