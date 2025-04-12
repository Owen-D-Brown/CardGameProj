package Entities.Enemies;

import MainPackage.Game;
import MainPackage.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {
    protected static Enemy enemy;
    protected  static Game game;
    protected  static int originalHp;
    @BeforeAll
    static void setUp() throws IOException {
       game = new Game();
       Game.gui.gameScreen.newFight(Game.gui.startFight(5));
       Game.gui.showScreen(Game.gui.gameScreen);
       enemy = Game.gui.gameScreen.northPanel.enemies.get(0);
       originalHp = Game.player.currentHealth;
    }


    @Test
    void populateEnemyAnimationData() {

        assertNotNull(enemy.animations);
    }

    @Test
    void addMapsToAnimations() {
        String name = enemy.name;
        String basePath = "/Resources/"+name+"/"+name;
        enemy.addMapsToAnimations(basePath);
        assertNotNull(enemy.animations);
    }

    @Test
    void getHitbox() {
        assertNotNull(enemy.getHitbox());
    }

    @Test
    void setStartBounds() {
        Rectangle bounds = new Rectangle(1, 1, 50, 50);
        enemy.setStartBounds(bounds);
        assertSame(bounds, enemy.startBounds);
    }

    @Test
    void takeDamage() throws InterruptedException {
        int ogHp = enemy.currentHealth;
        enemy.takeDamage(5);
        Thread.sleep(1000);
        assertTrue(enemy.currentHealth < ogHp);

        enemy.takeDamage(50000);
        assertTrue(Game.enemyWaiting);
    }

    @Test
    void attack() throws IOException, InterruptedException {
        Game.changeStateToEnemyTurn();
        long start = System.currentTimeMillis();
        long timeout = 5000; // 5 seconds max
        while (System.currentTimeMillis() - start < timeout) {
            if (Game.gameState == Game.gameState.CARD_PLAY) {
                assertTrue(Game.player.currentHealth < originalHp);
            } else if(Game.gameState == Game.gameState.ENEMY_PHASE&& enemy.state == Enemy.State.ATTACKING) {
                assertTrue(enemy.isAttacking);
            }
            Thread.sleep(50); // don't hammer CPU
        }
    }


    //
    @Test
    void getLootTable() {
        assertNotNull(enemy.lootTable);
    }

    @Test
    void generateLoot() {
        assertNotNull(enemy.generateLoot());
    }

    @Test
    void getWeight() {
        assertTrue(enemy.getWeight()>0);
    }
}