// Admin Dashboard Scripts
document.addEventListener('DOMContentLoaded', function() {
    // Toggle mobile menu
    const menuToggle = document.getElementById('menuToggle');
    const mobileMenu = document.getElementById('mobileMenu');
    
    if (menuToggle && mobileMenu) {
        menuToggle.addEventListener('click', function() {
            mobileMenu.classList.toggle('hidden');
        });
    }
    
    // Auto-hide alerts after 5 seconds
    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            alert.style.opacity = '0';
            alert.style.transition = 'opacity 0.5s';
            setTimeout(function() {
                alert.style.display = 'none';
            }, 500);
        });
    }, 5000);
    
    // Form validation
    const forms = document.querySelectorAll('form');
    forms.forEach(function(form) {
        form.addEventListener('submit', function(e) {
            const requiredFields = form.querySelectorAll('[required]');
            let isValid = true;
            
            requiredFields.forEach(function(field) {
                if (!field.value.trim()) {
                    field.style.borderColor = '#ef4444';
                    isValid = false;
                } else {
                    field.style.borderColor = '#cbd5e1';
                }
            });
            
            if (!isValid) {
                e.preventDefault();
                alert('Per favore, compila tutti i campi obbligatori.');
            }
        });
    });
    
    // Confirm actions
    const rejectButtons = document.querySelectorAll('.btn-reject');
    rejectButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            if (!confirm('Sei sicuro di voler segnalare questa notizia come non attendibile?')) {
                e.preventDefault();
            }
        });
    });
});