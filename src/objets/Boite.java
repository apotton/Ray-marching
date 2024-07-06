package objets;

import java.awt.Color;

import scene.Vector3D;

/**
 * Classe qui régit le comportement d'un parallélépipède rectangle
 */
public class Boite extends Element {
    /**
     * Largeur du côté selon l'axe x
     */
    private double x;

    /**
     * Largeur du côté selon l'axe y
     */
    private double y;

    /**
     * Largeur du côté selon l'axe z
     */
    private double z;

    /**
     * Constructeur qui spawn une boite (parallélépipède rectangle) en position
     * souhaitée
     * 
     * @param position La position du centre de la boite
     * @param couleur  La couleur de la boite
     * @param x        La largeur
     * @param y        La longueur
     * @param z        La hauteur
     * @param reflexion Un booléen définissant si la surface est réflexive
     */
    public Boite(Vector3D position, Color couleur, double x, double y, double z, boolean reflexion) {
        super(position, couleur);
        this.x = x;
        this.y = y;
        this.z = z;
        this.reflexion = reflexion;
    }

    @Override
    public double SDF(Vector3D rayon) {
        double qx = Math.max((Math.abs(rayon.x - this.position.x) - this.x), 0);
        double qy = Math.max((Math.abs(rayon.y - this.position.y) - this.y), 0);
        double qz = Math.max((Math.abs(rayon.z - this.position.z) - this.z), 0);

        return Math.sqrt(Math.pow(qx, 2) + Math.pow(qy, 2) + Math.pow(qz, 2))
                + Math.min(Math.max(qx, Math.max(qy, qz)), 0.0);
    }

    public String toString() {
        return "Boite de dimensions (" + this.x + ", " + this.y + ", " + this.z + "), en position (" + this.position.x
                + ", " + this.position.y + ", " + this.position.z + ")";
    }
}
