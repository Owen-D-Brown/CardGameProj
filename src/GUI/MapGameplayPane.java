package GUI;

import MainPackage.MapNode;
import MainPackage.MapData;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Gameplay Pane overlay for drawing map nodes and their connections.
 * Handles node interaction for starting combat.
 */
public class MapGameplayPane extends JPanel {
    private MapData mapData; // Stores all nodes
    private RootContainer rootContainer; // Reference to RootContainer
    private Map<String, Image> nodeIcons; // Stores icons for different node types
    private final int iconSize = 60; // Updated icon size for better visibility

    public MapGameplayPane(RootContainer rootContainer) {
        setOpaque(false);
        this.rootContainer = rootContainer;
        this.nodeIcons = new HashMap<>();

        // Load icons
        nodeIcons.put("combat", new ImageIcon("src/Resources/Icons/combat.png").getImage());
        nodeIcons.put("boss", new ImageIcon("src/Resources/Icons/boss.png").getImage());
        nodeIcons.put("default", new ImageIcon("src/Resources/Icons/default.png").getImage()); // Fallback icon

        // Mouse click listener to detect node clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleNodeClick(e.getPoint());
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
    private void handleNodeClick(Point clickPoint) {
        if (mapData == null || mapData.getNodes() == null) return;

        for (MapNode node : mapData.getNodes()) {
            int iconX = node.getX() - (iconSize / 2); // Use randomized X
            int iconY = node.y - (iconSize / 2); // Keep Y position as is
            Rectangle bounds = new Rectangle(iconX, iconY, iconSize, iconSize);

            if (bounds.contains(clickPoint)) {
                System.out.println("Node clicked: " + node.id + " - Starting Combat " + node.getCombatID());

                // Start the new fight using the same process as the menu button
                rootContainer.gameScreen.newFight(rootContainer.startFight(node.getCombatID()));

                // Ensure the gameplay pane for combat UI is visible
                Game.gui.gameScreen.glassPane.setVisible(true);

                // Show the correct fight panel in the combat UI
                Game.gui.gameScreen.cardLayout.show(Game.gui.gameScreen.centerContainer, "main");

                // Reset and refresh combat UI
                Game.unslotAllCards();
                rootContainer.gameScreen.center.revalidate();
                rootContainer.gameScreen.center.repaint();

                // Switch to combat screen
                rootContainer.showScreen(rootContainer.gameScreen);

                // Hide the Gameplay Pane since the map is no longer needed
                setVisible(false);
                return;
            }
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
        g2d.setStroke(new BasicStroke(3)); // Set line thickness

        // Draw connections FIRST (so nodes are drawn on top)
        g2d.setColor(Color.LIGHT_GRAY);
        for (MapNode node : mapData.getNodes()) {
            for (int connectedId : node.connections) {
                MapNode connectedNode = mapData.getNodeById(connectedId);
                if (connectedNode != null) {
                    g2d.drawLine(node.getX(), node.y, connectedNode.getX(), connectedNode.y);
                }
            }
        }

        // Draw node icons
        for (MapNode node : mapData.getNodes()) {
            Image icon = nodeIcons.getOrDefault(node.type, nodeIcons.get("default"));
            int iconX = node.getX() - (iconSize / 2); // Use randomized X
            int iconY = node.y - (iconSize / 2); // Keep Y position as is
            g2d.drawImage(icon, iconX, iconY, iconSize, iconSize, this);
        }
    }
}