// scripts/image-handler.js
// Gestione errori immagini e fallback

function handleImageError(imgElement) {
    const imageId = imgElement.getAttribute('data-image-id');
    const imageTitle = imgElement.getAttribute('data-image-title');
    const contextPath = getContextPath();
    
    // Determina immagine di fallback in base al titolo
    let fallbackImage = 'Microsoft.png';
    if (imageTitle && imageTitle.toLowerCase().includes('louvre')) {
        fallbackImage = 'Louvre.png';
    }
    
    // Prova percorso locale
    imgElement.src = contextPath + '/Interface/images/' + fallbackImage;
    
    // Se anche il fallback fallisce
    imgElement.onerror = function() {
        showImagePlaceholder(imgElement, 'Immagine non disponibile');
    };
}

function showImagePlaceholder(imgElement, message) {
    imgElement.style.display = 'none';
    
    const placeholder = document.createElement('span');
    placeholder.className = 'image-placeholder';
    placeholder.textContent = message;
    
    imgElement.parentElement.appendChild(placeholder);
}

function getContextPath() {
    // Estrae il context path dalla URL corrente
    const path = window.location.pathname;
    const contextPath = path.substring(0, path.indexOf('/', 1));
    return contextPath || '';
}

// Inizializza al caricamento della pagina
document.addEventListener('DOMContentLoaded', function() {
    // Aggiungi gestori di errori a tutte le immagini
    const images = document.querySelectorAll('img[onerror*="handleImageError"]');
    images.forEach(img => {
        // Backup del src originale
        img.setAttribute('data-original-src', img.src);
    });
    
    console.log('Image handler initialized');
});