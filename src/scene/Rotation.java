package scene;

import main.Main;

/**
 * Classe initialisant la position des rayons à partir de leur origine et selon
 * l'orientation et l'inclinaison de la caméra
 */
public class Rotation {
    private static final double sinPhi = Math.sin(Main.ORIENTATION);
    private static final double cosPhi = Math.cos(Main.ORIENTATION);

    private static final double sinTheta = Math.sin(Main.INCLINAISON);
    private static final double cosTheta = Math.cos(Main.INCLINAISON);

    /**
     * Fonction qui applique les transformations liées à l'orientation et
     * l'inclinaison de la caméra pour construire un rectangle de points devant la
     * position de la caméra.
     * 
     * @param x L'abscisse du point
     * @param z L'ordonnée du point
     * 
     * @return Une position 3D correspondant au bon espacement
     */
    public static Vector3D position(int x, int z) {
        // Coordonnées initiales
        double x_c = (double) (x - (Main.X_SIZE / 2)) / Main.X_SIZE;
        double y_c = Main.ZOOM;
        double z_c = (double) (z - (Main.Z_SIZE / 2)) / (Main.X_SIZE);

        // Transformation (multiplication matricielle)
        double n_x = x_c * cosPhi + y_c * (-sinPhi * cosTheta) + z * (sinTheta * sinPhi);
        double n_y = x_c * sinPhi + y_c * (cosPhi * cosTheta) + z_c * (-sinTheta * cosPhi);
        double n_z = y_c * sinTheta + z_c * cosTheta;

        // Position finale
        return new Vector3D(Main.ORIGINE.x + n_x, Main.ORIGINE.y + n_y,
                Main.ORIGINE.z + n_z);
    }

}
