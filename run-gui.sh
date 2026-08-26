#!/usr/bin/env bash
# Script de lancement 1-clic de la GUI Playwright (macOS / Linux)

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cd "$DIR"

echo "======================================================="
echo " 🚀 Démarrage de l'Interface Graphique Playwright (GUI)"
echo "======================================================="

npm run test:gui
