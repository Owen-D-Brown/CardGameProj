package Entities;

import MainPackage.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Animation extends JComponent {


    public boolean isMoving = false;

    protected int x, y;
    protected int FPS = 20;
    public BufferedImage[] sprites;
    protected String filePath;
    public int duration = 1000;  // 2 seconds in milliseconds
    public int interval = 60/ duration;
    protected int aniIndex = 0;
    protected int aniSpeed;
    protected int aniCounter = 0;

    public Animation( String filePath, int w, int h, int col, int row, int aniSpeed) throws IOException {
        this.filePath = filePath;
        this.aniSpeed = aniSpeed;
        this.sprites = importSprites(filePath, col, row, w, h);
        //calculateDuration();

    }

    public void calculateInterval() {
        this.interval = (aniSpeed * 1000) / 60;
    }


    public void calculateDuration() {
        if (sprites == null || sprites.length == 0) {
            this.duration = 0;
        } else {
            this.duration = (aniSpeed * sprites.length * 1000) / 60;
            System.out.println("Duration: " + duration);
        }
    }

    protected BufferedImage[] importSprites(String pathName, int cols, int rows, int spriteWidth, int spriteHeight) throws IOException {
        BufferedImage image = loadImage(pathName);
        if (image == null) {
            return null;
        }


        BufferedImage[] sprites = new BufferedImage[cols * rows];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                sprites[y * cols + x] = image.getSubimage(x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight);
            }
        }
        return sprites;
    }

    ///






    public void startAnimation() {
      //  this.circleX = Game.gui.gameScreen.northPanel.playerX;
       // this.circleY = Game.gui.gameScreen.northPanel.playerY;
        //working here
        isMoving = true;
    }

    public void stopAnimation() {
    //    this.circleX = Game.gui.gameScreen.northPanel.playerX;
     //   this.circleY = Game.gui.gameScreen.northPanel.playerY;
        isMoving = false;
    }





    public void updateAni() {

        if (isMoving) {
            aniCounter++;

            if (aniCounter >= aniSpeed) {
                aniCounter = 0;
                aniIndex++;

                if (sprites.length > 0 && aniIndex >= sprites.length) {
                    aniIndex = 0;
                    isMoving = false;
                }
            }
        }
       // Game.gui.gameScreen.northPanel.attackPlane.repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isMoving) {

            g.drawImage(sprites[aniIndex], x, y, 53*2, 32*2, null);
        }
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
}
