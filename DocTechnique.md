#Documentation technique

Ce document reprend les éléments techniques de l'application ISwearBox. L'**Architecture générale**, l'**Architecture applicative**, le **Modèle de données** et les **Dépendances** y sont décrits.

##Architecture générale

L’application se compose de quatre parties distinctes :

* Le serveur (back-end)
* L’application web (front-end)
* L’application mobile
* Le back-office (accessible via l'application web)

![system](https://github.com/Serli/swear-box/blob/master/public/images/system-rapport.jpg?raw=true)

Le serveur est hébergé sur **Heroku** avec l'application web. Ils sont tous les deux développés avec le framework **Play2** sur le même projet. L'application mobile est développée à part avec **Apache Cordova** et **Ionic**, elle communique avec le serveur via les web services d'Heroku. Le back-office est accessible via l'application web seulement aux administrateurs de l'application.

---
##Architecture applicative

L'application web et l'appli mobile sont composées de fichiers **HTML**, **CSS** et **Javascript**. Toutes les pages html sont rendues dynamiques grâce à un contrôleur **AngularJS** présent dans un fichier javascript associé. Il y a donc des directives Angular présentes dans certaines balises des pages html des deux applications, et des directives Ionic dans l’application mobile. Le contrôleur Angular s'occupe de la partie dynamique de la page et c'est lui qui fait les appels aux web services d'Heroku.

### Client *(front-end)*
Il est manipulable sur tous les types de supports (ordinateurs, tablettes et smartphones) grâce à l’utilisation de **Twitter Bootstrap**. Les échanges entre l’application web et le serveur se font via des requêtes HTTP envoyées depuis les contrôleurs Javascript liés aux pages html vers le serveur. Les contrôleurs récupèrent les informations nécessaires de la page html, créent leur requête et l’envoient au serveur. Ensuite, ils réceptionnent la réponse et modifient les informations de la page html dynamiquement.

Il y a 6 pages web :

* La page de **connexion** permet à l’utilisateur de se connecter à l’application via son compte google.
* La page **utilisateur** permet à l’utilisateur de visualiser l’ensemble des membres de la famille et d’incrémenter leur dette.
* La page **configuration** permet à l’utilisateur d’ajouter, de supprimer et de modifier des membres. Elle permet également de modifier la pénalité.
* La page **statistiques** permet à l’utilisateur de visualiser le nombre de gros mots prononcés par les membres. Il peut manipuler différents paramètres pour modifier l’affichage des statistiques.
* La page d’**aide** permet de guider l’utilisateur dans l’utilisation de l’application.
* La page d’**administration** correspond au back-office de l'application. Elle permet de gérer tout les utilisateurs de l’application. Elle n’est accessible qu’aux utilisateurs Administrateurs.

### Application mobile
L’application mobile est compatible avec **Android**, **IOS**, **Windows Mobile** et toutes les autres plates-formes desservies par Ionic. Ces plates-formes sont ajoutés avec la commande Ionic suivante entrée dans le terminal (exemple avec android) :
```
cordova platform add android
```
En plus de créer le squelette de l’application mobile, le framework Ionic est utilisé pour gérer le design de l’application.
Le changement entre les pages de l’application mobile est géré par le module  stateProvider d'Angular. Ce dernier les gère en tant que différents états et associe un contrôleur Angular à chacune d’entre elle.

L’application mobile ne permet pas de visionner les statistiques ou d’accéder au back-office. Par contre, elle permet de prendre une photo directement en tant que photo de profil. Pour le reste, elle possède les mêmes fonctionnalités que l’application web.

### Serveur *(back-end)*
Le serveur possède une couche modèle qui interface la base de données avec le serveur. Elle permet de manipuler les documents de la base de données depuis le serveur en Java.

Le serveur possède 5 contrôleurs :

* **Global**, qui sert à initialiser l’application.
* **Home**, qui gère la connexion et l'affichage des différentes vues .
* **Backoffice**, **Configuration** et **Statistics** qui gèrent les requêtes des pages correspondantes.

Les contrôleurs permettent de faire les fonctionnalités complexes de l’application.

Le serveur possède aussi une couche **DAO** (Data Access Object) qui permet d'interagir avec la base de données et les contrôleurs. Un contrôleur va demander à la couche DAO de récupérer, de modifier ou de supprimer un document grâce aux modèles. Cette couche est la seule à accéder à la base de données pour plus de sécurité.

### Routes
L’appel des fonctions de ces contrôleurs est défini dans le fichier routes. Chaque route est liée à une méthode d'un contrôleur qui sera appelée.

---
##Modèle de données

Les données sont stockées sur une base de données **MongoDB**.

Il y a 2 collections :

* **Consumer**, qui représente un utilisateur de l'application.
* **Statictics**, qui représente la prononciation d'un gros mot.

###Consumer
Un utilisateur se compose ainsi :

* Un id correspondant à son adresse e-mail  : String
* Un montant de pénalité pour ses membres : int
* Un booléen admin déterminant si il est administrateur du système
* Un booléen blacklisted déterminant si il est blacklisté
* Une liste de membres

 Un membre se compose ainsi :
	* Il est identifié par un id généré : ObjectId
	* Un nom : String
	* Un prénom : String
	* Une dette : int
	* Une URL pour l'image : String

###Statistics
Une statistique se compose ainsi :

* Un id généré : ObjectId
* Une date de prononciation : Date
* La personne ayant prononcé le gros mot : Person

---
##Dépendances

Pour réaliser toutes les fonctionnalités, l’application utilise certains plugins, librairies, services et frameworks.

###Application mobile :
L’application mobile nécessite l'importation de deux plugins créés par Apache Cordova :

* **InAppBrowser**, qui permet l’utilisation du browser utilisé dans les contrôleurs Angular, pour la connexion mobile.
* **Camera**, qui permet l’utilisation de l’appareil photo et de la sélection et du redimensionnement de photos existantes.

Ces deux plugins sont ajoutés avec les commandes Ionic suivantes entrées dans le terminal :
```
cordova plugin add org.apache.cordova.inAppBrowser
cordova plugin add org.apache.cordova.camera
```

###Front-end :
Le front-end nécessite l’importation d’une librairie et utilise un outil :

* **D3.js**, qui permet d’afficher des statistiques attrayantes et dynamiques.
* **Grunt**, outil d’automatisation de tâches qui est utilisé pour la minification du code.

###Back-end :
Le back-end et le stockage des données requiert aussi les services et frameworks suivants :

* **Play-pac4j**, module Play de connexion OAuth, permet la connexion via Google
* **Cloudinary**, service d’hébergement d’images, qui permet de stocker les photos de profil des membres.
* **Google Guice**, framework d’injection de dépendances, qui sert à injecter les DAO dans les contrôleurs.
* **Jongo**, framework, qui sert à faire le lien entre la base de données MongoDB et l’application web.

---
## Problèmes rencontrés

### Connexion compte google, application mobile
En ce qui concerne la connexion pour l’application mobile, des difficultés ont été rencontrées. Dans l’application web, la connexion se fait avec le **protocole OAuth** via des identifiants Google (identifiant + code secret).

Cependant, la connexion Google avec les applications mobiles ne fonctionnent pas de la même façon. Il y a un identifiant mais pas de code secret, la connexion utilise l’empreinte de l’application mobile. Pour que l’application web et l’application mobile fonctionnent toutes les deux, il faut qu’elles se connectent sur la même application Google et donc avec les mêmes identifiants. Pour pallier à ce problème le plugin inAppBrowser de Cordova est utilisé, il permet d’ouvrir un navigateur interne à l’application.

Lorsque l’utilisateur appuie sur le bouton de connexion, le navigateur s’ouvre et l’utilisateur est dirigé vers la page de connexion à l’application Google comme sur l’application Web. L’utilisateur se connecte sur son compte puis doit accepter les autorisations de l’application. Dès que ceci est fait, le navigateur se ferme, l’utilisateur se retrouve de nouveau sur l’application mobile et il peut accéder aux différentes pages de l’application.
