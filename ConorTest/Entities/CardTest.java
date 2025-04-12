package Entities;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void testLoadImage_validPath() {
        BufferedImage img = Card.loadImage("/Resources/Cards/Firebolt.png");
        assertNotNull(img, "Image should load successfully from valid path.");
    }

    @Test
    void testLoadImage_invalidPath() {
        BufferedImage img = Card.loadImage("/Resources/Cards/fake.png");
        assertNull(img, "Image should be null if the path does not exist.");
    }
}