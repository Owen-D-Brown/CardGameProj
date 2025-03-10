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
    protected BufferedImage[] goblinAttackStateAni;
    protected enum State {IDLE, WALKING, ATTACKING};
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
        BufferedImage[] goblinAttackStateAni = importSprites(
                //filepath
                "/Resources/Goblin/GoblinAttack.png", 6, 1, 600, 500
        );
        //check the sprites in the array
        if (idleSprites.length > 0) {
            //loaded frames from animations
            animations.add(idleSprites);
            animations.add(walkikngSprites);
            animations.add(goblinAttackStateAni);
        } else {
            System.err.println("Error: Goblin sprites failed to load!");
        }

    }


    protected int walkIndex = 0;
    protected int walkCounter = 0;
    protected int walkSpeed = 6;
    protected int attackIndex = 0;
    protected int attackCounter = 0;
    protected int attackSpeed = 6;
    int startX = 0;
    protected boolean startXset = false;
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
            if(!startXset) {
                startBounds = this.getBounds();
                startXset = true;
            }
            this.setBounds(temp.x -=9, temp.y, temp.width, temp.height);
            System.out.println("LOOK HERE PLEASEEEEEEEEE  "+this.getBounds());

            currentX = temp.x;
        }

        if(state == State.ATTACKING) {
            attackCounter++;
            if (attackCounter >= attackSpeed) {
                attackCounter = 0;
                attackIndex++;

                if (!animations.isEmpty() && attackIndex >= animations.get(2).length) {
                    attackIndex = 0;
                }
            }
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

        if(state == State.ATTACKING) {
            g.drawImage(animations.get(2)[attackIndex], 0, 0, 75, 75, null);
        }

    }

    //Resolve next enemy calls enemy.attack. This is the method that defines the difference
    //Between static and dynamic parts of the engine. Here, timing must be introduced to stop
    //The recursive flow until the animation is finished playing.

    @Override
    public void attack(Runnable onComplete, int x, int y) throws IOException {
        // Get random damage
        Random rand = new Random();
        int dmg = rand.nextInt(10); // Random between 0-9

        // Reset attack animation index before starting
        attackIndex = 0;

        // Set state to walking
        this.state = State.WALKING;

        // Timer to monitor enemy reaching attack position
        Timer movementTimer = new Timer(16, e -> {
            if (currentX >= 700)
                return; // Keep waiting until it reaches threshold

            ((Timer) e.getSource()).stop(); // Stop movement check
            this.state = State.ATTACKING;  // Start attack animation

            // Ensure attack animation fully loops before playing AttackPlane animation
            Timer waitForAttack = new Timer(16, e1 -> {
                if (attackIndex < animations.get(2).length - 1)
                    return;  // Keep waiting until attack animation loop finishes

                ((Timer) e1.getSource()).stop(); // Stop waiting

                // Reset animation state for next attack
                this.state = State.IDLE;
                attackIndex = 0;  // Ensure attackIndex is reset

                // Only NOW do we place the attack effect & play AttackPlane animation
                attackAnimation.placeAnimation(x, y);
                AttackPlane.addAniToQue(attackAnimation);
                AttackPlane.animations.get(0).startAnimation();

                // Play AttackPlane animation **ONLY AFTER attack animation finishes**
                Game.gui.gameScreen.northPanel.attackPlane.playAnimation(() -> {
                    AttackPlane.animations.get(0).stopAnimation(); // Stop animation
                    Game.player.takeDamage(dmg);  // Apply damage

                    // Reset position & state
                    this.state = State.IDLE;
                    this.setBounds(startBounds);
                    currentX = this.getBounds().x;

                    // NOW the game flow continues
                    onComplete.run();
                });
            });

            waitForAttack.start(); // Start waiting for attack animation loop to finish
        });

        movementTimer.start(); // Start movement monitoring
    }


}


