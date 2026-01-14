<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Controllo se il gestore è loggato
    String gestoreNome = (String) session.getAttribute("gestoreNome");
    String gestoreEmail = (String) session.getAttribute("gestoreEmail");
    String gestoreRuolo = (String) session.getAttribute("gestoreRuolo");
%>

<!-- CSS -->
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/header-gestore.css">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

<header class="header-gestore">
    <nav class="navbar-gestore">
        <div class="nav-inner-gestore">

            <!-- LOGO -->
            <a href="<%= request.getContextPath() %>/visualizzaNotizie" class="logo-link-gestore">
                <img src="<%= request.getContextPath() %>/images/logo.png"
                     alt="FakeNews Checker"
                     class="logo-image-gestore">
            </a>

            <!-- DESKTOP NAVIGATION -->
            <div class="nav-links-gestore">
                <% if (gestoreNome != null) { %>
                    <!-- Menu a tendina Gestore -->
                    <div class="user-menu-gestore">
                        <button class="user-btn-gestore" onclick="toggleGestoreMenu()">
                            <i class="fas fa-user-shield"></i>
                            <%= gestoreNome %>
                            <i class="fas fa-chevron-down"></i>
                        </button>
                        <div id="gestoreDropdown" class="user-dropdown-gestore hidden">
                            <div class="dropdown-header">
                                <i class="fas fa-shield-alt"></i>
                                <span><%= "GESTORE_VERIFICHE".equals(gestoreRuolo) ? "Gestore Verifiche" : "Gestore Tecnico" %></span>
                            </div>
                            
                            <% if ("GESTORE_VERIFICHE".equals(gestoreRuolo)) { %>
                                <a href="<%= request.getContextPath() %>/gestoreVerifiche/dashboard" class="dropdown-item-gestore">
                                    <i class="fas fa-tachometer-alt"></i>Dashboard
                                </a>
                                <a href="<%= request.getContextPath() %>/gestoreVerifiche/gestioneArticoli" class="dropdown-item-gestore">
                                    <i class="fas fa-newspaper"></i>Gestione Articoli
                                </a>
                                <a href="<%= request.getContextPath() %>/gestoreVerifiche/registraGestore" class="dropdown-item-gestore">
                                    <i class="fas fa-user-plus"></i>Registra Gestore
                                </a>
                                <a href="<%= request.getContextPath() %>/gestoreVerifiche/elencoGestori" class="dropdown-item-gestore">
                                    <i class="fas fa-users"></i>Elenco Gestori
                                </a>
                            <% } else { %>
                                <a href="<%= request.getContextPath() %>/gestoreTecnico/dashboard" class="dropdown-item-gestore">
                                    <i class="fas fa-tachometer-alt"></i>Dashboard
                                </a>
                            <% } %>
                            
                            <div class="dropdown-divider-gestore"></div>
                            <a href="<%= request.getContextPath() %>/logout" class="dropdown-item-gestore logout-gestore">
                                <i class="fas fa-sign-out-alt"></i>Logout
                            </a>
                        </div>
                    </div>
                <% } %>
            </div>

            <!-- MOBILE MENU BUTTON -->
            <button class="mobile-menu-btn-gestore" onclick="toggleMobileMenuGestore()">
                <i class="fas fa-bars"></i>
            </button>
        </div>

        <!-- MOBILE MENU -->
        <div id="mobileMenuGestore" class="mobile-menu-gestore hidden">
            <% if (gestoreNome != null) { %>
                <div class="mobile-user-info-gestore">
                    <span class="mobile-user-name-gestore">
                        <i class="fas fa-user-shield"></i>
                        <%= gestoreNome %>
                    </span>
                    <span class="mobile-user-role">
                        <%= "GESTORE_VERIFICHE".equals(gestoreRuolo) ? "Gestore Verifiche" : "Gestore Tecnico" %>
                    </span>
                </div>
                
                <% if ("GESTORE_VERIFICHE".equals(gestoreRuolo)) { %>
                    <a href="<%= request.getContextPath() %>/gestoreVerifiche/dashboard" class="mobile-item-gestore">
                        <i class="fas fa-tachometer-alt"></i> Dashboard
                    </a>
                    <a href="<%= request.getContextPath() %>/gestoreVerifiche/gestioneArticoli" class="mobile-item-gestore">
                        <i class="fas fa-newspaper"></i> Gestione Articoli
                    </a>
                    <a href="<%= request.getContextPath() %>/gestoreVerifiche/registraGestore" class="mobile-item-gestore">
                        <i class="fas fa-user-plus"></i> Registra Gestore
                    </a>
                    <a href="<%= request.getContextPath() %>/gestoreVerifiche/elencoGestori" class="mobile-item-gestore">
                        <i class="fas fa-users"></i> Elenco Gestori
                    </a>
                <% } else { %>
                    <a href="<%= request.getContextPath() %>/gestoreTecnico/dashboard" class="mobile-item-gestore">
                        <i class="fas fa-tachometer-alt"></i> Dashboard
                    </a>
                <% } %>
                
                <a href="<%= request.getContextPath() %>/logout" class="mobile-logout-btn-gestore">
                    <i class="fas fa-sign-out-alt"></i> Logout
                </a>
            <% } %>
        </div>
    </nav>
</header>

<script src="<%= request.getContextPath() %>/scripts/header-gestore.js"></script>