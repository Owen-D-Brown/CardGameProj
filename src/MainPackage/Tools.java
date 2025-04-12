package MainPackage;

import Entities.Enemies.Enemy;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Tools {

    //Import Sprite Method for When the first sprite is to the right of the spriteSheet
    public static BufferedImage[] importSpritesCutFromRight(String pathName, int cols, int rows, int spriteWidth, int spriteHeight) {
        BufferedImage image = loadImage(pathName);
        if (image == null) {
            return new BufferedImage[0];
        }
        BufferedImage[] sprites = new BufferedImage[cols * rows];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                int reversedX = image.getWidth() - (x + 1) * spriteWidth;

                sprites[y * cols + x] = image.getSubimage(
                        reversedX, y * spriteHeight, spriteWidth, spriteHeight
                );
            }
        }
        return sprites;
    }

    public static BufferedImage[] importSpritesCutFromLeft(String pathName, int cols, int rows, int spriteWidth, int spriteHeight) {
        BufferedImage image = loadImage(pathName);
        if (image == null) {
            return new BufferedImage[0]; // Return empty array if loading fails
        }

        BufferedImage[] sprites = new BufferedImage[cols * rows];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                sprites[y * cols + x] = image.getSubimage(x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight);
            }
        }
        return sprites;
    }

    public static BufferedImage loadImage(String path) {
        try (InputStream is = Enemy.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Error: Image not found at " + path);
                return null;
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
