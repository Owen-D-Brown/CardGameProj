package Entities;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class AttackPlane extends JComponent {

    private Rectangle attackPlane = new Rectangle(0, 0, 640, 50);
    public static ArrayList<Animation> animations = new ArrayList<>();
    public AttackPlane() {
        setSize(new Dimension(attackPlane.width, attackPlane.height));
        setBorder(new LineBorder(Color.black));
    }


    public void addAniToQue(Animation ani) {
        animations.add(ani);
    }

    public void playAnimation() {

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(Animation ani : animations) {

            ani.paint(g);
        }

    }

    public void updateAnimations() {
        for (Animation animation : animations) {
            animation.updateAni();
        }
        repaint();
    }

}
