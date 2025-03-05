package Entities;

import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.imageio.ImageIO;

public abstract class Enemy extends JComponent {

   // Rpg stats for each Enemy
    protected int maxHealth;
    public int currentHealth;
    protected int attackPower;  //attack power (base attack stat
    protected int defense;      // base defense stat
    protected int agility;      //base agility
    protected int speed;        // base speed


    protected ArrayList<BufferedImage[]> animations = new ArrayList<>();
    protected int aniIndex = 0;
    public int aniSpeed = 10;
    protected int aniCounter = 0;

    // Health bar & hitbox
    protected Rectangle hitbox;
    protected Rectangle healthBar = new Rectangle(0, 0, 75, 10);

    // Track if this Enemy is targeted
    protected boolean isTargeted = false;

    public int x, y;

    // Loot Related properties
    protected Map<String, Object> lootTable = new HashMap<String, Object>();

    public Rectangle getHitbox() {
        return this.hitbox;
    }


    // main constructor with all of the stats and specific size dimensions
    public Enemy(int maxHealth, int attackPower, int defense, int agility, int speed, int w, int h) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.attackPower = attackPower;
        this.defense = defense;
        this.agility = agility;
        this.speed = speed;
        setSize(new Dimension(w, h));
       // setBorder(BorderFactory.createLineBorder( Color.red, 3));
        populateLootTable();
        hitbox = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        System.out.println("TESTING "+hitbox);
    }

   // default enemy with stats
    public Enemy() {
        this(30, 5, 2, 1, 1, 50, 100);
    }

  // setting the target
    public void setTargeted(boolean targeted) {
        this.isTargeted = targeted;
        repaint();
    }

    public boolean isTargeted() {
        return this.isTargeted;
    }

   // loading the image from input path
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

    public static BufferedImage[] importSprites(String pathName, int cols, int rows,
                                                int spriteWidth, int spriteHeight) {
        BufferedImage image = loadImage(pathName);
        if (image == null) {
            return new BufferedImage[0];
        }
        BufferedImage[] sprites = new BufferedImage[cols * rows];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                sprites[y * cols + x] = image.getSubimage(
                        x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight
                );
            }
        }
        return sprites;
    }

    // animation speed and counter
    public void animate() {
        aniCounter++;
        if (aniCounter >= aniSpeed) {
            aniCounter = 0;
            aniIndex++;

            if (!animations.isEmpty() && aniIndex >= animations.get(0).length) {
                aniIndex = 0;
            }
        }
    }

  //custome paint
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // animate and draw sprite sheet if available
        animate();
        if (!animations.isEmpty() && animations.get(0).length > 0) {
           // g.drawImage(animations.get(0)[aniIndex], 10, 10, this.getWidth(), this.getHeight(), null);
        }

        // draw the hitbox (centered) and health bar
        g.setColor(isTargeted ? Color.GREEN : Color.RED);

        int centeredX = (getWidth() - hitbox.width) / 2;
        int centeredY = (getHeight() - hitbox.height) / 2;

        g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

        // health bar across the top
        int barX = (getWidth() - healthBar.width) / 2;
        healthBar.setLocation(barX, 0);
        g.fillRect(healthBar.x, healthBar.y, healthBar.width, healthBar.height);

        // fill portion of the bar for current health
        int healthBarWidth = (int)((double) currentHealth / maxHealth * healthBar.width);
        g.setColor(Color.GREEN);
        g.fillRect(healthBar.x, healthBar.y, healthBarWidth, healthBar.height);
    }

   // take damage mechanic
    public void takeDamage(int damage) {
        // reducing damage by defense
        int reducedDamage = Math.max(damage - defense, 1);
        currentHealth = Math.max(currentHealth - reducedDamage, 0);
        FloatingText.createEffect("-" + damage, this, Color.RED);
        System.out.println("visuals have started ------");
        if (currentHealth <= 0) {
            System.out.println(getEnemyType() + " has been defeated!");
        } else {
            System.out.println(getEnemyType() + " Health: " + currentHealth + "/" + maxHealth);
        }

        // Refresh UI
        revalidate();
        repaint();
    }

    //example attack method from fireball
    public void attack(Runnable onComplete) throws IOException {
        System.out.println("THE ENEMY IS ATTACKING. THE FOLLOWING HAVE BEEN PASSED TO Animation:\n  startX: "+this.getX()+"   |   finishX: "+Game.player.getX()+"\n  startY: "+this.getY()+"   |   finishY: "+Game.player.getY());
        Animation ani = new Fireball() {

        };
        Random rand = new Random();
        int dmg = rand.nextInt(10); // random between 0-9





        AttackPlane.addAniToQue(ani);

        // start of the animation (attack)
        AttackPlane.animations.get(0).startAnimation();
        Game.gui.gameScreen.northPanel.attackPlane.playAnimation(() -> {
            AttackPlane.animations.get(0).stopAnimation();
            Game.player.takeDamage(dmg);
            onComplete.run();
        });
    }

    //Get the enemy type from sub-classes
    public abstract String getEnemyType();

    public abstract void populateLootTable();

    public Map<String, Object> getLootTable() {
        return lootTable;
    }

    public Map.Entry<String, Object> generateLoot(){
        if(Config.debug)
            System.out.println("--* "+getClass()+".generateLoot() CALLED *--\n");

        Random rng = new Random();
        int x = rng.nextInt(lootTable.size());

        Iterator<Map.Entry<String, Object>> iterator = lootTable.entrySet().iterator();
        for (int i = 0; i < x; i++) {
            iterator.next(); // Skip to the desired index
        }
        if(Config.debug)
            System.out.println("--* "+getClass()+".generateLoot() FINISHED *--\n");

        return iterator.next();
    }
}
