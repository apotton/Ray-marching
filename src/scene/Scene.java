package scene;

import java.awt.Color;
import java.util.ArrayList;

import main.Main;
import objets.Boite;
import objets.Element;
import objets.Sphere;

public class Scene {
    static final int X_SIZE = Main.X_SIZE;
    static final int Z_SIZE = Main.Z_SIZE;

    public static final double DISTANCE_MAX = 10000;
    public static final double DISTANCE_MIN = 0.00001;

    /** Ensemble des rayons de lumière */
    public Rayon[] lumiere = new Rayon[X_SIZE * Z_SIZE];

    /** Position de la caméra */
    public Vector3D origine = Main.ORIGINE;

    /** Ensemble des objets dans la scène */
    public ArrayList<Element> objets = new ArrayList<>();

    /** Éclairage de la scène */
    public Element eclairage = new Sphere(new Vector3D(-200, 400, 60), 10, Color.WHITE, false);

    /**
     * Macro qui détermine l'indice [x][z] dans un tableau unidimensionnel
     * 
     * @param x L'abscisse du point
     * @param z L'ordonnée du point
     * @return L'entier correspondant au bon indice du tableau
     */
    static public int IX(int x, int z) {
        // x = Math.max(0, Math.min(x, X_SIZE - 1));
        // z = Math.max(0, Math.min(z, Z_SIZE - 1));
        return x + X_SIZE * z;
    }

    /*
     * Constructeur qui initialise une scène contenant l'éclairage et quelque objets
     */
    public Scene() {
        this.objets.add(eclairage);

        for (int x = 0; x < X_SIZE; x++) {
            for (int z = 0; z < Z_SIZE; z++) {
                this.setRayon(x, z);
            }
        }

        this.ajouterObjets();
    }

    /**
     * Initialise un rayon uniformément sur une sphère centrée en l'origine
     * 
     * @param x L'abscisse du rayon
     * @param z L'ordonnée du rayon
     */
    public void setRayon(int x, int z) {
        this.lumiere[IX(x, z)] = new Rayon(Rotation.position(x, z), Main.ORIGINE);
    }

    /**
     * Ajoute des objets incongrus, les murs, le sol, la déco
     */
    public void ajouterObjets() {
        // Décoration d'intérieur
        this.objets.add(new Sphere(new Vector3D(-25, 200, -30), 30, Color.MAGENTA, true));
        this.objets.add(new Sphere(new Vector3D(00, 150, -5), 10, Color.GREEN, false));
        this.objets.add(new Sphere(new Vector3D(70, 150, -20), 30, Color.YELLOW, true));
        this.objets.add(new Sphere(new Vector3D(20, 150, -20), 10, Color.PINK, false));
        this.objets.add(new Boite(new Vector3D(-200, 150, -40), Color.ORANGE, 30, 10, 50, false));

        // Murs avant et arriere
        this.objets.add(new Boite(new Vector3D(0, 500, -100), Color.LIGHT_GRAY, 300, 1, 300, false));
        this.objets.add(new Boite(new Vector3D(0, -100, 100), Color.CYAN, 400, 1, 300, false));

        // Murs droite et gauche
        this.objets.add(new Boite(new Vector3D(-300, 300, -100), Color.RED, 1, 400, 300, false));
        this.objets.add(new Boite(new Vector3D(300, 300, -100), new Color(0, 150, 0), 1, 400, 300, false));

        // Plafond et plancher
        this.objets.add(new Boite(new Vector3D(0, 300, 100), Color.BLUE, 300, 300, 2, false));
        this.objets.add(new Boite(new Vector3D(0, 300, -50), Color.GRAY, 300, 300, 2, false));

    }

    /**
     * @param position La position du rayon
     * @return L'objet le plus proche de la position actuelle
     */
    public Element plusProche(Vector3D position) {
        Element ret = null;
        double dist = DISTANCE_MAX;

        for (Element objet : this.objets) {
            double temp = objet.SDF(position);
            if (temp < dist) {
                dist = temp;
                ret = objet;
            }
        }

        return ret;
    }

    /**
     * La fonction modifie la couleur du rayon incident pour bien s'adapter à
     * l'objet
     * 
     * @param rayon Le rayon en question
     * @return La distance à l'objet le plus proche dans la scène
     */
    public double SDFScene(Rayon rayon) {
        double dist = DISTANCE_MAX;

        for (Element objet : this.objets) {
            double temp = objet.SDF(rayon.position);
            if (dist > temp) {
                dist = temp;
                // rayon.couleur = objet.couleur;
            }
        }

        return dist;
    }

