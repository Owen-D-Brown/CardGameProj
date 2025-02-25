package GUI;

import Entities.*;
import MainPackage.Config;
import MainPackage.Game;
import MainPackage.NorthPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class BattleGUI extends JPanel {

    // Components
    public Game game;
    public GameplayPane glassPane; // The custom glass pane.

    public CenterPanel center;
    public NorthPanel northPanel;
    public SouthPanel southPanel;
    private JLayeredPane layeredPane; // Allows us to overlay the glass pane.
    public TrinketSideBar trinketSideBar;
    public JPanel centerContainer;
    public CardLayout cardLayout;
    public JPanel gamePanel;
    public JPanel rewardScreen;

    // Constructor
    public BattleGUI(Game game) {

        //Initializing this panel
        this.game = game;
        setLayout(new BorderLayout());
        setPreferredSize(Config.frameSize);

        //Create a JLayeredPane to hold game components + glass pane
        //It's a layered pane because the visual GUI components sit under the glass pane.
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(Config.frameSize);
        layeredPane.setLayout(null); // Allows absolute positioning
        add(layeredPane, BorderLayout.CENTER);

        //Initialize components inside the layered pane
        init();

        //Refresh
        revalidate();
        repaint();
    }

    //Initializing GUI components inside the layered pane. - This is where the main North, Center, and South of the GUI are instantiated.
    private void init() {

        //Game panel (holds all game components)
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBounds(0, 0, Config.frameSize.width, Config.frameSize.height);
        layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER); // Base layer


        //Creating the center of the battle GUI. This is where card slots exist - and also where the reward screen appears after a fight.
        //As such, it has a centerContainer with a card layout. We use this layout to swap between the rewards screen and the playable center screen with CardSlots.
        centerContainer = new JPanel();
        cardLayout = new CardLayout();
        centerContainer.setLayout(cardLayout);

        //Main center panel with card slots for gameplay.
        center = new CenterPanel();
        gamePanel.add(centerContainer, BorderLayout.CENTER);
        centerContainer.add(center, "main");

        //Reward screen that appears after each successful fight.
        rewardScreen = new JPanel();
        rewardScreen.setBackground(Color.black);
        rewardScreen.setLayout(null);
        centerContainer.add(rewardScreen, "rewardScreen");




        //Display the gameplay screen first - when the encounter (NorthPanel instance) is created.
        cardLayout.show(centerContainer, "main");

        //Button to bring you back to the main menu,.
        JButton toMenu = new JButton("to menu");
        toMenu.setBounds(0, 0, 200, 50);
        toMenu.addActionListener(e -> {
            Game.player.resetDeck();//Rest users deck
            Game.gui.showScreen(Game.gui.menuScreen);//Swap to menu
        });
        rewardScreen.add(toMenu);

        //South panel. This is the simplest panel. It's just the background - The actual cards exist and are drawn in the glass pane.
        southPanel = new SouthPanel();
        gamePanel.add(southPanel, BorderLayout.SOUTH);

        //Side bar for the user's trinkets - currently a WIP.
        trinketSideBar = new TrinketSideBar();
        add(trinketSideBar, BorderLayout.WEST);

        //Glass Pane - This overlays everything inside BattleGUI and is where all functionality is located.
        //Cards are drawn here - CardSlots exist here (but are drawn on the centerPanel).
        glassPane = new GameplayPane();
        glassPane.setOpaque(false);
        glassPane.setBounds(0, 0, Config.frameSize.width, Config.frameSize.height);
        layeredPane.add(glassPane, JLayeredPane.PALETTE_LAYER); //Ensures it stays on top of the layered pane.
        glassPane.setVisible(true);



    }

    public void getLootRewards() {
        //for each enemy - will require changing
        for(int i = 0; i<=northPanel.enemies.size()-1; i++) {
            Map.Entry<String, Object> item = northPanel.enemies.get(i).generateLoot();
            JLabel itemName = new JLabel(String.valueOf(item.getKey()));
            JLabel itemValue = new JLabel(String.valueOf(item.getValue()));
            int iterate = i*70;
            itemName.setBounds(10, 100+iterate, 200, 50);
            itemValue.setBounds(230, 100+iterate, 200, 50);
            itemName.setForeground(Color.white);
            itemValue.setForeground(Color.white);
            rewardScreen.add(itemName);
            rewardScreen.add(itemValue);
            JButton takeLoot = new JButton("TAKE");
            takeLoot.setBounds(450, 100+iterate, 200, 50);
            takeLoot.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(item+" Taken");
                    rewardScreen.remove(itemName);
                    rewardScreen.remove(itemValue);
                    rewardScreen.remove(takeLoot);
                    if(Objects.equals(item.getKey(), "Gold")) {
                        Game.player.giveGold((int) item.getValue());
                    } else {
                        Game.player.giveTrinket(item);
                    }
                    System.out.println("Player gold: "+Game.player.getGold()+"\n Player trinkets: "+Game.player.getTrinkets());
                    trinketSideBar.populateTrinketBar();
                    rewardScreen.revalidate();
                    rewardScreen.repaint();
                }
            });
            rewardScreen.add(takeLoot);
        }
    }

    //Create and load in a new fight encounter. Pass a NorthPanel instance, and it will slot it into the northPanel slot.
    //There are debug print statements here for texting the transition between NorthPanel encounters.
    //MOVE THE CODE RESETTING THE GAMESTATE INTO THIS CLASS. CALL THE METHOD IN NEW FIGHT. ADD GAME CLASS METHOD TO CALL THIS ONE.
    public void newFight(NorthPanel fight) {
        if(Config.debug)
            System.out.println("\n--* BattleGUI.newFight() TRIGGERED IN BattleGUI CLASS *--. CHECKING FOR CORRECT NorthPanel SWAP.\n  *Components Before removal: " + Arrays.toString(this.getComponents())+"*");//We are checking that the northPanel swapped correctly.

        //Checking if there is currently an encounter / NorthPanel added.
        if(this.northPanel != null)
            this.gamePanel.remove(northPanel);//Removing it if one present.

        if(Config.debug)
            System.out.println("  *Components After removal: " + Arrays.toString(this.getComponents())+"*\n--* BattleGui.newFight() FINISHED *--\n");

        //Adding the new fight encounter to the GUI, and setting the northPanel reference to it.
        this.gamePanel.add(fight, BorderLayout.NORTH);
        this.northPanel = fight;
        getLootRewards();
        this.revalidate();
        this.repaint();

    }
}
