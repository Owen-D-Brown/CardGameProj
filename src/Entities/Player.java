package Entities;

import MainPackage.Config;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Player extends JComponent {
    public ArrayList<Card> cards = new ArrayList<>();
    public ArrayList<Card> hand = new ArrayList<>();
    public ArrayList<Card> discard = new ArrayList<>();
    public int maxHealth = 100;
    public int currentHealth = 100;

    private Rectangle hitbox = new Rectangle(10, 0, 24, 99);
    private Rectangle healthBar = new Rectangle(0, 0, 75, 10);

    public Player() {
        setSize(new Dimension(100, 200));
        //setBorder(BorderFactory.createLineBorder(Color.BLACK));
        for(int i = 0; i<10; i++) {
            cards.add(new Card());
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // Calculate the centered position for the hitbox
        int centeredX = (getWidth() - hitbox.width) / 2;


        // Update the hitbox's position (optional, if you need it for other logic)
        hitbox.setLocation(centeredX, 25);

        g.setColor(Color.white);
        // Draw the centered hitbox
        g.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

        int barX = (getWidth() - healthBar.width) / 2;
        //int barY = (getHeight() - healthBar.height) / 2;
        healthBar.setLocation(barX, 0);
        g.fillRect(barX, 0, healthBar.width, healthBar.height);

        // Calculate maxHealth bar width based on current maxHealth
        int healthBarWidth = (int) ((double) currentHealth / maxHealth * healthBar.width);

        // Draw the maxHealth bar (green for maxHealth)
        g.setColor(Color.green);
        g.fillRect(healthBar.x, healthBar.y, healthBarWidth, healthBar.height);
    }

    public void takeDamage(int damage) {
        if(currentHealth > maxHealth)
            currentHealth = maxHealth;

        currentHealth = currentHealth - damage;
        if(currentHealth <= 0) {
            System.out.println("youre dead");
        } else {
            System.out.println("current health: "+currentHealth);
        }

        revalidate();
        repaint();
    }

}
