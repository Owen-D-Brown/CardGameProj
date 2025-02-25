package MainPackage;

import Entities.*;
import GUI.GameplayPane;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class NorthPanel extends JPanel {

    //Variables
    public ArrayList<Enemy> enemies = new ArrayList<>();//ArrayList that holds the enemies present in this encounter.
    private BufferedImage background;//Image holding the background for this encounter.
    private Player player = Game.player;//Getting a handier pointer to the Player class in Game.
    public AttackPlane attackPlane;


    public NorthPanel(ArrayList<Enemy> enemies) {

        //Initializing this panel
        setLayout(null);
        background = loadImage("UpperBackground.png");
        setBackground(Color.red);
        setPreferredSize(new Dimension(100, (int)(Config.frameSize.height * 0.3)));

        //Iterating through enemies passes to this constructor, and adding them.
        for(Enemy enemy : enemies)
            addEnemy(enemy);

        //Initializing the player.
        player.setBounds(100, 200, player.getWidth(), player.getHeight());
        add(player);

        //Initializing the AttackPlane.
        attackPlane = new AttackPlane();
        attackPlane.setBounds(100, 200, attackPlane.getWidth(), attackPlane.getHeight());
        add(attackPlane);

        //Ensuring that the state of the game is reset every time a new encounter is made. Reset card slots, player deck, etc.
        resetFightState();
    }

    public void resetFightState() {
        if(Config.debug)
            System.out.println("\n--* NorthPanel.resetFightState() RUNNING *--");

        GameplayPane.currentCardIndex = 0;
        Game.gui.gameScreen.glassPane.unslotAllCards();
        Game.player.resetDeck();
        Game.gui.gameScreen.glassPane.updateHandRender();
        Game.changeStateToCardPlay();
        revalidate();
        repaint();

        if(Config.debug)
            System.out.println("--* NorthPanel.resetFightState() COMPLETE *--");
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


    public void addEnemy(Enemy enemy) {
        if(enemies.size() == 3) {
            System.out.println("Already 3 enemies in enemies array. -NorthPanel");
            return;
        }
        else {

            Enemy enemyAdded = enemy;
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
        FloatingText.render(g);
    }

}
