// Preview immagine per l'inserimento articolo
document.addEventListener('DOMContentLoaded', function() {
    const fotoInput = document.getElementById('fotoInput');
    const uploadContent = document.getElementById('uploadContent');
    const imagePreview = document.getElementById('imagePreview');

    if (fotoInput) {
        fotoInput.addEventListener('change', function() {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    imagePreview.src = e.target.result;
                    imagePreview.style.display = 'block';
                    uploadContent.style.display = 'none';
                };
                reader.readAsDataURL(file);
            }
        });
    }
});