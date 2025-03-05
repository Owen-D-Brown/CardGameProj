package Entities;

import MainPackage.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Fireball extends XaxisAnimation {
    private enum AnimationState { MOVING, IMPACT }
    private AnimationState currentState = AnimationState.MOVING;

    BufferedImage[] impactSprites;


    public Fireball() throws IOException {
        super(64, 32,0, 0, "/Resources/Fireball/FB00", 7);
        this.impactSprites = super.importSprites("/Resources/explosion.png", 8, 1, 32, 32);
        this.sprites = importSprites("",1,1,1,1) ;
    }

    @Override
    protected BufferedImage[] importSprites(String pathName, int cols, int rows, int spriteWidth, int spriteHeight) throws IOException {
        BufferedImage[] temp = new BufferedImage[6];
            for(int i = 1; i<=5; i++) {
                String path = "/Resources/Fireball/FB00" + i+".png";
                temp[i-1] = ImageIO.read(getClass().getResourceAsStream(path));
            }
            return temp;
    }
    @Override
    public void paintComponent(Graphics g) {
        //super.paintComponent(g);
        if (isMoving) {
            if (currentState == AnimationState.MOVING) {
                g.drawImage(sprites[aniIndex], (int) currentX, (int) currentY, 64 * 3, 32 * 3, null);
            } else if (currentState == AnimationState.IMPACT) {
                g.drawImage(impactSprites[aniIndex], targetX, targetY, 64 * 3, 32 * 3, null);
            }
        }
    }

    @Override
    public void updateAni() {
       // super.updateAni();
        if(isMoving) {

            if (currentState == AnimationState.MOVING) {
                aniCounter++;

                if (aniCounter >= aniSpeed) {
                    aniCounter = 0;
                    aniIndex++;

                    if (sprites.length > 0 && aniIndex >= sprites.length) {
                        aniIndex = 0;
                    }
                }
                double speed = 0.05;
                currentX += (targetX - currentX) * speed;
                currentY += (targetY - currentY) * speed;

                // Check if we reached the target
                if (Math.abs(currentX - targetX) < 60 && Math.abs(currentY - targetY) < 60) {
                    currentX = targetX;
                    currentY = targetY;

                    // Switch to impact animation
                    currentState = AnimationState.IMPACT;

                    // Change sprite sheet or reset animation index for impact
                    aniIndex = 0;

                }
            } else if (currentState == AnimationState.IMPACT) {
                // Play impact animation frames
                aniCounter++;
                if (aniCounter >= aniSpeed) {
                    aniCounter = 0;
                    aniIndex++;
                    System.out.println("ani index: "+aniIndex);
                    // If impact animation finishes, stop animation
                    if (aniIndex >= impactSprites.length-1) {
                        isMoving = false;  // End animation completely
                        System.out.println("STTTTTTTTTTTTTTTTTTTOOOOOOOOOOPPPPPPPPPPP");
                       // this.currentState = AnimationState.MOVING;
                    }
                }
            }
        }
    }

}
