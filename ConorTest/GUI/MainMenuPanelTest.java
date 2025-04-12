package GUI;


import org.junit.jupiter.api.Test;


import java.awt.image.BufferedImage;


import static org.junit.jupiter.api.Assertions.*;

public class MainMenuPanelTest {

    @Test
   public void testLoadImage_validPath() {
        BufferedImage img = MainMenuPanel.loadImage("/Resources/MenuImages/MainMenuFinal.png");
        assertNotNull(img);
    }

    @Test
    public void testLoadImage_invalidPath() {
        BufferedImage img = MainMenuPanel.loadImage("/Resources/MenuImages/nope.png");
        assertNull(img);
    }

}