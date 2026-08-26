function toggleBurger(el) {
    if (document.querySelector('.nav').classList.contains('open') && el.classList.contains('open')){
        document.querySelector('.nav').classList.remove('open');
        el.classList.remove('open');
        document.querySelector('.nav').classList.add('close');
        el.classList.add('close');
    }
    else{
        document.querySelector('.nav').classList.add('open');
        el.classList.add('open');
        document.querySelector('.nav').classList.remove('close');
        el.classList.remove('close');
    }
}

    function updateButtonLink() {
      const citySlug   = document.getElementById('city').value || '';
      const domainSlug = document.getElementById('domain').value || '';

      if (!citySlug && !domainSlug) {
        alert('Sélectionnez au moins une ville ou un domaine.');
        return;
      }

      let regionSlug = '';
      if (citySlug) {
        const cityObj = window.citiesData.find(c => c.slug === citySlug);
        regionSlug = (cityObj && cityObj.regionSlug) ? cityObj.regionSlug : '';
        if (!regionSlug) {
          alert('Région manquante pour la ville sélectionnée.');
          return;
        }
      }

      // Construction de l'URL
      let url = '';
      if (citySlug && domainSlug) {
        url = `/categorie/${domainSlug}/${regionSlug}/${citySlug}`;
      } else if (domainSlug) {
        url = `/categorie/${domainSlug}`;
      } else {
        url = `/region/${regionSlug}/${citySlug}`;
      }

      // redirection
      window.location.href = url;
    }

    document.addEventListener('DOMContentLoaded', () => {
      const btn = document.getElementById('linkButton');
      if (btn) {
        btn.addEventListener('click', updateButtonLink);
      }
    });
       function validerRecaptcha(){
                const captchaField = document.getElementById('captchafield');
                const confirmationField = document.getElementById('validercaptcha');

                var captchaVal = captchaField.value;
                var confirmationVal = confirmationField.value;

                if(captchaVal === confirmationVal){
                   document.getElementById('validercaptcha').value = 'true';
                   return true;
                } else {
                 confirmationVal.value = 'false';
                 alert("recaptcha est non valide");
                 return false;
                 }
           }
