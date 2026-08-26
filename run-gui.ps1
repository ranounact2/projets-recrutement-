# Script de lancement 1-clic de la GUI Playwright (Windows PowerShell)

$PSScriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location $PSScriptRoot

Write-Host "=======================================================" -ForegroundColor Cyan
Write-Host " 🚀 Démarrage de l'Interface Graphique Playwright (GUI)" -ForegroundColor Green
Write-Host "=======================================================" -ForegroundColor Cyan

npm run test:gui
