package Entities;

import javax.swing.*;
import java.awt.*;

public class Animation extends JComponent {
    private int circleX, circleY;
    private int targetX, targetY;
    private boolean isMoving = false;

    public Animation(int startX, int startY, int targetX, int targetY) {
        this.circleX = startX;
        this.circleY = startY;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public void startAnimation() {
        isMoving = true;
    }

    public void updateAni() {
        if (isMoving) {
            int stepX = (targetX - circleX) / 10;
            int stepY = (targetY - circleY) / 10;
            circleX += stepX;
            circleY += stepY;

            if (Math.abs(circleX - targetX) < 1 && Math.abs(circleY - targetY) < 1) {
                isMoving = false; // Stop the animation when target is reached
            }
        }
    }

    public void paint(Graphics g) {

        if (isMoving) {
            System.out.println("Animation started");
            g.setColor(Color.orange);
            g.fillOval(circleX, circleY, 40, 40); // Circle moving animation
        }
    }
}
