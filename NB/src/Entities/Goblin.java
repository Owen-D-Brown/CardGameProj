package Entities;

import java.util.Random;

public class Goblin extends Enemy {

    private static final double B_D_chance = 0.15; //15% per agility

    public Goblin() {
        super(20, 5, 1, 4, 2); //health, attack, defense, agility, speed
    }

    @Override
    public String getEnemyType() {
        return "Goblin";
    }

    @Override
    public void takeDamage(int damage) {
        Random rand = new Random();

        //if agility = 2 for example dodge chance is 30%
        double D_chance = this.agility * B_D_chance;

        if (rand.nextDouble() < D_chance) {
            System.out.println("Goblin dodged the attack with agility!");
            return; // No damage taken
        }
        super.takeDamage(damage); // Call the regular method if dodge fails
    }
}
