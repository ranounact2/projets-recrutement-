Tests Playwright — Module Recherche (emploismaroc)

Ce document décrit la suite de tests automatisés Playwright (Java) mise en place pour valider le module de recherche de la plateforme emploismaroc, ainsi que l'interface graphique de pilotage développée pour lancer et suivre ces tests sans passer par le terminal.

🎯 Objectif

Couvrir de bout en bout le module de recherche de la plateforme : fonctionnement, sécurité, performance et fiabilité des données, à travers des tests automatisés rejouables à chaque évolution du code.

📁 Structure du projet de tests
test/
└── playwright/
    ├── config/       # Configuration Playwright (navigateurs, timeouts, environnements)
    ├── factory/       # Fabriques de données de test / instanciation des objets de test
    ├── pages/          # Page Object Model : représentation des pages du site testées
    └── tests/
        ├── BasePlaywrightTest.java
        ├── DataAndNetworkValidationTest.java
        ├── FunctionalSearchTest.java
        ├── PerformanceAndSecurityTest.java
        └── UiVerificationSearchTest.java
Description des classes de test
Classe	Rôle
BasePlaywrightTest	Classe de base : initialise et ferme le navigateur, centralise la configuration commune (setup/teardown) partagée par tous les autres tests.
FunctionalSearchTest :	Vérifie le comportement fonctionnel du moteur de recherche : recherche par mot-clé, filtres, résultats attendus, cas limites (recherche vide, résultats absents, etc.).
UiVerificationSearchTest :	Contrôle l'affichage et l'interface de la page de recherche : présence des éléments visuels, mise en page, retours visuels à l'utilisateur.
DataAndNetworkValidationTest :	Valide les données renvoyées par le serveur/l'API lors d'une recherche, ainsi que le comportement réseau (requêtes, réponses, formats de données).
PerformanceAndSecurityTest:	Mesure les temps de réponse du module de recherche et vérifie l'absence de failles de sécurité basiques (ex. injection, exposition de données sensibles).

ℹ️ À compléter/ajuster : n'hésite pas à préciser ici les scénarios exacts couverts par chaque classe si tu veux un niveau de détail plus fin (ex. liste des cas de test).

🖥️ Interface de pilotage des tests (Test GUI)

Une interface web dédiée a été développée pour lancer et suivre l'exécution des tests sans utiliser le terminal, accessible en local sur http://localhost:3000.

Structure
test-gui/
├── public/
│   ├── app.js         # Logique front-end (affichage des résultats, interactions)
│   ├── index.html      # Page principale de l'interface
│   └── styles.css      # Mise en forme de l'interface
├── open-ui.js           # Script d'ouverture automatique de l'interface dans le navigateur
└── server.js             # Serveur local (Node.js) qui sert l'interface et déclenche les tests
Fonctionnalités

L'interface "Playwright Test Platform" affiche en temps réel :

Total des tests exécutés
Tests réussis
Tests échoués
Tests ignorés
Taux de conformité (score global de réussite)
Temps d'exécution total

Elle propose un Centre d'Action et de Lancement avec les commandes suivantes :

Bouton	Actionjj
Lancer Tous les Tests	Exécute l'intégralité de la suite de tests Playwright
Mode Démo (Navigateur Visible)	Lance les tests en mode non-headless pour visualiser les actions du navigateur en direct
Tester Module Recherche	Lance uniquement les tests liés au module de recherche
Mode Playwright UI	Ouvre l'interface native de debug/inspection de Playwright
Rapport HTML	Génère et ouvre le rapport de résultats au format HTML
Viewer de Traces	Ouvre le visualiseur de traces Playwright (captures d'écran, réseau, DOM à chaque étape) pour déboguer un test
Arrêter	Interrompt l'exécution des tests en cours
Lancer l'interface
bash
node server.js

Puis ouvrir : http://localhost:3000

⚙️ Exécution des tests en ligne de commande (Maven)
bash
mvn test -Dtest=FunctionalSearchTest surefire-report:report

⚠️ Note sur l'intégration continue (CI/CD) : ces tests Playwright nécessitent que les navigateurs (Chromium) et leurs dépendances système soient installés dans l'environnement d'exécution. À ce jour, ils sont exclus du pipeline GitLab CI (maven-surefire-plugin → <excludes>) car le runner CI ne dispose pas de ces dépendances. Ils restent exécutables en local via Maven ou l'interface de pilotage ci-dessus.

 État actuel
✅ Suite de tests fonctionnelle en local
✅ Interface graphique de pilotage opérationnelle
⏳ Intégration complète dans le pipeline CI/CD GitLab (à finaliser — installation de Chromium + dépendances sur le runner)