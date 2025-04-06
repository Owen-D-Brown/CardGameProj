package Entities;

import java.io.IOException;

public class Orc2 extends Enemy {

    private boolean isEnraged = false; // Tracks if the Orc has entered rage mode

    public Orc2() throws IOException {
        super("Orc", 50, 15, 5, 2, 1, 83, 73, 63, 63, 5,1, 7,1, 4,1, 4,1, new GoblinAttackAnimation(), 20); // (HP, Attack, Defense, Agility, Speed, Weight)
    }



    @Override
    protected void addMapsToAnimations(String basePath) {
        //System.out.println(idleColCount+"  |  "+idleRowCount+"  |  "+spriteWidth+"  |  "+spriteHeight);
        animations.add(importSprites(basePath+"IdleMap.png", idleColCount, idleRowCount, 64, 63));
        animations.add(importSprites(basePath+"WalkMap.png", walkColCount, walkRowCount, 63, 63));
        animations.add(importSprites(basePath+"AttackMap.png", attackColCount, attackRowCount, 83, 63));
        animations.add(importSprites(basePath+"DeathMap.png", deathColCount, deathRowCount, 70, 63));
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