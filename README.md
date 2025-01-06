# Testez une application Full-Stack

Ce projet a pour but de voir les différentes phases de test auquel un développeur peut être confronté.

## Installation

### Récupération des sources

1. Récupération du code source de l'application: [le dépôt GitHub du projet](https://github.com/OpenClassrooms-Student-Center/Testez-une-application-full-stack).

### Base de données

1. Lancez un terminal et lancez votre application MySQL

2. Créer la base de donnée qui sera utilisé par l'application avec la commande

    ```mysql
    CREATE DATABASE <nom de la database>;
    USE <nom de la database>;
    ```

3. Exécutez le script SQL qui se trouve dans le répertoire suivant : ressources/sql/script.sql.

### Back-end

1. Lancez un terminal et placez vous dans le répertoire backend.

2. Exécutez la commande suivante pour installer les dépendances :

    ```bash
    mvn install
    ```

3. Démarrez le serveur backend avec la commande :

    ```bash
    mvn spring-boot:run
    ```

### Front-end

1. Lancez un terminal et placez vous dans le répertoire frontend.

2. Exécutez la commande suivante pour installer les dépendances :

    ```bash
    npm i
    ```

3. Démarrez le serveur de développement en exécutant :

    ```bash
    ng serve --open
    ```

Le frontend sera accessible à l'adresse [localhost:4200](http://localhost:4200/).

## Tests

### Tests front-end

1. Lancez un terminal et placez vous dans le répertoire frontend.

2. Exécutez la commande suivante pour lancer les tests unitaires :

    ```bash
    ng test
    ```

3. Pour générer le rapport de couverture de code, exécutez la commande suivante :

    ```bash
    ng test --coverage
    ```

Le rapport de couverture sera présent aussi dans le fichier index.html qui se trouve dans le répertoire coverage/jest/lcov-report du frontend.

### Tests End to end

1. Lancez un terminal et placez vous dans le répertoire frontend.

2. Exécutez la commande suivante pour lancer les tests end-to-end :

    ```bash
    npm run cypress:run
    ```

3. Pour générer le rapport de couverture des tests end-to-end, exécutez dans un premier temps la commande suivante :

    ```bash
    npm run cypress:run
    ```

Puis, exécutez la commande suivante :
    ```bash
    npm run e2e:coverage
    ```

Le rapport de couverture sera présent aussi dans le fichier index.html qui se trouve dans le répertoire coverage/lcov-report du frontend.

### Tests Back-end

1. Lancez un terminal et rendez-vous dans le répertoire du backend.

2. Exécutez la commande suivante pour lancer les tests unitaires :

    ```bash
    mvn test
    ```

3. Pour générer le rapport de couverture de code, exécutez la commande suivante :

    ```bash
    mvn test jacoco:report
    ```

Le rapport de couverture de code sera présent aussi dans le fichier index.html qui se trouve dans le répertoire target/site/jacoco du backend.
