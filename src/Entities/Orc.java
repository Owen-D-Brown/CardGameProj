package Entities;

import java.awt.*;
import java.io.IOException;

public class Orc extends Enemy {

    private boolean isEnraged = false; // Tracks if the Orc has entered rage mode

    public Orc() throws IOException {
        super("Orc", 30, 10, 5, 2, 1, 96, 96, 96, 96, 5,1, 7,1, 4,1, 4,1, new GoblinAttackAnimation(), 10); // (HP, Attack, Defense, Agility, Speed, Weight)
        this.hitbox = new Rectangle(30, 30, 50, 60);
    }



    @Override
    protected void addMapsToAnimations(String basePath) {
        //System.out.println(idleColCount+"  |  "+idleRowCount+"  |  "+spriteWidth+"  |  "+spriteHeight);
        animations.add(importSprites(basePath+"IdleMap.png", idleColCount, idleRowCount, 96, 96));
        animations.add(importSprites(basePath+"WalkMap.png", walkColCount, walkRowCount, 96, 96));
        animations.add(importSprites(basePath+"AttackMap.png", attackColCount, attackRowCount, 96, 96));
        animations.add(importSprites(basePath+"DeathMap.png", deathColCount, deathRowCount, 96, 96));
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage); // Take damage normally

        // If health drops to 14 or less and rage hasn't triggered yet
        if (currentHealth <= 14 && !isEnraged) {
            isEnraged = true; // Prevents multiple rage triggers
            attackPower += 20; // Boost attack power by 20
            System.out.println("Orc enters a berserk rage! Attack power increased to " + attackPower);
        }
    }
    @Override
    public void populateLootTable() {
        this.lootTable.put("Gold", 400);
        this.lootTable.put("Orc's Tooth", 0);
    }


}
