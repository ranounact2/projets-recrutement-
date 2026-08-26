<!DOCTYPE html>
<html lang="en">
<head>
    <#include "../common/head.ftl" />
        <link rel="stylesheet" href="/assets/css-min/style.min.css" defer/>
        <link rel="stylesheet" href="/assets/css-min/message.min.css" defer/>
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

    <#include "../common/header.ftl" />
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
        <div style="background-color: #d4edda; color: #155724; padding: 20px; margin: 20px auto; border-radius: 5px; border: 1px solid #c3e6cb; max-width: 700px; text-align: center;">
            <strong style="font-size: 18px;">Succès !</strong>
            <p style="margin-top: 10px; font-size: 15px;">${data.messages.message}</p>
        </div>
    </#if>
    <div style="text-align: center; margin-top: 20px;">
        <a class="btn" href="/">Accueil</a>
        <#if data.job?? && data.job.secretCode??>
            <a class="btn" href="/m-office/mes-annonces/${data.job.secretCode}" style="margin-left: 10px;">Voir mes offres</a>
        </#if>
    </div>
</div>

    <#include "../common/footer.ftl" />
    <script src="/assets/js/script.js" defer></script>
</body>
</html>