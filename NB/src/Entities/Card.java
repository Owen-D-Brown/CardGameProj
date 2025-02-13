package Entities;

import GUI.GameplayPane;
import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Card extends JComponent implements MouseListener, MouseMotionListener {
    // Variables
    private Color color = Color.yellow; // Placeholder for card art
    public boolean primed = false; // Whether the card is in a card slot

    private Point intialGrab; // Stores where the mouse grabbed the card
    public Animation animation = new Fireball(0, 0, 700, 20);

    // Constructor
    public Card() {
        setSize(Config.cardSize); // Default card size.
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // Card effect logic
    public void effect() {
        Random rand = new Random();
        int dmg = rand.nextInt(10) + 5;
        Game.gui.gameScreen.northPanel.enemies.get(0).takeDamage(dmg);
    }

    // Dissolve animation for removing the card
    public void disolve(Runnable onComplete) {
        final int targetFPS = 20;
        final int delay = 1000 / targetFPS;
        Timer timer = new Timer(delay, null);

        ActionListener animationLis = evt -> {
            if (this.getWidth() <= 0 || this.getHeight() <= 0) {
                ((Timer) evt.getSource()).stop();
                this.setVisible(false);
                if (onComplete != null) {
                    animation.startAnimation();
                    onComplete.run();
                }
                return;
            }
            int newWidth = Math.max(0, this.getWidth() - 15);
            int newHeight = Math.max(0, this.getHeight() - 15);

            // Adjust x and y to keep the card centered
            int newX = this.getX() + (this.getWidth() - newWidth) / 2;
            int newY = this.getY() + (this.getHeight() - newHeight) / 2;

            this.setBounds(newX, newY, newWidth, newHeight);
            Game.gui.revalidate();
            Game.gui.repaint();
        };

        timer.addActionListener(animationLis);
        timer.start();
    }

    // Paint component
    public boolean initswitch = false;
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(this.color);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (primed && draggingArrow && arrowEnd != null) {
            g.setColor(Color.red);

            // Convert global positions to local coordinates
            Point start = SwingUtilities.convertPoint(this.getParent(), arrowStart, this);
            Point end = SwingUtilities.convertPoint(this.getParent(), arrowEnd, this);

            g.drawLine(start.x, start.y, end.x, end.y);
        }
    }

    public boolean draggingArrow = false;
    public Point arrowStart;
    public Point arrowEnd;

    // MouseListener & MouseMotionListener Logic
    @Override
    public void mousePressed(MouseEvent e) {
        intialGrab = e.getPoint();
        System.out.println("You have selected " + this);

        if (SwingUtilities.isRightMouseButton(e) && primed) {
            System.out.println("Started right-click drag from card.");
            draggingArrow = true;

            // Convert card center to global coordinates
            arrowStart = new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
            arrowEnd = arrowStart;

            getParent().repaint(); // Ensure parent repaints
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        GameplayPane.checkIntersect(this);

        if (SwingUtilities.isRightMouseButton(e) && draggingArrow) {
            System.out.println("Right-click drag released.");


            Point releasePoint = e.getLocationOnScreen();
            SwingUtilities.convertPointFromScreen(releasePoint, getParent());


            for (Enemy enemy : Game.gui.gameScreen.northPanel.enemies) {
                enemy.isTargeted = false;
            }


            draggingArrow = false;
            arrowEnd = null;
            getParent().repaint();
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {


        if (draggingArrow) {
            Point currentArrow = e.getLocationOnScreen();
            SwingUtilities.convertPointFromScreen(currentArrow, getParent());
            arrowEnd = currentArrow;

            getParent().repaint();
            for (Enemy enemy : Game.gui.gameScreen.northPanel.enemies) {
                if (enemy.getBounds().contains(currentArrow)) {
                    enemy.isTargeted = true;
                    enemy.repaint();

                    break;
                } else {
                    enemy.isTargeted = false;
                    enemy.repaint();
                }
            }
        }
        if (intialGrab == null) return;

        if (SwingUtilities.isLeftMouseButton(e)) {
            Point current = e.getLocationOnScreen();
            SwingUtilities.convertPointFromScreen(current, this.getParent());
            int x = current.x - intialGrab.x;
            int y = current.y - intialGrab.y;

            this.setLocation(x, y);
            this.getParent().repaint();
        }
    }


    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
