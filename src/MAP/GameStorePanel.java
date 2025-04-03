package MAP;

import Entities.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class GameStorePanel extends JPanel {
    private BufferedImage merchantImage;
    private JList<String> inventoryList;
    private Player player;

    public GameStorePanel(Player player) {
        this.player = player;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 500));

        try {
            merchantImage = ImageIO.read(getClass().getResourceAsStream("/Resources/MapSprite/merchant.png"));
        } catch (Exception e) {
            System.out.println("Failed to load merchant image: " + e.getMessage());
        }

        // Top panel with merchant image
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

        // Bottom panel with inventory and buy button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setPreferredSize(new Dimension(500, 250));

        // Shop inventory
        String[] items = {
                "Potion Card - 10 Gold",
                "Vampire Card - 50 Gold",

        };
        inventoryList = new JList<>(items);
        inventoryList.setBackground(Color.LIGHT_GRAY);
        inventoryList.setFont(new Font("Arial", Font.BOLD, 18));
        inventoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(inventoryList);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        // Buy button
        JButton buyButton = new JButton("Buy Item");
        buyButton.setFont(new Font("Arial", Font.BOLD, 16));
        buyButton.addActionListener(e -> {
            String selectedItem = inventoryList.getSelectedValue();
            if (selectedItem != null) {
                int cost = getCostFromItemName(selectedItem);
                if (player.getGold() >= cost) {
                    Card newCard = createCardFromShopItem(selectedItem);
                    if (newCard != null) {
                        player.removeGold(cost);
                        player.hand.add(newCard); // trying to add cards to hand
                        JOptionPane.showMessageDialog(this, "You bought: " + selectedItem + "\nRemaining Gold: " + player.getGold());
                    } else {
                        JOptionPane.showMessageDialog(this, "Error: Card creation failed.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Not enough gold!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to buy.");
            }
        });

        bottomPanel.add(buyButton, BorderLayout.SOUTH);


        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private Card createCardFromShopItem(String itemName) {
        if (itemName.contains("Potion")) return new Potion_Card();

        if (itemName.contains("LE")) return new LastEmbrace_Card();
        if (itemName.contains("Vampire")) return new Vampire_MCard();
        return null;
    }

    private int getCostFromItemName(String itemName) {
        try {
            String[] parts = itemName.split(" - ");
            return Integer.parseInt(parts[1].replace(" Gold", "").trim());
        } catch (Exception e) {
            return 0; // fallback
        }
    }

    public static void main(String[] args) {
        try {
            Player player = new Player();

            JFrame frame = new JFrame("Game Store");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new GameStorePanel(player));
            frame.pack();
            frame.setResizable(false);
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
