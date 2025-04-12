package Entities.Cards;

import Entities.Animations.LastEmbraceAnimation;
import Entities.Enemies.Enemy;
import Entities.Player;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class LastEmbrace_Card extends Card {

    private static final String IMAGE_PATH = "/Resources/Cards/LastEmbrace_Card.png";

    public LastEmbrace_Card() throws IOException {
        super(IMAGE_PATH);
        this.image = loadImage(IMAGE_PATH);

        this.animation = new LastEmbraceAnimation();
    }

    @Override
    public void resetAnimation() throws IOException {

    }

    @Override
    public void effect() {

        Random rand = new Random();
        int damage = rand.nextInt(50) + 1; // Vampire Bite deals 5-15 damage

        // Deal damage to the first enemy
        if (!Game.gui.gameScreen.northPanel.enemies.isEmpty()) {
            Game.gui.gameScreen.northPanel.enemies.get(0).takeDamage(damage);
            Game.player.takeDamage(damage/2);
        }
    }

    @Override
    public void initCardAniBounds(Player player, Enemy enemy) throws IOException {
        Point relativeOrigin = SwingUtilities.convertPoint(enemy, new Point(enemy.hitbox.x-(enemy.hitbox.width/2), enemy.hitbox.y), Game.gui.gameScreen.northPanel);

        int x;
        x = relativeOrigin.x-animation.w/2+50;
        ((LastEmbraceAnimation) animation).placeAnimation(x, Game.gui.gameScreen.northPanel.yFloor-(animation.h/2)-60);
    }

    @Override
    public void initCardAniBounds(Enemy enemy, Player player) throws IOException {

        ((LastEmbraceAnimation) animation).placeAnimation(player.relativeX, Game.gui.gameScreen.northPanel.yFloor-animation.h);
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

