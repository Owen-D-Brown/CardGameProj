package Entities;

import MainPackage.Game;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class Hellfire extends Card{


    private static final String IMAGE_PATH = "/Resources/Cards/HellfireCard.png"; // switch to cards image path


    public Hellfire() throws IOException {
        super(IMAGE_PATH); //pass the image path to card constructor
        this.image = loadImage(IMAGE_PATH); // load image from the path
        animation = new Fireball();


    }
    @Override
    public void effect() {
        Random rand = new Random();
        int damage = rand.nextInt(10) + 1; // Firebolt deals 5-15 damage
        System.out.println("Firebolt cast! Deals " + damage + " damage.");

        // Deal damage to the first enemy
        if (!Game.gui.gameScreen.northPanel.enemies.isEmpty()) {
            Game.gui.gameScreen.northPanel.enemies.get(0).takeDamage(damage);
        }
    }


    @Override
    public void initCardAniBounds() throws IOException {
        ((Fireball) animation).initAnimation(Game.player, Game.gui.gameScreen.northPanel.enemies.get(0));
    }

    //paint component for drawing the cards image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//clears old drawings and renders cards

        if (image != null) {//check if image is available, draw it if so or use placeholder
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);//draw card image

        } else {//else add red placeholder
            g.setColor(Color.RED);
            g.fillRect(0, 0, getWidth(), getHeight()); // Red box if image is missing

        }
    }


}
