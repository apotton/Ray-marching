package scene;

/**
 * Classe régissant le comportement des vecteurs de l'espace à trois coordonnées
 */
public class Vector3D {
    /**
     * L'abscisse
     */
    public double x;

    /**
     * L'ordonnée
     */
    public double y;

    /**
     * La hauteur
     */
    public double z;

    /**
     * Renvoie un nouveau vecteur de coordonnées nulles
     */
    public Vector3D() {
        x = 0;
        y = 0;
        z = 0;
    }

    /**
     * Renvoie un vecteur de coordonnées choisies
     * 
     * @param x L'abscisse
     * @param y L'ordonnée
     * @param z La hauteur
     */
    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Copie un vecteur pour éviter les effets de bord
     * 
     * @param old Le vecteur à copier
     */
    public Vector3D(Vector3D old) {
        this.x = old.x;
        this.y = old.y;
        this.z = old.z;
    }

    /**
     * Produit scalaire de deux vecteur
     * 
     * @param vec1
     * @param vec2
     * @return Un double contenant leur produit scalaire
     */
    public static double PS(Vector3D vec1, Vector3D vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z;
    }

    /**
     * Détermine si un rayon est sous l'horizon
     * 
     * @return -1 si le rayon descend, 1 sinon
     */
    public int orientation() {
        return this.z < 0 ? -1 : 1;
    }

    /**
     * @return La norme euclidienne du vecteur
     */
    public double norme() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    /***
     * @return Un nouveau vecteur de norme unitaire
     */
    public Vector3D normaliser() {
        double norme = this.norme();
        return new Vector3D(this.x / norme, this.y / norme, this.z / norme);
    }

    /**
     * Fixe la norme du vecteur à 1
     */
    public void normaliserEnPlace() {
        double norme = this.norme();
        this.x /= norme;
        this.y /= norme;
        this.z /= norme;
    }

    /**
     * @param norme La norme souhaitée
     * @return Un nouveau vecteur ayant la bonne norme
     */
    public Vector3D normer(double norme) {
        Vector3D ret = this.normaliser();
        ret.x *= norme;
        ret.y *= norme;
        ret.z *= norme;
        return ret;
    }

    /**
     * Donne au vecteur la norme choisie
     * 
     * @param norme
     */
    public void normerEnPlace(double norme) {
        double mult = this.norme();
        mult = norme / mult;
        this.x *= mult;
        this.y *= mult;
        this.z *= mult;
    }

    /**
     * Fait avancer le vecteur dans la direction origine->position
     * 
     * @param distance La distance du pas
     * @param origine  L'origine de la trajectoire
     */
    public void avancer(double distance, Vector3D origine) {
        Vector3D avancement = new Vector3D(this.x - origine.x, this.y - origine.y, this.z - origine.z);
        avancement.normaliserEnPlace();

        this.x += avancement.x * distance;
        this.y += avancement.y * distance;
        this.z += avancement.z * distance;
    }

    /**
     * @param point Un vecteur de l'espace
     * @return La distance entre le vecteur de base et point
     */
    public double distance(Vector3D point) {
        return Math.sqrt(Math.pow(point.x - this.x, 2) + Math.pow(point.y - this.y, 2) + Math.pow(point.z - this.z, 2));
    }

    /**
     * @param a
     * @param b
     * @return Le vecteur b-a, c'est à dire un vecteur allant de a vers b
     */
    static public Vector3D difference(Vector3D a, Vector3D b) {
        return new Vector3D(b.x - a.x,
                b.y - a.y,
                b.z - a.z);
    }

    /**
     * Ajoute deux vecteurs
     * 
     * @param origine Le vecteur à ajouter
     */
    public void ajouter(Vector3D origine) {
        this.x += origine.x;
        this.y += origine.y;
        this.z += origine.z;
    }

    /**
     * @param axe L'axe de réflexion
     * @return Un vecteur normalisé correspondant à la réflexion du vecteur
     */
    public Vector3D reflexion(Vector3D axe) {
        axe.normerEnPlace(2 * PS(this, axe) / Math.pow(axe.norme(), 2));

        Vector3D ret = difference(this, axe);

        ret.normerEnPlace(Scene.DISTANCE_MIN);

        return ret;
    }

    /**
     * @return La valeur absolue d'un vecteur, coordonnées par coordonnées
     */
    public Vector3D abs() {
        return new Vector3D(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    /**
     * @return Un vecteur où toutes les composantes sont >= 0
     */
    public Vector3D positives() {
        return new Vector3D(Math.max(this.x, 0), Math.max(this.y, 0), Math.max(this.z, 0));
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

}