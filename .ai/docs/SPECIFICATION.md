# Spécifications Fonctionnelles - Emplois Maroc

## Vue d'ensemble

Ce document contient les spécifications fonctionnelles et la roadmap du projet Emplois Maroc. Il sert de référence pour le développement et peut être enrichi via des prompts pour ajouter de nouvelles fonctionnalités.

---

## Roadmap

### Version Actuelle
- **Version**: 1.0
- **Date**: 2026-01-26
- **Statut**: En développement actif

### Prochaines Versions

#### Version 1.1 (Planifiée)
- [ ] Fonctionnalité A
- [ ] Fonctionnalité B
- [ ] Amélioration C

#### Version 1.2 (Future)
- [ ] Fonctionnalité D
- [ ] Fonctionnalité E

---

## Exigences Fonctionnelles

### FR-001: Gestion des Offres d'Emploi
**Priorité**: Haute  
**Statut**: Implémenté

- FR-001.1: Création d'une offre d'emploi
- FR-001.2: Modification d'une offre d'emploi
- FR-001.3: Suppression d'une offre d'emploi
- FR-001.4: Consultation d'une offre d'emploi
- FR-001.5: Liste paginée des offres d'emploi

### FR-002: Recherche et Filtrage
**Priorité**: Haute  
**Statut**: Implémenté

- FR-002.1: Recherche par domaine
- FR-002.2: Recherche par région
- FR-002.3: Recherche par ville
- FR-002.4: Recherche par mot-clé

### FR-003: Candidature
**Priorité**: Haute  
**Statut**: Implémenté

- FR-003.1: Soumission d'une candidature
- FR-003.2: Upload de CV
- FR-003.3: Validation des données de candidature

### FR-004: Middle Office
**Priorité**: Moyenne  
**Statut**: Implémenté

- FR-004.1: Liste des annonces par code secret
- FR-004.2: Modification d'annonce via code secret
- FR-004.3: Suppression d'annonce via code secret

---

## Exigences Non-Fonctionnelles

### NFR-001: Performance
- Temps de réponse < 2 secondes pour les pages principales
- Support de 1000 utilisateurs simultanés
- Pagination efficace pour les grandes listes

### NFR-002: Sécurité
- Validation des données d'entrée
- Protection contre les injections
- Gestion sécurisée des uploads de fichiers
- Codes secrets pour la gestion des annonces

### NFR-003: Scalabilité
- Architecture modulaire permettant l'extension
- Base de données MongoDB scalable
- Gestion efficace de la mémoire

### NFR-004: Disponibilité
- Disponibilité cible: 99.5%
- Gestion d'erreurs robuste
- Pages d'erreur personnalisées

---

## User Stories

### US-001: En tant que recruteur
**Je veux** publier une offre d'emploi  
**Afin de** attirer des candidats qualifiés

**Critères d'acceptation**:
- Je peux remplir un formulaire avec les détails de l'offre
- Je reçois un code secret pour gérer mon offre
- L'offre apparaît sur le site après validation

### US-002: En tant que candidat
**Je veux** rechercher des offres d'emploi  
**Afin de** trouver un poste qui correspond à mes compétences

**Critères d'acceptation**:
- Je peux rechercher par domaine, région, ou ville
- Je vois une liste paginée des résultats
- Je peux consulter les détails d'une offre

### US-003: En tant que candidat
**Je veux** postuler à une offre  
**Afin de** soumettre ma candidature

**Critères d'acceptation**:
- Je peux remplir un formulaire de candidature
- Je peux uploader mon CV
- Je reçois une confirmation de candidature

### US-004: En tant que recruteur
**Je veux** gérer mes annonces  
**Afin de** les modifier ou les supprimer

**Critères d'acceptation**:
- Je peux accéder à mes annonces via un code secret
- Je peux modifier les détails d'une annonce
- Je peux supprimer une annonce

---

## Spécifications Techniques

### Architecture
- **Framework**: Servlet API avec Jetty embarqué
- **Langage**: Java 17
- **Base de données**: MongoDB 5.1.1
- **Templating**: FreeMarker 2.3.31
- **Build**: Maven

### Contraintes Techniques
- Compatibilité Java 17+
- Support des navigateurs modernes
- Responsive design pour mobile et desktop
- Encodage UTF-8 pour le support multilingue

### Dépendances Externes
- MongoDB (base de données)
- Jetty (serveur web embarqué)
- FreeMarker (moteur de templates)
- SLF4j + Logback (logging)

---

## Critères d'Acceptation

### Critères Généraux
- Tous les tests unitaires passent
- Tous les tests d'intégration passent
- Code review approuvé
- Documentation à jour
- Pas de régression fonctionnelle

### Critères par Fonctionnalité

#### Création d'Offre
- ✅ Formulaire valide avec tous les champs requis
- ✅ Validation des données côté serveur
- ✅ Génération d'un code secret unique
- ✅ Persistance en base de données
- ✅ Affichage de l'offre sur le site

#### Recherche
- ✅ Recherche par domaine fonctionnelle
- ✅ Recherche par région fonctionnelle
- ✅ Pagination correcte
- ✅ Tri des résultats

---

## Notes

Ce document peut être enrichi via des prompts pour ajouter:
- Nouvelles fonctionnalités
- Modifications des exigences existantes
- Mise à jour de la roadmap
- Ajout de user stories

Pour chaque modification, mettre à jour:
- La version du document
- La date de modification
- Le statut des fonctionnalités concernées
