<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, it.unisa.storage.Notizia" %>
<%
    List<Notizia> notizie = (List<Notizia>) request.getAttribute("notizie");
    String keyword = (String) request.getAttribute("searchKeyword");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Risultati ricerca: <%= keyword %></title>
    <style>
        .search-header {
            background: #f8fafc;
            padding: 2rem;
            margin-bottom: 2rem;
        }
        .no-results {
            text-align: center;
            padding: 4rem;
            color: #64748b;
        }
    </style>
</head>
<body>
    <div class="search-header">
        <h1>Risultati per: "<%= keyword %>"</h1>
        <p><%= notizie != null ? notizie.size() : 0 %> risultati trovati</p>
    </div>
    
    <% if (notizie == null || notizie.isEmpty()) { %>
        <div class="no-results">
            <h2>Nessun articolo trovato</h2>
            <p>Prova con parole chiave diverse</p>
        </div>
    <% } else { %>
        <!-- Mostra lista notizie come in visualizzaNotizie.jsp -->
    <% } %>
</body>
</html>