![Moov'Simply](/src/main/webapp/img/logo-medium.png)

La version master de MoovSimply est un simple projet web avec un jetty inclus.

## Installation

Pour lancer le projet :
```
git clone git@github.com:DuchessFrance/MoovInTheCity.git
mvn clean install
mvn jetty:run
```

La page d'accueil est accessible depuis [localhost:8080](http://localhost:8080/)

### Ressources

- [.../rest/moovin/hello](http://localhost:8080/rest/moovin/hello)
- [.../rest/moovin/where?LAT=1&LNG=2](http://localhost:8080/rest/moovin/where?LAT=1&LNG=2])
- [.../rest/moovin/transports](http://localhost:8080/rest/moovin/transports)
- [.../rest/moovin/around?LAT=48.868648&LNG=2.341374](http://localhost:8080/rest/moovin/around?LAT=48.868648&LNG=2.341374)

### Lombok

Le plugin Lombok est nécessaire dans l'IDE pour permettre de voir les getters, setters, et constructeurs.

Pour l'installer dans IntelliJ :
- Aller sur l'écran d'accueil (fermer les projets)
- Sur le panneau de droite "Open Plugin Manager"
- Browse repositories > Chercher "lombok"
- Double-cliquer sur "Lombok Plugin" dans la liste à gauche > Download and Install: Yes > Ok > Ok > Restart

## Déploiement

### En intégration
- Se connecter : `heroku login` (utiliser votre compte ou celui de moovsimply)
- Relier le projet à l'application sur heroku : `heroku git:remote -a moovsimply-test`
- Déployer : `git push heroku master`

#### Changer de plateforme de déploiement
- Supprimer le remote sur l'environnement précédent : `git remote rm heroku`

### En production
- Se connecter : `heroku login` (utiliser votre compte ou celui de moosimply)
- Relier le projet à l'application sur heroku : `heroku git:remote -a moovsimply`
- Déployer : `git push heroku master`

#### Changer la timezone
- Changer la timezone de l'heure serveur : `heroku config:add TZ=Europe/Paris`