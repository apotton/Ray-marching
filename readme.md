# Programme de Ray marching ⚡☄️🔆

## Introduction

Le ray marching est une méthode de ray tracing qui utilise un pas d'avancement variable. Chaque objet dispose d'un SDF (signed distance function), qui renvoie la distance du rayon à la surface de l'objet. Cette distance est donc positive à l'extérieur de l'objet, nulle à la surface et négative à l'intérieur. Au lieu d'avancer à chaque fois d'une distance précise, les rayons avancent du minimum des SDF de la scène. Il est ainsi certain de ne rien touchersur son passage, tout en minimisant la durée de trajet.

Au début du `Main` sont définies toutes les constantes importantes, notamment celles de la caméra. L'inclinaison correspond monter ou baisser la caméra, tandis que son orientation se fait sur le côté. L'origine est placée à (0,0,0), et l'orientation à 0. Mais une origine à (0,400,0) (de l'autre côté de la scène) et une orientation de PI montre la scène vue depuis le mur d'en face. De manière générale, on peut se placer n'importe où entre les murs pour regarder les objets.

Tous les objets de la scène sont définis dans la fonction `Scene.ajouterObjets()`. Chaque objet possède une couleur, une position, une composante spéculaire (pour les ombres), ainsi que son matériau: réfléchissant ou mat. Un objet réfléchissant agira exactement comme un miroir parfait, et renverra les rayons selon la loi de réflexion de Descartes. Il est drôle de supprimer la ligne effectuant les ombrages (ombragePhong(rayon, objetPlusProche) à la fin de `Scene.java`), et de réduire le nombre de reflets à 0. La scène apparaît alors comme si elle était en 2D, et avec des couleurs très vives.

## Fonctionnement du code

### Obtention de l'affichage

Pour afficher la scène, la fonction `paint` du `Main` calcule en parallèle (utilisation de Streams) la couleur de chaque pixel. Les pixels partent tous du même point (`ORIGINE`), et sont projetés sur un rectangle de largeur 1 et à une distance `ZOOM` de l'origine, grâce à la classe `Rotation`. Ce rectangle est bien évidemment tourné selon l'orientation de la caméra, mais a toujours la même largeur. Le paramètre `ZOOM` règle ainsi l'éloignement, donc ce qui sera visible dans le cadre.

### Couleur d'un pixel

Pour chaque pixel, une fois le rayon projeté, il continue son chemin grâce à la fonction `avancer`. L'origine du rayon sert donc à le maintenir dans une ligne droite. Une fois qu'il touche un objet (à une distance inférieure à `Scene.DISTANCE_MIN`), il en prend la couleur. Si l'objet est réfléchissant, alors le rayon est réfléchi selon la normale au point qu'il touche, et son origine est fixée à ce point précis. Son parcours reprend alors à partir de ce point, et tant que le nombre maximal de reflets n'est pas atteint. Si le rayon ne touche aucun objet jusqu'à `Scene.DISTANCE_MAX`, alors sa couleur est réglée selon si il est au dessus ou au dessous de l'horizon.

Une fois que la couleur du rayon est déterminée, il reste à appliquer l'ombre par la méthode dite d'ombrage de Phong. Celle-ci agit en trois phases:

- La composante diffuse: elle indique l'intensité qui repart en tenant compte de l'inclinaison avec laquelle la lumière incidente arrive sur la surface, mais en supposant que l'intensité est la même quelle que soit la direction que prend le rayon réfléchi
- La composante spéculaire indique le "brillant" d'une surface, elle correspond à une tache relativement petite correspondant à un reflet un peu flouté de l'éclairage lorsque le rayon est reflété dans sa direction à la sruface d'un objet
- La composante ambiante: un éclairage constant appliqué à la fin du processus qui correspond aux lumières parasites se reflétant sur tous les objets de la scène

Enfin, après tout cela, on regarde si il y a un obstacle entre la position du rayon et l'éclairage. Si c'est le cas, alors le point considéré est à l'ombre et on retire toute sa couleur, sauf la composante ambiante.

### Pour aller plus loin

Dans le futur, il pourrait être intéressant de coder les fonctionalités suivantes:

- Pouvoir avoir plusieurs sources lumineuses, et gérer les ombres en conséquence (notamment le chevauchement d'ombres provenant de deux sources différentes)
- Ajouter de nouveaux objets, tels que des cônes ou des tores
- Implémenter les objets métalliques, c'est à dire réfléchissants mais ayant une couleur, comme quand on voit son reflet sur la carosserie d'une voiture brillante
- Implémenter une méthode d'antialiasing, car les bords des objets sont précis au pixel près
