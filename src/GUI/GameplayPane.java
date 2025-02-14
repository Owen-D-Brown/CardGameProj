package GUI;

import Entities.Card;
import Entities.Player;
import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

//This is the glass pane overlaying the main GUI. This is where all interactable elements are placed. The GUI Below this is not directly interactable.
public class GameplayPane extends JPanel {

    //Variables
    public static ArrayList<CardSlot> cardSlots = new ArrayList<>(); //This array list holds the card slots the player has to use.

    //These are used to calculate the x and y coordinates of the card slots..
    int x;
    int y;

    //Constructor
    public GameplayPane() {

        //Setting up this panel.
        setOpaque(false);
        setVisible(true);
        setLayout(null);

        //Setting up the Play Hand Button
        PlayHandBtn playBtn = new PlayHandBtn();
        add(playBtn);
        playBtn.setLocation(700, 600);
        playBtn.setVisible(true);


    }

    //This is a small method to do the math needed for calculating card coordinates.
    public void cardSlotMath() {
        x = (1000 - 160) / 2;
        y = (int) ((int) (((Config.frameSize.height * 0.3) - 210) / 2) + Config.frameSize.height *0.3);
    }

    //Working on the add cardslot functionality
    public void addCardSlot() {
        int currentNoOfSlots = cardSlots.size();
        int xToAdd;
        int yToAdd;
        cardSlotMath();
        switch(currentNoOfSlots) {
            case 0:
                xToAdd = 400;
                yToAdd = 700;
                break;
            case 1:
                cardSlots.get(0).setLocation(300, y);
                xToAdd = 500;
                yToAdd = 700;
                break;
            case 2:
                cardSlots.get(0).setLocation(100, y);
                cardSlots.get(1).setLocation(300, y);
                xToAdd = 500;
                yToAdd = 700;
                break;
            case 3:
                xToAdd = 700;
                yToAdd = 700;
                break;
            default:
                System.out.println("ERROR ADDING NEW CARD SLOT");
                return;
        }
        cardSlots.add(new CardSlot(xToAdd, y));
        Game.gui.revalidate();
        Game.gui.repaint();
    }

    public void removeCardSlot() {
        y = (int) ((int) (((Config.frameSize.height * 0.3) - 210) / 2) + Config.frameSize.height *0.3);
        int currentNoOfSlots = cardSlots.size();
        if (currentNoOfSlots == 0) {
            System.out.println("No card slots to remove!");
            return;
        }

        // Remove the last slot
        cardSlots.removeLast();

        // Reposition remaining slots
        switch (currentNoOfSlots - 1) {  // Adjust because one slot is removed
            case 0:
                // No slots left, nothing to reposition
                break;
            case 1:
                cardSlots.get(0).setLocation(400, y);
                break;
            case 2:
                cardSlots.get(0).setLocation(300, y);
                cardSlots.get(1).setLocation(500, y);
                break;
            case 3:
                cardSlots.get(0).setLocation(100, y);
                cardSlots.get(1).setLocation(300, y);
                cardSlots.get(2).setLocation(500, y);
                break;
        }

        // Refresh GUI
        Game.gui.revalidate();
        Game.gui.repaint();
    }


    //Method for drawing a card to the hand. Call this any time you need a player to draw a card. IMPORTANT METHOD
    public void drawCard() {

        if(Game.player.cards.size() <= 0) {
            for(int i = 0; i<Game.player.discard.size(); i++) {
                Game.player.cards.add(Game.player.discard.get(i));

            }
            Collections.shuffle(Game.player.cards);
            Game.player.discard.clear();
        }
        Card drawedCard = Game.player.cards.getFirst();
        Game.player.cards.removeFirst();
        Game.player.hand.add(drawedCard);
        add(drawedCard);
        //Setting up card size and coordinates.
        int size = Game.player.hand.size();
        //System.out.println(size);
        int xcord = ((size-1) * Config.cardSize.width)+(size*10);
        drawedCard.initX = xcord;
        drawedCard.initY = 700;
        //System.out.println(xcord);
        drawedCard.setLocation(xcord, 700);
        drawedCard.setVisible(true);
        drawedCard.setSize(Config.cardSize.width, Config.cardSize.height);

        //Refreshing this pane to display changes.
        this.revalidate();
        this.repaint();
    }

