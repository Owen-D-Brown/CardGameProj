package Entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class WaterBlastAnimation extends InPlaceAnimation {
    public WaterBlastAnimation() throws IOException {
        super("filePath", 1800, 1200, 1, 1, 3);
    }

    @Override
    protected BufferedImage[] importSprites(String pathName, int cols, int rows, int spriteWidth, int spriteHeight) throws IOException {BufferedImage[] temp = new BufferedImage[5];
        BufferedImage[] array = new BufferedImage[37];
        for(int i = 0; i<=21; i++) {
            String path = "/Resources/WaterBlastAnimation/water3000" + i+".png";

            array[i] = ImageIO.read(getClass().getResourceAsStream(path));
        }
        for(int i = 0; i<=14; i++) {
            String path = "/Resources/WaterBlastAnimation/water4000" + i+".png";
            System.out.println(path);
            array[22+i] = ImageIO.read(getClass().getResourceAsStream(path));
        }
        return array;
    }

    @Override
    public void paintComponent(Graphics g) {
     //   super.paintComponent(g);
        if (isMoving) {

            g.drawImage(sprites[aniIndex], x, y, 128*6, 64*6, null);
        }
    }
}
