<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Errore - FakeNews Checker</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css">
     <link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
</head>
<body>
    <div class="error-page">
        <div class="error-container">
            <div class="error-icon">
                <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="12" cy="12" r="10"></circle>
                    <line x1="12" y1="8" x2="12" y2="12"></line>
                    <line x1="12" y1="16" x2="12.01" y2="16"></line>
                </svg>
            </div>
            
            <h1 class="error-title">Si è verificato un errore</h1>
            
            <% 
                String errore = (String) request.getAttribute("errore");
                if (errore != null) {
            %>
                <p class="error-text"><%= errore %></p>
            <% } else { %>
                <p class="error-text">Si è verificato un errore imprevisto. Riprova più tardi.</p>
            <% } %>
            
            <div class="error-actions">
                <a href="<%= request.getContextPath() %>/visualizzaNotizie" class="btn-back">
                    Torna alla Home
                </a>
            </div>
        </div>
    </div>
</body>
</html>