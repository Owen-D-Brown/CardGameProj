package Entities;

import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

//changed to abstract to force enemy types to define getEnemyType()
public abstract class Enemy extends JComponent {

    public int maxHealth = 30;
    public int currentHealth = 30;
    protected int attackPower;
    protected int defense;
    protected int agility;
    protected int speed;
    private Rectangle hitbox = new Rectangle(10, 0, 24, 99);
    private Rectangle healthBar = new Rectangle(0, 0, 75, 10);

    public Enemy(int maxHealth, int attackPower, int defense, int agility, int speed) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth; // Start at full health
        this.attackPower = attackPower;
        this.defense = defense;
        this.agility = agility;
        this.speed = speed;
        setSize(new Dimension(100, 150));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.red);

        // Calculate the centered position for the hitbox
        int centeredX = (getWidth() - hitbox.width) / 2;
        int centeredY = (getHeight() - hitbox.height) / 2;

        // Update the hitbox's position (optional, if you need it for other logic)
        hitbox.setLocation(centeredX, centeredY);

        // Draw the centered hitbox
        g.fillRect(centeredX, centeredY-3, hitbox.width, hitbox.height);

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
        int reducedDamage = Math.max(damage - defense, 1);
        currentHealth = Math.max(currentHealth - reducedDamage, 0);

        if (currentHealth <= 0) {
            System.out.println(getEnemyType() + " has been defeated!");
        } else {
            System.out.println(getEnemyType() + " Health: " + currentHealth);
        }

        revalidate();
        repaint();
    }

    public void attack(Runnable onComplete) {
        Random rand = new Random();
        int dmg = rand.nextInt(attackPower) + 1; //rand damage between 1 and attack power
        Game.player.takeDamage(dmg);
        System.out.println(getEnemyType() + " attacks for " + dmg + " damage!");

        // Example animation call
        Animation ani = new Fireball(700, 20, 0, 0);
        AttackPlane.addAniToQue(ani);
        AttackPlane.animations.get(0).startAnimation();
        Game.gui.attackPlane.playAnimation(() -> {
            AttackPlane.animations.get(0).stopAnimation();
            onComplete.run();
        });
    }

    // Force subclasses to define their enemy type
    public String getEnemyType() {
        return null;
    }
}