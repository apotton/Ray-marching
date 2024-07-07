package objets;

import java.awt.Color;

import scene.Vector3D;

/**
 * Classe abstraite qui détermine le comportement d'un objet sur la scène
 */
public abstract class Element {
    /** Position du centre de l'objet */
    public Vector3D position;

    /** Couleur de l'objet */
    public Color couleur;

    /** True si l'objet réfléchit la lumière, false sinon */
    public boolean reflexion = false;

    /** Composante spéculaire du matériau */
    public double specular = 0;

    /** Degré de précision pour le calcul de la normale */
    public final double EPSILON = 0.001;

    /**
     * Constructeur abstrait
     * 
     * @param position La position du centre de l'objet
     * @param couleur  La coleur de l'objet
     */
    public Element(Vector3D position, Color couleur) {
        this.position = position;
        this.couleur = couleur;
    }

    /**
     * Fonction qui renvoie une distance signée : >0 si on est en dehors de l'objet,
     * et <0 sinon (signed distance function)
     * 
     * @param position La position actuelle
     * @return La SDF de l'objet en question
     */
    public abstract double SDF(Vector3D position);

    /**
     * Calcul de la normale pour les réflexions et ombres
     * 
     * @param p La position du point sur l'objet
     * @return La normale de l'objet en ce point
     */
    public Vector3D normale(Vector3D p) {
        Vector3D norm = new Vector3D(
                (SDF(new Vector3D(p.x + EPSILON, p.y, p.z)) - SDF(new Vector3D(p.x - EPSILON, p.y, p.z))),
                (SDF(new Vector3D(p.x, p.y + EPSILON, p.z)) - SDF(new Vector3D(p.x, p.y - EPSILON, p.z))),
                (SDF(new Vector3D(p.x, p.y, p.z + EPSILON)) - SDF(new Vector3D(p.x, p.y, p.z - EPSILON))));
        norm.normaliser();
        return norm;
    }

    @Override
    public String toString() {
        return "Element à la position (" + position.x + ", " + position.y + ", " + position.z + ")";
    }
}
