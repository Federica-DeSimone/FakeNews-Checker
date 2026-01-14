<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inserisci Articolo - FakeNews Checker</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/gestore.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body class="form-body-gestore">
<div class="form-wrapper-gestore">
    <div class="form-logo-gestore">
        <img src="<%= request.getContextPath() %>/images/logo.png" alt="FakeNews Checker">
    </div>

    <div class="form-container-gestore">
        <h2 class="form-title-gestore">Inserisci Nuovo Articolo</h2>
        <p class="form-subtitle-gestore">Compila il form per pubblicare un articolo</p>

        <%
            String errore = (String) request.getAttribute("errore");
            if (errore != null) {
        %>
        <div style="background-color: #fee2e2; border: 1px solid #ef4444; color: #991b1b; padding: 0.75rem; border-radius: 0.5rem; margin-bottom: 1rem;">
            <%= errore %>
        </div>
        <% } %>

        <form action="<%= request.getContextPath() %>/gestoreVerifiche/inserisciArticolo" method="post" enctype="multipart/form-data">
            <div class="form-grid-gestore">
                <!-- Foto -->
                <div class="form-field-gestore form-field-full">
                    <label class="form-label-gestore">Foto Articolo *</label>
                    <input type="file" name="foto" id="fotoInput" accept="image/*" style="display: none;">
                    <label for="fotoInput" class="upload-area-gestore" id="uploadArea">
                        <div id="uploadContent">
                            <svg style="width: 48px; height: 48px; margin: 0 auto; color: #9ca3af;" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                                <polyline points="17 8 12 3 7 8"></polyline>
                                <line x1="12" y1="3" x2="12" y2="15"></line>
                            </svg>
                            <p style="margin-top: 1rem; color: #6b7280;">Clicca per caricare un'immagine</p>
                            <p style="font-size: 0.75rem; color: #9ca3af; margin-top: 0.5rem;">PNG, JPG (MAX. 10MB)</p>
                        </div>
                        <img id="imagePreview" style="display: none; max-width: 100%; max-height: 300px; border-radius: 0.5rem;" alt="Anteprima">
                    </label>
                </div>

                <!-- Titolo -->
                <div class="form-field-gestore form-field-full">
                    <label for="titolo" class="form-label-gestore">Titolo *</label>
                    <input type="text" id="titolo" name="titolo" class="form-input-gestore" required>
                </div>

                <!-- Descrizione -->
                <div class="form-field-gestore form-field-full">
                    <label for="descrizione" class="form-label-gestore">Descrizione *</label>
                    <textarea id="descrizione" name="descrizione" class="form-textarea-gestore" required></textarea>
                </div>

                <!-- Autore -->
                <div class="form-field-gestore">
                    <label for="autore" class="form-label-gestore">Autore</label>
                    <input type="text" id="autore" name="autore" class="form-input-gestore">
                </div>

                <!-- Stato -->
                <div class="form-field-gestore">
                    <label for="stato" class="form-label-gestore">Stato *</label>
                    <select id="stato" name="stato" class="form-select-gestore" required>
                        <option value="">Seleziona stato</option>
                        <option value="verificata">Verificata</option>
                        <option value="in_verifica">In Verifica</option>
                        <option value="segnalata">Segnala come sospetta</option>
                    </select>
                </div>
            </div>

            <div class="form-actions-gestore">
                <button type="button" onclick="window.location.href='<%= request.getContextPath() %>/gestoreVerifiche/gestioneArticoli'" class="btn-gestore btn-cancel-gestore">
                    Annulla
                </button>
                <button type="submit" class="btn-gestore btn-submit-gestore">
                    Pubblica Articolo
                </button>
            </div>
        </form>
    </div>
</div>

<script src="<%= request.getContextPath() %>/scripts/inserisci-articolo.js"></script>
</body>
</html>
