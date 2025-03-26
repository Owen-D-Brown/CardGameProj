package CombatMap;

import java.util.List;
import java.util.Random;

/**
 * Represents a single node (level) on the map.
 */
public class MapNode {
    public int id;
    public String type;
    public int x, y;
    private int randomizedX; // Stores the randomized position
    private int combatID;
    private boolean defeated = false; // Track defeated status
    public List<Integer> connections;

    public int getCombatID() { return combatID; }
    public int getX() { return randomizedX; } // Use randomizedX instead of x

    // ✅ Getter & Setter for defeated status
    public boolean isDefeated() { return defeated; }
    public void setDefeated(boolean defeated) { this.defeated = defeated; }

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
        this.randomizedX = x; // Default to the original position
    }

    /**
     * Randomizes the X position, ensuring it only happens once per game start.
     */
    public void randomizePosition(Random random, int range) {
        if (randomizedX == 0) { // Only randomize if it hasn't been set yet
            randomizedX = x + random.nextInt(range * 2 + 1) - range; // Keep close to original
        }
    }
}
