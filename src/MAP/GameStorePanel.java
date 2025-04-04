package MAP;

import Entities.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameStorePanel extends JPanel {
    private BufferedImage merchantImage;
    private JList<String> inventoryList;
    private Player player;

    // gamestore constructor
    public GameStorePanel(Player player) {
        this.player = player;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 500));

        // Load merchant image
        try {
            merchantImage = ImageIO.read(getClass().getResourceAsStream("/Resources/MapSprite/merchant.png"));
        } catch (IOException ex) {
            System.err.println("Failed to load merchant image: " + ex.getMessage());
        }

        // Top panel: Display merchant image
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

        // Bottom panel: Shop inventory and buy button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setPreferredSize(new Dimension(500, 250));

        // inventory list with shop items (String)
        String[] items = {
                "Potion Card - 10 Gold",
                "Vampire Card - 50 Gold"
        };
        inventoryList = new JList<>(items);
        inventoryList.setBackground(Color.LIGHT_GRAY);
        inventoryList.setFont(new Font("Arial", Font.BOLD, 18));
        inventoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(inventoryList);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        // buy button here with logic after
        JButton buyButton = new JButton("Buy Item");
        buyButton.setFont(new Font("Arial", Font.BOLD, 16));
        buyButton.addActionListener(e -> handlePurchase());
        bottomPanel.add(buyButton, BorderLayout.SOUTH);

        // Add top and bottom panels to the main panel
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void handlePurchase() {
        String selectedItem = inventoryList.getSelectedValue();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an item to buy.");
            return;
        }
        int cost = getCostFromItemName(selectedItem);
        if (player.getGold() < cost) {
            JOptionPane.showMessageDialog(this, "Not enough gold!");
            return;
        }
        Card newCard = createCardFromShopItem(selectedItem);
        if (newCard == null) {
            JOptionPane.showMessageDialog(this, "Error: Card creation failed.");
            return;
        }

        // Debug output before purchase
        System.out.println("DEBUG: Before purchase:");
        System.out.println(" - Player gold: " + player.getGold());
        System.out.println(" - Cards in deck: " + player.cards.size());


        // deduct gold and trying to add cards to player
        player.removeGold(cost);
        player.cards.add(newCard);


        // debugging after purchase
        System.out.println("DEBUG: After purchase:");
        System.out.println(" - New card added: " + newCard);
        System.out.println(" - Player gold: " + player.getGold());
        System.out.println(" - Cards in deck: " + player.cards.size());


        // player display is refreshed
        player.revalidate();
        player.repaint();

        JOptionPane.showMessageDialog(this, "You bought: " + selectedItem
                + "\nRemaining Gold: " + player.getGold());
    }

    // instance based on String
    private Card createCardFromShopItem(String itemName) {
        if (itemName.contains("Potion"))
            return new Potion_Card();
        if (itemName.contains("Vampire"))
            return new Vampire_MCard();
        if (itemName.contains("LE"))
            return new LastEmbrace_Card();
        return null;
    }

    //gold cost from the String
    private int getCostFromItemName(String itemName) {
        try {
            String[] parts = itemName.split(" - ");
            return Integer.parseInt(parts[1].replace(" Gold", "").trim());
        } catch (Exception ex) {
            System.err.println("Error parsing cost: " + ex.getMessage());
            return 0;
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Player player = new Player();

                JFrame frame = new JFrame("Game Store");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                frame.add(new GameStorePanel(player));
                frame.pack();
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
