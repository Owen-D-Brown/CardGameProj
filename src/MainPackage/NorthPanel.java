package MainPackage;

import Entities.Enemy;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class NorthPanel extends JPanel {

    public ArrayList<Enemy> enemies = new ArrayList<>();
    private BufferedImage background;

    public NorthPanel() {
        setLayout(null);
        background = loadImage("UpperBackground.png");
        setBackground(Color.red);
        setPreferredSize(new Dimension(100, (int)(Config.frameSize.height * 0.3)));
        addEnemy();
        addEnemy();
        revalidate();
        repaint();
    }

    private BufferedImage loadImage(String filename) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("Resources/" + filename)) {
            if (is == null) {
                throw new IOException("Resource not found: " + filename);
            }
            BufferedImage original = ImageIO.read(is);
            return original.getSubimage(0, 0, original.getWidth(), Math.min(333, original.getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void addEnemy() {
        if(enemies.size() == 3) {
            System.out.println("Already 3 enemies in enemies array. -NorthPanel");
            return;
        }
        else {

            Enemy enemyAdded = new Enemy();
            enemies.add(enemyAdded);
            add(enemies.getLast());
            //
             int x = 600;
             int y = 150;
             x = x+(enemies.size()*enemyAdded.getWidth())+10;
            //
            enemies.getLast().setBounds(x, y, enemyAdded.getWidth(), enemyAdded.getHeight());
            revalidate();
            repaint();
            System.out.println("Enemy added at: "+x+" - NorthPanel");
        }
    }
    public void repositionEnemies() {
        int x = 710;
        int y = 150;

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).setBounds(x, y, enemies.get(i).getWidth(), enemies.get(i).getHeight());
            x = x + enemies.get(i).getWidth() + 10; // Increment x for the next enemy
            System.out.println("Enemy repositioned to: "+x);
        }

        revalidate();
        repaint();
    }

    public void removeEnemy(int index) {
        if(enemies.size() >0) {
            remove(enemies.get(index));
            enemies.remove(index);
            repositionEnemies();
            revalidate();
            repaint();
        }
        else
        {
            System.out.println(("No Enemies In the enemies array - NorthPanel"));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);


    }

}
