<!DOCTYPE html>
<html lang="en">
<head>
   <#include "../common-new/head.ftl">
   <link rel="stylesheet" href="${assetsPath}/css-min/error-page.min.css" defer/>
</head>

<body>
<img src="/assets/img/logo.png" alt="" class="logo">
<section>
    <#if code?? && code == "404">
        <img src="/assets/img/404.png" alt="" srcset="">
        <h1>Nous sommes désolés, page introuvable</h1>
        <p>Malheureusement, la page que vous cherchiez n'a pas pu être trouvée. Il peut être temporairement indisponible,
            déplacé ou ne plus exister.</p>
    <#elseif code?? && code == "500">
        <h1>Erreur serveur interne</h1>
        <p>Une erreur interne du serveur s'est produite. Notre équipe technique a été notifiée et travaille à résoudre le problème.</p>
        <p>Veuillez réessayer dans quelques instants ou retourner à la page d'accueil.</p>
    <#else>
        <h1>Erreur ${code!""}</h1>
        <#if error??>
            <p>${error}</p>
        <#else>
            <p>Une erreur s'est produite. Veuillez réessayer plus tard.</p>
        </#if>
    </#if>
    
    <a href="/">page d'accueil</a>
    
    <#-- Afficher les détails techniques UNIQUEMENT en mode développement -->
    <#if showDetails?? && showDetails>
        <div style="margin-top: 30px; padding: 20px; background-color: #fff3cd; border: 1px solid #ffc107; border-radius: 5px; text-align: left; font-family: monospace; font-size: 12px; max-width: 900px; margin-left: auto; margin-right: auto; overflow: auto;">
            <h3 style="color: #856404; margin-top: 0;">⚠️ Détails techniques (mode développement uniquement)</h3>
            <p><strong>URL demandée:</strong> ${requestUri!""}</p>
            <p><strong>Code erreur:</strong> ${code!""}</p>
            <#if error??>
                <p><strong>Message:</strong> ${error}</p>
            </#if>
            <#if exception??>
                <hr style="border-color: #ffc107;">
                <p><strong>Type d'exception:</strong> ${exception.class.name}</p>
                <p><strong>Message exception:</strong> ${(exception.message)!""}</p>
            </#if>
            <#if stackTrace?? && stackTrace?has_content>
                <hr style="border-color: #ffc107;">
                <h4 style="color: #856404;">Stack Trace:</h4>
                <pre style="background-color: #1e1e1e; color: #d4d4d4; padding: 15px; border-radius: 5px; overflow: auto; max-height: 400px; font-size: 11px;">${stackTrace}</pre>
            </#if>
        </div>
    </#if>
</section>
</body>
</html>