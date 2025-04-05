package Entities;

import GUI.GameplayPane;
import MainPackage.Config;
import MainPackage.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Player extends JComponent {
    public ArrayList<Card> cards = new ArrayList<>();
    public ArrayList<Card> hand = new ArrayList<>();
    public ArrayList<Card> discard = new ArrayList<>();
    protected ArrayList<Map.Entry> trinkets = new ArrayList<>();

    public int getGold() {
        return gold;
    }

    public ArrayList<Map.Entry> getTrinkets() {
        return trinkets;
    }
    public enum State {IDLE, WALKING, ATTACKING, DYING}

    protected int gold = 0;
    public int maxHealth = 100;
    public int currentHealth = 50;

    private Rectangle hitbox = new Rectangle(25, 50, 50, 70);
    private Rectangle healthBar = new Rectangle(0, 0, 75, 10);
    private ArrayList<BufferedImage[]> animations = new ArrayList<>();

    public Point rangedOrigin;
    public int relativeX;
    public int relativeY;

    public Player() throws IOException {
        setSize(new Dimension(128, 128));
        if(Config.hitboxesOn)
            //setBorder(BorderFactory.createLineBorder(Color.white));
        for(int i = 0; i<3; i++) {
            //cards.add(new Firebolt());
           // cards.add(new IceBurst());
            //cards.add(new Potion_Card());
            //cards.add(new Satyr_MCard());
            //cards.add(new LastEmbrace_Card());
          //  cards.add(new Satyr_MCard());
           // cards.add(new Vampire_MCard());
            cards.add(new Insanity_Card());
            cards.add(new Insanity_Card());
        }

        //
       // cards.add(new Insanity_Card());
        cards.add(new IceBurst());
        animations.add(importSprites("/Resources/FireWizard/FireWizardIdleMap.png", 7, 1, 128, 128));
        animations.add(importSprites("/Resources/FireWizard/FireWizardRangeAttackMap.png", 8, 1, 128, 128));
        animations.add(importSprites("/Resources/FireWizard/FireWizardMeleeAttackMap.png", 4, 1, 128, 128));
        rangedOrigin = new Point(85, 75);

    }

    public void giveGold(int n) {
        this.gold += n;
    }

    public void removeGold(int n) {
        if(this.gold >= n) {
            gold -=n;
        }
    }

    public void giveTrinket(Map.Entry<String, Object> trinket) {
        this.trinkets.add(trinket);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Calculate the centered position for the sprite
        int centeredX = (getWidth() - 33) / 2; // 33 is sprite width
        int centeredY = 25; // Matches the previous hitbox position

        if(Config.hitboxesOn) {
            g.setColor(Color.red);
            g.drawRect(rangedOrigin.x, rangedOrigin.y, 10, 10);
        }
        // Update the hitbox's position (optional, if needed for other logic)
       // hitbox.setLocation(centeredX, centeredY);

        // Draw animation instead of the hitbox
        drawAni(g, centeredX, centeredY);
        if(Config.hitboxesOn) {
            g.setColor(Color.white);
            g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
            if(this.getBorder() == null) {
                setBorder(BorderFactory.createLineBorder(Color.white));
            }
        } else {
            this.setBorder(null);
        }


        // Center the health bar above the hitbox
        int barX = hitbox.x + (hitbox.width - healthBar.width) / 2;
        int barY = hitbox.y - healthBar.height - 5; // Offset by 5 pixels above the hitbox

// Draw background bar (red)
        g.setColor(Color.red);
        g.fillRect(barX, barY, healthBar.width, healthBar.height);

// Calculate and draw current health (green)
        int healthBarWidth = (int) ((double) currentHealth / maxHealth * healthBar.width);
        g.setColor(Color.green);
        g.fillRect(barX, barY, healthBarWidth, healthBar.height);
    }


    public void takeDamage(int damage) {
        if(currentHealth > maxHealth) {
            currentHealth = maxHealth;

        }
        FloatingText.createEffect("-" + damage, this, Color.RED);
        currentHealth = currentHealth - damage;
        if(currentHealth <= 0) {
            System.out.println("youre dead");
        } else {
            System.out.println("current health: "+currentHealth);
        }

        revalidate();
        repaint();
    }

    public void heal(int healAmount) {
        if (Game.player.currentHealth < Game.player.maxHealth) { //if players current health is less than max (100)
            Game.player.currentHealth = Math.min(Game.player.currentHealth + healAmount, Game.player.maxHealth); //heal 10 to players health
            FloatingText.createEffect("+"+healAmount, this, Color.GREEN);
        //    System.out.println("Bandage used! Healed for " + healAmount + ". Current Health: " + Game.player.currentHealth);
        } else {
          //  System.out.println("Health is already full! Bandage has no effect.");
        }
    }

    public static BufferedImage loadImage(String path) {
        try (InputStream is = Enemy.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Error: Image not found at " + path);
                return null;
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace(); // Print error details for debugging
            return null;
        }
    }


    public static BufferedImage[] importSprites(String pathName, int cols, int rows, int spriteWidth, int spriteHeight) {
        BufferedImage image = loadImage(pathName);
        if (image == null) {
            return new BufferedImage[0]; // Return empty array if loading fails
        }

        BufferedImage[] sprites = new BufferedImage[cols * rows];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                sprites[y * cols + x] = image.getSubimage(x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight);
            }
        }
        return sprites;
    }
    public State currentState = State.IDLE;
    int aniCounter = 0;
    int aniSpeed = 5;
    int aniIndex = 0;

    //Attacking Animation
    protected int attackIndex = 0;
    protected int attackCounter = 0;
    protected int attackSpeed = 4;
    public void animate() {
        if (currentState == State.IDLE) {


            aniCounter++;//Increments by one every frame.
            if (aniCounter >= aniSpeed) {//If the number of frames I want per animation sprite has passed
                aniCounter = 0;//Reset the counter
                aniIndex++;//Increase the index by one to move to the next sprite
            }
            if (aniIndex >= this.animations.get(0).length) //If we reach the end of the sprite map
                aniIndex = 0;//Reset the index
        }

        if(currentState == State.ATTACKING) {
            attackCounter++;
            if(attackCounter>=attackSpeed) {
                attackCounter = 0;
                attackIndex++;
                if(attackIndex==animations.get(1).length-1) {
                    whatToDoAfterAttackAnimation.run();
                }
                if(!animations.isEmpty()&&attackIndex >= animations.get(1).length) {
                    whatToDoAfterAttackAnimation.run();
                    attackIndex = 0;
                    currentState = State.IDLE;

                }
            }
        }
    }
    Runnable whatToDoAfterAttackAnimation;
    public void setStateToAttacking(Runnable onComplete) {
        this.currentState = State.ATTACKING;
        whatToDoAfterAttackAnimation = onComplete;
    }
    public void drawAni(Graphics g, int x, int y) {
       // animate();
        if(currentState == State.IDLE)
            g.drawImage(animations.get(0)[aniIndex], 0, 0, 128, 128, null);
        if(currentState == State.ATTACKING)
            g.drawImage(animations.get(1)[attackIndex], 0, 0, 128, 128, null);

    }

    public void refillDeck() {
        cards.addAll(discard);
        discard.clear();
        Collections.shuffle(cards);
    }

    public void resetDeck() {
        cards.addAll(hand);
        cards.addAll(discard);
        discard.clear();
        hand.clear();
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if(cards.isEmpty())
            refillDeck();
        Card c = cards.get(0);
        hand.add(c);
        cards.remove(c);

        return c;
    }

    public void discard(Card c) {
        if(hand.contains(c)) {
            hand.remove(c);
            discard.add(c);
        } else if(GameplayPane.slottedCards.contains(c)) {
            GameplayPane.slottedCards.remove(c);
            discard.add(c);
        }
    }

    // Increases max HP
    public void increaseMaxHP(int amount) {
        maxHealth += amount;
        currentHealth += amount; // Optional: boost current HP too
        System.out.println("Max HP increased by " + amount + ". New max: " + maxHealth);
        revalidate();
        repaint();
    }

    // Reduces max HP (e.g., for failed encounter rolls)
    public void decreaseMaxHP(int amount) {
        maxHealth = Math.max(1, maxHealth - amount); // Prevent dropping to zero
        currentHealth = Math.min(currentHealth, maxHealth); // Clamp current HP
        System.out.println("Max HP reduced by " + amount + ". New max: " + maxHealth);
        revalidate();
        repaint();
    }

    public boolean tryChestEncounter(int hpPenalty, int goldReward) {
        double chance = Math.random();
        if (chance <= 0.25) {
            giveGold(goldReward);
            return true;
        } else {
            maxHealth -= hpPenalty;
            currentHealth = Math.min(currentHealth, maxHealth); // prevent overhealing
            return false;
        }
    }

}
