<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registra Gestore - FakeNews Checker</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/gestore.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<jsp:include page="/Interface/HeaderGestore.jsp" />
    <div class="min-h-screen bg-white flex items-center justify-center">
        <!-- Logo -->
        <div class="logo-top">
            <a href="<%= request.getContextPath() %>/visualizzaNotizie">
                <img src="<%= request.getContextPath() %>/images/logo.png" alt="FakeNews Checker" class="logo">
            </a>
        </div>

        <!-- Form Card -->
        <div class="form-container">
            <div class="form-card">
                <div class="form-header">
                    <h1 class="form-title">Registra Gestore</h1>
                    <p class="form-subtitle">Crea un nuovo account gestore</p>
                </div>

                <% 
                    String errore = (String) request.getAttribute("errore");
                    if (errore != null) {
                %>
                <div class="alert alert-error">
                    <%= errore %>
                </div>
                <% } %>

                <form action="<%= request.getContextPath() %>/gestoreVerifiche/registraGestore" method="post" class="form">
                    <!-- Nome -->
                    <div class="form-group">
                        <label for="nome" class="form-label">Nome</label>
                        <input id="nome" name="nome" type="text" placeholder="Inserisci il nome" 
                               class="form-input" required>
                    </div>

                    <!-- Cognome -->
                    <div class="form-group">
                        <label for="cognome" class="form-label">Cognome</label>
                        <input id="cognome" name="cognome" type="text" placeholder="Inserisci il cognome" 
                               class="form-input" required>
                    </div>

                    <!-- Email -->
                    <div class="form-group">
                        <label for="email" class="form-label">Email</label>
                        <input id="email" name="email" type="email" placeholder="esempio@email.com" 
                               class="form-input" required>
                    </div>

                    <!-- Password -->
                    <div class="form-group">
                        <label for="password" class="form-label">Password</label>
                        <input id="password" name="password" type="password" placeholder="••••••••" 
                               class="form-input" required>
                    </div>

                    <!-- Telefono -->
                    <div class="form-group">
                        <label for="telefono" class="form-label">Telefono</label>
                        <input id="telefono" name="telefono" type="tel" placeholder="+39 123 456 7890" 
                               class="form-input">
                    </div>

                    <!-- Ruolo -->
                    <div class="form-group">
                        <label for="ruolo" class="form-label">Ruolo</label>
                        <select id="ruolo" name="ruolo" class="form-select" required>
                            <option value="">Seleziona ruolo</option>
                            <option value="admin-verifiche">Gestore Verifiche</option>
                            <option value="admin-tecnico">Gestore Tecnico</option>
                        </select>
                    </div>

                    <!-- Buttons -->
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            Registra Gestore
                        </button>
                        <a href="<%= request.getContextPath() %>/gestoreVerifiche/dashboard" class="btn btn-secondary">
                            Annulla
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>