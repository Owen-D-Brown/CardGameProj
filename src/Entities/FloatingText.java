package Entities;

import Entities.Enemies.Enemy;
import MainPackage.Game;

import java.awt.*;
import java.util.ArrayList;

public class FloatingText {
    private static FloatingText instance; // Only one active effect at a time

    public static ArrayList<FloatingText> instances = new ArrayList<>();

    private String text;
    private Color color;
    private int x, y;
    private int duration = 60; // Frames before disappearing
    private int alpha = 255;   // Transparency (fades out)
    private int speed = 1;     // Moves upward

    private FloatingText(String text, int x, int y, Color color) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public static void createEffect(String text, Enemy enemy, Color color) {
        FloatingText instance1 = new FloatingText(text, enemy.getX() + enemy.getWidth() / 2, enemy.getY(), color);
        instances.add(instance1);
    }
    public static void createEffect(String text, Player player, Color color) {
        FloatingText instance1 = new FloatingText(text, player.getX() + player.getWidth() / 2, player.getY(), color);
        System.out.println("create effect: "+player.getX());
        instances.add(instance1);
    }

    public static void removeInstances() {
       // instance = null;
        instances.removeIf(instance -> instance.duration <= 0);

    }

    public static void update() {
        for(FloatingText instance : instances) {
            if (instance != null) {
                instance.y -= instance.speed;
                instance.duration--;
                instance.alpha = Math.max(0, instance.alpha - 5);

                if (instance.duration <= 0) {
                    //instances.remove(instance); // Remove effect when finished
                    //System.out.println("VISUALS HAVE COMPLETED -------");

                }
            }
        }
    }

    public static void render(Graphics g) {
        for (FloatingText instance : instances) {
            if (Game.gui.gameScreen.northPanel.enemies.size() <= 0 && instance.duration <= 0) {
                instance = null;
            }
            if (instance != null) {
                //System.out.println("VISUALS ARE NOT NULL");
                g.setColor(new Color(instance.color.getRed(), instance.color.getGreen(), instance.color.getBlue(), instance.alpha));
                g.setFont(new Font("Arial", Font.BOLD, 18));
                g.drawString(instance.text, instance.x, instance.y);
             //   System.out.println(instance.x +"  |  "+ instance.y);

            }
        }
    }
}
