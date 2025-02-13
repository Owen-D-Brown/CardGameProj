package MainPackage;

import java.awt.*;

public abstract class Config {
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static Dimension frameSize = new Dimension(1000, 1000);
    public static Dimension cardSize = new Dimension(150, 210);

    public static boolean debug = true;

    public enum GameState  {
        CARD_PLAY,
        CARD_RESOLUTION,
        ENEMY_PHASE;
    }
}
