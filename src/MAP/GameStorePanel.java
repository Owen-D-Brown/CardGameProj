package MAP;

import Entities.*;
import MainPackage.Game;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameStorePanel extends JPanel {
    private BufferedImage merchantImage;
    private JList<String> inventoryList;

    //revmaped panel
    public GameStorePanel(Player player) {

        //new layout with one orw and 2 columns
        setLayout(new GridLayout(1, 2));
        setPreferredSize(new Dimension(800, 500)); // chang size here of needed

        // Load merchant image
        try {
            merchantImage = ImageIO.read(getClass().getResourceAsStream("/Resources/MapSprite/Merchant2.png"));
        } catch (IOException ex) {
            System.out.println("Failed to load merchant image: " + ex.getMessage());
        }

        // Merchant image will fill the left panel
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (merchantImage != null) {
                    g.drawImage(merchantImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        imagePanel.setBackground(Color.DARK_GRAY);

        // right panel will consist of buy button and scroll list for cards
        JPanel inventoryPanel = new JPanel(new BorderLayout());
        inventoryPanel.setPreferredSize(new Dimension(300, 500));

        // Inventory list with shop items
        String[] items = {
                "Bow Card - 15 Gold",
                "Hellfire Card - 30 Gold",
                "Insanity Card - 25 Gold",
                "Last Embrace Card - 35 Gold",
                "Lightning Bolt Card - 40 Gold",
                "Magic Hand Card - 20 Gold",
                "Potion Card - 10 Gold",
                "Punch Card - 5 Gold",
                "Satyr Monster Card - 20 Gold",
                "Sword Card - 15 Gold",
                "Vampire Card - 40 Gold"
        };

        inventoryList = new JList<>(items);
        inventoryList.setBackground(Color.BLACK);
        inventoryList.setFont(new Font("Arial", Font.BOLD, 18));

        inventoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inventoryList.setSelectedIndex(0);
        inventoryList.setCellRenderer(new CardListRenderer());
        JScrollPane scrollPane = new JScrollPane(inventoryList);
        inventoryPanel.add(scrollPane, BorderLayout.CENTER);

        // Buy button at the bottom of the inventory panel
        JButton buyButton = new JButton("Buy Item");
        buyButton.setFont(new Font("Arial", Font.BOLD, 16));
        buyButton.addActionListener(e -> {
            try {
                handlePurchase();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        inventoryPanel.add(buyButton, BorderLayout.SOUTH);

        //the panels on the mian gui
        add(imagePanel);
        add(inventoryPanel);
    }

    private void handlePurchase() throws IOException {
       //getting the item thats selected
        String selectedItem = inventoryList.getSelectedValue();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an item to buy.");
            return;
        }

        System.out.println("Selected item: " + selectedItem);
        int cost = getCostFromItemName(selectedItem); // cost of the selected card
        Player player = Game.player; //accessing player data here to I can add cards etc..

        //check if player has enough gold
        if (player.getGold() < cost) {
            JOptionPane.showMessageDialog(this, "Not enough gold!");
            return;
        }

        // creating the new card based on object description
        Card newCard = createCardFromShopItem(selectedItem);
        if (newCard == null) {
            JOptionPane.showMessageDialog(this, "Error: Card creation failed.");
            return;
        }

        System.out.println("DEBUG: Before purchase:");
        System.out.println(" - Player gold: " + player.getGold());
        System.out.println(" - Cards in deck: " + player.cards.size());

        player.removeGold(cost);
        player.cards.add(newCard);

        System.out.println("DEBUG: After purchase:");
        System.out.println(" - New card added: " + newCard);
        System.out.println(" - Player gold: " + player.getGold());
        System.out.println(" - Cards in deck: " + player.cards.size());

        player.revalidate();
        player.repaint();

        JOptionPane.showMessageDialog(this, "You bought: " + selectedItem
                + "\nRemaining Gold: " + player.getGold());
    }

    // cards from String item to Card instance
    private Card createCardFromShopItem(String itemName) throws IOException {
        if (itemName.contains("Potion"))
            return new Potion_Card();
        if (itemName.contains("Vampire"))
            return new Vampire_MCard();
        if (itemName.contains("Bow"))
            return new Bow_Card();
        if (itemName.contains("Hellfire"))
            return new Hellfire();
        if (itemName.contains("Insanity"))
            return new Insanity_Card();
        if (itemName.contains("Last Embrace Card"))
            return new LastEmbrace_Card();
        if (itemName.contains("Lightning Bolt"))
            return new LightningBolt();
        if (itemName.contains("Magic Hand"))
            return new MagicHand();
        if (itemName.contains("Punch Card"))
            return new Punch();
        if (itemName.contains("Satyr Monster Card"))
            return new Satyr_MCard();
        if (itemName.contains("Sword Card"))
            return new Sword();
        return null;
    }

    private int getCostFromItemName(String itemName) {
        try {
            String[] parts = itemName.split(" - ");
            return Integer.parseInt(parts[1].replace(" Gold", "").trim());
        } catch (Exception ex) {
            System.out.println("Error parsing cost: " + ex.getMessage());
            return 0;
        }
    }

    // for designing the list

    private class CardListRenderer extends JLabel implements ListCellRenderer<String> {
        public CardListRenderer() {
            setOpaque(true);
        }

        // Link for how to implement and use a ListCellRenderer, used to paint cells in a JList
        //https://docs.oracle.com/javase/8/docs/api/javax/swing/ListCellRenderer.html
        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            setText(value); //text based on value
            setIcon(getIconForCard(value)); // image based on value
            setIconTextGap(10); // the gaps between the text and the cards

            // the background for the selected items here
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }

    // checking if the card text equals the specified text, the image path for the card is then rendered accordingly
    private ImageIcon getIconForCard(String cardText) {
        String path = null;
        if (cardText.contains("Potion"))
            path = "/Resources/Cards/Potion_Card.png";
        else if (cardText.contains("Vampire"))
            path = "/Resources/Cards/Vampire_MCard.png";
        else if (cardText.contains("Bow"))
            path = "/Resources/Cards/BowCard.png";
        else if (cardText.contains("Hellfire"))
            path = "/Resources/Cards/HellfireCard.png";
        else if (cardText.contains("Insanity"))
            path = "/Resources/Cards/Insanity_Card.png";
        else if (cardText.contains("Last Embrace Card"))
            path = "/Resources/Cards/LastEmbrace_Card.png";
        else if (cardText.contains("Lightning Bolt"))
            path = "/Resources/Cards/LightningBolt_Card.png";
        else if (cardText.contains("Magic Hand"))
            path = "/Resources/Cards/MagicHandCard.png";
        else if (cardText.contains("Punch Card"))
            path = "/Resources/Cards/PunchCard.png";
        else if (cardText.contains("Satyr Monster"))
            path = "/Resources/Cards/Satyr_MonsterCard.png";
        else if (cardText.contains("Sword"))
            path = "/Resources/Cards/SwordCard.png";

        if (path != null) {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image img = icon.getImage();
            //loading and scaling the image here
            Image scaledImg = img.getScaledInstance(100, 120, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        }
        return null;
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                if (Game.player == null) {
                    Game.player = new Player();
                }
                JFrame frame = new JFrame("Game Store");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new GameStorePanel(Game.player));
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