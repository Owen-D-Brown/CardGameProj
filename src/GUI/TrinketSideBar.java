package GUI;

import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TrinketSideBar extends JPanel {

    public ArrayList<JPanel> boxes = new ArrayList<>();


    public TrinketSideBar() {
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(100, 1000));
        this.setLayout(new GridLayout(10,1,0,5));
        int i = 0;
        while(i<5) {
            JPanel temp = new JPanel();
            temp.setBorder(BorderFactory.createLineBorder(Color.black, 2));
            temp.setLayout(new GridBagLayout());
            add(temp);
            boxes.add(temp);
            i++;
        }

    }

    public void populateTrinketBar() {
        for(int i = 0; i< Game.player.getTrinkets().size(); i++) {
            System.out.println(Game.player.getTrinkets().get(i).getValue());
            Component comp = (Component) Game.player.getTrinkets().get(i).getValue();
            comp.setPreferredSize(new Dimension(12*6,9*6));
            boxes.get(i).add(comp);
            boxes.get(i).revalidate();
            boxes.get(i).repaint();
            for(Component component: this.boxes.get(i).getComponents()) {
                System.out.println("comp: "+component);
            }
        }
        revalidate();
        repaint();
    }
}
