package Entities;

import MainPackage.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

public class VampireCountessAttack extends XaxisAnimation {

    private State currentState = State.WAITING;

    BufferedImage[] impactSprites;


    public VampireCountessAttack() throws IOException {
        super(64, 48,1, 3, "/Resources/VampireCountessAttack/sprite.png", 2);
        //this.impactSprites = super.importSprites("/Resources/explosion.png", 8, 1, 32, 32);
      //  this.sprites = importSprites("",1,1,1,1) ;

    }
    @Override
    public BufferedImage[] importSprites(String pathName, int cols, int rows,
                                         int spriteWidth, int spriteHeight) {
        System.out.println("enter");
        BufferedImage image = loadImage(pathName);
        if (image == null) {
            System.out.println("noting");
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


    @Override
    public void paintComponent(Graphics g) {
        //super.paintComponent(g);
        if (isMoving) {
            if (currentState == State.MOVING) {
                g.drawImage(sprites[aniIndex], (int) currentX, (int) currentY, 64, 48, null);
            }
        }
    }

    private int yCounter = 0;
    boolean yUp = true;

    @Override
    public Animation clone() {
        try {
            return new VampireCountessAttack();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAni() {
        // super.updateAni();
        //System.out.println("Animation instance: " + System.identityHashCode(this));

        //  System.out.println("STARTING UPDATEANI currentState "+currentState);
        if(isMoving) {

            if (currentState == State.MOVING) {
                aniCounter++;

                if (aniCounter >= aniSpeed) {
                    aniCounter = 0;
                    aniIndex++;

                    if (sprites.length > 0 && aniIndex >= sprites.length) {
                        aniIndex = 0;
                    }
                }

                //currentY += (targetY - currentY) * speed;

                // Check if we reached the target
                //System.out.println("debuggng currentX: "+currentX+" | TargetX: "+targetX+"\n calculation: "+(Math.abs(currentX - targetX)  ));
                if(goingRight) {
                   // System.out.println("going right");
                    double speed = 1;
                    currentX += 200 * speed;
                    if (currentX == targetX||currentX>targetX) {
                        //  System.out.println("reached its target");
                        currentX = targetX;
                        //currentY = targetY;

                        // Switch to impact animation
                        currentState = State.IMPACT;

                        // Change sprite sheet or reset animation index for impact
                        aniIndex = 0;

                    }
                } else if(!goingRight) {

                    double speed = 0.15;
                    currentX -= 100 * speed;
                    yCounter++;
                    if(yUp)
                        currentY = currentY + 1;
                    else if(!yUp)
                        currentY = currentY - 1;
                    if(yCounter>2) {

                        yCounter = 0;
                        yUp = !yUp;
                    }
                    if (currentX == targetX||currentX<targetX) {
                        //  System.out.println("reached its target");
                        currentX = targetX;
                        //currentY = targetY;

                        // Switch to impact animation
                        currentState = State.IMPACT;

                        // Change sprite sheet or reset animation index for impact
                        aniIndex = 0;

                    }
                }


            }
        }
        //System.out.println("CURRENT STATE AT END OF UPDATE ANI "+currentState);
    }
    protected int explosionSpeed =15;

    @Override
    public void checkForUpdates(Iterator<Animation> i) throws IOException {
        //System.out.println("checkForUpdates "+currentState+"    "+isMoving);
        if(isMoving && currentState == State.WAITING) {
            currentState = State.MOVING;
        }

        if(currentState == State.MOVING) {

        }

        if(currentState == State.IMPACT) {

            isMoving = false;


            if(!isMoving) {

                currentState = State.FINISHED;
                i.remove();
                if(card!=null) {
                    card.effect();
                    Game.gui.gameScreen.glassPane.removeCard(card);

                }

                onComplete.run();
            }
        }
    }
}
