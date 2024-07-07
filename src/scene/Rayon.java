package scene;

import java.awt.Color;

/**
 * Classe qui régit le comportement d'un rayon de lumière
 */
public class Rayon {
    /** Position actuelle du rayon */
    public Vector3D position;

    /** Origine du rayon */
    public Vector3D origine;

    /** Couleur actuelle du rayon */
    public Color couleur;

    /**
     * Constructeur qui initialise un rayon noir de coordonnées nulles
     */
    public Rayon() {
        this.position = new Vector3D();
        this.origine = new Vector3D();
        this.couleur = Color.BLACK;
    }

    /**
     * Constructeur qui copie le rayon pour éviter les effets de bord
     * 
     * @param rayon
     */
    public Rayon(Rayon rayon) {
        this.position = new Vector3D(rayon.position);
        this.origine = new Vector3D(rayon.origine);
        this.couleur = new Color(rayon.couleur.getRGB());
    }

    /**
     * Constructeur principal, qui renvoie un rayon noir
     * 
     * @param position La position du rayon
     * @param origine  L'origine du rayon
     */
    public Rayon(Vector3D position, Vector3D origine) {
        this.position = position;
        this.couleur = new Color(0);
        this.origine = origine;
    }

    /**
     * Fait avancer le rayon selon l'axe (origine -> position)
     * 
     * @param distance La distance d'avancement
     */
    public void avancer(double distance) {
        this.position.avancer(distance, this.origine);
    }

    /**
     * Établit la couleur du rayon
     * 
     * @param r La composante rouge (>= 0 et <= 255)
     * @param g La composante verte (>= 0 et <= 255)
     * @param b La composante bleue (>= 0 et <= 255)
     */
    public void setColor(int r, int g, int b) {
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));

        this.couleur = new Color(r, g, b);
    }

    /**
     * Obscurcit selon un facteur
     * 
     * @param mult Le facteur en question (0 <= mult <= 1)
     */
    public void ombrerCouleur(double mult) {
        int r = (int) (this.couleur.getRed() * mult);
        int g = (int) (this.couleur.getGreen() * mult);
        int b = (int) (this.couleur.getBlue() * mult);

        this.setColor(r, g, b);
    }

    /**
     * Ajoute i fois couleur à la couleur actuelle
     * 
     * @param i       Intensité de l'éclaircissement (1 pour doubler)
     * @param couleur Couleur à ajouter (blanc pour éclaircir)
     */
    public void ajouterAmbiant(double i, Color couleur) {
        int r = (int) (this.couleur.getRed());
        r = (int) Math.min(255, r + i * couleur.getRed());
        int g = (int) (this.couleur.getGreen());
        g = (int) Math.min(255, g + i * couleur.getGreen());
        int b = (int) (this.couleur.getBlue());
        b = (int) Math.min(255, b + i * couleur.getBlue());

        this.setColor(r, g, b);
    }

    /**
     * Réfléchit un rayon incident par rapport à la normale
     * 
     * @param norm La normale à l'objet touché
     * @return Le vecteur réfléchi
     */
    public Vector3D reflexion(Vector3D norm) {
        // Construction du vecteur incident (de l'objet vers l'origine)
        Vector3D incident = Vector3D.difference(this.position, this.origine);

        // Réflexion du vecteur incident
        return incident.reflexion(norm);
    }

}
