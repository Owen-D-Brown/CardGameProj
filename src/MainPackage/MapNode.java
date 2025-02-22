package MainPackage;

import java.util.List;

/**
 * Represents a single node (level) on the map.
 */
public class MapNode {
    public int id;
    public String type;
    public int x, y;
    public List<Integer> connections;

    // Default constructor required for JSON parsing
    public MapNode() {}

    /**
     * Constructor to initialize a map node.
     */
    public MapNode(int id, String type, int x, int y, List<Integer> connections) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.connections = connections;
    }
}