package Entities;

import MainPackage.Config;
import MainPackage.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Animation extends JComponent {

    private int targetX, targetY;
    private boolean isMoving = false;
    private double circleX, circleY;
    public int duration = 1200;  // 2 seconds in milliseconds
    public int FPS = 20;   // 20 updates per second
    public int interval = 1000 / FPS;
    boolean test= false;
    public ArrayList<BufferedImage> sprites = new ArrayList<>();

    public Animation(int startX, int startY, int targetX, int targetY) {
        this.circleX = startX;
        this.circleY = startY;
        this.targetX = targetX;
        this.targetY = targetY;

        for(int i = 1; i<=5; i++) {
            String path = "/Resources/Fireball/FB00" + i+".png";
            sprites.add(loadImage(path));
        }
    }
    public Enemy target;
    public Player player = Game.player;

    public void setAnimationStartFinish() {
        this.targetX = target.getX();
        this.circleX = player.getX();
        this.targetY = target.getY();
        this.circleY = player.getY();
    }



    public void setTarget(Enemy en) {
        for(Enemy eni : Game.gui.gameScreen.northPanel.enemies) {
            if(eni == en) {
                this.target = eni;
                setAnimationStartFinish();
            }
        }
    }

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
    protected int aniIndex = 0;
    protected int aniSpeed = 1;
    protected int aniCounter = 0;
    public void updateAni() {



        if (isMoving) {

            //

            aniCounter++;
            if (aniCounter >= aniSpeed) {
                aniCounter = 0;
                aniIndex++;

                if (!sprites.isEmpty() && aniIndex >= sprites.size()) {
                    aniIndex = 0;
                }
            }
            //
            double speed = 0.1; // Moves 20% of the remaining distance each frame

            circleX += (targetX - circleX) * speed;
            circleY += (targetY - circleY) * speed;

            // Stop animation when close enough
            if (Math.abs(circleX - targetX) < 5 && Math.abs(circleY - targetY) < 5) {
                circleX = targetX;
                circleY = targetY;
                isMoving = false;
            }
        }
        Game.gui.gameScreen.northPanel.attackPlane.repaint();
    }

    public void runAnimation() {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isMoving) {
            //System.out.println("Animation started");
            //g.setColor(Color.orange);
            //g.fillOval((int)circleX, (int)circleY, 40, 40); // Circle moving animation
          //  System.out.println("Animation.paintComponent triggered. X = "+circleX+" Y = "+circleY);
            g.drawImage(sprites.get(aniIndex), (int)circleX, (int) circleY, 64*3, 32*3, null);

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
