document.addEventListener('DOMContentLoaded', function () {
  const form = document.getElementById('offreForm');
  if (!form) return;

  // Fields object
  const fields = {
    title: document.getElementById('title'),
    domain: document.getElementById('domain'),
    type: document.getElementById('type'),
    city: document.getElementById('city'),
    nbrDePostes: document.getElementById('nbrDePostes'),
    formation: document.getElementById('formation'),
    experienceLevel: document.getElementById('experienceLevel'),
    content: document.getElementById('content'),
    facebook: document.getElementById('facebook'),
    twitter: document.getElementById('twitter'),
    linkedin: document.getElementById('linkedin'),
    companyCode: document.getElementById('companyCode'),
    companyName: document.getElementById('companyName'),
    email: document.getElementById('email'),
    tel: document.getElementById('tel')
  };

  const errors = {
    title: document.getElementById('title-error'),
    domain: document.getElementById('domain-error'),
    type: document.getElementById('type-error'),
    city: document.getElementById('city-error'),
    nbrDePostes: document.getElementById('nbrDePostes-error'),
    formation: document.getElementById('formation-error'),
    experienceLevel: document.getElementById('experienceLevel-error'),
    content: document.getElementById('content-error'),
    facebook: document.getElementById('facebook-error'),
    twitter: document.getElementById('twitter-error'),
    linkedin: document.getElementById('linkedin-error'),
    companyCode: document.getElementById('companyCode-error'),
    companyName: document.getElementById('companyName-error'),
    email: document.getElementById('email-error'),
    tel: document.getElementById('tel-error')
  };

  let showErrors = false; // Only show errors after submit attempt

  function isEmailValid(value) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i;
    return re.test(String(value).trim());
  }

  function isPhoneValid(value) {
    const trimmed = String(value).trim();
    // Pattern 1: 05, 06 or 07 followed by 8 digits (10 digits total)
    const localPattern = /^0[567]\d{8}$/;
    // Pattern 2: +212 5, +212 6 or +212 7 followed by 8 digits (with optional space after +212)
    const intlPattern = /^\+212\s?[567]\d{8}$/;
    
    return localPattern.test(trimmed) || intlPattern.test(trimmed);
  }

  function notEmpty(el) {
    return el && el.value && el.value.trim().length > 0;
  }

  function showError(key, message) {
    const container = errors[key];
    if (container) {
      container.textContent = showErrors ? (message || '') : '';
    }
  }

  function setValidity(el, valid) {
    if (!el) return;
    el.setAttribute('aria-invalid', valid ? 'false' : 'true');
    if (!valid && showErrors) {
      el.classList.add('is-invalid');
    } else {
      el.classList.remove('is-invalid');
    }
  }

  function validateRequired(key) {
    const el = fields[key];
    const ok = notEmpty(el);
    setValidity(el, ok);
    showError(key, ok ? '' : 'Ce champ est requis.');
    return ok;
  }

  function validateEmail() {
    const el = fields.email;
    const ok = notEmpty(el) && isEmailValid(el.value);
    setValidity(el, ok);
    let msg = '';
    if (!notEmpty(el)) msg = 'Ce champ est requis.';
    else if (!isEmailValid(el.value)) msg = "Adresse email invalide.";
    showError('email', msg);
    return ok;
  }

  function validatePhone() {
    const el = fields.tel;
    const ok = notEmpty(el) && isPhoneValid(el.value);
    setValidity(el, ok);
    let msg = '';
    if (!notEmpty(el)) msg = 'Ce champ est requis.';
    else if (!isPhoneValid(el.value)) msg = 'Numéro invalide (ex: 0612345678 ou +212 612345678).';
    showError('tel', msg);
    return ok;
  }

  function evaluateAll() {
    const results = [
      validateRequired('title'),
      validateRequired('domain'),
      validateRequired('type'),
      validateRequired('city'),
      validateRequired('nbrDePostes'),
      validateRequired('formation'),
      validateRequired('experienceLevel'),
      validateRequired('content'),
      validateRequired('facebook'),
      validateRequired('twitter'),
      validateRequired('linkedin'),
      validateRequired('companyCode'),
      validateRequired('companyName'),
      validateEmail(),
      validatePhone()
    ];
    const allOk = results.every(Boolean);
    return allOk;
  }

  form.addEventListener('submit', function (e) {
    showErrors = true; // Enable error display on submit
    if (!evaluateAll()) {
      e.preventDefault();
      // Scroll to first error
      const firstError = form.querySelector('.is-invalid');
      if (firstError) {
        firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
        firstError.focus();
      }
    }
  });
});
