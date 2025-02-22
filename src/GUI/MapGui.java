package GUI;

import MainPackage.MapData;
import MainPackage.MapLoader;

import javax.swing.*;
import java.awt.*;

/**
 * GUI panel for displaying the map.
 */
public class MapGui extends JPanel {
    public MapGui() {
        setLayout(new BorderLayout());

        // Load map data from JSON
        MapData mapData = MapLoader.loadMap("Resources/maps/map01.json");

        // Create and add the MapPanel
        MapPanel mapPanel = new MapPanel(mapData);
        add(mapPanel, BorderLayout.CENTER);
    }
}