# Fallen Guardian
Le jeu *Fallen Guardian*, est une forme de tower defense, rogue like. <br>
Il consiste à défendre un moment centrale contre les attaques des goblins.

Le joueur peux se déplacer librement à travers la carte et est équipé d'une<br>
épée pour se défendre et gagne de l'xp en éliminant des goblins ainsi que des<br>
sous. Le joueur monte de niveau en franchisant un seuil d'xp et tous ces stats<br>
(sauf la vitesse) augement en conséquent. <br>
Le joueur ne reçoit que de l'xp s'il élimine lui-même les goblins et lors des<br>
phases de vagues, le joueur reçoit également de la monnaie passive.

La monnaie est utilisé pour acheter, améliorer et placer des tours sur la carte. <br>
Les tours visent et tirent automatiquement sur les ennemies se trouvant <br>
dans son rayon de tire. Tous ses stats augmentent en fonction de son niveau.

En passant à la prochaine vague, les stats des goblins (sauf la vitesse), <br>
augement proportionellement.

Si le joueur se fait éliminer, son niveau baisse de 1.

Le jeu se termine lorsque les goblins parviennent à détruire la base/monument centrale. <br>
Le but est de survivre le plus longtemps et d'obtenir le meilleur score !

# Screenshots
#### Start screen
![Screenshot1.png](src%2Fres%2FScreenshots%2FScreenshot1.png)
#### Enemy spawn
![Screenshot2.png](src%2Fres%2FScreenshots%2FScreenshot2.png)
#### Tower spawn
![Screenshot3.png](src%2Fres%2FScreenshots%2FScreenshot3.png)
#### Wave screen
![Screenshot4.png](src%2Fres%2FScreenshots%2FScreenshot4.png)
#### Base loosing hp
![Screenshot5.png](src%2Fres%2FScreenshots%2FScreenshot5.png)
#### End screen
![Screenshot6.png](src%2Fres%2FScreenshots%2FScreenshot6.png)

# Tutorial:

    - Enter                 => start game

    - LeftClick on map      => move player to
                               click position

    - LeftClick on enemy    => attack enemy    

    - LeftClick on tower    => upgrade tower

    - q                     => enter build mode    

    - LeftClick on map      => place tower at
      (in build mode)          click position

    - LeftClick on tower    => sell tower
      (in build mode)

    - q/RightClick on map   => exit build mode
      (in build mode)
    

      Buy 1 tower     => -50 coins
      Sell 1 tower    => +25 coins
      Upgrade tower   => -50-25*upgradeLevel coins


# Structure du code
Avec plusieurs fonctionalités prémédités, une base solide a été bati.

## Structure générale
La structure générale du projet est séparée en 4 grandes parties : <br>
1) Ressources et données constantes
2) Outils de développement 
3) Game Manager qui s'occupe du déroulement global du jeu
4) Classes et objets servant à construire le jeu

## Points clés
1) Le Main initialise le game manager et commence la boucle de jeu gérée par ce dernier.
   <br><br>
2) L'objet Game Manager se charge :
   - d'initialiser la fenêtre graphique, la grille de jeu, la caméra, etc.
   - de créer le HUD
   - de lier tous les input listeners aux fonctions concernées
   - de gérer la logique du jeu
     <br><br>
3) L'objet Enities Manager gère :
   - l'ensemble des entités présentes dans le jeu
   - l'apparition et la destructions de ses entités
   - la logique des actions de ses entités
     <br><br>
4) La classe mère abstraite "Entity" :
    - est la classe de référence pour l'ensemble des classes pouvant intéragir avec <br>
    l'environnement de jeu
    - englobe Character (abstraite) -> Player, Enemy
    - englobe Building -> Tower
    - englobe Base, Bullet
      <br><br>
5) Le Grid s'occupe:
    - de la gestion spatiale des entités
    - de la logique de path finding
  <br><br>
6) Les outils:
    - Rendering -> plusieurs layers, Sprites, ancres personalisées, optimisation pour afficher <br>
    tout le contenu visuel
    - InputManager -> système d'events avec subscribers
    - Shapes -> Collision manager avec formes simples (box2d)
    - AnimationsManager -> Crée des animations et les gère efficacement
    - BetterFunGraphics et BetterAudio -> Quelques modifications de la librairie FunGraphics
    - UiManager -> permet la création de UI simples avec sprites et textes. Peut notemment appliquer <br>
    une logique externe à ses éléments. P.ex. lier la vie du personnage avec du texte
