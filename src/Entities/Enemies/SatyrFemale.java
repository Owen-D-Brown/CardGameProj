package Entities.Enemies;

import Entities.Animations.Fireball;
import Trinkets.Dagger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static Entities.Player.importSprites;
import static MainPackage.Tools.importSpritesCutFromRight;

public class SatyrFemale extends RangedEnemy {
    public SatyrFemale() throws IOException {
        super("SatyrFemale", 100, 6, 6, 6, 6, 128, 128, 128, 128, 7, 1, 12, 1, 4, 1, 4, 1, new Fireball(), 5);
        this.attackSpeed = 4;
        rangedOrigin = new Point(35, 75);
        attackSpeed = 3;
    }

    @Override
    protected void addMapsToAnimations(String basePath) {
        //System.out.println(idleColCount+"  |  "+idleRowCount+"  |  "+spriteWidth+"  |  "+spriteHeight);
        animations.add(importSpritesCutFromRight(basePath+"IdleMap.png", idleColCount, idleRowCount, 128, 128));
        animations.add(importSpritesCutFromRight(basePath+"WalkMap.png", walkColCount, walkRowCount, 56, 77));
        animations.add(importSpritesCutFromRight(basePath+"AttackMap.png", attackColCount, attackRowCount, 128, 128));
        animations.add(importSpritesCutFromRight(basePath+"DeathMap.png", deathColCount, deathRowCount, 128, 128));
    }





    @Override
    public void populateLootTable() {
        // this.lootTable.put("Gold", 500);
        this.lootTable.put("Dagger", new Dagger());
    }
}
