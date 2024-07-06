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
     * @param x L'abscisse du point
     * @param z L'ordonnée du point
     * @return Une position 3D correspondant au bon espacement
     */
    public static Vector3D position(int x, int z) {
        double x_c = (double) (x - (Main.X_SIZE / 2));
        double y_c = 0.5;
        double z_c = (double) (z - (Main.Z_SIZE / 2)) / (Main.X_SIZE);

        double n_x = x_c * cosPhi + y_c * (-sinPhi * cosTheta) + z * (sinTheta * sinPhi);
        double n_y = x_c * sinPhi + y_c * (cosPhi * cosTheta) + z_c * (-sinTheta * cosPhi);
        double n_z = y_c * sinTheta + z_c * cosTheta;

        return new Vector3D(Main.ORIGINE.x + n_x / (Main.X_SIZE), Main.ORIGINE.y + n_y,
                Main.ORIGINE.z + n_z);
    }

}
