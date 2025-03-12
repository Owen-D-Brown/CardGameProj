package MAP;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GameStorePanel extends JPanel {
    private BufferedImage merchantImage; // Image used for the top panel
    private JList<String> inventoryList;



    public GameStorePanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 500));

        try {
            merchantImage = ImageIO.read(getClass().getResourceAsStream("/Resources/MapSprite/merchant.png"));

        } catch (Exception e) {
            System.out.println("Failed to load merchant image: " + e.getMessage());
        }





        // Top panel for the merchant image (250px height)
        JPanel topPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (merchantImage != null) {
                    g.drawImage(merchantImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        topPanel.setPreferredSize(new Dimension(500, 250));
        topPanel.setBackground(Color.DARK_GRAY);

        // bottom panel for inventory (250px height)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(500, 250));
        bottomPanel.setLayout(new BorderLayout());

        // sample inventory
        String[] items = {"Heal Card - 10 Gold", "Sword Card - 50 Gold", "Shield Card - 30 Gold", "Bow Card - 40 Gold", "Armor Card - 100 Gold", "Armor Card - 100 Gold", "Armor Card - 100 Gold", "Armor Card - 100 Gold", "Armor Card - 100 Gold", "Bow Card - 40 Gold", "Bow Card - 40 Gold"};
        inventoryList = new JList<>(items);
        inventoryList.setBackground(Color.LIGHT_GRAY);  // A light grey background to make text visibile
        inventoryList.setFont(new Font("Arial", Font.BOLD, 18)); // Setting the fonts style here
        inventoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Scrollable inventory list
        JScrollPane scrollPane = new JScrollPane(inventoryList);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        // Buy button
        JButton buyButton = new JButton("Buy Item");
        buyButton.setFont(new Font("Arial", Font.BOLD, 16)); // Specific font for the Button
        buyButton.addActionListener(e -> {
            String selectedItem = inventoryList.getSelectedValue();
            if (selectedItem != null) {
                JOptionPane.showMessageDialog(this, "You bought: " + selectedItem);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to buy.");
            }
        });

        bottomPanel.add(buyButton, BorderLayout.SOUTH);

        // Add panels to main store panel
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Store");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GameStorePanel());
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
