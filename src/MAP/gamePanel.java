package MAP;

import java.awt.*;
import javax.swing.*;
import Mentity.Mplayer;
import Tiles.TileManager;

public class gamePanel extends JPanel implements Runnable {

    public static final int S_PLAY = 0; //default gamplay here no shop/merchant interaction
    public static final int S_SHOP = 1; // trying to get the player to stop moving when interacting with the merchant/shop
    public static final int S_COMBAT_MAP = 2; // new state for combat map
    public static int gameState = S_PLAY; //default state/play state


    final int screenWidth = 1000;
    final int screenHeight = 1000;

    public final int tileSize = 20;

    int FPS = 60;

    KeyH kh = new KeyH();
    /*
     * Game thread to start and stop, keep program running, for repeating a process
     * over and over
     */
    Thread gameThread;
    Mplayer player = new Mplayer(this, kh);
    public TileManager tileManager;

    public gamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(kh);
        this.setFocusable(true);

        tileManager = new TileManager(kh); // Load map.png as a background
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Game loop logic goes here
    /* GameThread is automatically called via run method */
    /* References for Game Loop Logic:
     * (https://java-design-patterns.com/patterns/game-loop/#programmatic-example-of-game-loop-pattern-in-java)
     * (https://gamedev.stackexchange.com/questions/52841/the-most-efficient-and-accurate-game-loop)
     */

    @Override
    public void run() {
        //here I am settingdrawInt = (1,000,000,000 / FPS)
        // Time per frame in nanoseconds SET
        // 1 billion nanoseconds = 1 second / (60) Fps, 0/01666666666 recurring
        double drawInt = 1000000000 / FPS;
        /*nextDrawT = current system time in nanoseconds + drawInt*/
        double nextDrawT = System.nanoTime() + drawInt;

        while(gameThread != null) {
            System.out.println("Game loop is working !");

            //Setting the current System time into nanoseconds
            long currentTime = System.nanoTime();


            //Update info positions, calling the update method
            update();
            //redraw screen here
            repaint();


            try {
                /*
                 * Convert nanseconds to miliseconds for Thread sleep as it only takes
                 * miliseconds
                 */

                double rTime = nextDrawT - System.nanoTime();
                rTime = rTime / 1000000;
                /*
                 * Prevent negative sleep time and prevent the
                 *  gameloop from taking too long
                 */
                if (rTime < 0) rTime = 0;
                /*
                 * pauses the program for a short time to make sure each frame is
                 * drawn at the correct speed, checks when the next frame should start
                 */
                Thread.sleep((long) rTime);
                // next draw for steady fps, note to self do not break again
                nextDrawT += drawInt;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        player.update();
    }


    //Standard method to draw onto a JPanel
    //super = parent class of the JPanel subclass
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; //2D functions

        // Draw the static map image
        tileManager.render(g2);

        // Draw Player
        player.draw(g2);

        g2.dispose();
    }
}
