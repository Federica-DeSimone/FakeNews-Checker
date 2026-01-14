<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.storage.Gestore" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Elenco Gestori - FakeNews Checker</title>
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
                <h1 class="page-title">Elenco Gestori</h1>
                <p class="page-subtitle">Tutti i gestori registrati nel sistema</p>
            </div>

            <!-- Bottone Registra -->
            <div style="margin-bottom: 2rem; text-align: right;">
                <a href="<%= request.getContextPath() %>/gestoreVerifiche/registraGestore" class="btn btn-primary" style="text-decoration: none;">
                    <svg style="width: 18px; height: 18px; margin-right: 0.5rem;" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
                        <circle cx="8.5" cy="7" r="4"></circle>
                        <line x1="20" y1="8" x2="20" y2="14"></line>
                        <line x1="23" y1="11" x2="17" y2="11"></line>
                    </svg>
                    Registra Nuovo Gestore
                </a>
            </div>

            <!-- Tabella Gestori -->
            <div class="section-card">
                <div class="table-container">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nome Completo</th>
                                <th>Email</th>
                                <th>Telefono</th>
                                <th>Ruolo</th>
                                <th>Data Registrazione</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<Gestore> gestori = (List<Gestore>) request.getAttribute("gestori");
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                
                                if (gestori != null && !gestori.isEmpty()) {
                                    for (Gestore g : gestori) {
                            %>
                            <tr>
                                <td><%= g.getId() %></td>
                                <td><%= g.getNomeCompleto() %></td>
                                <td><%= g.getEmail() %></td>
                                <td><%= g.getTelefono() != null ? g.getTelefono() : "-" %></td>
                                <td>
                                    <% if (g.getRuolo() == Gestore.TipoGestore.GESTORE_VERIFICHE) { %>
                                        <span class="badge badge-blue">Gestore Verifiche</span>
                                    <% } else { %>
                                        <span class="badge badge-success">Gestore Tecnico</span>
                                    <% } %>
                                </td>
                                <td><%= sdf.format(g.getDataRegistrazione()) %></td>
                            </tr>
                            <%
                                    }
                                } else {
                            %>
                            <tr>
                                <td colspan="6" style="text-align: center; padding: 2rem; color: #6b7280;">
                                    Nessun gestore registrato nel sistema
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