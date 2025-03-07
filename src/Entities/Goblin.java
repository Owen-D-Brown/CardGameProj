package Entities;

import MainPackage.Game;
import Trinkets.Dagger;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Goblin extends Enemy {
    //Dodge chance increments of 15% will be + by agility base stat
    private static final double B_DODGE_CHANCE = 0.15;
    protected GoblinAttackAnimation attackAnimation;
    protected enum State {IDLE, WALKING};
    State state = State.IDLE;

    public Goblin() throws IOException {

        super( 20, 5, 1, 4, 2, 100, 100);
        this.aniSpeed = 5;
        this.attackAnimation = new GoblinAttackAnimation();
        // call import sprites
        BufferedImage[] idleSprites = importSprites(
                //filepath
                "/Resources/Goblin/GoblinMap.png", 8, 1, 600, 500
        );
        BufferedImage[] walkikngSprites = importSprites(
                //filepath
                "/Resources/Goblin/GoblinWalk.png", 6, 1, 600, 500
        );
        //check the sprites in the array
        if (idleSprites.length > 0) {
            //loaded frames from animations
            animations.add(idleSprites);
            animations.add(walkikngSprites);
        } else {
            System.err.println("Error: Goblin sprites failed to load!");
        }

    }

    protected int walkIndex = 0;
    protected int walkCounter = 0;
    protected int walkSpeed = 6;
    @Override
    public void animate() {
        aniCounter++;
        if (aniCounter >= aniSpeed) {
            aniCounter = 0;
            aniIndex++;

            if (!animations.isEmpty() && aniIndex >= animations.get(0).length) {
                aniIndex = 0;
            }
        }
        if(state == State.WALKING) {

            walkCounter++;
            if (walkCounter >= walkSpeed) {
                walkCounter = 0;
                walkIndex++;

                if (!animations.isEmpty() && walkIndex >= animations.get(1).length) {
                    walkIndex = 0;
                }
            }
            Rectangle temp = this.getBounds();
            this.setBounds(temp.x -=9, temp.y, temp.width, temp.height);
            currentX = temp.x;
        }
    }




    //return enemy from getEnemy method and return the goblin as a string
    @Override
    public String getEnemyType() {
        return "Goblin";
    }

    @Override
    public void populateLootTable() {
       // this.lootTable.put("Gold", 500);
        this.lootTable.put("Dagger", new Dagger());
    }

    // Dodge mechanic and how it works
    // if agility is 4 then 15% * 4 = 60% Dodge Chance
    //Based on if rand < agility
    @Override
    public void takeDamage(int damage) {
        if (Math.random() < (this.agility * B_DODGE_CHANCE)) {
            System.out.println("Goblin dodged the attack!");
            FloatingText.createEffect("DODGE", this, Color.BLUE);
            return;
        }
        super.takeDamage(damage);
    }

    public int currentX, currentY = 0;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
   


        if(state == State.WALKING) {
            g.drawImage(animations.get(1)[walkIndex], 0, 0, 75, 75, null);


        }
        if(state == State.IDLE) {
            g.drawImage(animations.get(0)[aniIndex], 0, 0, 75, 75, null);
        }

    }


    @Override
    public void attack(Runnable onComplete, int x, int y) throws IOException {
        // System.out.println("THE ENEMY IS ATTACKING. THE FOLLOWING HAVE BEEN PASSED TO Animation:\n  startX: "+this.getX()+"   |   finishX: "+Game.player.getX()+"\n  startY: "+this.getY()+"   |   finishY: "+Game.player.getY());

        Random rand = new Random();
        int dmg = rand.nextInt(10); // random between 0-9

        this.state = State.WALKING;
        if (currentX < -100) {
            this.state = State.IDLE;
            attackAnimation.placeAnimation(x, y);
            AttackPlane.addAniToQue(attackAnimation);

            // start of the animation (attack)
            AttackPlane.animations.get(0).startAnimation();
            Game.gui.gameScreen.northPanel.attackPlane.playAnimation(() -> {
                AttackPlane.animations.get(0).stopAnimation();
                Game.player.takeDamage(dmg);
                onComplete.run();
            });
        }
    }
}
