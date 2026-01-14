// Gestione menu gestore
function toggleGestoreMenu() {
    const dropdown = document.getElementById('gestoreDropdown');
    if (dropdown) {
        dropdown.classList.toggle('hidden');
    }
}

function toggleMobileMenuGestore() {
    const menu = document.getElementById('mobileMenuGestore');
    if (menu) {
        menu.classList.toggle('hidden');
    }
}

// Chiudi menu quando si clicca fuori
document.addEventListener('click', function(event) {
    const dropdown = document.getElementById('gestoreDropdown');
    const button = document.querySelector('.user-btn-gestore');
    
    if (dropdown && button && !dropdown.contains(event.target) && !button.contains(event.target)) {
        dropdown.classList.add('hidden');
    }
});
