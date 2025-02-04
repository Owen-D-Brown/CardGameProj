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



    }

}
