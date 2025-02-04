package Entities;

import Inputs.MouseHandler;
import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

public class Card extends JComponent implements MouseListener, MouseMotionListener {

    //Variables
    private Color color = Color.yellow; //Placeholder for card art
    public boolean primed = false; //Whether or not the card is currently in a card slot.

    //Animations are JComponents that have methods to move their co-ords. These are placed in the AttackPlane and ran.
    public Animation animation = new Fireball(0, 0, 700, 20);

    //Constructor
    public Card() {

        //Initializing this component
        setSize(Config.cardSize);//Default card size.
        MouseHandler mouseHandler = new MouseHandler(); //Controls for card movement - move to this class eventually.
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }

    //The card's effect.
    public void effect() {

        //The effect on the enemy
        Random rand = new Random();
        int dmg = rand.nextInt(10) + 5;
        Game.gui.northPanel.enemies.get(0).takeDamage(dmg);


        //Triggering the card's disolve animation
        //disolve();
    }

    //Flow -> Disolve() on all cards, pass animations to plane in disolve. - dont remove cards yet, just make them non visible. Animations play out, with effect
    //after animation. then the cards are removed from play, and the game progresses.
    //Animation after the card has been resolved. It disolves and removes the card from play.
    public void disolve(Runnable onComplete) {
        final int targetFPS = 20;
        final int delay = 1000 / targetFPS;

        Timer timer = new Timer(delay, null);

        ActionListener animationLis = evt -> {
            if (this.getWidth() <= 0 || this.getHeight() <= 0) {
                ((Timer) evt.getSource()).stop();
                this.setVisible(false);

                // Call onComplete when dissolve is finished
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
        //

    public boolean initswitch = false;
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(this.color);
        if(!initswitch) {
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        } else {
            g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
            initswitch = true;
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
