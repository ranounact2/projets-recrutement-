<!DOCTYPE html>
<html lang = "en">

<head>
    <#include "../common/head.ftl">
        <link rel="stylesheet" href="/assets/css-min/style.min.css" defer/>
        <link rel="stylesheet" href="/assets/css-min/temp/about.min.css" defer/>
</head>

<body>
<header>
    <#include "../common/header.ftl">
</header>
    <section class="breadcumb">
        <h1>A propos</h1>
        <ul>
            <li><a href="../index.html">Acceuil</a></li>
            <li><a href="">A propos</a></li>
        </ul>
    </section>
    <section class="both-sections">
        <div class="entreprise-content">
            <h1>
                <span class="text-speciale">Entreprises</span>, déposez vos annonces
                de recrutement.
            </h1>
            <p>
                Cette plateforme vous permet de partager vos offres d’emploi
                gratuitement et rapidement. Il vous suffit de remplir un formulaire
                simple et attendre la validation instantanée de votre annonce. Vous
                avez aussi la possibilité de modifier ou supprimer votre offre
                d’emploi quand vous le souhaitez avec un simple bouton. La création de
                compte n’est pas nécessaire.
            </p>
            <a href="/ajouter-offre-emploi"
            >Commencer <img src="/assets/img/right-arrow.png" alt=""
            /></a>
        </div>
        <div class="image">
            <img src="/assets/img/about-1.jpg" alt="Entreprise" />
        </div>
    </section>
    <div class="divider-wrapper">
        <hr class="section-divider" />
    </div>
    <section class="both-sections2">
        <div class="image-side">
            <img src="/assets/img/about-2.jpg" alt="Entreprise" />
        </div>
        <div class="jobseeker-content">
            <h1>
                <span class="text-speciale">Chercheurs d’emploi,</span> trouvez
                l’offre d’emploi qui vous convient.
            </h1>
            <p>
                Notre plateforme reçoit chaque jour de nouvelles offres et annonces de
                recrutement dans toutes les villes et dans tous les domaines. C’est
                simple, cherchez l’offre d’emploi de votre choix et postulez en 2
                minutes. Vous n’avez pas besoin de créer un compte pour postuler.
            </p>
            <a href="/ajouter-offre-emploi">
                Chercher une offre <img src="/assets/img/right-arrow.png" alt="" />
            </a>
        </div>
    </section>

    <section class="processus">
        <h3>Processes facile et rapide</h3>
        <div class="etapes">
            <div class="etape">
                <img
                src="/assets/img/step-1.png"
                alt="creation d annonce"
                />
                <h4>Creation d'annonce</h4>
                <p>Remplir un formulaire simple et gratuit</p>
            </div>
            <div class="etape">
                <img
                src="/assets/img/validation.png"
                alt="creation d annonce"
                />
                <h4>Creation d'annonce</h4>
                <p>Remplir un formulaire simple et gratuit</p>
            </div>
            <div class="etape">
                <img
                src="/assets/img/step-3.png"
                alt="creation d annonce"
                />
                <h4>Creation d'annonce</h4>
                <p>Remplir un formulaire simple et gratuit</p>
            </div>
        </div>
    </section>
<footer>
    <#include "../common/footer.ftl">
</footer>
<script src="/assets/js-min/script.min.js" defer></script>
</body>
</html>
