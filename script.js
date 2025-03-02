// Assurez-vous que le code s'exécute après le chargement du DOM
document.addEventListener('DOMContentLoaded', () => {
    // 1. Animation des sections
    document.addEventListener('scroll', () => {
        const sections = document.querySelectorAll('section');
        sections.forEach(section => {
            const rect = section.getBoundingClientRect();
            if (rect.top >= 0 && rect.top < window.innerHeight) {
                section.classList.add('active');
            } else {
                section.classList.remove('active');
            }
        });
    });

    // 2. Validation du formulaire de contact
    document.querySelector('form').addEventListener('submit', function(event) {
        const name = this.name.value.trim();
        const email = this.email.value.trim();
        const message = this.message.value.trim();
        if (name === '' || email === '' || message === '') {
            alert('Veuillez remplir tous les champs.');
            event.preventDefault();
        }
    });

    // 3. Bouton retour en haut de page
    const backToTopButton = document.createElement('button');
    backToTopButton.textContent = '↑';
    backToTopButton.className = 'fixed bottom-4 right-4 bg-blue-500 text-white rounded-full p-3 hidden';
    document.body.appendChild(backToTopButton);

    backToTopButton.addEventListener('click', () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });

    window.addEventListener('scroll', () => {
        backToTopButton.style.display = (window.scrollY > 300) ? 'block' : 'none';
    });

    // 4. Thème sombre/clair
    const themeToggleButton = document.getElementById('theme-toggle');

    themeToggleButton.addEventListener('click', () => {
        document.body.classList.toggle('dark'); // Basculer la classe 'dark' sur le body
    });
});