<!DOCTYPE html>
<html lang="en">
<head>
    <#include "../common-new/head.ftl" />
        <link rel="stylesheet" href="${assetsPath}/css-min/message.min.css" defer/>
        <style>
            html, body {
                height: 100%;
                margin: 0;
                padding: 0;
            }
            body {
                display: flex;
                flex-direction: column;
                min-height: 100vh;
            }
            .bread,
            .container2 {
                flex: 1;
            }
            footer {
                margin-top: auto;
            }
        </style>
</head>

<body>

    <#include "../common-new/header.ftl" />
<!-- bread -->
<div class="bread">
    <div class="titre">
        <h2>Mes offres d'emploi</h2>
        <div class="breadcrumbs" id="breadcrumbs">
            <a class="breadcrumbs__link" href="/">Accueil</a>
            <span> > </span>
            <br>
            <a class="breadcrumbs__link" href="/a-propos-emplois-maroc">Apropos</a>
            <span> > </span>
            <#if data.job??>
                <a href="/m-office/mes-annonces/${data.job.secretCode}">Voir mes offres</a>
                <span> > </span>
                <br>
            </#if>
            <#if !data.job??>
                Mes offres
            </#if>
        </div>
    </div>
</div>

<!-- container -->
<div class="container2">
    <#if data.messages.message??>
        <b><pre>${data.messages.message}</pre></b>
    </#if>
    <a class="btn" href="/">Accueil</a>
</div>

    <#include "../common-new/footer.ftl" />
    <script src="${assetsPath}/js/script.js" defer></script>
</body>
</html>