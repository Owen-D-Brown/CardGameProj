package GUI;

import Entities.AttackPlane;
import Entities.Card;
import Entities.Enemy;
import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;

public class MainGui extends JFrame {
   Game game;
    public MainGui(Game game) {
        this.game = game;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("CardGame");
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(Config.frameSize);
        setLayout(new BorderLayout());

        init();

        revalidate();
        repaint();
    }
    public GameplayPane glassPane;
    public AttackPlane attackPlane;
    private void init() {
        Card card = new Card();
        card.setBounds(10, 10, card.getWidth(), card.getHeight());
        Game.player.cards.add(card);

        //North Panel
        JPanel north = new JPanel();
        north.setLayout(null);
        north.setBackground(Color.red);
        north.setPreferredSize(new Dimension(100, (int) (Config.frameSize.height * 0.3)));
        this.add(north, BorderLayout.NORTH);
        Enemy enemy = Game.enemy;
        north.add(enemy);
        enemy.setBounds(700, 170, enemy.getWidth(), enemy.getHeight());
        attackPlane = new AttackPlane();
        north.add(attackPlane);
        attackPlane.setBounds(100, 200, attackPlane.getWidth(), attackPlane.getHeight());


        //Center Panel
        CenterPanel center = new CenterPanel();

        this.add(center, BorderLayout.CENTER);

        //South Panel
        SouthPanel southPanel = new SouthPanel();
        this.add(southPanel, BorderLayout.SOUTH);


        glassPane = new GameplayPane();
        glassPane.setOpaque(false);

        setGlassPane(glassPane);
        glassPane.setVisible(true);

    }
}
