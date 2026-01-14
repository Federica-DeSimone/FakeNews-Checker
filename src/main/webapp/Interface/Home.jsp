<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Home</title>

    <!-- CSS con context path -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/home.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">

    <!-- Aggiungi JavaScript per la navigazione -->
    <script>
        var contextPath = '<%= request.getContextPath() %>';
        var isLoggedIn = <%= (session.getAttribute("userEmail") != null) ? "true" : "false" %>;
    </script>
    <script src="<%= request.getContextPath() %>/scripts/home.js"></script>

</head>
<body>
<jsp:include page="Header.jsp" />
<section id="home" class="hero-container">

    <!-- Background -->
    <div class="hero-background">
        <img src="<%= request.getContextPath() %>/images/Home.png"
             alt="Hero Image"
             class="hero-image">
        <div class="hero-overlay"></div>
    </div>

    <!-- Content -->
    <div class="hero-content">
        <h1>Verifica l'Autenticità delle Notizie</h1>

        <p>
            Proteggi te stesso dalla disinformazione con il nostro
            sistema avanzato di verifica delle fonti
        </p>

        <!-- CORREZIONE: Cambiato da /lista-articoli a /visualizzaNotizie -->
        <button class="hero-button" onclick="window.location.href='<%= request.getContextPath() %>/visualizzaNotizie'">
            LEGGI ARTICOLI
        </button>
    </div>

</section>

<jsp:include page="Features.jsp" />
<jsp:include page="Footer.jsp" />

</body>
</html>