package Entities;

import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;

public class Animation extends JComponent {

    private int targetX, targetY;
    private boolean isMoving = false;
    private double circleX, circleY;
    public int duration = 1200;  // 2 seconds in milliseconds
    public int FPS = 20;   // 20 updates per second
    public int interval = 1000 / FPS;
    boolean test= false;

    public Animation(int startX, int startY, int targetX, int targetY) {
        this.circleX = startX;
        this.circleY = startY;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public void startAnimation() {
        isMoving = true;
    }

    public void stopAnimation() {
        isMoving = false;
    }

    public void updateAni() {
        if (isMoving) {
            double speed = 0.1; // Moves 20% of the remaining distance each frame

            circleX += (targetX - circleX) * speed;
            circleY += (targetY - circleY) * speed;

            // Stop animation when close enough
            if (Math.abs(circleX - targetX) < 5 && Math.abs(circleY - targetY) < 5) {
                circleX = targetX;
                circleY = targetY;
                isMoving = false;
            }
        }
        repaint();
    }

    public void runAnimation() {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isMoving) {
            //System.out.println("Animation started");
            g.setColor(Color.orange);
            g.fillOval((int)circleX, (int)circleY, 40, 40); // Circle moving animation
           // System.out.println("In the paint method of Animation. X = "+circleX);

        }
    }
}
