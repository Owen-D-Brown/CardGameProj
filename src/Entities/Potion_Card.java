package Entities;

import MainPackage.Game;

import java.awt.*;
import java.io.IOException;
import java.util.Random;
import java.awt.image.BufferedImage;

public class Potion_Card extends Card{

    private static final String IMAGE_PATH = "/Resources/Cards/Potion_Card.png";

    public Potion_Card() {
        super(IMAGE_PATH);
        this.image = loadImage(IMAGE_PATH);
    }

    @Override
    public void resetAnimation() throws IOException {

    }

    @Override
    public void effect() {

        if (Game.player.currentHealth < Game.player.maxHealth) { //if players current health is less than max (100)
            int healAmount = 50; //heal amount for bandage is 10
            Game.player.currentHealth = Math.min(Game.player.currentHealth + healAmount, Game.player.maxHealth); //heal 10 to players health
            System.out.println("Bandage used! Healed for " + healAmount + ". Current Health: " + Game.player.currentHealth);
        } else {
            System.out.println("Health is already full! Bandage has no effect.");
        }
    }

    @Override
    public void initCardAniBounds(Player player, Enemy enemy) throws IOException {
        ((IceBlast) animation).initAnimation(player, enemy);
    }

    @Override
    public void initCardAniBounds(Enemy enemy, Player player) throws IOException {
        ((IceBlast) animation).initAnimation(enemy, player);
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

