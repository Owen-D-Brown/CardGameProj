package Entities.Animations;

import Entities.Enemies.Enemy;
import Entities.Player;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class XaxisAnimation extends Animation {

    protected int targetX, targetY;
    protected double currentX, currentY;
    protected int width, height;

    public XaxisAnimation(int width, int height, int row, int col, String path,int aniSpeed) throws IOException {
        super(path, width, height, col, row, aniSpeed);
        this.width = width;
        this.height = height;
    }

    public void initAnimation(int startX, int StartY, int finX, int finY) {
        this.currentX = startX;
        this.currentY = StartY;
        this.targetX = finX;
        this.targetY = finY;
    }

    public void initAnimation(Player player, Enemy enemy) {
        this.currentX = player.relativeX;
        this.currentY = player.relativeY;


        Point relativeOrigin = SwingUtilities.convertPoint(enemy, new Point(enemy.hitbox.x-(enemy.hitbox.width/2), enemy.hitbox.y), Game.gui.gameScreen.northPanel);


        this.targetX = relativeOrigin.x-(enemy.hitbox.width/2);
        this.targetY = player.relativeY;
        goingRight = true;
    }
    public boolean goingRight=false;
    public void initAnimation(Enemy e, Player p) {


        Point relativeOrigin = SwingUtilities.convertPoint(e, e.rangedOrigin, Game.gui.gameScreen.northPanel);
        e.relativeX = relativeOrigin.x;
        e.relativeY = relativeOrigin.y;

        this.currentX = e.relativeX;
        this.currentY = e.relativeY;




        this.targetX = p.relativeX;
        this.targetY = e.relativeY;

    }



    @Override
    public void updateAni() {
      //  System.out.println("updating ani");
       // super.updateAni();
        if(isMoving) {
            double speed = 0.2; // Moves 20% of the remaining distance each frame

            currentX += (targetX - currentX) * speed;
            currentY += (targetY - currentY) * speed;

            // Stop animation when close enough
            if (Math.abs(currentX - targetX) < 5 && Math.abs(currentY - targetY) < 5) {

                currentX = targetX;
                currentY = targetY;
//isMoving = false;
                currentState = State.IMPACT;
            }
        }

    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isMoving) {
            g.drawImage( sprites[aniIndex], (int) currentX, (int) currentY, 64*3, 32*3, null);
        }
    }

}