    /**
     * La fonction ne modifie pas la couleur de l'objet
     * 
     * @param rayon  Le rayon actuel
     * @param objetP L'objet le plus proche
     * @return La distance au deuxième objet le plus proche
     */
    public double SDFScene2(Rayon rayon, Element objetP) {
        double dist = DISTANCE_MAX;

        for (Element objet : this.objets) {
            if (objet.equals(objetP)) {
                continue;
            }
            double temp = objet.SDF(rayon.position);
            if (dist > temp) {
                dist = temp;
            }
        }

        return dist;
    }

    /**
     * Si le rayon est trop loin, on fixe sa couleur selon si il est au dessus de
     * l'horizon ou pas
     * 
     * @param rayon Le rayon en question
     */
    public void setMaxColor(Rayon rayon) {
        if (rayon.position.distance(this.origine) >= DISTANCE_MAX) {
            if (rayon.position.orientation() == -1) {
                // Noir en dessous de l'horizon
                rayon.setColor(0, 0, 0);
            } else {
                // Blanc au dessus de l'horizon
                rayon.setColor(255, 255, 255);
            }
        }
    }

    /**
     * 
     * @param norm     La normale à l'objet
     * @param position La position du rayon
     * @return Le coefficient par lequel la couleur sera mutlipliée en fonction de
     *         l'angle d'incidence de la lumière
     */
    public double diffusionMultiplier(Vector3D norm, Vector3D position) {
        // Calcul du plus court chemin vers l'éclairage
        Vector3D diff = Vector3D.difference(position, this.eclairage.position);
        diff.normaliserEnPlace();

        // Le résultat est proportionnel au cosinus de l'angle entre la normale et le
        // vecteur vers l'éclairage. Si le cos est négatif, c'est qu'on est du mauvais
        // côté du solide (donc à l'ombre), et on renvoie 0.
        return Math.max(0, Vector3D.PS(norm, diff)) / 1.2;
    }

    /**
     * Ajoute la réflexion spéculaire (les réflexions directes sur les objets)
     * 
     * @param rayon     Le rayon actuel
     * @param norm      La normale à l'objet
     * @param diffusion Le coefficient de diffusion associé à la fonction
     *                  diffusionMultiplier
     * @param specular  La puissance à laquelle sera élevée le produit scalaire
     *                  (+élevé = tâche moins étendue)
     */
    public void ajouterSpeculaire(Rayon rayon, Vector3D norm, double diffusion, double specular) {
        // Construction du rayon réfléchi
        Vector3D direction = Vector3D.difference(rayon.position, this.eclairage.position);
        direction.normaliserEnPlace();
        direction = direction.reflexion(norm);
        direction.normaliserEnPlace();

        // Construction du rayon du point vers la caméra
        Vector3D vision = Vector3D.difference(rayon.position, rayon.origine);
        vision.normaliserEnPlace();

        // Calcul de la proximité entre eux
        double coefficientMultiplicateur = Math.pow(Vector3D.PS(direction, vision), specular);
        rayon.ajouterAmbiant(coefficientMultiplicateur * diffusion, this.eclairage.couleur);
    }

    /**
     * Fonction qui détermine si un point se situe à l'ombre d'un autre objet
     * 
     * @param rayon_origine Le rayon actuel
     * @param objet         L'objet le plus proche
     * @return True si le point est à l'ombre, false sinon
     */
    public boolean ombre(Rayon rayon_origine, Element objet) {
        // L'éclairage n'est, par définition, pas vraiment à l'ombre
        if (objet.equals(this.eclairage)) {
            return false;
        }

        // Construction d'un deuxième rayon identique pour éviter les effets de bord
        Rayon rayonTemp = new Rayon(rayon_origine);

        // L'origine est désormais sa position actuelle
        rayonTemp.origine = new Vector3D(rayonTemp.position);

        // On fixe sa nouvelle position à DISTANCE_MIN de l'origine, dans la direction
        // de la lumière
        Vector3D nouvellePos = Vector3D.difference(rayonTemp.origine, eclairage.position);
        nouvellePos.normerEnPlace(DISTANCE_MIN);
        nouvellePos.ajouter(rayonTemp.position);
        rayonTemp.position = nouvellePos;

        // Initialisation de la boucle suivante
        double dist = this.SDFScene(rayonTemp);
        Element objetplusProche = objet;

        // Tant que le rayon est dans les limites de la scène et qu'il ne touche pas un
        // autre objet que celui qu'il a déjà atteint, on le fait avancer
        while ((rayonTemp.position.distance(this.origine) < DISTANCE_MAX)
                && ((dist > DISTANCE_MIN) || objetplusProche.equals(objet))) {

            rayonTemp.avancer(Math.max(dist, DISTANCE_MIN));

            // Actualisation de l'objet plus proche
            objetplusProche = plusProche(rayonTemp.position);

            if (objetplusProche.equals(objet)) {
                // Si l'objet le plus proche est toujours l'objet de départ, on avance de la
                // distance du deuxième objet à atteindre
                dist = SDFScene2(rayonTemp, objet);
            } else {
                // Sinon on avance normalement
                dist = this.SDFScene(rayonTemp);
            }
        }

        // Si on a atteint directement l'écairage, alors il n'y avait pas d'obstacle et
        // on est donc pas à l'ombre
        if (objetplusProche.equals(this.eclairage)) {
            return false;
        }

        // Sinon, on a tapé quelque chose qui nous bloque la lumière, et on est donc à
        // l'ombre
        return true;
    }

