document.addEventListener('DOMContentLoaded', function () {
  const form = document.getElementById('contactForm');
  if (!form) return;

  const nameEl = document.getElementById('name');
  const emailEl = document.getElementById('email');
  const phoneEl = document.getElementById('tel');
  const subjectEl = document.getElementById('subject');
  const messageEl = document.getElementById('content');
  const sendBtn = document.getElementById('sendBtn');

  const errors = {
    name: document.getElementById('name-error'),
    email: document.getElementById('email-error'),
    tel: document.getElementById('tel-error'),
    subject: document.getElementById('subject-error'),
    content: document.getElementById('content-error')
  };

  let showErrors = false; // Only show errors after submit attempt

  function isEmailValid(value) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i;
    return re.test(String(value).trim());
  }

  function isPhoneValid(value) {
    const trimmed = String(value).trim();
    // Pattern 1: 06 or 07 followed by 8 digits (10 digits total)
    const localPattern = /^0[67]\d{8}$/;
    // Pattern 2: +212 6 or +212 7 followed by 8 digits (with optional space after +212)
    const intlPattern = /^\+212\s?[67]\d{8}$/;
    
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

  function validateName() {
    const ok = notEmpty(nameEl);
    setValidity(nameEl, ok);
    showError('name', ok ? '' : 'Ce champ est requis.');
    return ok;
  }

  function validateEmail() {
    const ok = notEmpty(emailEl) && isEmailValid(emailEl.value);
    setValidity(emailEl, ok);
    let msg = '';
    if (!notEmpty(emailEl)) msg = 'Ce champ est requis.';
    else if (!isEmailValid(emailEl.value)) msg = "Adresse email invalide.";
    showError('email', msg);
    return ok;
  }

  function validatePhone() {
    const ok = notEmpty(phoneEl) && isPhoneValid(phoneEl.value);
    setValidity(phoneEl, ok);
    let msg = '';
    if (!notEmpty(phoneEl)) msg = 'Ce champ est requis.';
    else if (!isPhoneValid(phoneEl.value)) msg = 'Numéro invalide. Veuillez respecter le format national ("06","07") ou international ("+212").';
    showError('tel', msg);
    return ok;
  }

  function validateSubject() {
    const ok = notEmpty(subjectEl);
    setValidity(subjectEl, ok);
    showError('subject', ok ? '' : 'Ce champ est requis.');
    return ok;
  }

  function validateMessage() {
    const ok = notEmpty(messageEl);
    setValidity(messageEl, ok);
    showError('content', ok ? '' : 'Ce champ est requis.');
    return ok;
  }

  function checkAllValid() {
    // Silent check - only for validation, no visual feedback
    const nameOk = notEmpty(nameEl);
    const emailOk = notEmpty(emailEl) && isEmailValid(emailEl.value);
    const phoneOk = notEmpty(phoneEl) && isPhoneValid(phoneEl.value);
    const subjectOk = notEmpty(subjectEl);
    const messageOk = notEmpty(messageEl);
    
    return nameOk && emailOk && phoneOk && subjectOk && messageOk;
  }

  function evaluateAll() {
    const results = [
      validateName(),
      validateEmail(),
      validatePhone(),
      validateSubject(),
      validateMessage()
    ];
    const allOk = results.every(Boolean);
    return allOk;
  }

  // No input listeners needed - button always stays enabled

  function validateKaptcha() {
    const kaptchaInput = document.getElementById('kaptchaResponse');
    if (!kaptchaInput || kaptchaInput.value.trim().length === 0) {
      if (kaptchaInput) kaptchaInput.style.border = '1px solid #dc3545';
      return false;
    }
    kaptchaInput.style.border = '';
    return true;
  }

  form.addEventListener('submit', function (e) {
    showErrors = true;

    if (!evaluateAll()) {
      e.preventDefault();
      return;
    }

    if (!validateKaptcha()) {
      e.preventDefault();
      var kaptchaInput = document.getElementById('kaptchaResponse');
      if (kaptchaInput) kaptchaInput.focus();
    }
  });
});
