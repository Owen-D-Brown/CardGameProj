package Entities;

import MainPackage.Game;

import java.awt.*;
import java.util.Random;
import java.awt.image.BufferedImage;

public class Satyr_MCard extends Card{

    private static final String IMAGE_PATH = "/Resources/Cards/Satyr_MonsterCard.png";

    public Satyr_MCard() {
        super(IMAGE_PATH);
        this.image = loadImage(IMAGE_PATH);
    }

    @Override
    public void effect() {

        Random rand = new Random();
        int damage = rand.nextInt(12) + 1; // Vampire Bite deals 5-15 damage
        System.out.println("Vampire Bite! Deals " + damage + " damage.");

        // Deal damage to the first enemy
        if (!Game.gui.gameScreen.northPanel.enemies.isEmpty()) {
            Game.gui.gameScreen.northPanel.enemies.get(0).takeDamage(damage);
        }
    }
    //paint component for drawing the cards image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//clears old drawings and renders cards

        if (image != null) {//check if image is available, draw it if so or use placeholder
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);//draw card image
        } else {//else add red placeholder
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight()); // Red box if image is missing
        }
    }
}

