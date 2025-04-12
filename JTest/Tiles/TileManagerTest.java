package Tiles;

import MAP.KeyH;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TileManagerTest {

    private TileManager tileManager;

    @BeforeEach
    void setUp() {
        KeyH keyH = new KeyH();
        tileManager = new TileManager(keyH);

        tileManager.mapData = new int[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
    }

    @Test
    void gateTileDetected() {

        tileManager.mapData[1][1] = 8;


        boolean result = tileManager.Gate(1, 1);


        assertTrue(result, "Gate(1,1) should return true when tile is a gate.");
    }

    @Test
    void gateTileNotDetected() {

        boolean result = tileManager.Gate(1, 1);


        assertFalse(result, "Gate(1,1) should return false when tile is not a gate.");
    }

    @Test
    void merchantTileDetected() {

        tileManager.mapData[1][1] = 3;


        boolean result = tileManager.MerchantTile(1, 1);


        assertTrue(result, "MerchantTile(1,1) should return true when tile is a merchant tile.");
    }

    @Test
    void merchantTileNotDetected() {

        boolean result = tileManager.MerchantTile(1, 1);


        assertFalse(result, "MerchantTile(1,1) should return false when tile is not a merchant tile.");
    }

    @Test
    void exitTileDetected() {

        tileManager.mapData[1][1] = 4;


        boolean result = tileManager.isExitTile(1, 1);


        assertTrue(result, "isExitTile(1,1) should return true when tile is an exit tile.");
    }

    @Test
    void exitTileNotDetected() {

        boolean result = tileManager.isExitTile(1, 1);


        assertFalse(result, "isExitTile(1,1) should return false when tile is not an exit tile.");
    }
}
