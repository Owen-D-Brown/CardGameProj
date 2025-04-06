package Entities;

import MainPackage.Config;
import Trinkets.Dagger;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class VampireCountess extends RangedEnemy {

    public VampireCountess() throws IOException {
        super("VampireCountess", 100, 6, 6, 6, 6, 128, 128, 0, 0, 5, 1, 6, 1, 6, 1, 8, 1, new VampireCountessAttack(), 5);
        this.attackSpeed = 4;
        rangedOrigin = new Point(35, 75);
        attackSpeed = 3;
    }

    @Override
    protected void addMapsToAnimations(String basePath) {
        //System.out.println(idleColCount+"  |  "+idleRowCount+"  |  "+spriteWidth+"  |  "+spriteHeight);
        animations.add(importSprites(basePath+"IdleMap.png", idleColCount, idleRowCount, 128, 128));
        animations.add(importSprites(basePath+"WalkMap.png", walkColCount, walkRowCount, 56, 77));
        animations.add(importSprites(basePath+"AttackMap.png", attackColCount, attackRowCount, 128, 128));
        animations.add(importSprites(basePath+"DeathMap.png", deathColCount, deathRowCount, 128, 128));
    }

    @Override
    public BufferedImage[] importSprites(String pathName, int cols, int rows,
                                         int spriteWidth, int spriteHeight) {
        BufferedImage image = loadImage(pathName);
        if (image == null) {
            return new BufferedImage[0];
        }
        BufferedImage[] sprites = new BufferedImage[cols * rows];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                int reversedX = image.getWidth() - (x + 1) * spriteWidth;

                sprites[y * cols + x] = image.getSubimage(
                        reversedX, y * spriteHeight, spriteWidth, spriteHeight
                );
            }
        }
        return sprites;
    }


    @Override
    protected void paintComponent(Graphics g) {
        // super.paintComponent(g);

        if(Config.hitboxesOn) {
            g.setColor(Color.red);
            g.drawRect(rangedOrigin.x, rangedOrigin.y, 10, 10);
        }

        // draw the hitbox (centered) and health bar
        // g.setColor(isTargeted ? Color.GREEN : Color.RED);

        int centeredX = (getWidth() - hitbox.width) / 2;
        int centeredY = (getHeight() - hitbox.height) / 2;

        if(Config.hitboxesOn) {
            g.setColor(Color.white);
            g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
            if(this.getBorder() == null) {
                setBorder(BorderFactory.createLineBorder(Color.white));
            }
        } else {
            this.setBorder(null);
        }

        // Center the health bar above the hitbox
        int barX = hitbox.x + (hitbox.width - healthBar.width) / 2;
        int barY = hitbox.y - healthBar.height - 5; // Offset by 5 pixels above the hitbox

// Draw background bar (red)
        g.setColor(Color.red);
        g.fillRect(barX, barY, healthBar.width, healthBar.height);

// Calculate and draw current health (green)
        int healthBarWidth = (int) ((double) currentHealth / maxHealth * healthBar.width);
        g.setColor(Color.green);
        g.fillRect(barX, barY, healthBarWidth, healthBar.height);



        if(state == State.WALKING) {
            g.drawImage(animations.get(1)[walkIndex], 0, 0, 56, 77, null);
        }
        if(state == State.IDLE) {
            g.drawImage(animations.get(0)[aniIndex], 0, 0, 128, 128, null);
        }

        if(state == State.ATTACKING) {
            g.drawImage(animations.get(2)[attackIndex], 0, 0, 128, 128, null);
        }

        if(state == State.DYING) {
            g.drawImage(animations.get(3)[deathIndex], 0, 0, 128, 128, null);
        }

    }

    @Override
    public void populateLootTable() {
        // this.lootTable.put("Gold", 500);
        this.lootTable.put("Dagger", new Dagger());
    }
}
