// Gestione ricerca globale
let searchTimeout;

// Apri dialog di ricerca
function openSearch() {
    const dialog = document.getElementById('searchDialog');
    const input = document.getElementById('searchInput');
    if (dialog && input) {
        dialog.classList.remove('hidden');
        input.focus();
        // Pulisci risultati precedenti
        const resultsDiv = document.getElementById('searchResults');
        if (resultsDiv) {
            resultsDiv.innerHTML = '<div class="search-empty">' +
                '<i class="fa fa-search" style="font-size: 48px; color: #cbd5e1; margin-bottom: 16px;"></i>' +
                '<p style="color: #64748b;">Inizia a digitare per cercare articoli</p>' +
                '</div>';
        }
    }
}

// Chiudi dialog di ricerca
function closeSearch() {
    const dialog = document.getElementById('searchDialog');
    const input = document.getElementById('searchInput');
    const results = document.getElementById('searchResults');
    if (dialog) dialog.classList.add('hidden');
    if (input) input.value = '';
    if (results) results.innerHTML = '';
}

// Ricerca live mentre digiti
function searchLive() {
    clearTimeout(searchTimeout);

    const query = document.getElementById('searchInput').value.trim();
    const resultsDiv = document.getElementById('searchResults');

    if (query.length < 2) {
        if (resultsDiv) {
            resultsDiv.innerHTML = query.length === 0
                ? '<div class="search-empty">' +
                '<i class="fa fa-search" style="font-size: 48px; color: #cbd5e1; margin-bottom: 16px;"></i>' +
                '<p style="color: #64748b;">Inizia a digitare per cercare articoli</p>' +
                '</div>'
                : '';
        }
        return;
    }

    if (resultsDiv) {
        resultsDiv.innerHTML = '<div class="search-loading">' +
            '<i class="fa fa-spinner fa-spin"></i> Ricerca in corso...</div>';
    }

    searchTimeout = setTimeout(function() {
        fetch(contextPath + '/cercaNotizie?keyword=' + encodeURIComponent(query) + '&ajax=true')
            .then(function(response) {
                if (!response.ok) {
                    throw new Error('Errore nella risposta del server');
                }
                return response.json();
            })
            .then(function(notizie) {
                const resultsDiv = document.getElementById('searchResults');
                if (!resultsDiv) return;

                if (notizie.length === 0) {
                    resultsDiv.innerHTML = '<div class="search-no-results">' +
                        '<p style="color: #64748b; margin-bottom: 8px;">Nessun articolo trovato per "' +
                        escapeHtml(query) + '"</p>' +
                        '<p style="color: #94a3b8; font-size: 14px;">Prova con parole chiave diverse</p>' +
                        '</div>';
                } else {
                    let html = '<p style="color: #64748b; font-size: 14px; margin-bottom: 12px;">' +
                        notizie.length + ' ' +
                        (notizie.length === 1 ? 'risultato trovato' : 'risultati trovati') +
                        '</p>';
                    html += '<div class="search-results-list">';

                    notizie.forEach(function(notizia) {
                        let desc = notizia.descrizione || '';
                        if (desc.length > 100) {
                            desc = desc.substring(0, 100) + '...';
                        }

                        html += '<div class="search-result-item" onclick="window.location.href=\'' +
                            contextPath + '/dettaglioNotizia?id=' + notizia.id + '\'">' +
                            '<h4>' + escapeHtml(notizia.titolo) + '</h4>' +
                            '<p class="result-desc">' + escapeHtml(desc) + '</p>';

                        if (notizia.autore) {
                            html += '<p class="result-author">Fonte: ' + escapeHtml(notizia.autore) + '</p>';
                        }

                        html += '</div>';
                    });

                    html += '</div>';
                    resultsDiv.innerHTML = html;
                }
            })
            .catch(function(error) {
                console.error('Errore ricerca:', error);
                const resultsDiv = document.getElementById('searchResults');
                if (resultsDiv) {
                    resultsDiv.innerHTML = '<div class="search-error">' +
                        '<i class="fa fa-exclamation-triangle"></i> Errore durante la ricerca</div>';
                }
            });
    }, 300);
}

// Funzione per escape HTML
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Esegui ricerca quando si preme Enter
function executeSearch() {
    const query = document.getElementById('searchInput').value.trim();
    if (query.length >= 2) {
        window.location.href = contextPath + '/cercaNotizie?keyword=' + encodeURIComponent(query);
    } else {
        alert('Inserisci almeno 2 caratteri per la ricerca');
    }
}

// Navigazione
function goTo(page) {
    const pages = {
        'login': '/Interface/AutenticazioneGUI/Login.jsp',
        'register': '/Interface/AutenticazioneGUI/Register.jsp'
    };
    if (pages[page]) {
        window.location.href = contextPath + pages[page];
    }
}

// Toggle menu mobile
function toggleMenu() {
    const menu = document.getElementById('mobileMenu');
    if (menu) {
        menu.classList.toggle('hidden');
    }
}

// Event listeners al caricamento
document.addEventListener('DOMContentLoaded', function() {
    // Menu utente dropdown
    const userBtn = document.querySelector('.user-btn');
    const userDropdown = document.querySelector('.user-dropdown');

    if (userBtn && userDropdown) {
        userBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            userDropdown.classList.toggle('show');
        });

        document.addEventListener('click', function() {
            userDropdown.classList.remove('show');
        });
    }

    // Chiudi dialog cliccando fuori
    const dialog = document.getElementById('searchDialog');
    if (dialog) {
        dialog.addEventListener('click', function(e) {
            if (e.target === dialog) {
                closeSearch();
            }
        });
    }

    // Ricerca con Enter
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                executeSearch();
            }
        });

        // Ricerca live mentre si digita
        searchInput.addEventListener('input', searchLive);
    }
});