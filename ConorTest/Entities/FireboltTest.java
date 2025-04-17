package Entities;


import Entities.Cards.Firebolt;
import Entities.Enemies.Goblin;
import Entities.Enemies.SatyrFemale;
import MainPackage.Game;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FireboltTest {

    //static variable for shared setup
    protected static Game game;


    //set up the game and environment before tests run
    @BeforeAll
    static void setUp() throws IOException {
        //initialize the game and start the fight
        game = new Game();
        Game.gui.gameScreen.newFight(Game.gui.startFight(5));  //load satyr female enemy
        Game.gui.showScreen(Game.gui.gameScreen);

    }

    @Test
    void testEffectDealsDamage() throws IOException {
        //store satyr initial health
        int initialHealth = SatyrFemale.currentHealth;  //access current health

        //create a Firebolt instance and apply its effect
        Firebolt firebolt = new Firebolt();
        firebolt.effect();

        //check if the damage dealt is within the expected range (1 to 9)
        int damageDealt = initialHealth - SatyrFemale.currentHealth;
        assertTrue(damageDealt >= 1 && damageDealt <= 9, "Damage dealt is out of expected range");
    }
}
