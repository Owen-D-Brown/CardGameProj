package Entities;

import MainPackage.Game;

import java.awt.*;
import java.io.IOException;
import java.util.Random;
import java.awt.image.BufferedImage;

public class Potion_Card extends Card{

    private static final String IMAGE_PATH = "/Resources/Cards/Potion_Card.png";

    public Potion_Card() throws IOException {
        super(IMAGE_PATH);
        this.image = loadImage(IMAGE_PATH);
        this.animation = new HealingRiftAnimation();
    }

    @Override
    public void resetAnimation() throws IOException {

    }

    @Override
    public void effect() {

        Game.player.heal(20);
    }

    @Override
    public void initCardAniBounds(Player player, Enemy enemy) throws IOException {
        ((InPlaceAnimation) animation).placeAnimation(player.relativeX-72, Game.gui.gameScreen.northPanel.yFloor-72);
    }

    @Override
    public void initCardAniBounds(Enemy enemy, Player player) throws IOException {
        ((InPlaceAnimation) animation).placeAnimation(enemy.relativeX, Game.gui.gameScreen.northPanel.yFloor-128);
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

