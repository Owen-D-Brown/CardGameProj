package GUI;

import Entities.Player;
import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;

public class SouthPanel extends JPanel {
    JLabel cardsInDeck;
    JLabel cardsInDiscard;
    public SouthPanel() {

        setBackground(Color.darkGray);
        setPreferredSize(new Dimension(1000, (int) (Config.frameSize.height * 0.3)));
        setLayout(new BorderLayout());

        JPanel deets = new JPanel();
        deets.setLayout(null);
        deets.setPreferredSize(new Dimension(150, 333));
        deets.setBackground(Color.blue);
        add(deets, BorderLayout.EAST);

        cardsInDeck = new JLabel("Deck: "+Game.player.cards.size());
        cardsInDeck.setBounds(0, 0, 100, 100);
        deets.add(cardsInDeck);

        cardsInDiscard = new JLabel("Discard: "+Game.player.discard.size());
        cardsInDiscard.setBounds(0, 100, 100, 100);
        deets.add(cardsInDiscard);


    }

    public void updateSouthPanel() {
        cardsInDeck.setText("Deck: "+Game.player.cards.size());
        cardsInDiscard.setText("Discard: "+Game.player.discard.size());
        this.revalidate();
        this.repaint();
    }

}
