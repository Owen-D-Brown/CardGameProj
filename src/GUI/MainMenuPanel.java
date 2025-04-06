package GUI;


import MainPackage.Config;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class MainMenuPanel extends JPanel {
    private static final String IMAGE_PATH = "/Resources/MenuImages/MainMenuFinal.png";
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
        centerPanel.setOpaque(false); //make panel transparent

        //positions the center panel for menu
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(-140, 0, 0, 0); //changed top to move up page

        //jpanel to hold the buttons and set layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); //put buttons vertically
        buttonPanel.setOpaque(false);//make panel transparent

        //set width for the buttons
        Dimension buttonSize = new Dimension(200, 50); //button size for menu

        //dev menu button - for now will switch to createmenuscreen for testing
        //JButton devMenu = new JButton("Dev Menu");
        //devMenu.setPreferredSize(buttonSize);
        //devMenu.setMaximumSize(buttonSize);
       // devMenu.addActionListener(e -> root.showScreen(root.createMenuScreen()));

        //button to start game will load open world
        JButton startGame = new JButton("START");
        startGame.setPreferredSize(buttonSize);
        startGame.setMaximumSize(buttonSize);
        startGame.setFont(new Font("Serif", Font.BOLD, 24));
        startGame.setForeground(Color.WHITE);//text colour
        startGame.setContentAreaFilled(false);//hide background
        startGame.setBorderPainted(true);
        startGame.setFocusPainted(false);
        startGame.addActionListener(e -> {
            root.showScreen(root.worldPanel); //switch to the open world screen
            root.worldPanel.startGameThread(); //start the game loop if not running
            root.worldPanel.requestFocusInWindow();
        });

        //exit game button
        JButton exitButton = new JButton("EXIT");
        exitButton.setPreferredSize(buttonSize);
        exitButton.setMaximumSize(buttonSize);
        exitButton.setFont(new Font("Serif", Font.BOLD, 24));
        exitButton.setForeground(Color.WHITE);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(true);
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(e -> System.exit(0));

        //add buttons to the panel
        buttonPanel.add(startGame);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); //gap between buttons
        buttonPanel.add(exitButton);

        //add the button panel to the center of the empty panel
        centerPanel.add(buttonPanel, gbc);

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
    //draws the background image on menu
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
