package Entities;

import Inputs.MouseHandler;
import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Card extends JComponent {
    private Color color = Color.yellow;
    private boolean beingPlayed = false;
    private int circleX, circleY;
    private int targetX, targetY;
    private final int circleRadius = 20;
    private boolean isMoving = false;
    public boolean primed = false;

    public Card() {
        setSize(Config.cardSize);
        MouseHandler mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }

    public void effect() {
        setBeingPlayed(true);
        Random rand = new Random();
        int dmg = rand.nextInt(10) + 5;
        Game.enemy.takeDamage(dmg);

        Fireball fireball = new Fireball();
        AttackPlane.animations.add(fireball);
        fireball.startAnimation();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(this.color);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());


    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isBeingPlayed() {
        return beingPlayed;
    }

    public void setBeingPlayed(boolean beingPlayed) {
        this.beingPlayed = beingPlayed;
    }
}
