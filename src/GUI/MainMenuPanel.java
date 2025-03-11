package GUI;

import MainPackage.Config;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(RootContainer root) {
        setPreferredSize(new Dimension(Config.frameSize.width+100, 1000)); //set size
        setBackground(Color.darkGray);//background color for now

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

        JButton startGame = new JButton("Start Game"); // New button to start first fight directly
        startGame.setPreferredSize(buttonSize);
        startGame.setMaximumSize(buttonSize);
        startGame.addActionListener(e -> {
            root.gameScreen.newFight(root.startFight(1)); // Start first fight
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
}
