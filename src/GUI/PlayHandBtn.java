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

//This class is the button that triggers the cards on the field to play. This button moves the game state and starts the round process.
public class PlayHandBtn extends JComponent implements MouseListener {

    //Setting the bounds of the Visual rectangle representing the button.
    private Rectangle playBtn = new Rectangle(0, 0, 500, 200);

    //Constructor
    public PlayHandBtn() {
        addMouseListener(this);
        setSize(new Dimension(200, 50));
    }

    //Overriding the paint method.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.cyan);
        g.fillRect(playBtn.x, playBtn.y, playBtn.width, playBtn.height);//Drawing the rectangle for the button.
    }



    @Override
    public void mouseClicked(MouseEvent e) {
        if (GameplayPane.currentCardIndex == 0) { // Only start if it's not already running
            //Check to make sure there is at least one card played.
            Game.changeStateToCardResolution();
            Game.gui.glassPane.resolveNextCard();
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
