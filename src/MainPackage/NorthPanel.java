package MainPackage;

import Entities.*;
import GUI.CardSlot;
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
    public int yFloor = 295;

    /// CONSTRUCTOR
    public NorthPanel(ArrayList<Enemy> enemiesArg) throws IOException {

        //Initializing this panel
        setLayout(null);
        background = loadImage("DungeonUpperPanelP2.png");
        setBackground(Color.red);
        setPreferredSize(new Dimension(100, (int)(Config.frameSize.height * 0.3)));

        //Iterating through enemies passes to this constructor, and adding them.
        for(Enemy enemy : enemiesArg) {
            addEnemy(enemy);
        }

        //Initializing the player.
        player.setBounds(70, yFloor-player.getHeight(), player.getWidth(), player.getHeight());

        add(player);

        //Initializing the AttackPlane.
        attackPlane = new AttackPlane();
        attackPlane.setBounds(0, 0, attackPlane.getWidth(), attackPlane.getHeight());
        add(attackPlane);

        setComponentZOrder(attackPlane, 0);
        //Ensuring that the state of the game is reset every time a new encounter is made. Reset card slots, player deck, etc.
        resetFightState();

        Point relativeOrigin = SwingUtilities.convertPoint(player, player.rangedOrigin, this);
        player.relativeX = relativeOrigin.x;
        player.relativeY = relativeOrigin.y;
        System.out.println(player.relativeX+" "+player.relativeY);
    }



    public void resetFightState() {
        if(Config.debug)
            System.out.println("\n--* NorthPanel.resetFightState() RUNNING *--");

        GameplayPane.currentCardIndex = 0;
        Game.gui.gameScreen.glassPane.unslotAllCards();
        Game.player.resetDeck();
        Game.gui.gameScreen.glassPane.updateHandRender();
        for(CardSlot slot : GameplayPane.cardSlots) {
            slot.isResolved = false;
            slot.currentlyResolving = false;
            slot.slottedCard = null;
        }
        Game.allEnemiesAlive = true;
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
        if (enemies.size() == 3) {
            System.out.println("Already 3 enemies in enemies array. - NorthPanel");
            return;
        }

        enemies.add(enemy);
        add(enemy);

        int baseX = 600;
        int spacing = 70;
        int x = baseX + ((enemies.size() - 1) * (enemy.getWidth() + spacing));


        enemy.setBounds(x, yFloor-enemy.getHeight(), enemy.getWidth(), enemy.getHeight());
        enemy.setStartBounds(this.getBounds());

        revalidate();
        repaint();

        System.out.println("Enemy added at: " + yFloor + " - NorthPanel - " + this.getBounds().toString() + " | " + enemy.startBounds.toString()+" "+player.getBounds().y);
    }

    public void repositionEnemies() {
        int baseX = 600;
        int spacing = 70;

        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            int x = baseX + (i * (enemy.getWidth() + spacing));
            enemy.setBounds(x, yFloor - enemy.getHeight(), enemy.getWidth(), enemy.getHeight());
            System.out.println("Enemy repositioned to: " + x);
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

    public void removeEnemy(Enemy e) {
        System.out.println("removing enemy");
        if(enemies.size() > 0) {

            if (enemies.contains(e)) {

                this.remove(e);
               // enemies.remove(e);
                //repositionEnemies();
                revalidate();
                repaint();
            }
        }
    }

    public void removeEnemyFromArray(Enemy e) {
            int index = enemies.indexOf(e);
            enemies.remove(e);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);

        FloatingText.render(g);
        if(Config.debug){
            for(Rectangle rect : spawnZones) {

                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.red);
                g2.setStroke(new BasicStroke(3));
               // g2.drawRect(rect.x, rect.y, rect.width, rect.height);
            }
        }
    }
    public int playerX;
    public int playerY;

    public void initPlayerAniBounds() {
        playerX = 70;
        playerY = 150;
    }

    ArrayList<Rectangle> spawnZones = new ArrayList<>();

    public void createSpawnZone(int x, int y, int width, int height) {
        Rectangle zone = new Rectangle(x, y, width, height);
        spawnZones.add(zone);
    }

    public void populateSpawnZones() {
        for(int i = 0; i<spawnZones.size();i++) {
            if(enemies.get(i)!=null) {
                Rectangle temp = spawnZones.get(i);
                enemies.get(i).setBounds(temp.x, temp.y, enemies.get(i).getWidth(), enemies.get(i).getHeight());
            }
        }
    }

}
