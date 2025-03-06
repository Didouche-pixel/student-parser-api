# API Parser d'Étudiants

Cette API permet de convertir des fichiers Excel contenant des informations d'étudiants en format JSON.

## Comment utiliser

### Avec Docker (recommandé)

1. Assurez-vous d'avoir Docker et Docker Compose installés
2. Clonez ce dépôt
3. Lancez l'application : docker-compose up -d
4. Déployez le fichier WAR généré sur Tomcat

## Endpoints disponibles

### GET /api/students/parse

Vérifie que l'API est opérationnelle.

### POST /api/students/parse

Convertit un fichier Excel en liste d'étudiants au format JSON.

#### Paramètres
- `file` : Fichier Excel (.xlsx ou .xls) contenant les données d'étudiants

#### Format attendu du fichier Excel
Le fichier doit contenir ces colonnes (l'ordre est important) :
- Nom
- Prénom
- Date de naissance (format DD/MM/YYYY)
- Adresse

## Exemple d'utilisation

Avec curl :
bash
curl -X POST -F "file=@chemin/vers/fichier.xlsx" http://localhost:8080/student-parser-api/api/students/parse

## Structure du projet

src/main/java/com/model : Contient la classe Student
src/main/java/com/service : Contient le service de parsing Excel
src/main/java/com/controller : Contient les servlets

## Étape 4 : Initialiser un dépôt Git (si ce n'est pas déjà fait)

```bash
# Si vous n'avez pas encore de dépôt Git
git init
git add .
git commit -m "Initial commit with student parser API"