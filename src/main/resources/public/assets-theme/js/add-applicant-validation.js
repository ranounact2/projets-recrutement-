(function () {
    // Client-side required field validation for the add applicant form.
    document.addEventListener('DOMContentLoaded', function () {
        var form = document.querySelector('.applicant form');
        if (!form) {
            return;
        }

        var errorMap = new WeakMap();

        var getErrorElement = function (field) {
            var existing = errorMap.get(field);
            if (existing) {
                return existing;
            }

            var error = document.createElement('p');
            error.className = 'field-error';
            error.style.margin = '0.25rem 0 0';
            error.style.color = '#c62828';
            error.style.fontSize = '0.85rem';
            error.style.fontWeight = '500';
            error.hidden = true;

            field.insertAdjacentElement('afterend', error);
            errorMap.set(field, error);
            return error;
        };

        var showError = function (field, message) {
            var error = getErrorElement(field);
            error.textContent = message;
            error.hidden = false;
        };

        var clearError = function (field) {
            var error = errorMap.get(field);
            if (error) {
                error.textContent = '';
                error.hidden = true;
            }
        };

        var hasValue = function (field) {
            if (field.type === 'file') {
                return field.files && field.files.length > 0;
            }
            return field.value.trim().length > 0;
        };

        var isEmailValid = function (value) {
            var pattern = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i;
            return pattern.test(String(value).trim());
        };

        var isPhoneValid = function (value) {
            var trimmed = String(value).trim();
            var localPattern = /^0[67]\d{8}$/;
            var intlPattern = /^\+212\s?[67]\d{8}$/;
            return localPattern.test(trimmed) || intlPattern.test(trimmed);
        };

        var requiredFields = [
            { selector: '#nom', missingMessage: 'Le nom est requis.' },
            { selector: '#prenom', missingMessage: 'Le prénom est requis.' },
            {
                selector: '#email',
                missingMessage: 'L\'email est requis.',
                validator: function (element) {
                    return isEmailValid(element.value) ? '' : 'Adresse email invalide.';
                }
            },
            {
                selector: '#phone',
                missingMessage: 'Le téléphone est requis.',
                validator: function (element) {
                    return isPhoneValid(element.value) ? '' : 'Numéro invalide (ex: 0612345678 ou +212 612345678).';
                }
            },
            { selector: '#motivation', missingMessage: 'Merci de renseigner votre motivation.' },
            { selector: '#formation', missingMessage: 'La formation est requise.' },
            {
                selector: '#experienceLevel',
                missingMessage: 'Le niveau d\'expérience est requis.'
            },
            {
                selector: '#cv',
                missingMessage: 'Merci d\'ajouter votre CV.',
                validator: function (element) {
                    var maxBytes = 5 * 1024 * 1024;
                    if (!element.files || !element.files.length) {
                        return '';
                    }
                    if (element.files[0].size > maxBytes) {
                        return 'La taille du fichier ne doit pas dépasser 5 Mo.';
                    }
                    return '';
                }
            }
        ]
            .map(function (field) {
                var element = form.querySelector(field.selector);
                if (!element) {
                    return null;
                }
                return {
                    element: element,
                    missingMessage: field.missingMessage,
                    validator: field.validator
                };
            })
            .filter(Boolean);

        var validateField = function (fieldConfig, options) {
            var opts = options || {};
            var silent = !!opts.silent;
            var element = fieldConfig.element;
            var isValid = true;
            var message = '';

            if (!hasValue(element)) {
                isValid = false;
                message = fieldConfig.missingMessage || 'Ce champ est requis.';
            } else if (fieldConfig.validator) {
                var validatorMessage = fieldConfig.validator(element);
                if (validatorMessage) {
                    isValid = false;
                    message = validatorMessage;
                }
            }

            if (isValid) {
                clearError(element);
            } else if (!silent) {
                showError(element, message);
            }

            return isValid;
        };

        form.addEventListener('submit', function (event) {
            var firstInvalidField = null;

            requiredFields.forEach(function (field) {
                if (!validateField(field)) {
                    if (!firstInvalidField) {
                        firstInvalidField = field.element;
                    }
                }
            });

            if (firstInvalidField) {
                event.preventDefault();
                event.stopPropagation();
                firstInvalidField.focus();
            }
        });
    });
}());
