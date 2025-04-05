package Entities;

import MainPackage.Config;
import MainPackage.Game;

import java.awt.*;
import java.io.IOException;

public class SatyrCardAttackAnimation extends XaxisAnimation {
    public SatyrCardAttackAnimation() throws IOException {
        super(128, 128, 1, 12, "../Resources/SatyrCharge/sprites.png", 2);
    }

    @Override
    public void updateAni() {
      //  System.out.println("outside is Moving");
        if (isMoving) {
            aniCounter++;

            if (aniCounter >= aniSpeed) {
                aniCounter = 0;
                aniIndex++;

                if (sprites.length > 0 && aniIndex >= sprites.length) {
                    aniIndex = 0;
                }
            }
           // System.out.println("inside is Moving");
            double speed = 0.2;

            // Move the projectile
            currentX += (50) * speed;
//currentY += (200) * speed;

            // Check collision with all enemies
            Rectangle animationBounds = new Rectangle((int) currentX, (int) currentY, width, height); // Adjust width/height accordingly

            for (Enemy enemy : Game.gui.gameScreen.northPanel.enemies) {
                Rectangle enemyBounds = enemy.getBounds(); // Assuming this exists

                if (animationBounds.intersects(enemyBounds)) {
                    damageCount++;
                    if(damageCount>=2) {
                        enemy.takeDamage(1); // however you're applying damage
                        damageCount = 0;
                    }
                }
            }

            // Optional: off-screen cleanup
            if (currentX > Config.frameSize.width + 20) {
                currentState = State.IMPACT;
            }
        }
    }

    private int damageCount = 0;


    @Override
    public void paintComponent(Graphics g) {
       // super.paintComponent(g);
        if (isMoving) {
            g.drawImage( sprites[aniIndex], (int) currentX, Game.gui.gameScreen.northPanel.yFloor-128, 128, 128, null);
        }
    }
}
