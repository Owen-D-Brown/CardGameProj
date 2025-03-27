package Entities;

import java.io.IOException;

public class BowBoneMan extends Enemy {

    public BowBoneMan() throws IOException {
        super(
                "Bow Bone Man",
                20, // Health (Even lower)
                22, // Higher attack for archer type
                1,  // Defense
                2,  // Agility (slightly faster)
                3,  // Speed
                100, 100,
                600, 500,
                6, 1, // Idle
                5, 1, // Walk
                5, 1, // Attack
                4, 1, // Death
                new GoblinAttackAnimation(), // Use a ranged animation class later
                10
        );
    }

    @Override
    public void populateLootTable() {
        this.lootTable.put("Bone Arrow", 0);
        this.lootTable.put("Gold", 250);
    }

}