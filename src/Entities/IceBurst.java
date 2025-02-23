package Entities;
import MainPackage.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class IceBurst extends Card {

    private static final String IMAGE_PATH = "/Resources/Cards/IceBurst.png"; // example image path change when have card


    public IceBurst() {
        super(IMAGE_PATH); //pass the image path to card constructor
        this.image = loadImage(IMAGE_PATH); // load image from the path

    }
    //could possibly add a random chance to freeze enemies too
    @Override
    public void effect() {
        System.out.println("IceBurst cast! Deals 8 damage to all Enemies!" );
        //casts 8 damage to all enemies in battle
        for (Enemy enemy : Game.gui.gameScreen.northPanel.enemies) {
            enemy.takeDamage(800);
        }
    }

    //paint componenent for drawing the cards image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//clears old drawings and renders cards

        if (image != null) {//check if image is available, draw it if so or use placeholder
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);//draw card image
        } else {//set placeholder colour for iceburst to blue
            g.setColor(Color.BLUE);
            g.fillRect(0, 0, getWidth(), getHeight()); // blue box if image is missing
        }
    }
}
