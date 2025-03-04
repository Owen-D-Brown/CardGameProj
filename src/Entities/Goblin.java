package Entities;

import Trinkets.Dagger;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Goblin extends Enemy {
    //Dodge chance increments of 15% will be + by agility base stat
    private static final double B_DODGE_CHANCE = 0.15;

    public Goblin() {

        super(20, 5, 1, 4, 2, 100, 100);
        this.aniSpeed = 5;
        // call import sprites
        BufferedImage[] idleSprites = importSprites(
                //filepath
                "/Resources/Goblin/GoblinMap.png", 8, 1, 400, 450
        );
        //check the sprites in the array
        if (idleSprites.length > 0) {
            //loaded frames from animations
            animations.add(idleSprites);
        } else {
            System.err.println("Error: Goblin sprites failed to load!");
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!animations.isEmpty()) {
            g.drawImage(animations.get(0)[aniIndex], 10, 10, 75, 75, null);
        }
    }
}
