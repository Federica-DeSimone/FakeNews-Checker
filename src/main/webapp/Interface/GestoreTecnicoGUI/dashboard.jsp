<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Gestore Tecnico</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/gestoreTecnico.css">
</head>
<body>

<jsp:include page="/Interface/HeaderGestore.jsp" />

<%
    Map<String, Object> stats = (Map<String, Object>) request.getAttribute("stats");
    List<Map<String, String>> activeQueries =
            (List<Map<String, String>>) request.getAttribute("activeQueries");
%>

<h1>Dashboard Gestore Tecnico</h1>

<hr>

<h2>Stato Database</h2>

<ul>
    <li>Connessione: <strong><%= stats.get("connessione") %></strong></li>
    <li>Tabelle: <strong><%= stats.get("tabelle") %></strong></li>
    <li>Record totali: <strong><%= stats.get("recordTotali") %></strong></li>
    <li>Backup: <strong><%= stats.get("backup") %></strong></li>
</ul>

<hr>

<h2>Query Attive</h2>

<%
    if (activeQueries != null && !activeQueries.isEmpty()) {
        for (Map<String, String> q : activeQueries) {
%>
<div style="border:1px solid #ccc; padding:10px; margin-bottom:10px;">
    <strong>Query #<%= q.get("numero") %></strong><br>
    Stato: <%= q.get("stato") %><br>
    Durata: <%= q.get("durata") %><br>
    <code><%= q.get("query") %></code>
</div>
<%
        }
    } else {
%>
<p>Nessuna query attiva</p>
<%
    }
%>

<hr>

<h2>Statistiche Sistema</h2>

<ul>
    <li>Uptime: <%= stats.get("uptime") %></li>
    <li>Latenza media: <%= stats.get("latenzaMedia") %></li>
    <li>Richieste / ora: <%= stats.get("richiesteOra") %></li>
    <li>Query attive: <%= activeQueries != null ? activeQueries.size() : 0 %></li>
</ul>

</body>
</html>
