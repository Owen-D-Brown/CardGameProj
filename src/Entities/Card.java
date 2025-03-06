package Entities;

import GUI.GameplayPane;
import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public abstract class Card extends JComponent implements MouseListener, MouseMotionListener {
    // Variables
    public boolean primed = false; // Whether the card is in a card slot
    public int initX, initY;
    private Point intialGrab; // Stores where the mouse grabbed the card
    public Animation animation;
    protected BufferedImage image;


    // Constructor
    public Card(String imagePath) {
        setSize(Config.cardSize); // Default card size.
        addMouseListener(this);
        addMouseMotionListener(this);
        this.image = loadImage(imagePath);
        initX = this.getX();
        initY = this.getY();
    }

    // abstract card effect method, implemented by card subclasses
    public abstract void effect();

    public abstract void initCardAniBounds() throws IOException;



    // Image loader like one found in player class
    public static BufferedImage loadImage(String path) {
        try (InputStream is = Card.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Error: Image not found at " + path);
                return null;
            }
            System.out.println("image in load: "+is);
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
            Game.gui.gameScreen.glassPane.revalidate();
            Game.gui.gameScreen.glassPane.repaint();
        };

        timer.addActionListener(animationLis);
        timer.start();
    }

    // Paint component
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//clears old drawings and renders cards
        if (image != null) { //check if image is available, draw it if so or use placeholder
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this); //draw card image
        } else {
            g.setColor(Color.YELLOW); // Placeholder color if image is missing
            g.fillRect(0, 0, getWidth(), getHeight());
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
           // this.getParent().repaint();
        }
    }


    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
