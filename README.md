# Fallen Guardian
Le jeu *Fallen Guardian*, est une forme de tower defense, rogue like. <br>
Il consiste à défendre un moment centrale contre les attaques des goblins.

Le joueur est équipé d'une épée pour se défendre et gagne de l'xp en <br>
éliminant des goblins ainsi que des sous. Le joueur monte de niveau <br>
en franchisant un seuil d'xp et tous ces stats (sauf la vitesse) augement <br> 
en conséquent. <br>
Lors de phases de vagues, le joueur reçoit également de la monnaie <br>
passive.

La monnaie est utilisé pour acheter, améliorer et placer des tours sur la carte. <br>
Les tours visent et tirent automatiquement sur les ennemies se trouvant <br>
dans son rayon de tire. Tous ses stats augmentent en fonction de son niveau.

En passant à la prochaine vague, les stats des goblins (sauf la vitesse), <br>
augement proportionellement.

Si le joueur se fait éliminer, son niveau baisse de 1.

Le jeu se termine lorsque les goblins parviennent à détruire la base/monument centrale.

# Screenshots
#### Loading screen
![Screenshot1.png](src%2Fres%2FScreenshots%2FScreenshot1.png)
#### Enemy spawn
![Screenshot2.png](src%2Fres%2FScreenshots%2FScreenshot2.png)
#### Loading screen
![Screenshot3.png](src%2Fres%2FScreenshots%2FScreenshot3.png)
#### Wave screen
![Screenshot4.png](src%2Fres%2FScreenshots%2FScreenshot4.png)
#### Base loosing hp
![Screenshot5.png](src%2Fres%2FScreenshots%2FScreenshot5.png)
#### End screen
![Screenshot6.png](src%2Fres%2FScreenshots%2FScreenshot6.png)

# Tutorial:

    - Enter              => start game

    - LeftClick on map   => move player to
                            click position

    - LeftClick on enemy => attack enemy    

    - LeftClick on tower => upgrade tower

    - q                  => enter build mode    

    - LeftClick          => place tower
      (in build mode)

    - LeftClick on tower => sell tower
      (in build mode)

    - q/RightClick       => exit build mode
      (in build mode)
    

    Buy 1 tower => -50 coins
    Sell 1 tower => +25 coins
    Upgrade tower => -50 (-25 coins on top, every upgrade)


# Structure du code
Avec plusieurs fonctionalités prémédités, une base solide a été bati pour <br>

La structure générale du projet est séparée en 4 grandes parties : <br>
1) Ressources et données constantes
2) Outils de développement 
3) Game Manager qui s'occupe du déroulement global du jeu
4) Classes et objets servant à construire le jeu

L'objet Constants, stocke 
1) Toutes les données fixes, comme la taille de la fenêtre par exemple
2) Toutes les valeurs par défaut du joueur, ennemies, tours, balles et base
3) Toutes les images du UI
4) Tous les clips audio
<br><br>

Pour la gestion de tous les objets dynamiques, la classe Entity est au summume.<br>
Les trois sous-classes, sont 
1) Character, qui parente la classe Player et Enemy
2) Building, qui parente Tower
3) Bullet
4) Base
