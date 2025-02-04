package GUI;

import Entities.AttackPlane;
import Entities.Card;
import Entities.Enemy;
import Entities.Player;
import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;

//Main GUI Class. Sits inside the Game instance. All other components sit inside this instance.
public class MainGui extends JFrame {

    //Properties
    public Game game;
    public GameplayPane glassPane; //This is the panel that overlays the visuals. All interactable objects are placed on this panel.
    public AttackPlane attackPlane; //This is the component in which the card animations play out.
    public CenterPanel center;
    public NorthPanel northPanel;

    //Constructor
    public MainGui(Game game) {

        //Getting a referenceable instance of the game for the GUI to use.
        this.game = game;

        //Configuring this frame.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("CardGame");
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(Config.frameSize);
        setLayout(new BorderLayout());

        //Initializing the components in this frame
        init();

        //Refreshing this frame after adding the components in init().
        revalidate();
        repaint();
    }

    //Initializing components in the GUI.
    private void init() {

        //Card is here for testing purposes.
        Card card = new Card();
        card.setBounds(10, 10, card.getWidth(), card.getHeight());
        Game.player.cards.add(card);

        //North Panel - This is where animations are played.
        northPanel = new NorthPanel();
        this.add(northPanel, BorderLayout.NORTH);


        Player player = Game.player;
        northPanel.add(player);
        player.setBounds(0, 150, player.getWidth(), player.getHeight());

        //Creating the attack plane area for the animations. Adding it to the north panel.
        attackPlane = new AttackPlane();
        northPanel.add(attackPlane);
        attackPlane.setBounds(100, 200, attackPlane.getWidth(), attackPlane.getHeight());

        //Center Panel - This is where the cards are played. This component contains the visuals for that area.
        center = new CenterPanel();
        this.add(center, BorderLayout.CENTER);

        //South Panel
        SouthPanel southPanel = new SouthPanel();
        this.add(southPanel, BorderLayout.SOUTH);

        //Glass pane that overlays the entire window.
        glassPane = new GameplayPane();
        glassPane.setOpaque(false);
        setGlassPane(glassPane);
        glassPane.setVisible(true);

    }
}
