<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Invia Segnalazione - FakeNews Checker</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body class="form-body">
<div class="form-wrapper">
    <!-- Logo -->
    <div class="form-logo-container">
        <img src="<%= request.getContextPath() %>/images/logo.png" alt="FakeNews Checker" class="logo">
    </div>

    <!-- Form Container -->
    <div class="form-container">
        <h2 class="form-title">Invia la tua segnalazione</h2>

        <%
            String errore = (String) request.getAttribute("errore");
            if (errore != null) {
        %>
        <div class="error-alert">
            <%= errore %>
        </div>
        <% } %>

        <%
            String titoloNotizia = (String) request.getAttribute("titoloNotizia");
            Integer idNotizia = (Integer) request.getAttribute("idNotizia");
            String immagineNotizia = (String) request.getAttribute("immagineNotizia");
            boolean isFromArticolo = (titoloNotizia != null && !titoloNotizia.isEmpty());
        %>

        <form action="<%= request.getContextPath() %>/inviaSegnalazione" method="post" enctype="multipart/form-data">
            <!-- Campo hidden per ID notizia -->
            <% if (idNotizia != null) { %>
            <input type="hidden" name="idNotizia" value="<%= idNotizia %>">
            <% } %>

            <!-- Campo hidden per immagine standard -->
            <% if (immagineNotizia != null && !immagineNotizia.isEmpty()) { %>
            <input type="hidden" name="immagineStandard" value="<%= immagineNotizia %>">
            <% } %>

            <div class="form-grid">
                <!-- Left Column - Photo Upload -->
                <div class="form-column">
                    <label class="form-label">Foto</label>
                    <% if (immagineNotizia != null && !immagineNotizia.isEmpty()) { %>
                    <!-- Mostra immagine readonly dall'articolo -->
                    <div style="border: 2px solid #d1d5db; border-radius: 0.5rem; padding: 1rem; background-color: #f9fafb;">
                        <img src="<%= request.getContextPath() %>/images/<%= immagineNotizia %>"
                             alt="Immagine articolo"
                             style="max-width: 100%; height: auto; border-radius: 0.5rem;">
                        <p style="text-align: center; margin-top: 0.5rem; font-size: 0.875rem; color: #6b7280;">
                            📷 Immagine dall'articolo
                        </p>
                    </div>
                    <% } else { %>
                    <div class="upload-container">
                        <input type="file"
                               name="foto"
                               id="fotoInput"
                               accept="image/*"
                               class="file-input">
                        <label for="fotoInput" class="upload-area" id="uploadArea">
                            <div class="upload-content" id="uploadContent">
                                <div class="upload-icon">
                                    <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                                        <polyline points="17 8 12 3 7 8"></polyline>
                                        <line x1="12" y1="3" x2="12" y2="15"></line>
                                    </svg>
                                </div>
                                <p class="upload-text">Clicca per caricare un'immagine</p>
                            </div>
                            <img id="imagePreview" class="image-preview hidden" alt="Anteprima">
                        </label>
                    </div>
                    <% } %>
                </div>

                <!-- Right Column - Form Fields -->
                <div class="form-column">
                    <div class="form-field">
                        <label for="titolo" class="form-label">
                            Titolo *
                            <% if (isFromArticolo) { %>
                            <span style="color: #6b7280; font-size: 0.875rem; font-weight: normal;">(dall'articolo segnalato)</span>
                            <% } %>
                        </label>
                        <input type="text"
                               id="titolo"
                               name="titolo"
                               class="form-input"
                               value="<%= isFromArticolo ? titoloNotizia : "" %>"
                            <%= isFromArticolo ? "readonly style='background-color: #f3f4f6; cursor: not-allowed;'" : "" %>
                               required>
                    </div>

                    <div class="form-field">
                        <label for="descrizione" class="form-label">Descrizione *</label>
                        <textarea id="descrizione"
                                  name="descrizione"
                                  class="form-input"
                                  rows="3"
                                  placeholder="Descrivi perché ritieni questa notizia sospetta..."
                                  required></textarea>
                    </div>

                    <div class="form-field">
                        <label for="url" class="form-label">URL Fonte *</label>
                        <input type="url"
                               id="url"
                               name="url"
                               class="form-input"
                               required
                               placeholder="https://">
                    </div>

                    <div class="form-field">
                        <label for="autore" class="form-label">Autore</label>
                        <input type="text"
                               id="autore"
                               name="autore"
                               class="form-input">

                    </div>
                </div>
            </div>

            <!-- Action Buttons -->
            <div class="form-actions">
                <button type="button"
                        onclick="window.location.href='<%= request.getContextPath() %>/visualizzaNotizie'"
                        class="btn-cancel">
                    Annulla
                </button>
                <button type="submit" class="btn-submit">
                    Invia Segnalazione
                </button>
            </div>
        </form>
    </div>
</div>

<script src="<%=request.getContextPath() %>/scripts/upload.js"></script>
</body>
</html>