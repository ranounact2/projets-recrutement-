<!DOCTYPE html>
<html lang = "en">
<head>
    <#include "../common-new/head.ftl">
        <link rel="stylesheet" href="${assetsPath}/css-min/contact.min.css" defer/>
</head>
<body>
<header>
    <#include "../common-new/header.ftl">
</header>
<section class="breadcumb">
    <h1>Contactez-Nous</h1>
    <ul>
        <li><a href="/">Accueil</a></li>
        <li><a href="/contact">Contactez-Nous</a></li>
    </ul>
</section>
<section class="contact-form">
    <#if param?? && param.success?? && (param.success?is_string) && (param.success?length > 0)>
        <div class="success-message">
            ${param.success}
        </div>
    </#if>
    <form action="/contact" method="post" id="contactForm" novalidate>
        <p>Pour toutes vos questions et suggestions, laissez-nous un message.</p>
        <div class="form-group-two">
            <div class="form-group">
                <label for="name">Nom complet *</label>
                <#if param?? && param.name?? && param.name?length gt 0>
                    <input type="text" name="name" id="name" value="${param.name}" required class="<#if param.errors?? && param.errors.name??>is-invalid</#if>">
                <#else>
                    <input type="text" name="name" id="name" required class="<#if param.errors?? && param.errors.name??>is-invalid</#if>">
                </#if>
                <#if param?? && param.errors?? && param.errors.name??>
                    <div class="field-error" id="name-error" aria-live="polite">${param.errors.name}</div>
                <#else>
                    <div class="field-error" id="name-error" aria-live="polite"></div>
                </#if>
            </div>
            <div class="form-group">
                <label for="email">Email *</label>
                <#if param?? && param.email?? && param.email?length gt 0>
                    <input type="email" name="email" id="email" value="${param.email}" required class="<#if param.errors?? && param.errors.email??>is-invalid</#if>">
                <#else>
                    <input type="email" name="email" id="email" required class="<#if param.errors?? && param.errors.email??>is-invalid</#if>">
                </#if>
                <#if param?? && param.errors?? && param.errors.email??>
                    <div class="field-error" id="email-error" aria-live="polite">${param.errors.email}</div>
                <#else>
                    <div class="field-error" id="email-error" aria-live="polite"></div>
                </#if>
            </div>
            <div class="form-group">
                <label for="tel">Teléphone *</label>
                <#if param?? && param.phone?? && param.phone?length gt 0>
                    <input type="tel" name="phone" id="tel" value="${param.phone}" required class="<#if param.errors?? && param.errors.phone??>is-invalid</#if>">
                <#else>
                    <input type="tel" name="phone" id="tel" required class="<#if param.errors?? && param.errors.phone??>is-invalid</#if>">
                </#if>
                <#if param?? && param.errors?? && param.errors.phone??>
                    <div class="field-error" id="tel-error" aria-live="polite">${param.errors.phone}</div>
                <#else>
                    <div class="field-error" id="tel-error" aria-live="polite"></div>
                </#if>
            </div>
            <div class="form-group">
                <label for="subject">Sujet *</label>
                <#if param?? && param.objet?? && param.objet?length gt 0>
                    <input type="text" name="objet" id="subject" value="${param.objet}" required class="<#if param.errors?? && param.errors.objet??>is-invalid</#if>">
                <#else>
                    <input type="text" name="objet" id="subject" required class="<#if param.errors?? && param.errors.objet??>is-invalid</#if>">
                </#if>
                <#if param?? && param.errors?? && param.errors.objet??>
                    <div class="field-error" id="subject-error" aria-live="polite">${param.errors.objet}</div>
                <#else>
                    <div class="field-error" id="subject-error" aria-live="polite"></div>
                </#if>
            </div>
        </div>
        <label for="content">Contenu *</label>
        <#if param?? && param.message?? && param.message?length gt 0>
            <textarea name="message" id="content" cols="30" rows="10" required class="<#if param.errors?? && param.errors.message??>is-invalid</#if>">${param.message}</textarea>
        <#else>
            <textarea name="message" id="content" cols="30" rows="10" required class="<#if param.errors?? && param.errors.message??>is-invalid</#if>"></textarea>
        </#if>
        <#if param?? && param.errors?? && param.errors.message??>
            <div class="field-error" id="content-error" aria-live="polite">${param.errors.message}</div>
        <#else>
            <div class="field-error" id="content-error" aria-live="polite"></div>
        </#if>
<#--        <div class="form-group">-->
<#--            <label for="captchafield">Veillez valider cette captcha</label>-->
<#--            <input type="text" id="captchafield" value="${captcha}" readonly>-->
<#--            <br>-->
<#--            <input type="text" name="g-recaptcha-response" id="validercaptcha">-->
<#--        </div>-->
        <#if param?? && param.error?? && param.error?length gt 0>
            <div class="error-message" style="color:red;">
                ${param.error}
            </div>
        </#if>
        <div style="text-align: center; ">
            <div class="g-recaptcha" data-sitekey="6LcOQEksAAAAAL9z0-ZKNZpzl-90mRJX4Fmt-UfO"></div>
        </div>
        <div class="conteneur-bouton">
            <button type="submit" id="sendBtn">Envoyer</button>
        </div>
    </form>
</section>
<section class="offre">
    <div class="grid-offre">
        <div>
            <h2>Recrutement?</h2>
            <p>Annoncez vos offres d'emploi à des millions d'utilisateurs mensuels</p>
            <a href="/ajouter-offre-emploi">Commencer <img src="assets/img/right-arrow.png" alt=""></a>
        </div>
        <img src="assets/img/offer.png" alt="Recrutement">
    </div>
</section>
<footer>
    <#include "../common-new/footer.ftl">
</footer>

<script src="${assetsPath}/js/script.js" defer></script>
<script src="${assetsPath}/js/contact.js" defer></script>
<script src="https://www.google.com/recaptcha/api.js" async defer></script>
</body>

</html>