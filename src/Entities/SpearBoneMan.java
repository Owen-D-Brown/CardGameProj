package Entities;

import java.io.IOException;

public class SpearBoneMan extends Enemy {

    public SpearBoneMan() throws IOException {
        super(
                "Spear Bone Man",
                25,  // Health
                18,  // Attack
                2,   // Defense
                1,   // Agility
                2,   // Speed
                64, 128, // Width, Height based on your sprite resolution
                600, 500, // Starting position
                7, 1,  // Idle: 6 cols, 1 row
                7, 1,  // Walk: 6 cols, 1 row
                4, 1,  // Attack: 4 cols, 1 row (Attack_1)
                5, 1,  // Dead: 6 cols, 1 row
                new SpearBoneManAttackAnimation(),
                10 // Weight
        );
        addMapsToAnimations("/Resources/SpearBoneMan/");
    }

    @Override
    protected void addMapsToAnimations(String basePath) {
        // Change basePath passed in the constructor to this:
        animations.add(importSprites("/Resources/SpearBoneMan/SpearIdle.png", idleColCount, idleRowCount, 128, 82));
        animations.add(importSprites("/Resources/SpearBoneMan/SpearWalk.png", walkColCount, walkRowCount, 128, 92));
        animations.add(importSprites("/Resources/SpearBoneMan/SpearAttack_1.png", attackColCount, attackRowCount, 128, 52));
        animations.add(importSprites("/Resources/SpearBoneMan/SpearDead.png", deathColCount, deathRowCount, 107, 82));
    }

    @Override
    public void populateLootTable() {
        lootTable.put("Gold", 30); // Drop 30 gold

    }

}