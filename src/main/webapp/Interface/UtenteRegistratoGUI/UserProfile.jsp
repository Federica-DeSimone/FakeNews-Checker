<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.unisa.storage.UtenteDao" %>
<%@ page import="it.unisa.storage.Utente" %>
<%
    // Controllo se l'utente è loggato
    String userEmail = (String) session.getAttribute("userEmail");
    String userName = (String) session.getAttribute("userName");

    // Se l'utente non è loggato, reindirizza al login
    if (userEmail == null) {
        response.sendRedirect(request.getContextPath() + "/Interface/AutenticazioneGUI/Login.jsp");
        return;
    }

    // Recupera i dati completi dell'utente dal database
    UtenteDao userDAO = new UtenteDao();
    Utente user = userDAO.getUserByEmail(userEmail);

    if (user == null) {
        // Se l'utente non esiste nel database, reindirizza al login
        response.sendRedirect(request.getContextPath() + "/Interface/AutenticazioneGUI/Login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Profilo Utente - FakeNews Checker</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/user-profile.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<div class="min-h-screen bg-white">
    <!-- Header con Logo e Logout -->
    <div class="bg-white border-b border-gray-200 shadow-md">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
            <div class="flex justify-between items-center">
                <a href="<%= request.getContextPath() %>/Interface/Home.jsp" class="cursor-pointer">
                    <img src="<%= request.getContextPath() %>/images/logo.png" alt="FakeNews Checker" class="h-16 w-auto">
                </a>
                <form action="<%= request.getContextPath() %>/logout" method="get" class="inline">
                    <button type="submit" class="logout-header-btn">
                        <i class="fas fa-sign-out-alt"></i>
                        Logout
                    </button>
                </form>
            </div>
        </div>
    </div>

    <!-- Main Content -->
    <div class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <!-- Titolo centrato -->
        <div class="text-center mb-12">
            <h1 class="text-4xl text-gray-900 mb-2">Profilo Utente</h1>
            <p class="text-gray-600">I tuoi dati personali</p>
        </div>

        <!-- Card contenitore -->
        <div class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-8">
            <!-- Avatar -->
            <div class="flex justify-center mb-8">
                <div class="w-24 h-24 bg-blue-600 rounded-full flex items-center justify-center shadow-lg">
                    <i class="fas fa-user text-white text-4xl"></i>
                </div>
            </div>

            <!-- Dati utente -->
            <div class="space-y-6">
                <!-- Nome -->
                <div class="flex items-center gap-4 p-4 bg-gray-50 rounded-lg">
                    <i class="fas fa-user text-gray-600 text-lg"></i>
                    <div class="flex-1">
                        <p class="text-xs text-gray-500 uppercase tracking-wide">Nome</p>
                        <p class="text-gray-900 mt-1"><%= user.getNome() != null ? user.getNome() : "Non specificato" %></p>
                    </div>
                </div>

                <!-- Cognome -->
                <div class="flex items-center gap-4 p-4 bg-gray-50 rounded-lg">
                    <i class="fas fa-user text-gray-600 text-lg"></i>
                    <div class="flex-1">
                        <p class="text-xs text-gray-500 uppercase tracking-wide">Cognome</p>
                        <p class="text-gray-900 mt-1"><%= user.getCognome() != null ? user.getCognome() : "Non specificato" %></p>
                    </div>
                </div>

                <!-- Telefono -->
                <div class="flex items-center gap-4 p-4 bg-gray-50 rounded-lg">
                    <i class="fas fa-phone text-gray-600 text-lg"></i>
                    <div class="flex-1">
                        <p class="text-xs text-gray-500 uppercase tracking-wide">Telefono</p>
                        <p class="text-gray-900 mt-1">
                            <%= user.getTelefono() != null && !user.getTelefono().isEmpty() ? user.getTelefono() : "Non specificato" %>
                        </p>
                    </div>
                </div>

                <!-- Email -->
                <div class="flex items-center gap-4 p-4 bg-gray-50 rounded-lg">
                    <i class="fas fa-envelope text-gray-600 text-lg"></i>
                    <div class="flex-1">
                        <p class="text-xs text-gray-500 uppercase tracking-wide">Email</p>
                        <p class="text-gray-900 mt-1"><%= user.getEmail() %></p>
                    </div>
                </div>

                <!-- Data Registrazione -->
                <div class="flex items-center gap-4 p-4 bg-gray-50 rounded-lg">
                    <i class="fas fa-calendar text-gray-600 text-lg"></i>
                    <div class="flex-1">
                        <p class="text-xs text-gray-500 uppercase tracking-wide">Data Registrazione</p>
                        <p class="text-gray-900 mt-1">
                            <%= user.getDataRegistrazione() != null ?
                                    user.getDataRegistrazione().toLocalDateTime().toLocalDate().toString() :
                                    "Non disponibile" %>
                        </p>
                    </div>
                </div>
            </div>

            <!-- Edit Button -->
            <div class="mt-8 flex justify-center">
                <button onclick="openEditModal()" class="edit-profile-btn">
                    <i class="fas fa-edit mr-2"></i>
                    Modifica Profilo
                </button>
            </div>
        </div>
    </div>
</div>

<!-- Modal Modifica Profilo -->
<div id="editModal" class="modal hidden">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Modifica Profilo</h3>
            <button onclick="closeEditModal()" class="close-btn">&times;</button>
        </div>
        <form action="<%= request.getContextPath() %>/update-profile" method="post" class="modal-body">
            <div class="form-group">
                <label for="editNome">Nome</label>
                <input type="text" id="editNome" name="nome" value="<%= user.getNome() != null ? user.getNome() : "" %>" class="form-input">
            </div>
            <div class="form-group">
                <label for="editCognome">Cognome</label>
                <input type="text" id="editCognome" name="cognome" value="<%= user.getCognome() != null ? user.getCognome() : "" %>" class="form-input">
            </div>
            <div class="form-group">
                <label for="editTelefono">Telefono</label>
                <input type="tel" id="editTelefono" name="telefono" value="<%= user.getTelefono() != null ? user.getTelefono() : "" %>" class="form-input">
            </div>
            <div class="modal-actions">
                <button type="button" onclick="closeEditModal()" class="btn-cancel">Annulla</button>
                <button type="submit" class="btn-save">Salva Modifiche</button>
            </div>
        </form>
    </div>
</div>

<script>
    var contextPath = '<%= request.getContextPath() %>';
</script>
<script src="<%= request.getContextPath() %>/scripts/user-profile.js"></script>

</body>
</html>