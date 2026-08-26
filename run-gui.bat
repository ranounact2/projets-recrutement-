@echo off
:: Script de lancement 1-clic de la GUI Playwright (Windows Command Prompt)
cd /d "%~dp0"

echo =======================================================
echo  🚀 Demarrage de l'Interface Graphique Playwright (GUI)
echo =======================================================

npm run test:gui
