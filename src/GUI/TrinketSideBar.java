package GUI;

import Entities.Enemy;
import MainPackage.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TrinketSideBar extends JPanel {

    public ArrayList<JPanel> boxes = new ArrayList<>();
    BufferedImage gold;

    public TrinketSideBar() {
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(100, 1000));
        this.setLayout(new GridLayout(10,1,0,5));
        int i = 0;
        while(i<5) {
            JPanel temp = new JPanel();
            temp.setBorder(BorderFactory.createLineBorder(Color.black, 2));
            temp.setLayout(new GridBagLayout());
            add(temp);
            boxes.add(temp);
            i++;
        }
        gold=loadImage("/Resources/gold.png");
        setGoldIcon(gold);

    }

    public void populateTrinketBar() {
        for(int i = 0; i< Game.player.getTrinkets().size(); i++) {
            //System.out.println(Game.player.getTrinkets().get(i).getValue());
            Component comp = (Component) Game.player.getTrinkets().get(i).getValue();
            comp.setPreferredSize(new Dimension(12*6,9*6));
            boxes.get(i).add(comp);
            boxes.get(i).revalidate();
            boxes.get(i).repaint();
            for(Component component: this.boxes.get(i).getComponents()) {
                System.out.println("comp: "+component);
            }
        }
        setGoldIcon(gold);
        revalidate();
        repaint();
    }

    public void setGoldIcon(BufferedImage goldImage) {
        if (goldImage == null || boxes.isEmpty()) return;

        // Clear the top box (the one displaying gold)
        boxes.get(0).removeAll();

        // Create a panel for the gold icon and label
        JPanel goldPanel = new JPanel();
        goldPanel.setLayout(new BorderLayout());
        goldPanel.setOpaque(false); // Keep the background transparent

        // Create an ImageIcon from the BufferedImage and scale it
        ImageIcon icon = new ImageIcon(goldImage.getScaledInstance(48, 36, Image.SCALE_SMOOTH));
        JLabel goldLabel = new JLabel(icon);
        goldPanel.add(goldLabel, BorderLayout.CENTER); // Add the gold icon

        // Create a JLabel to display the player's gold amount
        JLabel goldAmountLabel = new JLabel(String.valueOf(Game.player.getGold()));
        goldAmountLabel.setForeground(Color.black);
        goldPanel.add(goldAmountLabel, BorderLayout.SOUTH); // Add the gold amount beneath the icon

        // Add the gold panel to the first box
        boxes.get(0).add(goldPanel);

        // Revalidate and repaint to refresh the panel
        boxes.get(0).revalidate();
        boxes.get(0).repaint();
    }


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
}
