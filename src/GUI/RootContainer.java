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
    public MapGui mapScreen;
    private JPanel containerPanel; // The main container using BorderLayout
    public Game game;

    public RootContainer(Game game) {
        setTitle("Card Game");
        setSize(Config.frameSize.width + 100, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        this.game = game; // Store game reference

        // Panel that holds the swappable screens
        containerPanel = new JPanel(new BorderLayout());
        add(containerPanel, BorderLayout.CENTER);

        // Initialize screens
        gameScreen = new BattleGUI(game);
        menuScreen = createMenuScreen();
        mapScreen = new MapGui();

        // Add and configure GlassPane
        MapGameplayPane glassPane = new MapGameplayPane(this);
        setGlassPane(glassPane);
        glassPane.setVisible(true); // Ensure it's visible

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

        JButton mapTestButton = new JButton("Map01 Test");//added button to test map function on launch
        mapTestButton.addActionListener(e -> showScreen(mapScreen));
        menu.add(mapTestButton);
        return menu;
    }


    public NorthPanel startFight(int num) {
        switch (num) {
            case 1:
                ArrayList<Enemy> entities = new ArrayList<>();
                entities.add(new Goblin());
                entities.add(new Orc());
                entities.add(new Slime());
                return new NorthPanel(entities);
            case 2:
                ArrayList<Enemy> entities1 = new ArrayList<>();
                entities1.add(new Goblin());
                entities1.add(new Goblin());
                return new NorthPanel(entities1);
        }
        return null;
    }

    public void showScreen(JPanel newScreen) {
        if (newScreen == null) {
            System.err.println("Error: Attempted to show a null screen!");
            return;
        }

        if (currentScreen != null) {
            containerPanel.remove(currentScreen);
        }

        currentScreen = newScreen;
        containerPanel.add(newScreen, BorderLayout.CENTER);
        containerPanel.revalidate();
        containerPanel.repaint();

        // If switching to the map screen, activate and resize the GlassPane
        if (newScreen instanceof MapGui) {
            MapGui mapGui = (MapGui) newScreen;
            MapGameplayPane glassPane = (MapGameplayPane) getGlassPane();

            if (mapGui.getMapData() != null) {
                glassPane.setMapData(mapGui.getMapData());

                // Set the glass pane size to match the MapPanel inside MapGui
                Component mapPanel = mapGui.getMapPanel();
                if (mapPanel != null) {
                    glassPane.setBounds(mapPanel.getBounds());
                }

                glassPane.setVisible(true);
                System.out.println("MapGlassPane activated and resized.");
            } else {
                System.err.println("Warning: Map data is null, cannot update GlassPane.");
            }
        } else {
            // Hide the GlassPane when switching away from the map
            getGlassPane().setVisible(false);
            System.out.println("MapGlassPane deactivated.");
        }
    }
}