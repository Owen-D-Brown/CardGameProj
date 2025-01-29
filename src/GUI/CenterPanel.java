package GUI;

import MainPackage.Config;

import javax.swing.*;
import java.awt.*;

public class CenterPanel extends JPanel {

    public CenterPanel() {
        setBackground(Color.green);
        setPreferredSize(new Dimension(100, (int) (Config.frameSize.height * 0.3)));
    }

    public static int rectWidth;
    public static int rectHeight;
    public static Rectangle rect;
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Get the panel's dimensions
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Rectangle dimensions (example values)
         rectWidth = 170;
         rectHeight = 235;

        // Calculate the coordinates to center the rectangle
        int x = (panelWidth - rectWidth) / 2;
        int y = (panelHeight - rectHeight) / 2;

        // Draw the rectangle at the calculated position
        g.setColor(Color.black);
        g.fillRect(x, y, rectWidth, rectHeight);
        rect = new Rectangle(x, y, rectWidth, rectHeight);
    }
}

