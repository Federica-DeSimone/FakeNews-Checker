<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestore Registrato - FakeNews Checker</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/gestore.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body class="success-body">
<div class="success-wrapper">
    <!-- Logo -->
    <div class="success-logo" onclick="window.location.href='<%= request.getContextPath() %>/gestoreVerifiche/dashboard'">
        <img src="<%= request.getContextPath() %>/images/logo.png" alt="FakeNews Checker">
    </div>

    <!-- Success Content -->
    <div class="success-content">
        <!-- Check Icon -->
        <div class="success-icon-wrapper">
            <div class="success-glow"></div>
            <div class="success-icon">
                <svg viewBox="0 0 24 24">
                    <path class="check-icon" d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                    <polyline class="check-icon" points="22 4 12 14.01 9 11.01"></polyline>
                </svg>
            </div>
        </div>

        <!-- Text -->
        <div class="success-text">
            <h2 class="success-title">
                🎉 Gestore Registrato<br>con Successo!
            </h2>
            <p class="success-message">
                Il nuovo gestore è stato aggiunto al sistema.
            </p>
            <p class="redirect-message">
                Sarai reindirizzato alla dashboard tra pochi secondi...
            </p>
        </div>
    </div>
</div>

<script>
    // Passa il context path allo script esterno
    window.contextPath = '<%= request.getContextPath() %>';
</script>
<script src="<%= request.getContextPath() %>/scripts/successo-registrazione.js"></script>
</body>
</html>
