package CombatMap;

import GUI.RootContainer;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

/**
 * A Gameplay Pane overlay for drawing map nodes and their connections.
 * Handles node interaction for starting combat.
 */
public class MapGameplayPane extends JPanel {
    private MapData mapData; // Stores all nodes
    private RootContainer rootContainer; // Reference to RootContainer
    private Map<String, Image> nodeIcons; // Stores icons for different node types
    private final int iconSize = 60; // Updated icon size for better visibility
    private MapNode hoveredNode = null; // Stores which node is currently being hovered

    public MapGameplayPane(RootContainer rootContainer) {
        setOpaque(false);
        this.rootContainer = rootContainer;
        this.nodeIcons = new HashMap<>();

        // Load icons
        nodeIcons.put("combat", new ImageIcon("src/Resources/Icons/combat.png").getImage());
        nodeIcons.put("boss", new ImageIcon("src/Resources/Icons/boss.png").getImage());
        nodeIcons.put("default", new ImageIcon("src/Resources/Icons/default.png").getImage()); // Fallback icon
        nodeIcons.put("defeated", new ImageIcon("src/Resources/Icons/defeated.png").getImage()); // New defeated icon

        // Mouse click listener to detect node clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    handleNodeClick(e.getPoint());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Mouse motion listener to detect hover events
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateHoveredNode(e.getPoint());
            }
        });
    }

    /**
     * Sets the map data and repaints.
     */
    public void setMapData(MapData mapData) {
        this.mapData = mapData;

        // Randomize positions ONLY if it's the first time loading the map
        this.mapData.randomizeNodePositions(200); // Adjust range as needed

        // Resize Gameplay Pane to match the parent component
        if (getParent() != null) {
            setBounds(getParent().getBounds());
        }

        repaint();
    }

    /**
     * Handles clicks on nodes and starts combat if clicked.
     */
    private void handleNodeClick(Point clickPoint) throws IOException {
        if (mapData == null || mapData.getNodes() == null) return;

        List<MapNode> availableNodes = mapData.getAvailableNodes();

        for (MapNode node : availableNodes) {
            if (node.isDefeated()) continue;

            int iconX = node.getX() - (iconSize / 2);
            int iconY = node.y - (iconSize / 2);
            Rectangle bounds = new Rectangle(iconX, iconY, iconSize, iconSize);

            if (bounds.contains(clickPoint)) {
                System.out.println("Node clicked: " + node.id + " - Starting Combat " + node.getCombatID());

                // Start combat
                rootContainer.gameScreen.newFight(rootContainer.startFight(node.getCombatID()));

                // Ensure the gameplay pane for combat UI is visible
                Game.gui.gameScreen.glassPane.setVisible(true);
                Game.gui.gameScreen.cardLayout.show(Game.gui.gameScreen.centerContainer, "main");

                // Reset and refresh combat UI
                Game.unslotAllCards();
                rootContainer.gameScreen.center.revalidate();
                rootContainer.gameScreen.center.repaint();

                // Switch to combat screen
                rootContainer.showScreen(rootContainer.gameScreen);

                // Mark node as defeated
                node.setDefeated(true);

                repaint(); // Refresh node appearance

                setVisible(false); // Hide map pane
                return;
            }
        }
    }

    private void updateHoveredNode(Point mousePosition) {
        MapNode previousHovered = hoveredNode;
        hoveredNode = null;

        for (MapNode node : mapData.getAvailableNodes()) {
            int iconX = node.getX() - (iconSize / 2);
            int iconY = node.y - (iconSize / 2);
            Rectangle bounds = new Rectangle(iconX, iconY, iconSize, iconSize);

            if (bounds.contains(mousePosition)) {
                hoveredNode = node;
                break;
            }
        }

        if (hoveredNode != previousHovered) {
            repaint();
        }
    }

    /**
     * Paints connections between nodes and then the node icons themselves.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapData == null || mapData.getNodes() == null) return;

        Graphics2D g2d = (Graphics2D) g;

        // Dashed line styling
        float[] dashPattern = {10, 10};
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dashPattern, 0));
        g2d.setColor(Color.BLACK);

        // Draw connections from defeated nodes
        for (MapNode node : mapData.getNodes()) {
            if (!node.isDefeated()) continue;

            for (int connectedId : node.connections) {
                MapNode connectedNode = mapData.getNodeById(connectedId);
                if (connectedNode == null) continue;

                g2d.drawLine(node.getX(), node.y, connectedNode.getX(), connectedNode.y);
            }
        }

        // Draw nodes (with grayscale for unavailable)
        for (MapNode node : mapData.getNodes()) {
            Image icon;

            if (node.isDefeated()) {
                icon = nodeIcons.get("defeated");
            } else if (mapData.getAvailableNodes().contains(node)) {
                icon = nodeIcons.getOrDefault(node.type, nodeIcons.get("default"));
            } else {
                // Unavailable nodes → grayscale
                icon = toGrayscale(nodeIcons.getOrDefault(node.type, nodeIcons.get("default")));
            }

            int drawSize = (node == hoveredNode) ? (int) (iconSize * 1.25) : iconSize;
            int iconX = node.getX() - (drawSize / 2);
            int iconY = node.y - (drawSize / 2);

            g2d.drawImage(icon, iconX, iconY, drawSize, drawSize, this);
        }
    }

    private Image toGrayscale(Image originalImage) {
        if (originalImage == null) return null;

        BufferedImage colored = new BufferedImage(
                originalImage.getWidth(null),
                originalImage.getHeight(null),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = colored.createGraphics();
        g2.drawImage(originalImage, 0, 0, null);
        g2.dispose();

        // Convert to grayscale
        for (int y = 0; y < colored.getHeight(); y++) {
            for (int x = 0; x < colored.getWidth(); x++) {
                int rgba = colored.getRGB(x, y);
                Color col = new Color(rgba, true);
                int gray = (int) (0.3 * col.getRed() + 0.59 * col.getGreen() + 0.11 * col.getBlue());
                Color grayColor = new Color(gray, gray, gray, col.getAlpha());
                colored.setRGB(x, y, grayColor.getRGB());
            }
        }

        return colored;
    }
}
