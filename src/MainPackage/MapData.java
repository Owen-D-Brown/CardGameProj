package MainPackage;

import java.util.List;

/**
 * Stores the entire map's data, including the map ID and all nodes.
 */
public class MapData {
    public String mapID;
    public List<MapNode> nodes;

    // Default constructor required for JSON parsing
    public MapData() {}

    /**
     * Constructor to initialize a map with nodes.
     */
    public MapData(String mapID, List<MapNode> nodes) {
        this.mapID = mapID;
        this.nodes = nodes;
    }

    /**
     * Retrieves a node by its ID.
     */
    public MapNode getNodeById(int id) {
        for (MapNode node : nodes) {
            if (node.id == id) return node;
        }
        return null;
    }
}