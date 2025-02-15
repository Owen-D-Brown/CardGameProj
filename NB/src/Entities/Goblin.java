package Entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Goblin extends Enemy {
    private static final double B_D_chance = 0.15; // 15% per agility
    private ArrayList<BufferedImage[]> animations = new ArrayList<>();
    private int aniIndex = 0;
    private int aniSpeed = 10; // set animation speed
    private int aniCounter = 0;

    public Goblin() {
        super(20, 5, 1, 4, 2);

        // Load and slice the idle animation
        animations.add(importSprites("Goblin/idleMap2.png", 8, 1, 255, 224));

    }

    @Override
    public String getEnemyType() {
        return "Goblin";
    }

    @Override
    public void takeDamage(int damage) {
        if (Math.random() < (this.agility * B_D_chance)) {
            System.out.println("Goblin dodged the attack!");
            return;
        }
        super.takeDamage(damage);
    }

    // Animate the idle animation
    public void animate() {
        aniCounter++;
        if (aniCounter >= aniSpeed) {
            aniCounter = 0;
            aniIndex++;
            if (aniIndex >= animations.get(0).length) aniIndex = 0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        animate();
        g.drawImage(animations.get(0)[aniIndex], 0, 0, getWidth(), getHeight(), null);
    }

}
