package GUI;

import MainPackage.MapData;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class is responsible for rendering the level map background.
 */
public class MapPanel extends JPanel {
    private BufferedImage backgroundImage; // Background image variable

    /**
     * Constructor to initialize the panel with map data.
     */
    public MapPanel(MapData mapData) {
        // Load the background image
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/Resources/MapBackground.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Background image not found!");
        }
    }

    /**
     * Override the paintComponent method to draw the map.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background image first (if available)
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Fallback: Fill with a solid color if no image is found
            g.setColor(Color.PINK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}

