<!DOCTYPE html>
<html lang = "en">
    <header class="white-header" id="header">
        <a href="/">
        <img src="/assets/img/logo.png" alt="logo" class="logo" />
        </a>
        <div class="burger" onclick="toggleBurger(this)">
            <img src="/assets/img/menu.svg" alt="menu icon" class="burger-icon" />
        </div>

        <div class="nav">
            <ul class="menu-links">
                <li><a href="/">Accueil</a></li>
                <li><a href="/a-propos-emplois-maroc">A propos</a></li>
                <li><a href="/contact">Contact</a></li>
            </ul>

            <div class="buttons">
                <button class="btn-white" onclick="window.location.href='/ajouter-offre-emploi';">Ajouter une offre</button>
                <button class="btn-orange" onclick="window.location.href='/mes-annonces-emploi';">mes offres</button>
            </div>
        </div>
    </header>
</html>