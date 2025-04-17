package Mentity;

import MAP.KeyH;
import MAP.gamePanel;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MplayerTest {

    @Test
    void testDefaultValues() {
        gamePanel gp = new gamePanel();
        KeyH kh = new KeyH();
        Mplayer player = new Mplayer(gp, kh);

        assertEquals(200, player.x);
        assertEquals(200, player.y);
        assertEquals(3, player.spd);
        assertEquals("down", player.direction);
    }

    @Test
    void testMovement() {
        gamePanel gp = new gamePanel();
        KeyH kh = new KeyH();
        kh.UPp = true;
        Mplayer player = new Mplayer(gp, kh);

        try {
            player.update();
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        assertNotEquals(200, player.y);
    }

    @Test
    void testNoMovement() {
        gamePanel gp = new gamePanel();
        KeyH kh = new KeyH();
        kh.UPp = false;
        kh.DOWNp = false;
        kh.LEFTp = false;
        kh.RIGHTp = false;
        Mplayer player = new Mplayer(gp, kh);

        try {
            player.update();
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        assertEquals(200, player.x); // Player should not move
        assertEquals(200, player.y);
    }
}
