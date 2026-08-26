<!DOCTYPE html>
<html lang = "en">
<head>
    <#include "../common-new/head.ftl" />
    <link rel="stylesheet" href="${assetsPath}/css-min/secret-code.min.css" defer/>
</head>
<body>
<header>
    <#include "/common-new/header.ftl">
</header>

<!-- bread -->
    <section class="breadcumb">
        <h1>Mes offres </h1>
        <ul>
            <li><a href="../index.html">Accueil</a></li>
            <li><a href="/ajouter-offre-emploi">Mes offres</a></li>
        </ul>
    </section>

    <div class="container1">

        <form id="emailForm" method="POST" action="/mes-annonces-emploi">
            <p>Merci de saisir votre adresse mail pour recevoir le lien vers vos offres.</p>
            <div class="linput">
                <div class="form-group">
                    <label for="email">Email *</label>
                    <input type="email" name="email" id="email" required>
                </div>
                <button class="btn" type="submit"><i class="fa fa-arrow-right"> </i> Envoyer</button>
            </div>


        </form>

    </div>

<footer>
    <#include "/common-new/footer.ftl">
</footer>

<script src="${assetsPath}/js/script.js" defer></script>

</body>
</html>
