package Entities;

import java.util.Random;

import MainPackage.Game;

public class Slime extends Enemy {

    private static final double Dissolve_chance = 0.10; //10% chance
    private static final int Dissolve_dam  = 5;

    public Slime() {
        super(35, 9, 4, 1, 1);
    }

    @Override
    public String getEnemyType() {
        return "Slime";
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
