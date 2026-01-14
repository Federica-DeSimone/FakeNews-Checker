// Script per gestire il menu mobile
document.addEventListener('DOMContentLoaded', function() {
    const menuToggle = document.getElementById('menuToggle');
    const mobileMenu = document.getElementById('mobileMenu');
    
    if (menuToggle && mobileMenu) {
        menuToggle.addEventListener('click', function() {
            mobileMenu.classList.toggle('hidden');
            
            // Cambia l'icona del menu
            const menuIcon = menuToggle.querySelector('.menu-icon');
            if (menuIcon) {
                if (mobileMenu.classList.contains('hidden')) {
                    menuIcon.textContent = '☰';
                } else {
                    menuIcon.textContent = '✕';
                }
            }
        });
    }
});