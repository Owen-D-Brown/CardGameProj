package Entities;

import MainPackage.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class Animation extends JComponent {


    public boolean isMoving = false;
    public Runnable onComplete;
    protected int x, y;
    protected int FPS = 20;
    public BufferedImage[] sprites;
    protected String filePath;
    public int duration = 1000;  // 2 seconds in milliseconds
    public int interval = 60/ duration;
    protected int aniIndex = 0;
    protected int aniSpeed;
    protected int aniCounter = 0;
    protected int w;
    protected int h;

    public Animation( String filePath, int w, int h, int col, int row, int aniSpeed) throws IOException {
        this.filePath = filePath;
        this.aniSpeed = aniSpeed;
        this.sprites = importSprites(filePath, col, row, w, h);
        //calculateDuration();
        setSize(w, h);
        this.w = w;
        this.h = h;
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));


    }

    public void checkForUpdates(Iterator<Animation> i) throws IOException {
       // System.out.println("parent "+currentState);

        if(isMoving && currentState == State.WAITING) {
            currentState = State.MOVING;

        }

        if(currentState == State.MOVING) {

        }

        if(currentState ==State.IMPACT) {

            isMoving = false;


            if(!isMoving) {

                currentState = State.FINISHED;
                if(card!=null) {
                    card.effect();
                    Game.gui.gameScreen.glassPane.removeCard(card);


                } else if (card == null) {


                }
                //AttackPlane.animations.remove(this);
                i.remove();
                onComplete.run();
            }
        }
       // System.out.println("2parent "+currentState);
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





    public enum State {WAITING, MOVING, IMPACT, FINISHED};
    public State currentState = State.WAITING;
    public void startAnimation() {
      //  this.circleX = Game.gui.gameScreen.northPanel.playerX;
       // this.circleY = Game.gui.gameScreen.northPanel.playerY;
        //working here
        isMoving = true;
        //state to moving
        currentState = State.MOVING;
        System.out.println("STARTING ANIMATION");
    }

    public void stopAnimation() {
    //    this.circleX = Game.gui.gameScreen.northPanel.playerX;
     //   this.circleY = Game.gui.gameScreen.northPanel.playerY;
        isMoving = false;

        currentState = State.FINISHED;
    }



    public Card card;

    public void updateAni() {
          //System.out.println("THIS IS FOR CARD ANINMATIONS - for ani in AttackPlane.animations "+currentState+" aniIndex: "+aniIndex+" aniCounter: "+aniCounter);
        if (isMoving) {
            aniCounter++;

            if (aniCounter >= aniSpeed) {
                aniCounter = 0;
                aniIndex++;

                if (sprites.length > 0 && aniIndex >= sprites.length) {

                    aniIndex = 0;
                    isMoving = false;
                    currentState = State.IMPACT;
                }
            }
        }
       // Game.gui.gameScreen.northPanel.attackPlane.repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isMoving) {

            g.drawImage(sprites[aniIndex], x, y, w, h, null);
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
