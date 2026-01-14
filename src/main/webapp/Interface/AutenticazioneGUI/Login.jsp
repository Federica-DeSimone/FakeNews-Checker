<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Accedi - FakeNews Checker</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/auth.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">

</head>
<body>
<jsp:include page="/Interface/Header.jsp" />
    <div class="min-h-screen bg-white flex items-center justify-center p-4">


        <!-- Form Card -->
        <div class="w-full max-w-md">
            <div class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-8 relative">
                <!-- Decorative shadow layers for depth -->
                <div class="absolute inset-0 bg-gradient-to-br from-blue-50/30 to-transparent rounded-2xl -z-10"></div>

                <div class="text-center mb-8">
                    <h1 class="text-3xl text-gray-900 mb-2">Accedi</h1>
                    <p class="text-gray-600 text-sm">Inserisci le tue credenziali per continuare</p>
                </div>

                <!-- UNICO FORM per tutti -->
                <form action="<%= request.getContextPath() %>/login" method="post">
                    <!-- Email -->
                    <div>
                        <label for="email" class="text-gray-700 block text-sm font-medium">Email</label>
                        <input id="email" name="email" type="email" placeholder="esempio@email.com"
                               class="mt-1.5 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                               required>
                    </div>

                    <!-- Password -->
                    <div>
                        <label for="password" class="text-gray-700 block text-sm font-medium">Password</label>
                        <input id="password" name="password" type="password" placeholder="••••••••"
                               class="mt-1.5 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                               required>
                    </div>

                    <!-- Submit Button -->
                    <div class="pt-4">
                        <button type="submit" class="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-md transition duration-200">
                            Accedi
                        </button>
                    </div>
                </form>



                <!-- Register Link -->
                <div class="mt-6 text-center text-sm text-gray-600">
                    Non hai un account?
                    <a href="<%= request.getContextPath() %>/Interface/AutenticazioneGUI/Register.jsp" class="text-blue-600 hover:text-blue-700 font-medium">
                        Registrati
                    </a><br>
                    <a href="<%= request.getContextPath() %>/Interface/Home.jsp" class="text-blue-600 hover:text-blue-700 font-medium">
                    	Torna indietro ->
                    </a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>