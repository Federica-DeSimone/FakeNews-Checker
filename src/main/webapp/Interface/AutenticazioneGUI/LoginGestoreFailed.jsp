<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Accesso Fallito - FakeNews Checker</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/auth.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
   
</head>
<body>
    <jsp:include page="/Interface/Header2.jsp" />
    <div class="min-h-screen bg-white flex items-center justify-center p-4">
        <!-- Logo in alto -->
        <div class="absolute top-8 left-8 z-10">
            <a href="<%= request.getContextPath() %>/visualizzaNotizie" class="cursor-pointer">
                <img src="<%= request.getContextPath() %>/images/logo.png" alt="FakeNews Checker" class="h-16 w-auto">
            </a>
        </div>

        <!-- Form Card -->
        <div class="w-full max-w-md">
            <div class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-8 relative">
                <!-- Decorative shadow layers for depth -->
                <div class="absolute inset-0 bg-gradient-to-br from-red-50/30 to-transparent rounded-2xl -z-10"></div>
                
                <div class="text-center mb-8">
                    <span class="admin-badge">🔐 AREA GESTORE</span>
                    <h1 class="text-3xl text-gray-900 mb-2">Accesso Gestore</h1>
                    <p class="text-gray-600 text-sm">Inserisci le credenziali di amministrazione</p>
                </div>

                <!-- Error Alert -->
                <div class="mb-6 bg-red-50 border border-red-200 rounded-lg p-4 flex items-start gap-3">
                    <svg class="text-red-600 mt-0.5 flex-shrink-0" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <circle cx="12" cy="12" r="10"></circle>
                        <line x1="12" y1="8" x2="12" y2="12"></line>
                        <line x1="12" y1="16" x2="12.01" y2="16"></line>
                    </svg>
                    <div>
                        <p class="text-sm text-red-600 font-semibold">Credenziali non valide</p>
                        <p class="text-xs text-red-500 mt-1">Email o password errate per l'accesso gestore</p>
                    </div>
                </div>

                <form action="<%= request.getContextPath() %>/loginGestore" method="post" class="space-y-5">
                    <!-- Email -->
                    <div>
                        <label for="email" class="text-gray-700 block text-sm font-medium">Email Gestore</label>
                        <input id="email" name="email" type="email" placeholder="gestore@fakenews.com" 
                               class="mt-1.5 w-full px-3 py-2 border border-red-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-red-500 focus:border-red-500" 
                               required>
                    </div>

                    <!-- Password -->
                    <div>
                        <label for="password" class="text-gray-700 block text-sm font-medium">Password</label>
                        <input id="password" name="password" type="password" placeholder="••••••••" 
                               class="mt-1.5 w-full px-3 py-2 border border-red-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-red-500 focus:border-red-500" 
                               required>
                    </div>

                    <!-- Submit Button -->
                    <div class="pt-4">
                        <button type="submit" class="w-full bg-gradient-to-r from-purple-600 to-blue-600 hover:from-purple-700 hover:to-blue-700 text-white font-medium py-2 px-4 rounded-md transition duration-200 shadow-lg">
                            Riprova Accesso
                        </button>
                    </div>
                </form>

                <!-- Link Utenti -->
                <div class="mt-6 text-center text-sm text-gray-600">
                    Sei un utente normale? 
                    <a href="<%= request.getContextPath() %>/Interface/AutenticazioneGUI/Login.jsp" class="text-blue-600 hover:text-blue-700 font-medium">
                        Accedi come Utente
                    </a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>