    //This method is called from each Card when it has a mouse released event via the mouseHandler.
    //It checks if the card just released intersects the card slot. If it does, it places it in it. If not, it resets its position.
    public static void checkIntersect(Card c) {
        //When mouse is released on card, iterate through the card slots on this pane
        for(CardSlot slot : cardSlots) {

                if(slot.intersects(c.getBounds())) {//If the card is released while intersecting this card slot.
                    c.setLocation(slot.x+5, slot.y+5);//Center the card in the card slot.
                    c.primed = true;//SET THE CARD'S STATUS TO PRIMED - TO BE REFINED
                    slot.slotCard(c);//Passing the card instance to the cardSlot object.

                    return;//Stop checking the card slots and leave this method.
                }
                else
                {
                    //figure out the logic for removing the card from the card slot when its dragged out of it without overriding other slots.
                    //if(slot.slottedCard != null && !slot.intersects(c.getBounds()))
                    //  slot.slotCard(null);//This is overriding the cards played before it,
                }
            }
        //This code should only be reached if the card is not intersecting anything. In which case, it is not primed and reset to the hand.
        c.setLocation(c.initX, c.initY);
        c.primed = false;

    }

    //This int is for tracking the card slot we are currently itterating through.
    public static int currentCardIndex = 0;

    //This method is triggered by the PLAY HAND BUTTON. This controls the flow of resolving the cards. When we get here, the game runs itself until the round starts again.
    //PlayHand Button triggers this method. This method calls CardSlot.resolve(). It does this for the first slot, then the second, and so on.
    public void resolveNextCard() {
        System.out.println("Resolving Card Slot");
        //If we have resolved all the card slots.
        if (currentCardIndex >= GameplayPane.cardSlots.size()) {
            Game.changeStateToEnemyTurn();
            //Game.runEnemyTurn();
            currentCardIndex = 0;
            return; //No more cards to process
        }

        //Getting the slot we are currently working on.
        CardSlot slot = GameplayPane.cardSlots.get(currentCardIndex);

        //Check if there is a card in the slot
        if (slot.slottedCard != null) {

            //Resolve the slot, passing a callback function. This function will run when slot.resolve finishes.
            //In this case, it increases the index and calls itself again. This allows it to iterate through each
            //Slot sequentially. Callback triggers when dissolve + animation finish.
            slot.resolve(() -> {
                currentCardIndex++;
                Game.player.hand.remove(slot.slottedCard);
                Game.player.discard.add(slot.slottedCard);
                remove(slot.slottedCard);
                slot.slottedCard = null;
                resolveNextCard(); // Move to next card
            });
        }

        else //If the card slot doesn't have a card in it.
        {
            currentCardIndex++;
            resolveNextCard(); // Skip empty slots
        }
    }

    //Overriding the paint method
    @Override
    public void paint(Graphics g) {

        super.paint(g);
        g.setColor(Color.white);

        //For each of the card slots present in the Array - IE. The card slots the player has
        for(CardSlot slot : cardSlots ) {
            g.drawRect(slot.x, slot.y, (int) slot.getWidth(), (int) slot.getHeight());//This draws the Card Slot's borders. The CenterPanel PaintMethod takes these bounds to draw the black slots on the board.
        }

        for (Component comp : getComponents()) {
            if (comp instanceof Card card && card.primed && card.draggingArrow && card.arrowEnd != null) {
                g.setColor(Color.RED);
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(5));
                g.drawLine(card.arrowStart.x, card.arrowStart.y, card.arrowEnd.x, card.arrowEnd.y);
            }
        }
    }
}
