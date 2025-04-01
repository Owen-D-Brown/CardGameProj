package GUI;


import MainPackage.Config;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class MainMenuPanel extends JPanel {
    private static final String IMAGE_PATH = "/Resources/MenuImages/Mainmenu.png";
    protected BufferedImage image;
    public MainMenuPanel(RootContainer root) {
        //load background image
        this.image = loadImage(IMAGE_PATH);
        setPreferredSize(new Dimension(Config.frameSize.width+100, 1000)); //set size


        //layout setup border style
        setLayout(new BorderLayout());

        // createempty panel to hold the button panel in the center
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setOpaque(false); // Make the panel transparent

        //jpanel to hold the buttons and set layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); //put buttons vertically
        buttonPanel.setOpaque(false);//make panel transparent

        // Set width for the buttons
        Dimension buttonSize = new Dimension(200, 50); // standard button size

        //dev menu button - for now will switch to createmenuscreen for testing
        JButton devMenu = new JButton("Dev Menu");
        devMenu.setPreferredSize(buttonSize);
        devMenu.setMaximumSize(buttonSize);
        devMenu.addActionListener(e -> root.showScreen(root.createMenuScreen()));

        //button to start game
        JButton startGame = new JButton("Start Game"); // New button to start first fight directly
        startGame.setPreferredSize(buttonSize);
        startGame.setMaximumSize(buttonSize);
        startGame.addActionListener(e -> {
            try {
                root.gameScreen.newFight(root.startFight(1)); // Start first fight
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            root.gameScreen.glassPane.setVisible(true);
            root.gameScreen.cardLayout.show(root.gameScreen.centerContainer, "main");
            root.game.unslotAllCards();
            root.gameScreen.center.revalidate();
            root.gameScreen.center.repaint();
            root.showScreen(root.gameScreen); // Show game screen
        });

        //exit game button
        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(buttonSize);
        exitButton.setMaximumSize(buttonSize);
        exitButton.addActionListener(e -> System.exit(0));

        //add buttons to the panel
        buttonPanel.add(startGame);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Small gap between buttons
        buttonPanel.add(devMenu);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Small gap between buttons
        buttonPanel.add(exitButton);

        //add the button panel to the center of the empty panel
        centerPanel.add(buttonPanel);

        //add the center panel to the center of the BorderLayout
        add(centerPanel, BorderLayout.CENTER);


    }

    // Image loader same logic as player class and card class one
    public static BufferedImage loadImage(String path) {
        try (InputStream is = MainMenuPanel.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Error: Image not found at " + path);
                return null;
            }
            System.out.println("image in load: "+is);
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {//check if image is available, draw it if so or use placeholder
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);//draw background image
        } else {//set placeholder colour for menu to dark gray if image wont load
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight()); // grey background if image is missing
        }
    }
}
