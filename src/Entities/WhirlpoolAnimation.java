package Entities;

import MainPackage.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

public class WhirlpoolAnimation extends InPlaceAnimation {
    public WhirlpoolAnimation() throws IOException {
        super("/Resources/Whirlpool/water1_00001.png", 1280, 720, 1, 1, 3);
    }

    @Override
    protected BufferedImage[] importSprites(String pathName, int cols, int rows, int spriteWidth, int spriteHeight) throws IOException {
        BufferedImage[] temp = new BufferedImage[49];
        String path;
        for(int i = 1; i<49; i++) {
            if(i<10) {
                path = "/Resources/Whirlpool/water1_0000" + i + ".png";
            }
            else {
                path = "/Resources/Whirlpool/water1_000" + i + ".png";
            }
            //System.out.println(path);
            temp[i-1] = ImageIO.read(getClass().getResourceAsStream(path));
        }
        return temp;
    }

    boolean isShuffled = false;
    @Override
    public void checkForUpdates(Iterator<Animation> i) throws IOException {
        // System.out.println("parent "+currentState);

        if(aniIndex == 27 && !isShuffled) {
            Collections.shuffle(Game.gui.gameScreen.northPanel.enemies);
            Game.gui.gameScreen.northPanel.repositionEnemies();
            isShuffled = true;
        }

        if(isMoving && currentState == State.WAITING) {
            currentState = State.MOVING;

        }

        if(currentState == State.MOVING) {

        }

        if(currentState ==State.IMPACT) {
            isShuffled = false;
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
}
