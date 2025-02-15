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


    // load image to open the file path
    public static BufferedImage loadImage(String path) {
        try (InputStream is = Enemy.class.getResourceAsStream(path)) {
            if (is == null) { // if the files exists....
                System.err.println("Error: Image not found at " + path);
                return null;
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // split the sprite sheet into frames
    public static BufferedImage[] importSprites(String pathName, int cols, int rows, int spriteWidth, int spriteHeight) {
        BufferedImage image = loadImage(pathName);
        if (image == null) {
            return new BufferedImage[0]; // if it fails to laod
        }
            //Array of sprites col * row
        BufferedImage[] sprites = new BufferedImage[cols * rows];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                sprites[y * cols + x] = image.getSubimage(x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight);
            }
        }
        return sprites;
    }

    // handle the animation
    public void animate() {
        aniCounter++;
        //ani counter >= ani speed reset the counter and add to the index
        //update frame index
        if (aniCounter >= aniSpeed) {
            aniCounter = 0;
            aniIndex++;
        }
        if (aniIndex >= animations.get(0).length) {
            aniIndex = 0;
        }
    }

    // draw the animation
    public void drawAni(Graphics g, int x, int y) {
        animate();
        g.drawImage(animations.get(0)[aniIndex], x, y, null);
    }





    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw animation instead of a hitbox
        int spriteX = (getWidth() - 50) / 2; // Centered sprite
        int spriteY = (getHeight() - 70) / 2;

        drawAni(g, spriteX, spriteY);

        // Draw health bar
        int barX = (getWidth() - healthBar.width) / 2;
        healthBar.setLocation(barX, 0);
        g.setColor(Color.white);
        g.fillRect(barX, 0, healthBar.width, healthBar.height);

        // Calculate health bar width
        int healthBarWidth = (int) ((double) currentHealth / maxHealth * healthBar.width);
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