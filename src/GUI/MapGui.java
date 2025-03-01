package GUI;

import MainPackage.MapData;
import MainPackage.MapLoader;

import javax.swing.*;
import java.awt.*;

/**
 * GUI panel for displaying the map.
 */
public class MapGui extends JPanel {
    private MapData mapData;
    private MapPanel mapPanel; // Store reference to MapPanel

    public MapGui() {
        setLayout(new BorderLayout());

        // Load map data from JSON
        mapData = MapLoader.loadMap("Resources/maps/map01.json");

        // Create and add the MapPanel (only for background)
        mapPanel = new MapPanel(mapData);
        add(mapPanel, BorderLayout.CENTER);
    }

    /**
     * Getter for mapData.
     * Allows RootContainer to access map information.
     */
    public MapData getMapData() {
        return mapData;
    }

    /**
     * Getter for MapPanel.
     * Allows RootContainer to size the GlassPane correctly.
     */
    public MapPanel getMapPanel() {
        return mapPanel;
    }
}