package GUI;

import CombatMap.MapGameplayPane;
import CombatMap.MapGui;
import Entities.*;
import MainPackage.Config;
import MainPackage.Game;
import MainPackage.NorthPanel;
import MAP.gamePanel; // Import your game panel

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class RootContainer extends JFrame {

    private JPanel currentScreen; // Tracks which panel is in the center
    public BattleGUI gameScreen;
    public JPanel menuScreen;
    public MapGui mapScreen;
    private JPanel containerPanel; // The main container using BorderLayout
    public Game game;
    public gamePanel worldPanel; // Reference to the gamePanel (Shop System)

    public RootContainer(Game game) {
        setTitle("Card Game");
        setSize(Config.frameSize.width+100, Config.frameSize.height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        this.game = game; // Store game reference

        // Panel that holds the swappable screens
        containerPanel = new JPanel(new BorderLayout());
        add(containerPanel, BorderLayout.CENTER);

        // Initialize screens
        gameScreen = new BattleGUI(game);
        menuScreen = new MainMenuPanel(this);
        //menuScreen = createMenuScreen();
        worldPanel = new gamePanel(); // Initialize shop system
        mapScreen = new MapGui();

        // Add and configure GlassPane
        MapGameplayPane glassPane = new MapGameplayPane(this);
        setGlassPane(glassPane);
        glassPane.setVisible(true); // Ensure it's visible

        // Start on the menu
        showScreen(menuScreen);

        setVisible(true);
    }

    public JPanel createMenuScreen() {
        JPanel menu = new JPanel();
        menu.setBackground(Color.BLACK);
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> {
            try {
                gameScreen.newFight(startFight(2));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Game.gui.gameScreen.glassPane.setVisible(true);
            Game.gui.gameScreen.cardLayout.show(Game.gui.gameScreen.centerContainer, "main");
            Game.unslotAllCards();
            gameScreen.center.revalidate();
            gameScreen.center.repaint();
            showScreen(gameScreen);

        });
        menu.add(startButton);
        JButton encounter1 = new JButton("Encounter fresh");
        encounter1.addActionListener(e -> {
            try {
                gameScreen.newFight(startFight(1));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Game.gui.gameScreen.cardLayout.show(Game.gui.gameScreen.centerContainer, "main");
            Game.unslotAllCards();
            gameScreen.center.revalidate();
            gameScreen.center.repaint();
            showScreen(gameScreen);
        });
        menu.add(encounter1);

        // Open the shop system
        JButton worldButton = new JButton("Open World");
        worldButton.addActionListener(e -> {
            showScreen(worldPanel); // Switch to gamePanel (Shop System)
            worldPanel.startGameThread(); // Start the game loop if it's not running
            worldPanel.requestFocusInWindow();
        });
        menu.add(worldButton);


        JButton mapTestButton = new JButton("Map01 Test");//added button to test map function on launch
        mapTestButton.addActionListener(e -> showScreen(mapScreen));
        menu.add(mapTestButton);


        JButton randomFightButton = new JButton("Random Fight");
        randomFightButton.addActionListener(e -> {
            try {
                gameScreen.newFight(startRandomFight(30, 0)); // Example: maxWeight = 10, minWeight = 0
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Game.gui.gameScreen.glassPane.setVisible(true);
            Game.gui.gameScreen.cardLayout.show(Game.gui.gameScreen.centerContainer, "main");
            Game.unslotAllCards();
            gameScreen.center.revalidate();
            gameScreen.center.repaint();
            showScreen(gameScreen);
        });
        menu.add(randomFightButton);
        return menu;
    }


    public NorthPanel startFight(int num) throws IOException {
        switch(num) {
            case 1:
                ArrayList<Enemy> entities = new ArrayList<>();

               // entities.add(new SatyrFemale());
                entities.add(new Knight());
                entities.add(new Goblin());
                entities.add(new Orc());

               // entities.add(new Orc());


                return new NorthPanel(entities);
            case 2:
                ArrayList<Enemy> entities1 = new ArrayList<>();
                Goblin goblin = new Goblin();
                Orc orc = new Orc();
                entities1.add(goblin);
                entities1.add(orc);
                NorthPanel encounter = new NorthPanel(entities1);
                //encounter.positionEnemy(goblin, 950, 150);
             //   encounter.createSpawnZone(750, 175, 100, 100);
              //  encounter.createSpawnZone(900, 175, 100, 100);
              //  encounter.populateSpawnZones();

                return encounter;
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

    public NorthPanel startRandomFight(int maxWeight, int minWeight) throws IOException {
        // ✅ Define available unique enemies
        ArrayList<Enemy> availableEnemies = new ArrayList<>();
        availableEnemies.add(new Goblin()); // Weight: 5
        availableEnemies.add(new Orc());    // Weight: 10
        // Add more enemy types as needed...

        ArrayList<Enemy> selectedEnemies = new ArrayList<>();
        HashSet<Class<?>> selectedEnemyTypes = new HashSet<>(); // Track selected enemy types
        int remainingWeight = maxWeight;
        Random random = new Random();

        // ✅ Filter valid enemies based on minWeight & maxWeight
        ArrayList<Enemy> validEnemies = new ArrayList<>();
        for (Enemy e : availableEnemies) {
            if (e.getWeight() >= minWeight && e.getWeight() <= maxWeight) {
                validEnemies.add(e);
            }
        }

        // ✅ Randomly select unique enemies while staying within weight & limit (3 enemies max)
        while (!validEnemies.isEmpty() && selectedEnemies.size() < 3) {
            Enemy chosenEnemy = validEnemies.get(random.nextInt(validEnemies.size()));

            // ✅ Ensure the enemy type is unique in this battle
            if (!selectedEnemyTypes.contains(chosenEnemy.getClass()) && remainingWeight - chosenEnemy.getWeight() >= 0) {
                selectedEnemies.add(chosenEnemy);
                selectedEnemyTypes.add(chosenEnemy.getClass()); // ✅ Store the type
                remainingWeight -= chosenEnemy.getWeight();
            }

            // ✅ Stop if all unique enemy types have been used
            if (selectedEnemyTypes.size() == validEnemies.size()) break;
        }

        // ✅ Debugging: Print selected enemies
        System.out.println("Randomized Combat - Selected Unique Enemies:");
        for (Enemy e : selectedEnemies) {
            System.out.println(e.getClass().getSimpleName() + " - Weight: " + e.getWeight() + " | Instance: " + e);
        }

        // ✅ Create the combat panel
        NorthPanel encounter = new NorthPanel(selectedEnemies);

        // ✅ Define spawn positions for up to 3 enemies
        int[][] spawnPositions = { {500, 175}, {650, 175}, {800, 175} };

        for (int i = 0; i < selectedEnemies.size(); i++) {
            Enemy enemy = selectedEnemies.get(i);
            int x = spawnPositions[i][0];
            int y = spawnPositions[i][1];

            encounter.createSpawnZone(x, y, 100, 100);

            // ✅ Explicitly assign the enemy's position
            enemy.setBounds(x, y, 100, 100);

            System.out.println("Enemy " + enemy.getClass().getSimpleName() + " placed at: " + x + ", " + y);
        }

        encounter.populateSpawnZones(); // ✅ Assign enemies to positions
        encounter.initPlayerAniBounds(); // ✅ Ensure animations update correctly

        return encounter;
    }
}