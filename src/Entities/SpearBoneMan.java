package Entities;

import MainPackage.Config;

import java.awt.*;
import java.io.IOException;

public class SpearBoneMan extends Enemy {

    // Frame and sprite settings
    private static final int FRAME_WIDTH = 128;
    private static final int FRAME_HEIGHT = 92;
    private static final int SCALED_WIDTH = (int)(FRAME_WIDTH * Config.scaleFactor);
    private static final int SCALED_HEIGHT = (int)(FRAME_HEIGHT * Config.scaleFactor);

    public SpearBoneMan() throws IOException {
        super(
                "SpearBoneMan",
                25,  // Health
                18,  // Attack
                2,   // Defense
                1,   // Agility
                2,   // Speed
                SCALED_WIDTH, SCALED_HEIGHT,   // Component size
                FRAME_WIDTH, FRAME_HEIGHT,     // Sprite frame size
                7, 1,  // Idle
                7, 1,  // Walk
                4, 1,  // Attack
                5, 1,  // Death
                new SpearBoneManAttackAnimation(),
                10     // Weight
        );

        // Setup hitbox centered and scaled
        int hitboxW = (int)(50 * Config.scaleFactor);
        int hitboxH = (int)(70 * Config.scaleFactor);
        int hitboxX = (SCALED_WIDTH - hitboxW) / 2;
        int hitboxY = (SCALED_HEIGHT - hitboxH) / 2;
        this.hitbox = new Rectangle(hitboxX, hitboxY, hitboxW, hitboxH);

        // Load animations
        addMapsToAnimations("/Resources/SpearBoneMan/");
    }

    @Override
    protected void addMapsToAnimations(String basePath) {
        animations.add(importSprites("/Resources/SpearBoneMan/SpearIdle.png", idleColCount, idleRowCount, 128, 82));
        animations.add(importSprites("/Resources/SpearBoneMan/SpearWalk.png", walkColCount, walkRowCount, 128, 92));
        animations.add(importSprites("/Resources/SpearBoneMan/SpearAttack_1.png", attackColCount, attackRowCount, 128, 52));
        animations.add(importSprites("/Resources/SpearBoneMan/SpearDead.png", deathColCount, deathRowCount, 107, 82));
    }

    @Override
    public void populateLootTable() {
        lootTable.put("Gold", 30);
    }
}
