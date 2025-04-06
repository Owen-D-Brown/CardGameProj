package Entities;

import MainPackage.Game;
import Trinkets.Dagger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Goblin extends Enemy {

    //Dodge chance increments of 15% will be + by agility base stat
    private static final double B_DODGE_CHANCE = 0.15;

    public Goblin() throws IOException {

        super("Goblin", 20, 5, 1, 0, 2, 72, 72, 600, 500, 8, 1, 6, 1, 6, 1, 6, 1, new GoblinAttackAnimation(), 5);
        this.hitbox = new Rectangle(20, 10, 40, 60);



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
}