    /**
     * Fonction qui règle la couleur du rayon en fonction de l'algorithme d'ombrage
     * de Phong, selon trois composantes: diffuse, spéculaire et ambiante. La
     * fonction détermine également si le rayon atteint une zone d'ombre
     * 
     * @param rayon           Le rayon actuel
     * @param objetPlusProche L'objet atteint
     */
    public void ombragePhong(Rayon rayon, Element objetPlusProche) {
        Vector3D norm = objetPlusProche.normale(rayon.position);

        // Composante diffuse
        double diffusion = this.diffusionMultiplier(norm, rayon.position);
        rayon.ombrerCouleur(diffusion);

        if (ombre(rayon, objetPlusProche)) {
            // Ombre conventionnelle
            rayon.ombrerCouleur(0);
        } else if (objetPlusProche.specular != 0) {
            // Composante spéculaire
            ajouterSpeculaire(rayon, norm, diffusion, objetPlusProche.specular);
        }

        // Composante ambiante
        rayon.ajouterAmbiant(Main.AMBIANT, objetPlusProche.couleur);
    }

    public void reflechir(Rayon rayon, Element objetPlusProche) {
        Vector3D norm = objetPlusProche.normale(rayon.position);

        Vector3D reflechi = rayon.reflexion(norm);
        // reflechi.normer(DISTANCE_MIN);

        // L'origine du rayon est maintenant le point de réflexion
        rayon.origine = new Vector3D(rayon.position);

        // On réfléchit le rayon par rapport à la normale (loi de Descartes)
        // rayon.position = rayon.position.reflexion(norm);
        rayon.position = reflechi;

        // On replace le rayon là où on était
        rayon.position.ajouter(rayon.origine);

        // On le fait avancer pour ne pas rester au contact de l'objet
        rayon.avancer(DISTANCE_MIN);
    }

    /**
     * Fonction la plus importante qui détermine la couleur d'un pixel en utilisant
     * toutes les mécaniques définies précédemment
     */
    public Color couleurPixel(int x, int z) {
        // On remet le rayon à 0 avant de l'envoyer dans la scène
        // setRayon(x, z);
        Rayon rayon = this.lumiere[IX(x, z)];
        double dist;
        Element objetPlusProche = null;

        // On fait avancer le rayon autant de fois qu'il y a de reflets autorisés
        for (int reflet = 0;; reflet++) {
            dist = this.SDFScene(rayon);

            // Tant qu'on est pas arrivé à proximité d'un objet ou que l'on ne s'est pas
            // trop éloigné, on avance d'une distance sûre
            while ((rayon.position.distance(this.origine) < DISTANCE_MAX)
                    && (dist > DISTANCE_MIN)) {

                rayon.avancer(dist);
                dist = this.SDFScene(rayon);
            }

            // Si on est allé trop loin, on fixe la couleur et basta
            if (rayon.position.distance(this.origine) > DISTANCE_MAX) {
                setMaxColor(rayon);
                return rayon.couleur;
            }

            // Si on est pas trop loin, c'est qu'on est au contact d'un objet
            objetPlusProche = plusProche(rayon.position);
            rayon.couleur = objetPlusProche.couleur;

            // Si l'objet n'est pas réfléchissant où que l'on a atteint le nombre max
            // d'itérations, on sort de la boucle
            if (!objetPlusProche.reflexion || reflet == Main.NOMBRE_REFLETS) {
                break;
            }

            // Sinon on réfléchit le rayon et on repart pour un tour
            reflechir(rayon, objetPlusProche);
        }

        // Si on est pas arrivé sur la lumière, il faut considérer l'ombrage
        if (!objetPlusProche.equals(eclairage)) {
            ombragePhong(rayon, objetPlusProche);
        }

        return rayon.couleur;
    }

}
