package main;

import scene.Scene;
import scene.Vector3D;

import java.awt.Color;
// import java.awt.Color;
import java.awt.Graphics;
// import java.awt.Graphics2D;
import java.util.stream.IntStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {
    // Paramètres de la caméra
    public static final double OUVERTURE_X = Math.PI / 2; // Angle horizontal
    public static final double OUVERTURE_Z = Math.PI / 4; // Angle vertical

    public static final double INCLINAISON = 0;// - Math.PI / 6; // Inclinaison verticale de la caméra
    public static final double ORIENTATION = 0;// - Math.PI;// Orientation horizontale de la caméra
    public static final Vector3D ORIGINE = new Vector3D(0, 0, 0); // Origine de la caméra

    // Taille de l'affichage
    public static final int ECHELLE = 500; // Largeur de la fenêtre
    public static final int ESPACEMENT = 1; // Nombre de pixels par rayon

    public static final double RATIO = OUVERTURE_Z / OUVERTURE_X; // LARGEUR/HAUTEUR
    public static final int X_SIZE = (int) (ECHELLE); // Largeur de la fenêtre
    public static final int Z_SIZE = (int) (ECHELLE * RATIO); // Hauteur de la fenêtre

    // Paramètres de l'éclairage
    public static final double AMBIANT = 0.15; // Composante ambiante à ajouter à chaque rayon
    public static final int NOMBRE_REFLETS = 10; // Le nombre max de reflets

    public static Scene scene = new Scene(); // La scène à afficher
    public static Color[] couleurs = new Color[X_SIZE * Z_SIZE];
    public static Color[] couleurs2 = new Color[X_SIZE * Z_SIZE];

    static class MyJFrame extends JFrame {
        // Pour une raison que j'ignore, la fonction paint tourne deux fois. Je ne fais
        // les calculs que la première fois.
        private static int count = 0;

        /**
         * Algorithme d'affichage de la scène
         */
        public void paint(Graphics g) {
            // Boucle en parallèle pour l'affichage de la scène
            if (count == 0) {
                IntStream.range(0, X_SIZE * Z_SIZE)
                        .parallel()
                        .forEach(i -> {
                            // Calcul et stockage de la couleur de chaque pixel
                            couleurs[i] = scene.couleurPixel(i % X_SIZE, i / X_SIZE);
                            g.setColor(couleurs[i]);
                            g.fillRect(i % X_SIZE * ESPACEMENT, (Z_SIZE - i / X_SIZE) * ESPACEMENT + 36, ESPACEMENT,
                                    ESPACEMENT);
                        });
            }
            count++;

            // L'affichage se fait du haut vers le bas et non du bas vers le haut, d'où la
            // modification pour les z

            for (int i = 0; i < X_SIZE * Z_SIZE; i++) {
                g.setColor(couleurs[i]);
                g.fillRect(i % X_SIZE * ESPACEMENT, (Z_SIZE - i / X_SIZE) * ESPACEMENT + 36,
                        ESPACEMENT, ESPACEMENT);
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        MyJFrame frame = new MyJFrame();

        frame.setTitle("Univers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(X_SIZE * ESPACEMENT, Z_SIZE * ESPACEMENT + 36);
        frame.setVisible(true);
    }
}