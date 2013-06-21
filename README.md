La version master de MoovSimply est un simple projet web avec un jetty inclus.
Pour le lancer, il faut le cloner, lancer avec maven un clean install et ensuite un jetty:run
La page d'accueil est visible sur http://localhost:8080/

## You can test resources :

- http://localhost:8080/rest/moovin/hello
- http://localhost:8080/rest/moovin/where?LAT=1&LNG=2
- http://localhost:8080/rest/moovin/transports
- http://localhost:8080/rest/moovin/around?LAT=48.868648&LNG=2.341374

## Pour déployer en prod
- Se connecter : heroku login (utiliser votre compte ou celui de moosimply)
- Relier le projet à l'application sur heroku : heroku git:remote -a moovsimply
- Déployer : git push heroku master

##Pour d'environnement
- Supprimer le remote sur l'environnement précédent : git remote rm heroku

## Pour déployer en test
- Se connecter : heroku login (utiliser votre compte ou celui de moosimply)
- Relier le projet à l'application sur heroku : heroku git:remote -a moovsimply-test
- Déployer : git push heroku master
