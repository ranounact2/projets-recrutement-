<!DOCTYPE html>
<html lang = "en">
<head>
    <#include "../common/head.ftl" />
        <link rel="stylesheet" href="/assets/css-min/style.min.css" defer/>
        <link rel="stylesheet" href="/assets/css-min/secret-code.min.css" defer/>
</head>
<body>
<header>
    <#include "/common/header.ftl">
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
    <#include "/common/footer.ftl">
</footer>

<script src="/assets/js-min/script.min.js" defer></script>

</body>
</html>
