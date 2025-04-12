package Entities.Animations;

import MainPackage.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

public class VampireCardAttackAnimation extends XaxisAnimation {
    BufferedImage[] sprites2;

    public enum State {WAITING, MOVING, ATTACKING, IMPACT, FINISHED}

    ;
    public State currentState;
    private int attackCounter = 0;
    private int attackIndex = 0;
    private int attackSpeed = 6;

    public VampireCardAttackAnimation() throws IOException {
        super(128, 128, 1, 6, "/Resources/VampireCharge/run.png", 6);
        sprites2 = importSprites("/Resources/VampireCharge/attack.png", 5, 1, 128, 128);
    }

    @Override
    public void checkForUpdates(Iterator<Animation> i) throws IOException {
        // System.out.println("parent "+currentState);

        if(isMoving && currentState == State.WAITING) {
            currentState = State.MOVING;
            System.out.println("ASSINGING TO MOVING");
        }

        if(currentState == State.MOVING) {

        }

        if(currentState == State.IMPACT) {
            System.out.println("IMPACT");
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

    @Override
    public void updateAni() {
     //   System.out.println("card "+currentState);
        if (isMoving) {
            if(this.currentState != State.MOVING && this.currentState != State.ATTACKING && this.currentState != State.IMPACT) {
                currentState = State.MOVING;
                System.out.println("ASSINGING TO MOVING");
            }
            aniCounter++;

            if (aniCounter >= aniSpeed) {
                aniCounter = 0;
                aniIndex++;

                if (sprites.length > 0 && aniIndex >= sprites.length) {
                    aniIndex = 0;
                }
            }
            Rectangle animationBounds = new Rectangle((int) currentX, (int) currentY, width, height); // Adjust width/height accordingly
            Rectangle enemyBounds = Game.gui.gameScreen.northPanel.enemies.get(0).getBounds();
            // System.out.println(enemyBounds);

            if (!animationBounds.intersects(enemyBounds)) {

                double speed = 0.2;
                currentX += (50) * speed;
            }

            if (animationBounds.intersects(enemyBounds)) {
                this.currentState = State.ATTACKING;
                attackCounter++;

                if (attackCounter >= attackSpeed) {
                    attackCounter = 0;
                    attackIndex++;

                    if (attackIndex >= sprites2.length) {
                        attackIndex = 0;
                        Game.gui.gameScreen.northPanel.enemies.get(0).takeDamage(20); // however you're applying damage
                        Game.player.heal(10);

                        this.currentState = State.IMPACT;


                    }
                }
            }
        }

      // System.out.println("card 2"+currentState);
    }


    private int damageCount = 0;


    @Override
    public void paintComponent(Graphics g) {
        // super.paintComponent(g);
        if (isMoving) {
            if (currentState == State.MOVING) {

                g.drawImage(sprites[aniIndex], (int) currentX, Game.gui.gameScreen.northPanel.yFloor - 128, 128, 128, null);
            }
                if (currentState == State.ATTACKING) {
                    g.drawImage(sprites2[attackIndex], (int) currentX, Game.gui.gameScreen.northPanel.yFloor - 128, 128, 128, null);
                }

        }
    }
}

