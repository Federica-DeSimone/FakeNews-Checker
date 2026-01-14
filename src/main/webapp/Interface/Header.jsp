<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    // Controllo se l'utente è loggato
    String userEmail = (String) session.getAttribute("userEmail");
    String userName = (String) session.getAttribute("userName");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Header - FakeNews Checker</title>
    
    <!-- CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/header.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <header class="header">
        <nav class="navbar">
            <div class="nav-inner">
                <!-- LOGO -->
                <a href="<%= request.getContextPath() %>/Interface/Home.jsp" class="logo-link">
                    <img src="<%= request.getContextPath() %>/images/logo.png"
                         alt="FakeNews Checker"
                         class="logo-image">
                </a>

                <!-- DESKTOP NAVIGATION -->
                <div class="nav-links">
                    <a href="<%= request.getContextPath() %>/Interface/Home.jsp" class="nav-item">Home</a>

                    <!-- Bottone ricerca -->
                    <button class="search-btn" onclick="openSearch()">
                        <i class="fas fa-search"></i>
                    </button>

                    <% if (userEmail != null) { %>
                        <!-- UTENTE LOGGATO - Menu a tendina -->
                        <div class="user-menu">
                            <button class="user-btn">
                                <i class="fas fa-user-circle"></i>
                                <%= userName != null ? userName : userEmail %>
                                <i class="fas fa-chevron-down"></i>
                            </button>
                            <div class="user-dropdown">
                                <a href="<%= request.getContextPath() %>/Interface/UtenteRegistratoGUI/UserProfile.jsp" class="dropdown-item">
                                    <i class="fas fa-user"></i> Profilo
                                </a>
                               
                                <!-- Link allo storico segnalazioni -->
                                <a href="<%= request.getContextPath() %>/storicoSegnalazioni" class="dropdown-item">
                                    <i class="fas fa-history"></i> Storico Segnalazioni
                                </a>
                                <div class="dropdown-divider"></div>
                                <a href="<%= request.getContextPath() %>/logout" class="dropdown-item logout">
                                    <i class="fas fa-sign-out-alt"></i> Logout
                                </a>
                            </div>
                        </div>
                    <% } else { %>
                        <!-- UTENTE NON LOGGATO -->
                        <button class="login-btn" onclick="goTo('login')">
                            Accedi
                        </button>
                    <% } %>
                </div>

                <!-- MOBILE MENU BUTTON -->
                <button class="mobile-menu-btn" onclick="toggleMenu()">
                    <i class="fas fa-bars"></i>
                </button>
            </div>

            <!-- MOBILE MENU -->
            <div id="mobileMenu" class="mobile-menu hidden">
                <a href="<%= request.getContextPath() %>/Interface/Home.jsp" class="mobile-item">
                    <i class="fas fa-home"></i> Home
                </a>

                <button onclick="openSearch(); toggleMenu()" class="mobile-item">
                    <i class="fas fa-search"></i> Cerca Notizie
                </button>

                <% if (userEmail != null) { %>
                    <!-- MOBILE - UTENTE LOGGATO -->
                    <div class="mobile-user-info">
                        <span class="mobile-user-name">
                            <i class="fas fa-user-circle"></i>
                            <%= userName != null ? userName : userEmail %>
                        </span>
                    </div>
                    <a href="<%= request.getContextPath() %>/Interface/UtenteRegistratoGUI/UserProfile.jsp" class="mobile-item">
                        <i class="fas fa-user"></i> Profilo
                    </a>
                  
                    <!-- Link allo storico segnalazioni -->
                    <a href="<%= request.getContextPath() %>/storicoSegnalazioni" class="mobile-item">
                        <i class="fas fa-history"></i> Storico Segnalazioni
                    </a>
                    <a href="<%= request.getContextPath() %>/logout" class="mobile-logout-btn">
                        <i class="fas fa-sign-out-alt"></i> Logout
                    </a>
                <% } else { %>
                    <!-- MOBILE - UTENTE NON LOGGATO -->
                    <button onclick="goTo('login'); toggleMenu()" class="mobile-login-btn">
                        <i class="fas fa-sign-in-alt"></i> Accedi
                    </button>
                    <button onclick="goTo('register'); toggleMenu()" class="mobile-register-btn">
                        <i class="fas fa-user-plus"></i> Registrati
                    </button>
                <% } %>
            </div>
        </nav>

        <!-- SEARCH DIALOG -->
        <div id="searchDialog" class="search-dialog hidden">
            <div class="dialog-box">
                <h3><i class="fas fa-search"></i> Cerca Notizie</h3>

                <div class="input-wrapper">
                    <span class="search-icon"><i class="fas fa-search"></i></span>
                    <input
                        type="text"
                        id="searchInput"
                        placeholder="  Inserisci titolo, autore o parole chiave..."
                        class="search-input"
                        oninput="searchLive()"
                        autocomplete="off"
                    >
                </div>

                <!-- Container per i risultati -->
                <div id="searchResults" class="search-results-container"></div>

                  <div class="dialog-buttons">
                    <button class="search-confirm" onclick="executeSearch()">
                        <i class="fas fa-search"></i> Cerca
                    </button>
                   <button class="dialog-close" onclick="closeSearch()">
                        <i class="fas fa-times"></i> Chiudi
                    </button> 
                </div>
            </div>
        </div>
    </header>

    <!-- Inclusione JS -->
    <script>
        var contextPath = '<%= request.getContextPath() %>';
        var isLoggedIn = <%= (session.getAttribute("userEmail") != null) ? "true" : "false" %>;
    </script>
    <script src="<%= request.getContextPath() %>/scripts/header.js"></script>
</body>
</html>