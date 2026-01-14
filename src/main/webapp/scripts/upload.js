// Script per gestire l'upload e l'anteprima dell'immagine
document.addEventListener('DOMContentLoaded', function() {
    const fotoInput = document.getElementById('fotoInput');
    const imagePreview = document.getElementById('imagePreview');
    const uploadContent = document.getElementById('uploadContent');
    
    if (fotoInput && imagePreview && uploadContent) {
        fotoInput.addEventListener('change', function(e) {
            const file = e.target.files[0];
            
            if (file) {
                // Verifica che sia un'immagine
                if (!file.type.startsWith('image/')) {
                    alert('Per favore seleziona un file immagine valido');
                    fotoInput.value = '';
                    return;
                }
                
                // Verifica dimensione file (max 10MB)
                if (file.size > 10 * 1024 * 1024) {
                    alert('Il file è troppo grande. Dimensione massima: 10MB');
                    fotoInput.value = '';
                    return;
                }
                
                // Mostra anteprima
                const reader = new FileReader();
                
                reader.onload = function(event) {
                    imagePreview.src = event.target.result;
                    imagePreview.classList.remove('hidden');
                    uploadContent.style.display = 'none';
                };
                
                reader.onerror = function() {
                    alert('Errore nella lettura del file');
                    fotoInput.value = '';
                };
                
                reader.readAsDataURL(file);
            }
        });
        
        // Permetti di cliccare sull'anteprima per cambiare immagine
        imagePreview.addEventListener('click', function() {
            fotoInput.click();
        });
    }
});