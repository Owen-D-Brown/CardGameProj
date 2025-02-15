package Entities;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

public abstract class Enemy extends JComponent {


    //Basic Stat  with RPG implements
    protected int maxHealth;
    protected int currentHealth;
    protected int attackPower;
    protected int defense;
    protected int agility;
    protected int speed;


    //State tracking from Owens player class
    protected ArrayList<BufferedImage[]> animations = new ArrayList<>();
    protected int aniIndex = 0;
    protected int aniSpeed = 10;
    protected int aniCounter = 0;

    //Construcotr to set stats and size etc..
    public Enemy(int maxHealth, int attackPower, int defense, int agility, int speed) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.attackPower = attackPower;
        this.defense = defense;
        this.agility = agility;
        this.speed = speed;
        setSize(new Dimension(100, 150));
    }

//    Using Owen's load image method,
//    Trying to open the resource file as the path,
//    check if its found or not found

    public static BufferedImage loadImage(String path) {
        try (InputStream is = Enemy.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Error: Image not found at " + path);
                return null;
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
// Reading the spritesheet via the load image method,
//    if image is null return the empty array, or create an array of Buffered Image,
//    with col*row (for each row (0 & -1) and store it within the array)

    public static BufferedImage[] importSprites(String pathName, int cols, int rows, int spriteWidth, int spriteHeight) {
        BufferedImage image = loadImage(pathName);
        if (image == null) {
            return new BufferedImage[0];
        }
        BufferedImage[] sprites = new BufferedImage[cols * rows];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                sprites[y * cols + x] = image.getSubimage(x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight);
            }
        }
        return sprites;
    }
    //add to animate counter
    //if the counter is >= aniSpeed + frame index
    public void animate() {
        aniCounter++;
        if (aniCounter >= aniSpeed) {
            aniCounter = 0;
            aniIndex++;
            if (aniIndex >= animations.get(0).length) {
                aniIndex = 0;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        animate();
        if (!animations.isEmpty()) {
            g.drawImage(animations.get(0)[aniIndex], 10, 10, 75, 75, null);
        }
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

    public abstract String getEnemyType();
}
