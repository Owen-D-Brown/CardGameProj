package Entities;

import MainPackage.Config;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Player extends JComponent {
    public ArrayList<Card> cards = new ArrayList<>();
    public int maxHealth = 100;
    public int currentHealth = 100;

    private Rectangle healthBar = new Rectangle(0, 0, 150, 20);

    public Player() {
        setSize(new Dimension((int) (Config.frameSize.width * 0.3), (int) (Config.frameSize.height * 0.3)));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        int centeredX = (getWidth() - getWidth() / 2) / 2;  // Center horizontally
        int centeredY = (getHeight() - getHeight() / 2) / 2; // Center vertically
        g.fillOval(centeredX, centeredY, getWidth()/2, getHeight()/2);

        int barX = (getWidth() - healthBar.width) / 2;
        //int barY = (getHeight() - healthBar.height) / 2;
        healthBar.setLocation(barX, 0);
        g.fillRect(barX, 40, healthBar.width, healthBar.height);

        // Calculate maxHealth bar width based on current maxHealth
        int healthBarWidth = (int) ((double) currentHealth / maxHealth * healthBar.width);

        // Draw the maxHealth bar (green for maxHealth)
        g.setColor(Color.green);
        g.fillRect(barX, 40, healthBarWidth, healthBar.height);
    }

    public void takeDamage(int damage) {
        if(currentHealth > maxHealth)
            currentHealth = maxHealth;

        currentHealth = currentHealth - damage;
        if(currentHealth <= 0) {
            System.out.println("youre dead");
        } else {
            System.out.println("maxHealth: "+currentHealth);
        }

        revalidate();
        repaint();
    }

}
