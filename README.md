# Frontend pour l'Application de Parsing d'Étudiants

Ce frontend simple permet d'importer des fichiers Excel contenant des données d'étudiants et d'afficher les résultats.

## Structure du projet

```
student-parser-front/
│
├── public/               # Dossier pour les fichiers statiques
│
├── src/
│   ├── assets/           # Ressources (images, styles)
│   │   ├── excel-icon.svg
│   │   └── styles.css
│   │
│   └── main.js           # Code JavaScript principal
│
└── index.html            # Page HTML principale
```

## Comment exécuter le projet

Comme ce projet n'utilise pas npm ou d'autres gestionnaires de paquets, vous pouvez simplement ouvrir le fichier `index.html` dans un navigateur pour le tester localement.

Pour un déploiement en production, il suffit de copier tous les fichiers sur votre serveur web.

## Configuration de l'API

L'URL de l'API est configurée dans le fichier `src/main.js` :

```javascript
const API_URL = 'http://localhost:8080/student-parser-api/api/students';
```

Modifiez cette URL pour correspondre à votre environnement de développement ou de production.

## Fonctionnalités

- Import de fichiers Excel (.xlsx, .xls)
- Validation des types de fichiers
- Affichage des données d'étudiants dans un tableau
- Recherche, tri et pagination des données
- Interface responsive

## Intégration avec le backend

Ce frontend est conçu pour fonctionner avec l'API Java que vous avez développée. Il envoie les fichiers Excel au point d'accès `/parse` et affiche les résultats retournés par l'API.

## Technologies utilisées

- HTML5
- CSS3
- JavaScript
- Vue.js (via CDN)
- Axios (via CDN)
