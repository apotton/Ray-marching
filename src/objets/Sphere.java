package objets;

import java.awt.Color;

import scene.Vector3D;

/**
 * Classe qui régit le comportement d'une sphère pleine
 */
public class Sphere extends Element {
    /**
     * Le rayon de la sphère
     */
    public double rayon;

    /**
     * Constructeur d'une sphère pleine
     * @param position Position du centre de la sphère
     * @param rayon Rayon de la sphère
     * @param couleur Couleur de la sphère
     * @param reflexion True si la sphère est réfléchissante, false sinon
     */
    public Sphere(Vector3D position, double rayon, Color couleur, boolean reflexion) {
        super(position, couleur);
        this.rayon = rayon;
        this.reflexion = reflexion ;
        this.specular = 100 ;
    }

    @Override
    public double SDF(Vector3D rayon) {
        return rayon.distance(this.position) - this.rayon;
    }

    public String toString() {
        return "Sphère de rayon " + this.rayon + " en position (" + this.position.x + ", " + this.position.y + ", " + this.position.z + ")" ;
    }

}
