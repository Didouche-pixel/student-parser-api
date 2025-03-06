<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Parser API</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        h1 { color: #333; }
        .container {
            background-color: #f9f9f9;
            border: 1px solid #ddd;
            padding: 20px;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<h1>API de Parsing d'Étudiants</h1>

<div class="container">
    <p>Cette API permet de transformer des fichiers Excel contenant des informations sur les étudiants en une liste structurée d'objets JSON.</p>
    <p><a href="api/students/parse">Tester l'API</a></p>

    <h2>Structure du fichier Excel attendue</h2>
    <p>Le fichier Excel doit avoir au minimum les colonnes suivantes (dans cet ordre) :</p>
    <ol>
        <li>Nom de famille</li>
        <li>Prénom</li>
        <li>Date de naissance (format: DD/MM/YYYY)</li>
        <li>Adresse</li>
    </ol>
</div>
</body>
</html>