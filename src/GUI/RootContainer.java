package GUI;

import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;

public class RootContainer extends JFrame {

    private JPanel currentScreen; // Tracks which panel is in the center
    public MainGui gameScreen;
    public JPanel menuScreen;
    private JPanel containerPanel; // The main container using BorderLayout

    public RootContainer(Game game) {
        setTitle("Card Game");
        setSize(Config.frameSize.width+100, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel that holds the swappable screens
        containerPanel = new JPanel(new BorderLayout());
        add(containerPanel, BorderLayout.CENTER);

        // Initialize screens
        gameScreen = new MainGui(game);
        menuScreen = createMenuScreen();

        // Start on the menu
        showScreen(menuScreen);

        setVisible(true);
    }

    private JPanel createMenuScreen() {
        JPanel menu = new JPanel();
        menu.setBackground(Color.BLACK);
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> showScreen(gameScreen));
        menu.add(startButton);
        return menu;
    }

    public void showScreen(JPanel newScreen) {
        if (currentScreen != null) {
            containerPanel.remove(currentScreen);
        }
        currentScreen = newScreen;
        containerPanel.add(newScreen, BorderLayout.CENTER);
        containerPanel.revalidate();
        containerPanel.repaint();
    }


}
