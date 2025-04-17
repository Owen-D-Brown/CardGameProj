package Filips_Tests;

import MainPackage.Game;
import MainPackage.NorthPanel;
import GUI.RootContainer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PresetCombatTest {

    @Test
    public void testStartFightReturnsValidEncounter() throws IOException {
        Game dummyGame = new Game();

        RootContainer root = new RootContainer(dummyGame);

        NorthPanel panel = root.startFight(1);

        assertNotNull(panel, "Expected a non-null encounter panel.");
        assertFalse(panel.enemies.isEmpty(), "Expected at least one enemy in the encounter.");
    }

    @Test
    public void testStartRandomFightCreatesEnemies() throws IOException {
        Game dummyGame = new Game();
        RootContainer root = new RootContainer(dummyGame);

        NorthPanel panel = root.startRandomFight(20,0);

        assertNotNull(panel);
        assertFalse(panel.enemies.isEmpty());
        assertTrue(panel.enemies.size() <= 3); // game logic limit
    }
}