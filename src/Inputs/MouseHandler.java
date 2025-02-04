package Inputs;

import Entities.Card;
import GUI.CenterPanel;
import GUI.GameplayPane;
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


    //method for passing to card slot
    @Override
    public void mouseReleased(MouseEvent e) {

        GameplayPane.checkIntersect(selectedCard);


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