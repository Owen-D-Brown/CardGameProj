package Entities;

import java.awt.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import MainPackage.Game;

public class Slime extends Enemy {

    private static final double Dissolve_chance = 0.10; //10% chance
    private static final int Dissolve_dam  = 5;

    public Slime() throws IOException {
        super("Slime", 25, 6, 6, 6, 6, 128, 128, 0, 0, 8, 1, 7, 1, 4, 1, 3, 1, new GoblinAttackAnimation(), 5);
        this.hitbox = new Rectangle(40, 100, 40, 30);

    }

    @Override
    protected void addMapsToAnimations(String basePath) {
        //System.out.println(idleColCount+"  |  "+idleRowCount+"  |  "+spriteWidth+"  |  "+spriteHeight);
        animations.add(importSprites(basePath+"IdleMap.png", idleColCount, idleRowCount, 128, 128));
        animations.add(importSprites(basePath+"WalkMap.png", walkColCount, walkRowCount, 128, 128));
        animations.add(importSprites(basePath+"AttackMap.png", attackColCount, attackRowCount, 128, 128));
        animations.add(importSprites(basePath+"DeathMap.png", deathColCount, deathRowCount, 128, 128));
    }




    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage); // Standard damage calculation

        // If Slime reaches 0 HP, check dissolve chance
        if (currentHealth <= 0) {
            dissolve();
        }
    }

    private void dissolve() {
        Random rand = new Random();
        if (rand.nextDouble() < Dissolve_chance) {
            System.out.println("The Slime dissolves and deals " + Dissolve_dam + " damage!");
            Game.player.takeDamage(Dissolve_dam); // Apply dissolve damage to player
        } else {
            System.out.println("The Slime collapses into a puddle.");
        }
    }

    @Override
    public void populateLootTable() {
        this.lootTable.put("Gold", 300);
        this.lootTable.put("Goo", 0);
    }




}
