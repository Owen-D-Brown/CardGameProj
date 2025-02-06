package GUI;

import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DevTools extends JFrame {

    public DevTools() {
        //Configuring this frame.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("DevTools");
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(new Dimension(500, 500));
        setLayout(null);

        JButton drawCard = new JButton("DRAW CARD");
        drawCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.gui.glassPane.drawCard();
                Game.gui.glassPane.revalidate();
                Game.gui.glassPane.repaint();
            }
        });
        add(drawCard);
        drawCard.setBounds(0,0,100,50);


        JButton addEnemy = new JButton(" ADD ENEMY");
        addEnemy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.gui.northPanel.addEnemy();
            }
        });
        add(addEnemy);
        addEnemy.setBounds(110,0,100,50);

        JButton removeEnemy = new JButton("REMOVE ENEMY");
        removeEnemy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.gui.northPanel.removeEnemy(0);
            }
        });
        add(removeEnemy);
        removeEnemy.setBounds(210,0,100,50);

        JButton removeSlot = new JButton("REMOVE SLOT");
        removeSlot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.gui.glassPane.removeCardSlot();
            }
        });
        add(removeSlot);
        removeSlot.setBounds(210,100,100,50);

        JButton addSlot = new JButton("ADD SLOT");
        addSlot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.gui.glassPane.addCardSlot();
            }
        });
        add(addSlot);
        addSlot.setBounds(10,100,100,50);
    }
}
