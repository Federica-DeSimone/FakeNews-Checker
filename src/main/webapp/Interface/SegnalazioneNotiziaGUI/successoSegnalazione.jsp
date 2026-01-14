<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Segnalazione Inviata - FakeNews Checker</title>

    <!-- CSS principale -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css">

    <!-- CSS estratto -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/success.css">
</head>

<body class="success-body">
    <div class="success-wrapper">

        <div class="success-logo" onclick="window.location.href='<%= request.getContextPath() %>/Interface/Home.jsp'">
            <img src="<%= request.getContextPath() %>/images/logo.png" alt="FakeNews Checker" class="logo">
        </div>

        <div class="success-content">
            <div class="success-icon-wrapper">
                <div class="success-glow"></div>

                <div class="success-icon">
                    <svg width="48" height="48" viewBox="0 0 24 24" fill="none"
                         stroke="white" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
                        <path class="check-icon" d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                        <polyline class="check-icon" points="22 4 12 14.01 9 11.01"></polyline>
                    </svg>
                </div>
            </div>

            <div class="success-text">
                <h2 class="success-title">Segnalazione avvenuta<br>con successo!</h2>
                <p class="success-message">Sarai reindirizzato alla home.</p>
            </div>
        </div>
    </div>

    <!-- Variabile JS per il redirect -->
    <script>
        const redirectHomeUrl = '<%= request.getContextPath() %>/Interface/Home.jsp';
    </script>

    <!-- Script esterno -->
    <script src="<%= request.getContextPath() %>/scripts/success.js"></script>

</body>
</html>
