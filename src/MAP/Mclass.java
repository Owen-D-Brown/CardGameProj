package MAP;

import javax.swing.JFrame;

public class Mclass {
    public static void main (String[] args) {

        // Basic Panel here
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Store");

        gamePanel GPanel = new gamePanel();
        window.add(GPanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        GPanel.startGameThread(); //start the game thread
    }
}
