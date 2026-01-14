<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/features.css">
 <link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">

<section id="features" class="features-section">

    <div class="features-grid">

        <!-- FEATURE 1 -->
        <div class="feature-item">
            <div class="icon-box">
                <img src="<%= request.getContextPath() %>/images/icon-book.png" class="feature-icon">
            </div>
            <h3 class="feature-title">Leggi articoli</h3>
            <p class="feature-desc">Accedi alle notizie del momento</p>
        </div>

        <!-- FEATURE 2 -->
        <div class="feature-item">
            <div class="icon-box">
                <img src="<%= request.getContextPath() %>/images/icon-alert.png" class="feature-icon">
            </div>
            <h3 class="feature-title">Segnala notizie</h3>
            <p class="feature-desc">Individua e segnala contenuti che potrebbero essere false</p>
        </div>

        <!-- FEATURE 3 -->
        <div class="feature-item">
            <div class="icon-box">
                <img src="<%= request.getContextPath() %>/images/icon-search.png" class="feature-icon">
            </div>
            <h3 class="feature-title">Confronta fonti</h3>
            <p class="feature-desc">Verifica le informazioni tramite fonti affidabili</p>
        </div>

    </div>

</section>
