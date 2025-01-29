package Inputs;

import Entities.Card;
import GUI.CenterPanel;
import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class MouseHandler implements MouseListener, MouseMotionListener {

    private ArrayList<Card> cards = Game.player.cards;
    private Point intialGrab;
    public MouseHandler() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    Card selectedCard;
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getSource() instanceof Card card) {
            selectedCard = card;
            intialGrab = e.getPoint();
            System.out.println("you have selected " + selectedCard);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Rectangle rectangle = CenterPanel.rect;
       // rectangle.x = rectangle.x + (int) (Config.frameSize.height * 0.3);
        rectangle.y = rectangle.y + (int) (Config.frameSize.height * 0.3);
        if(rectangle.intersects(selectedCard.getBounds()) ) {
            selectedCard.setLocation(rectangle.x+10, rectangle.y+10);
            selectedCard.primed = true;
        } else {
            selectedCard.setLocation(startx, starty);
            selectedCard.primed = false;
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    int startx = 400;
    int starty = 700;
    @Override
    public void mouseDragged(MouseEvent e) {

            Point current = e.getLocationOnScreen();
            SwingUtilities.convertPointFromScreen(current, selectedCard.getParent());
            int x = current.x - intialGrab.x;
            int y = current.y - intialGrab.y;

            selectedCard.setLocation(x, y);
            selectedCard.getParent().repaint();

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}