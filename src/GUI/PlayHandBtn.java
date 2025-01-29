package GUI;

import Entities.AttackPlane;
import Entities.Card;
import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PlayHandBtn extends JComponent implements MouseListener {

    private Rectangle playBtn = new Rectangle(0, 0, 500, 200);

    public PlayHandBtn() {
        addMouseListener(this);
        setSize(new Dimension(200, 50));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.cyan);
        g.fillRect(playBtn.x, playBtn.y, playBtn.width, playBtn.height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (playBtn.contains(e.getX(), e.getY()) && Game.player.cards.get(0).primed) {
            System.out.println("click");

            Card card = Game.player.cards.get(0);

            final int targetFPS = 20;
            final int delay = 1000 / targetFPS; // Delay in milliseconds (20 FPS)

            int initialWidth = card.getWidth();
            int initialHeight = card.getHeight();
            int initialX = card.getX();
            int initialY = card.getY();

            // Shrink step values
            int shrinkStep = 15;

            Timer timer = new Timer(delay, null); // Create timer with delay
            ActionListener animation = evt -> {
                int currentWidth = card.getWidth();
                int currentHeight = card.getHeight();

                if (currentWidth <= 0 || currentHeight <= 0) {
                    ((Timer) evt.getSource()).stop(); // Stop the timer when the card is fully shrunk
                    card.setVisible(false); // Hide the card after shrinking
                    Game.gameState = Config.GameState.CARD_RESOLUTION;
                    Game.player.cards.get(0).effect();
                    //
                    int duration = 2000;  // 2 seconds in milliseconds
                    int FPS = 20;   // 20 updates per second
                    int interval = 1000 / FPS;  // Interval in milliseconds for 20 FPS (50ms)
                    long startTime = System.currentTimeMillis(); // Start time of the animation
                    // Timer to trigger every 50ms (20 times per second)
                    Timer animationTimer = new Timer(interval, ev -> {
                        // This code will be called 20 times per second
                        System.out.println("in the loop");
                        Game.gui.attackPlane.repaint();
                        Game.gui.attackPlane.updateAnimations();


                        // After 2 seconds, stop the timer
                        if (System.currentTimeMillis() - startTime >= duration) {
                            ((Timer) ev.getSource()).stop();  // Stop the timer
                        }
                    });


                    animationTimer.start(); // Start the timer
                    //
                    Game.gameState = Config.GameState.ENEMY_PHASE;
                    Game.enemy.attack();
                    Game.gameState = Config.GameState.CARD_PLAY;
                    Game.gui.glassPane.drawCard();
                    return;
                }

                // Shrink the card size
                currentWidth = Math.max(0, currentWidth - shrinkStep);
                currentHeight = Math.max(0, currentHeight - shrinkStep);

                // Adjust the position to keep the card centered while shrinking
                int x = initialX + (initialWidth - currentWidth) / 2;
                int y = initialY + (initialHeight - currentHeight) / 2;

                // Update card bounds
                card.setBounds(x, y, currentWidth, currentHeight);

                // Repaint the GUI to reflect changes
                Game.gui.revalidate();
                Game.gui.repaint();
            };

            timer.addActionListener(animation); // Add animation logic to the timer
            timer.start(); // Start the timer



        }
    }



    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
