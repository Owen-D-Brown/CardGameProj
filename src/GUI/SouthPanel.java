package GUI;

import Entities.Player;
import MainPackage.Config;
import MainPackage.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class SouthPanel extends JPanel {
    JLabel cardsInDeck;
    JLabel cardsInDiscard;
    public SouthPanel() {


        setPreferredSize(new Dimension(1000, (int) (Config.frameSize.height * 0.3)));
        setLayout(new BorderLayout());


        background = loadImage("LowerBackground.png");


    }

    private BufferedImage background;

    private BufferedImage loadImage(String filename) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("Resources/" + filename)) {
            if (is == null) {
                throw new IOException("Resource not found: " + filename);
            }
            BufferedImage original = ImageIO.read(is);
            return original.getSubimage(0, 6, original.getWidth(), Math.min(333, original.getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateSouthPanel() {

        this.revalidate();
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(background, -2, -10, 1000, 400, null);
    }

}
