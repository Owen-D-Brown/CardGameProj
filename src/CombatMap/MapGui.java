package CombatMap;

import javax.swing.*;
import java.awt.*;

/**
 * GUI panel for displaying the map.
 */
public class MapGui extends JPanel {
    private MapData mapData;
    private MapPanel mapPanel; //Store reference to MapPanel
    private int currentDungeonIndex = 0;

    public MapGui() {
        setLayout(new BorderLayout());

        //Initialize with a blank/default MapData
        mapData = new MapData(); // Empty map placeholder

        //Create placeholder mapPanel until setMapData() is called
        mapPanel = new MapPanel(mapData);
        add(mapPanel, BorderLayout.CENTER);
    }

    /**
     * Sets new map data and updates the map panel.
     */
    public void setMapData(MapData newMapData) {
        this.mapData = newMapData;

        // Remove old map panel if present
        if (mapPanel != null) {
            remove(mapPanel);
        }

        // Replace with a panel based on the new map
        mapPanel = new MapPanel(newMapData);
        add(mapPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    public MapData getMapData() {
        return mapData;
    }

    public MapPanel getMapPanel() {
        return mapPanel;
    }

    //Add these:
    public void setCurrentDungeonIndex(int index) {
        this.currentDungeonIndex = index;
    }

    public int getCurrentDungeonIndex() {
        return currentDungeonIndex;
    }
}