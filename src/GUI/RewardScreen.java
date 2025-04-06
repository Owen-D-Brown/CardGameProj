package GUI;

import Entities.Enemy;
import MainPackage.Config;
import MainPackage.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

//This is the visual component class that cards are played on top of.
//The main function of this class is to take the bounds of the CardSlots in the GameplayPane, and draw corrosponding visual references for the positions.
public class RewardScreen extends JPanel {
    BufferedImage gold;
    public RewardScreen() {
        //setBackground(Color.green);
        setPreferredSize(new Dimension(100, (int) (Config.frameSize.height * 0.3)));
        gold=loadImage("/Resources/gold.png");
    }

    private BufferedImage background = loadImage("/Resources/Battleground1.png");

    public static BufferedImage loadImage(String path) {
        try (InputStream is = Enemy.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Error: Image not found at " + path);
                return null;
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace(); // Print error details for debugging
            return null;
        }
    }
    TrinketSideBar trinketSideBar = new TrinketSideBar();
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage bck = background.getSubimage(0, 0, 1000, 333);
        g.drawImage(bck, 0, -6, 1000, this.getHeight()+6, null);
        g.setColor(Color.black);


    }

    public void getLootRewards() {
        for (int i = 0; i <= Game.gui.gameScreen.northPanel.enemies.size() - 1; i++) {
            Map.Entry<String, Object> item = Game.gui.gameScreen.northPanel.enemies.get(i).generateLoot();
            String itemNameStr = String.valueOf(item.getKey());
            String itemValueStr = String.valueOf(item.getValue());

            int iterate = i * 70;

            JLabel itemName = new JLabel(itemNameStr);
            JLabel itemValue = new JLabel(itemValueStr);
            itemName.setBounds(60, 100 + iterate, 200, 50); // Moved right to make space for image
            itemValue.setBounds(280, 100 + iterate, 200, 50);
            itemName.setForeground(Color.white);
            itemValue.setForeground(Color.white);

            // Optional image icon for gold
            JLabel goldIconLabel = null;
            if ("Gold".equals(itemNameStr)) {
                ImageIcon icon = new ImageIcon(gold.getScaledInstance(32, 32, Image.SCALE_SMOOTH));
                goldIconLabel = new JLabel(icon);
                goldIconLabel.setBounds(20, 100 + iterate, 32, 32); // Next to item name
                this.add(goldIconLabel);
            }

            JButton takeLoot = new JButton("TAKE");
            takeLoot.setBounds(500, 100 + iterate, 200, 50);
            JLabel finalGoldIconLabel = goldIconLabel; // Required for lambda access
            takeLoot.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(item + " Taken");
                    remove(itemName);
                    remove(itemValue);
                    remove(takeLoot);
                    if (finalGoldIconLabel != null) remove(finalGoldIconLabel);

                    if (Objects.equals(item.getKey(), "Gold")) {
                        Game.player.giveGold((int) item.getValue());
                    } else {
                        Game.player.giveTrinket(item);
                    }
                    Game.gui.gameScreen.trinketSideBar.populateTrinketBar();
                    revalidate();
                    repaint();
                }
            });

            this.add(itemName);
            this.add(itemValue);
            this.add(takeLoot);
        }
    }




}
//TODO:
/*
For each CardSlot in array in GameplayPane, we need to draw a rectangle at there coords.
 */
