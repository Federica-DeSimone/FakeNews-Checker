<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Registrati - FakeNews Checker</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/auth.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
</head>
<body>
<jsp:include page="/Interface/Header.jsp" />
    <div class="min-h-screen bg-white flex items-center justify-center p-4">
        <!-- Logo in alto -->
        <div class="absolute top-8 left-8 z-10">
            <a href="<%= request.getContextPath() %>/Interface/Home.jsp" class="cursor-pointer">

            </a>
        </div>

        <!-- Form Card -->
        <div class="w-full max-w-md">
            <div class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-8 relative">
                <!-- Decorative shadow layers for depth -->
                <div class="absolute inset-0 bg-gradient-to-br from-green-50/30 to-transparent rounded-2xl -z-10"></div>
                <div class="text-center mb-8">
                    <h1 class="text-3xl text-gray-900 mb-2">Registrati</h1>
                    <p class="text-gray-600 text-sm">Crea il tuo account per iniziare</p>
                </div>

                <%-- Messaggio di errore --%>
                <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
                <% if (errorMessage != null) { %>
                    <div class="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded">
                        <%= errorMessage %>
                    </div>
                <% } %>

                <form action="<%= request.getContextPath() %>/register" method="post" class="space-y-5">
                    <!-- Nome -->
                    <div>
                        <label for="nome" class="text-gray-700 block text-sm font-medium">Nome</label>
                        <input id="nome" name="nome" type="text" placeholder="Inserisci il nome"
                               class="mt-1.5 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500" required>
                    </div>

                    <!-- Cognome -->
                    <div>
                        <label for="cognome" class="text-gray-700 block text-sm font-medium">Cognome</label>
                        <input id="cognome" name="cognome" type="text" placeholder="Inserisci il cognome"
                               class="mt-1.5 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500" required>
                    </div>

                    <!-- Telefono -->
                    <div>
                        <label for="telefono" class="text-gray-700 block text-sm font-medium">Telefono</label>
                        <input id="telefono" name="telefono" type="tel" placeholder="+39 123 456 7890"
                               class="mt-1.5 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
                    </div>

                    <!-- Email -->
                    <div>
                        <label for="email" class="text-gray-700 block text-sm font-medium">Email</label>
                        <input id="email" name="email" type="email" placeholder="esempio@email.com"
                               class="mt-1.5 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500" required>
                    </div>

                    <!-- Password -->
                    <div>
                        <label for="password" class="text-gray-700 block text-sm font-medium">Password</label>
                        <input id="password" name="password" type="password" placeholder="••••••••"
                               class="mt-1.5 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500" required>
                    </div>

          <!-- Buttons -->
<div class="space-y-3 pt-4">
    <button type="submit" class="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-md transition duration-200">
        Registrati
    </button>

    <a href="<%= request.getContextPath() %>/Interface/AutenticazioneGUI/Login.jsp"
       class="w-full inline-block text-center border border-gray-300 hover:bg-gray-50 text-gray-700 font-medium py-2 px-4 rounded-md transition duration-200">
        Annulla
    </a>
</div>

<!-- Login Link -->
<div class="mt-6 text-center text-sm text-gray-600">
    Hai già un account?
    <a href="<%= request.getContextPath() %>/Interface/AutenticazioneGUI/Login.jsp" class="text-blue-600 hover:text-blue-700 font-medium">
        Accedi
    </a>
</div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>