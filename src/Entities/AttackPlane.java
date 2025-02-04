package Entities;

import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

//This class is the component in which the card animations take place. It is a rectangular area between the player sprite and first enemy sprite.
//Attack sprites travel along the box.
public class AttackPlane extends JComponent {

    private Rectangle attackPlane = new Rectangle(0, 0, 640, 50);
    public static ArrayList<Animation> animations = new ArrayList<>();

    public AttackPlane() {
        setSize(new Dimension(attackPlane.width, attackPlane.height));
        setBorder(new LineBorder(Color.black));
    }


    public static void addAniToQue(Animation ani) {
        animations.clear();
        animations.add(ani);
    }

    public void playAnimation(Runnable onComplete) {

        if (animations.isEmpty()) {
            if (onComplete != null) {
                onComplete.run(); // Move on if no animations
            }
            return;
        }

        long startTime = System.currentTimeMillis();

        Timer animationTimer = new Timer(animations.get(0).interval, ev -> {
            repaint();
            updateAnimations();
            revalidate();


            if (System.currentTimeMillis() - startTime >= animations.get(0).duration) {
                ((Timer) ev.getSource()).stop();

                // Call onComplete after the animation ends
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });

        animationTimer.start();
    }

    //
    public void playAnimation() {



        long startTime = System.currentTimeMillis();

        Timer animationTimer = new Timer(animations.get(0).interval, ev -> {
            repaint();
            updateAnimations();
            revalidate();


            if (System.currentTimeMillis() - startTime >= animations.get(0).duration) {
                ((Timer) ev.getSource()).stop();


            }
        });

        animationTimer.start();
    }
    //

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(Animation ani : animations) {
            ani.paintComponent(g);
        }

    }

    public void updateAnimations() {
        for (Animation animation : animations) {
            animation.updateAni();
        }
        repaint();
    }

}
