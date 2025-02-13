package GUI;

import javax.swing.*;
import java.awt.*;

public class TrinketSideBar extends JPanel {

    public TrinketSideBar() {
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(100, 1000));
        this.setLayout(new GridLayout(5,1,0,5));
        int i = 0;
        while(i<5) {
            JPanel temp = new JPanel();
            temp.setBorder(BorderFactory.createLineBorder(Color.black, 2));
            add(temp);
            i++;
        }

    }
}
