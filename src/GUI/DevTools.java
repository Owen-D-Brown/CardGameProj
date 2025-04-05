package GUI;

import Entities.Goblin;
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
        // Configuring this frame.
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

        JButton addEnemy = new JButton("ADD ENEMY");
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

        // Complete Combat Button
        JButton returnToCombatMap = new JButton("RETURN TO COMBAT MAP");
        returnToCombatMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("RETURN TO COMBAT MAP button clicked!");

                // ⬆ Scale randomized combat weights here
                Game.randomCombatNodeClicks++;
                Game.randomCombatMaxWeight += 10;
                if (Game.randomCombatNodeClicks >= 2) {
                    Game.randomCombatMinWeight += 10;
                }

                System.out.println("➡ New Combat Weight Range - Min: " + Game.randomCombatMinWeight +
                        ", Max: " + Game.randomCombatMaxWeight);

                //Call existing cleanup method
                completeCombatAndReturnToMap();
            }
        });
        add(returnToCombatMap);
        returnToCombatMap.setBounds(110, 100, 150, 50);

        JButton returnToOverworld = new JButton("RETURN TO OVERWORLD");
        returnToOverworld.addActionListener(e -> {
            Game.gui.returnToOverworld();
        });
        add(returnToOverworld);
        returnToOverworld.setBounds(110, 160, 200, 50);



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

    /**
     * Switches the game back to the Map Panel after combat.
     * Ensures all combat-related components are turned off.
     */
    private void completeCombatAndReturnToMap() {
        System.out.println("Completing combat and returning to the map");

        // Hide combat-related UI elements
        Game.gui.gameScreen.glassPane.setVisible(false);
        Game.gui.gameScreen.center.setVisible(false);
        Game.gui.gameScreen.northPanel.setVisible(false);

        // Reset any combat states if needed
        Game.unslotAllCards();
        Game.gui.gameScreen.center.revalidate();
        Game.gui.gameScreen.center.repaint();

        // Show the Map Panel
        Game.gui.showScreen(Game.gui.mapScreen);

        // Make sure the MapGameplayPane is re-enabled
        Game.gui.getGlassPane().setVisible(true);
    }
}
