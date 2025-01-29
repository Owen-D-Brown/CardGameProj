package GUI;

import Entities.Player;
import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;

public class SouthPanel extends JPanel {

    public SouthPanel() {

        setBackground(Color.darkGray);
        setPreferredSize(new Dimension(100, (int) (Config.frameSize.height * 0.3)));
        setLayout(new BorderLayout());

        JPanel west = new JPanel();
        west.setBackground(Color.white);
        west.setPreferredSize(new Dimension((int) (Config.frameSize.width * 0.3), 10));
        add(west, BorderLayout.WEST);

        west.setLayout(null);
        Player player = Game.player;
        west.add(player);
        player.setBounds(0, 0, player.getWidth(), player.getHeight());

    }

}
