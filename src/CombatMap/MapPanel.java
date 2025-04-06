package CombatMap;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class is responsible for rendering the level map background.
 */
public class MapPanel extends JPanel {
    private BufferedImage[] bgFrames;
    private int currentBgFrame = 0;
    private Timer bgTimer;

    /**
     * Constructor to initialize the panel with map data.
     */
    public MapPanel(MapData mapData) {
        setOpaque(false); // So anything underneath still renders

        // Load animated background sprite sheet
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Resources/MapBackgroundAnimation.png"));
            int frameWidth = 1000;
            int frameHeight = 1000;
            int frameCount = 3;

            bgFrames = new BufferedImage[frameCount];
            for (int i = 0; i < frameCount; i++) {
                bgFrames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }

            // Start background animation timer
            bgTimer = new Timer(250, e -> {
                currentBgFrame = (currentBgFrame + 1) % bgFrames.length;
                repaint();
            });
            bgTimer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Override the paintComponent method to draw the background animation frame.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bgFrames != null && bgFrames[currentBgFrame] != null) {
            g.drawImage(bgFrames[currentBgFrame], 0, 0, getWidth(), getHeight(), this);
        }
    }
}
