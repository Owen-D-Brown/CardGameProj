package GUI;

import Entities.Enemy;
import Entities.Goblin;
import Entities.Orc;
import Entities.Slime;
import MainPackage.Config;
import MainPackage.Game;
import MainPackage.NorthPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RootContainer extends JFrame {

    private JPanel currentScreen; // Tracks which panel is in the center
    public BattleGUI gameScreen;
    public JPanel menuScreen;
    private JPanel containerPanel; // The main container using BorderLayout
    public Game game;

    public RootContainer(Game game) {
        setTitle("Card Game");
        setSize(Config.frameSize.width+100, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel that holds the swappable screens
        containerPanel = new JPanel(new BorderLayout());
        add(containerPanel, BorderLayout.CENTER);

        // Initialize screens
        gameScreen = new BattleGUI(game);
        menuScreen = createMenuScreen();

        // Start on the menu
        showScreen(menuScreen);

        setVisible(true);
    }

    private JPanel createMenuScreen() {
        JPanel menu = new JPanel();
        menu.setBackground(Color.BLACK);
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> {
            gameScreen.newFight(startFight(2));
            Game.gui.gameScreen.glassPane.setVisible(true);
            Game.gui.gameScreen.cardLayout.show(Game.gui.gameScreen.centerContainer, "main");
            Game.unslotAllCards();
            gameScreen.center.revalidate();
            gameScreen.center.repaint();
            showScreen(gameScreen);

        });
        menu.add(startButton);
        JButton encounter1 = new JButton("Encounter 1");
        encounter1.addActionListener(e -> {
            gameScreen.newFight(startFight(1));
            Game.gui.gameScreen.glassPane.setVisible(true);
            Game.gui.gameScreen.cardLayout.show(Game.gui.gameScreen.centerContainer, "main");
            Game.unslotAllCards();
            gameScreen.center.revalidate();
            gameScreen.center.repaint();
            showScreen(gameScreen);
        });
        menu.add(encounter1);
        return menu;
    }


    public NorthPanel startFight(int num) {
        switch(num) {
            case 1:
                ArrayList<Enemy> entities = new ArrayList<>();
                entities.add(new Goblin());
                entities.add(new Orc());
                entities.add(new Slime());
                return new NorthPanel(entities);
            case 2:
                ArrayList<Enemy> entities1 = new ArrayList<>();
                Goblin goblin = new Goblin();
                entities1.add(goblin);
                entities1.add(new Goblin());
                NorthPanel encounter = new NorthPanel(entities1);
                //encounter.positionEnemy(goblin, 950, 150);
                encounter.createSpawnZone(750, 175, 100, 100);
                encounter.createSpawnZone(900, 175, 100, 100);
                encounter.populateSpawnZones();
                encounter.initAniBounds();
                return encounter;
        }
        return null;
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
