package GUI;

import Entities.Goblin;
import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class DevTools extends JFrame {

    public JLabel deckCount;
    public JLabel discardCount;
    public JLabel handCount;
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
                Game.gui.gameScreen.glassPane.drawCard();
                Game.gui.gameScreen.glassPane.revalidate();
                Game.gui.gameScreen.glassPane.repaint();
            }
        });
        add(drawCard);
        drawCard.setBounds(0,0,100,50);


        JButton addEnemy = new JButton(" ADD ENEMY");
        addEnemy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Game.gui.gameScreen.northPanel.addEnemy(new Goblin());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(addEnemy);
        addEnemy.setBounds(110,0,100,50);

        JButton removeEnemy = new JButton("REMOVE ENEMY");
        removeEnemy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.gui.gameScreen.northPanel.removeEnemy(0);
            }
        });
        add(removeEnemy);
        removeEnemy.setBounds(210,0,100,50);

        JButton removeSlot = new JButton("REMOVE SLOT");
        removeSlot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.gui.gameScreen.glassPane.removeCardSlot();
            }
        });
        add(removeSlot);
        removeSlot.setBounds(210,100,100,50);

        JButton addSlot = new JButton("ADD SLOT");
        addSlot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.gui.gameScreen.glassPane.addCardSlot();
            }
        });
        add(addSlot);
        addSlot.setBounds(10,100,100,50);

        deckCount = new JLabel();
        deckCount.setBounds(400, 200, 200, 50);
        add(deckCount);
        discardCount = new JLabel();
        discardCount.setBounds(400, 260, 200, 50);
        add(discardCount);
        handCount = new JLabel();
        handCount.setBounds(400, 320, 200, 50);
        add(handCount);
        updateCounts();

    }

    public void updateCounts() {
        this.deckCount.setText(String.valueOf(Game.player.cards.size()));
        this.discardCount.setText(String.valueOf(Game.player.discard.size()));
        this.handCount.setText(String.valueOf(Game.player.hand.size()));
        this.revalidate();
        this.repaint();
    }
}
