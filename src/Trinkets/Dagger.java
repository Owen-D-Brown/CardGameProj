package Trinkets;

import Entities.Enemies.Enemy;
import MainPackage.Config;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Dagger extends JComponent {

    protected BufferedImage sprite = loadImage("/Resources/daggerTrinket.png");
    protected String name = "Dagger";

    public Dagger() {
        this.setPreferredSize(new Dimension(12*6, 9*6));
        if(Config.debug);
            //this.setBorder(BorderFactory.createLineBorder(Color.black, 3));
    }

    public static BufferedImage loadImage(String path) {
        try (InputStream is = Enemy.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Error: Image not found at " + path);
                return null;
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace(); // Print error details for debugging
            return null;
        }
    }

    public String getTrinketName() {
        return this.name;
    }

    public BufferedImage getSprite() {
        return this.sprite;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (sprite != null) {
            int imgWidth = 12 * 6;
            int imgHeight = 9 * 6;

            // Calculate centered position
            int x = (getWidth() - imgWidth) / 2;
            int y = (getHeight() - imgHeight) / 2;

            g.drawImage(sprite, x, y, imgWidth, imgHeight, null);
        }
    }

}
