package Entities.Enemies;

import MainPackage.Game;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GoblinTest extends EnemyTest {

    @BeforeAll
    static void setUp() throws IOException {
        game = new Game();
        Game.gui.gameScreen.newFight(Game.gui.startFight(6));
        Game.gui.showScreen(Game.gui.gameScreen);
        enemy = Game.gui.gameScreen.northPanel.enemies.get(0);
        originalHp = Game.player.currentHealth;
    }

    @Test
    @Override
    void takeDamage() {
        int ogHp = enemy.currentHealth;
        enemy.agility = 1000;
        enemy.takeDamage(5);
        assertEquals(ogHp, enemy.currentHealth);
        enemy.agility = 0;
        enemy.takeDamage(5);
        assertTrue(ogHp> enemy.currentHealth);
        enemy.takeDamage(500000);
        assertTrue(Game.enemyWaiting);
    }
}