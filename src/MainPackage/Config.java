package MainPackage;

import java.awt.*;

public abstract class Config {

    /*Attempting to fix the issue with size and timings being different on different screens.
    * This is due to the DPI being different on different screens, resulting in different physical representation of pixel-based size.
     */

   public static final double scaleFactor = Toolkit.getDefaultToolkit().getScreenResolution() /  96;

    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static Dimension frameSize = new Dimension((int)(1000 * scaleFactor), (int)(1000 * scaleFactor));
    public static Dimension cardSize = new Dimension((int)(150 * scaleFactor), (int)(210 * scaleFactor));


    public static boolean debug = true;

    public enum GameState  {
        CARD_PLAY,
        CARD_RESOLUTION,
        ENEMY_PHASE;
    }
}
