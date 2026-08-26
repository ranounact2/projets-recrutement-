function slide(element,index) {
    console.log('slide param', index);
    let width=document.querySelector('.slide').offsetWidth;
    document.querySelector('.slide').style.transform = 'translateX(-' + (width * index) + 'px)';
    document.querySelectorAll('.dots span').forEach(el=>{
        el.classList.remove('active');
    });
    element.classList.add('active');
}
function toggleDomains(event) {
            event.preventDefault();

            const hiddenDomains = document.querySelectorAll('.domain-hidden');
            const voirPlusBtn = document.getElementById('voir-plus-btn');
            const voirMoinsBtn = document.getElementById('voir-moins-btn');

            // Vérifier si les domaines cachés sont actuellement affichés
            const isHidden = hiddenDomains[0].style.display === 'none';

            if (isHidden) {
            // Afficher les domaines cachés
            hiddenDomains.forEach(domain => {
            domain.style.display = 'block';
            });
            voirPlusBtn.style.display = 'none';
            voirMoinsBtn.style.display = 'inline-block';
            } else {
            // Cacher les domaines supplémentaires
            hiddenDomains.forEach(domain => {
            domain.style.display = 'none';
            });
            voirPlusBtn.style.display = 'inline-block';
            voirMoinsBtn.style.display = 'none';

            // Optionnel: faire défiler vers le haut de la section
            document.querySelector('.domaine').scrollIntoView({
            behavior: 'smooth',
            block: 'start'
            });
            }
            }