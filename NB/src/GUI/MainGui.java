package GUI;

import Entities.AttackPlane;
import Entities.Card;
import Entities.Player;
import MainPackage.Config;
import MainPackage.Game;
import MainPackage.NorthPanel;

import javax.swing.*;
import java.awt.*;

public class MainGui extends JPanel {

    // Components
    public Game game;
    public GameplayPane glassPane; // The custom glass pane.
    public AttackPlane attackPlane;
    public CenterPanel center;
    public NorthPanel northPanel;
    public SouthPanel southPanel;
    private JLayeredPane layeredPane; // Allows us to overlay the glass pane.
    public TrinketSideBar trinketSideBar;
    // Constructor
    public MainGui(Game game) {
        this.game = game;
        setLayout(new BorderLayout());
        setPreferredSize(Config.frameSize);

        // Create a JLayeredPane to hold game components + glass pane
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(Config.frameSize);
        layeredPane.setLayout(null); // Allows absolute positioning
        add(layeredPane, BorderLayout.CENTER);

        // Initialize components inside the layered pane
        init();

        // Refresh
        revalidate();
        repaint();
    }

    private void init() {
        // Game panel (holds all game components)
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBounds(0, 0, Config.frameSize.width, Config.frameSize.height);
        layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER); // Base layer

        // Setting up main game sections
        northPanel = new NorthPanel();
        gamePanel.add(northPanel, BorderLayout.NORTH);

        Player player = Game.player;
        northPanel.add(player);
        player.setBounds(0, 150, player.getWidth(), player.getHeight());

        attackPlane = new AttackPlane();
        northPanel.add(attackPlane);
        attackPlane.setBounds(100, 200, attackPlane.getWidth(), attackPlane.getHeight());

        center = new CenterPanel();
        gamePanel.add(center, BorderLayout.CENTER);

        southPanel = new SouthPanel();
        gamePanel.add(southPanel, BorderLayout.SOUTH);

        trinketSideBar = new TrinketSideBar();
        add(trinketSideBar, BorderLayout.WEST);

        // **Glass Pane - This overlays everything inside MainGui**
        glassPane = new GameplayPane();
        glassPane.setOpaque(false);
        glassPane.setBounds(0, 0, Config.frameSize.width, Config.frameSize.height);
        layeredPane.add(glassPane, JLayeredPane.PALETTE_LAYER); // Ensures it stays on top

        glassPane.setVisible(true);


    }
}
