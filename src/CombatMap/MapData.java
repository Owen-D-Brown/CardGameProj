package CombatMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Stores the entire map's data, including the map ID and all nodes.
 * Handles node position randomization at game start while ensuring consistency.
 */
public class MapData {
    private String mapID;
    private List<MapNode> nodes;
    private boolean positionsRandomized = false; // Ensures randomization happens once

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
     * Gets the map ID.
     *
     * @return The map ID as a string.
     */
    public String getMapID() {
        return mapID;
    }

    /**
     * Gets the list of all nodes.
     *
     * @return The list of nodes in the map.
     */
    public List<MapNode> getNodes() {
        return nodes;
    }

    /**
     * Retrieves a node by its ID.
     *
     * @param id The node ID to search for.
     * @return The corresponding MapNode, or null if not found.
     */
    public MapNode getNodeById(int id) {
        if (nodes == null) return null; // Prevent null pointer exceptions

        return nodes.stream()
                .filter(node -> node.id == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if a node exists in the map.
     *
     * @param id The node ID to check.
     * @return True if the node exists, false otherwise.
     */
    public boolean hasNode(int id) {
        return getNodeById(id) != null;
    }

    /**
     * Randomizes node positions when the map is first loaded.
     * Ensures it only happens ONCE per game session.
     *
     * @param range The max pixel distance each node can shift left or right.
     */
    public void randomizeNodePositions(int range) {
        if (!positionsRandomized && nodes != null) {
            Random random = new Random();
            for (MapNode node : nodes) {
                node.randomizePosition(random, range); // Apply offset correctly
            }
            positionsRandomized = true; // Prevents further changes
        }
    }

    public List<MapNode> getAvailableNodes() {
        List<MapNode> availableNodes = new ArrayList<>();

        for (MapNode node : nodes) {
            if (node.isDefeated()) {
                // Unlock connected nodes
                for (int connectedId : node.connections) {
                    MapNode connectedNode = getNodeById(connectedId);
                    if (connectedNode != null && !connectedNode.isDefeated() && !availableNodes.contains(connectedNode)) {
                        availableNodes.add(connectedNode);
                    }
                }
            }
        }

        // If no nodes are defeated, unlock the starting node(s)
        if (availableNodes.isEmpty()) {
            for (MapNode node : nodes) {
                if (node.getId() == 4) { // Node 4 is the starting point
                    availableNodes.add(node);
                    break;
                }
            }
        }

        return availableNodes;
    }
}
