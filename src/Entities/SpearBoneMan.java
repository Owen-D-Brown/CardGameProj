package Entities;

import java.io.IOException;

public class SpearBoneMan extends Enemy {

    public SpearBoneMan() throws IOException {
        super(
                "Spear Bone Man",
                25, // Health (Low)
                18, // Attack (High)
                2,  // Defense
                1,  // Agility
                2,  // Speed
                100, 100, // Width, Height (adjust as needed)
                600, 500, // X, Y Position (default placeholders)
                6, 1, // Idle cols, rows
                5, 1, // Walk cols, rows
                5, 1, // Attack cols, rows
                4, 1, // Death cols, rows
                new SpearBoneManAttackAnimation(), // Placeholder animation logic
                10 // Weight
        );
    }

    @Override
    protected void addMapsToAnimations(String basePath) {
        animations.add(importSprites(basePath + "Idle.png", idleColCount, idleRowCount, 64, 64));
        animations.add(importSprites(basePath + "WalkMap.png", walkColCount, walkRowCount, 64, 64));
        animations.add(importSprites(basePath + "AttackMap.png", attackColCount, attackRowCount, 64, 64));
        animations.add(importSprites(basePath + "DeathMap.png", deathColCount, deathRowCount, 64, 64));
    }

    @Override
    public void populateLootTable() {
        this.lootTable.put("Bone Shard", 0); // Drop item + chance
        this.lootTable.put("Gold", 250);
    }

}