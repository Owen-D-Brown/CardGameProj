package Entities;

import MainPackage.Game;

import java.io.IOException;

public class IceBlast extends XaxisAnimation {

    public IceBlast() throws IOException {
        super(64, 64, 1, 11, "/Resources/IceBlast.png", 2);
        this.duration = 2000;
    }



    @Override
    public void updateAni() {

        if(isMoving && aniIndex != 6) {
            aniCounter++;
            if (aniCounter >= aniSpeed) {
                aniCounter = 0;
                aniIndex++;

                if (sprites.length > 0 && aniIndex >= sprites.length) {
                    aniIndex = 0;
                }
            }
        }

            if(aniIndex == 6) {
                double speed = 0.1; // Moves 20% of the remaining distance each frame

                currentX += (targetX - currentX) * speed;
                currentY += (targetY - currentY) * speed;
            }
            if(aniIndex == 6 && currentX > targetX - 100) {
                aniIndex++;
            }


            // Stop animation when close enough
            if (Math.abs(currentX - targetX) < 5 && Math.abs(currentY - targetY) < 5) {
                currentX = targetX;
                currentY = targetY;
                isMoving = false;
            }

        Game.gui.gameScreen.northPanel.attackPlane.repaint();
    }

}
