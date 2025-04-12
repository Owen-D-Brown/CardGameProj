package Entities.Cards;

import Entities.Animations.WhirlpoolAnimation;
import Entities.Enemies.Enemy;
import Entities.Player;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class Insanity_Card extends Card {

    private static final String IMAGE_PATH = "/Resources/Cards/Insanity_Card.png";

    public Insanity_Card() throws IOException {
        super(IMAGE_PATH);
        this.image = loadImage(IMAGE_PATH);
        this.animation = new WhirlpoolAnimation();
    }

    @Override
    public void resetAnimation() throws IOException {

    }

    @Override
    public void effect() {}

    @Override
    public void initCardAniBounds(Player player, Enemy enemy) throws IOException {
        Point relativeOrigin = SwingUtilities.convertPoint(enemy, new Point(enemy.hitbox.x-(enemy.hitbox.width/2), enemy.hitbox.y), Game.gui.gameScreen.northPanel);

        int x;
        x = relativeOrigin.x-animation.w/2+50;
        ((WhirlpoolAnimation) animation).placeAnimation(x+50, Game.gui.gameScreen.northPanel.yFloor-(animation.h/2));
    }

    @Override
    public void initCardAniBounds(Enemy enemy, Player player) throws IOException {

        ((WhirlpoolAnimation) animation).placeAnimation(player.relativeX, Game.gui.gameScreen.northPanel.yFloor-animation.h);
    }


}

