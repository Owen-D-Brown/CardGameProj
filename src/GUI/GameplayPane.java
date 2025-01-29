package GUI;

import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;

public class GameplayPane extends JPanel {

    private Rectangle playarea = new Rectangle(10, 10, 500, 500);


    public GameplayPane() {
        setOpaque(false);
        setVisible(true);
        setLayout(null);



        PlayHandBtn playBtn = new PlayHandBtn();
        add(playBtn);
        playBtn.setLocation(700, 600);
        playBtn.setVisible(true);
        add(Game.player.cards.get(0));
        Game.player.cards.get(0).setLocation(400, 700);
    }

    public void drawCard() {
        add(Game.player.cards.get(0));
        Game.player.cards.get(0).setLocation(400, 700);
        Game.player.cards.get(0).setVisible(true);
        Game.player.cards.get(0).setSize(Config.cardSize.width, Config.cardSize.height);
        this.revalidate();
        this.repaint();
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);

    }


}
