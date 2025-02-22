package GUI;

import MainPackage.MapData;
import MainPackage.MapNode;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class is responsible for rendering the level map on the screen.
 * It draws nodes, connections, and a background image.
 */
public class MapPanel extends JPanel {
    private MapData mapData; // The map data to display
    private BufferedImage backgroundImage; // Background image variable

    /**
     * Constructor to initialize the panel with map data.
     * @param mapData The map data containing nodes and connections.
     */
    public MapPanel(MapData mapData) {
        this.mapData = mapData;

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
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        if (mapData == null) return; // If no map data, don't draw anything

        // Draw connections (lines between connected nodes)
        g.setColor(Color.RED);
        for (MapNode node : mapData.nodes) {
            for (int connectedId : node.connections) {
                MapNode connectedNode = mapData.getNodeById(connectedId);
                if (connectedNode != null) {
                    g.drawLine(node.x, node.y, connectedNode.x, connectedNode.y);
                }
            }
        }

        // Draw nodes (circles for each level)
        for (MapNode node : mapData.nodes) {
            switch (node.type) {
                case "combat":
                    g.setColor(Color.RED);
                    break;
                case "shop":
                    g.setColor(Color.GREEN);
                    break;
                case "boss":
                    g.setColor(Color.MAGENTA);
                    break;
                default:
                    g.setColor(Color.BLUE); // Default for unknown node types
                    break;
            }

            g.fillOval(node.x - 10, node.y - 10, 20, 20); // Draw a circle
        }
    }
}

