package Entities;

import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Enemy extends JComponent {

    public int maxHealth = 30;
    public int currentHealth = 30;
    private Rectangle hitbox = new Rectangle(10, 0, 24, 99);
    private Rectangle healthBar = new Rectangle(0, 0, 75, 10);

    public Enemy() {
        setSize(new Dimension(100, 150));
        //setBorder(BorderFactory.createLineBorder(Color.white));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.red);

        //Calculate the centered position for the hitbox
        int centeredX = (getWidth() - hitbox.width) / 2;
        int centeredY = (getHeight() - hitbox.height) / 2;

        //Update the hitbox's position (optional, if you need it for other logic)
        hitbox.setLocation(centeredX, centeredY);

        //Draw the centered hitbox
        g.fillRect(hitbox.x, hitbox.y-3, hitbox.width, hitbox.height);

        int barX = (getWidth() - healthBar.width) / 2;
        //int barY = (getHeight() - healthBar.height) / 2;
        healthBar.setLocation(barX, 0);
        g.fillRect(healthBar.x, (int) healthBar.getY(), healthBar.width, healthBar.height);

        //Calculate maxHealth bar width based on current maxHealth
        int healthBarWidth = (int) ((double) currentHealth / maxHealth * healthBar.width);

        //Draw the maxHealth bar (green for maxHealth)
        g.setColor(Color.green);
        g.fillRect(healthBar.x, healthBar.y, healthBarWidth, healthBar.height);
    }

    public void takeDamage(int damage) {
        if(currentHealth > maxHealth)
            currentHealth = maxHealth;

        currentHealth = currentHealth - damage;
        if(currentHealth <= 0) {
            System.out.println("Enemy dead");
        } else {
            System.out.println("maxHealth: "+currentHealth);
        }

        revalidate();
        repaint();
    }

    public void attack(Runnable onComplete) {
        Animation ani = new Fireball(700, 20, 0, 0);
        Random rand = new Random();
        int dmg = rand.nextInt(10);
        Game.player.takeDamage(dmg);
        AttackPlane.addAniToQue(ani);
        AttackPlane.animations.get(0).startAnimation();
        Game.gui.attackPlane.playAnimation(()-> {
            AttackPlane.animations.get(0).stopAnimation();
            onComplete.run();
        });


    }
}
