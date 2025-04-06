package GUI;

import Entities.Card;
import Entities.Enemy;
import Entities.Player;
import MainPackage.Config;
import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

//This is the glass pane overlaying the main GUI. This is where all interactable elements are placed. The GUI Below this is not directly interactable.
public class GameplayPane extends JPanel {

    //Variables
    public static ArrayList<CardSlot> cardSlots = new ArrayList<>(); //This array list holds the card slots the player has to use.
    public static ArrayList<Card> hand = new ArrayList<>();
    public static ArrayList<Card> slottedCards = new ArrayList<>();

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

    public void updateHandRender() {
        hand.clear();
        // Iterate through all the components in this container
        for (Component comp : this.getComponents()) {
            // Check if the component is an instance of Card
            if (comp instanceof Card) {
                Card card = (Card) comp; // Cast to Card type
                // If the card is in the player's hand, remove it

                    this.remove(card); // Remove the card from the container

            }
        }

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
        Game.gui.gameScreen.glassPane.revalidate();
        Game.gui.gameScreen.glassPane.repaint();
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
        Game.gui.gameScreen.glassPane.revalidate();
        Game.gui.gameScreen.glassPane.repaint();
    }




    //Method for drawing a card to the hand. Call this any time you need a player to draw a card. IMPORTANT METHOD
    public void drawCard() {
        if (Game.player.cards.size() <= 0) {
            System.out.println("REFILLING PLAYER DECK");
            Game.player.refillDeck();
        }
        Card drawnCard = Game.player.drawCard();
        add(drawnCard);
        hand.clear();  // Clear GUI hand before syncing
        hand.addAll(Game.player.hand);  // Ensure GUI hand is the same

        // Debugging output
        /*
        System.out.println("Hand size after draw (GUI): " + hand.size());
        System.out.println("Hand size after draw (Player): " + Game.player.hand.size());
        System.out.println("Card Positions:");
        for (Card c : hand) {
            System.out.println(" - " + c + " at (" + c.getX() + ", " + c.getY() + ")");
        }*/
        // Recalculate coordinates
        int xcord = 30; // Start position
        for (int i = 0; i < Game.player.hand.size(); i++) {
            Card card = Game.player.hand.get(i);

            // Make sure the card is actually in the hand and added to the GUI
            if (!this.isAncestorOf(card)) {
                this.add(card);  // Ensure it's added before positioning
            }

            // Set proper position and size
            card.setLocation(xcord, 700);
            card.setSize(Config.cardSize.width, Config.cardSize.height);
            card.setVisible(true);  // Ensure it's visible

            xcord += Config.cardSize.width + 10; // Adjust spacing
            card.initX = card.getX();
            card.initY = card.getY();
        }

// Force UI refresh
        this.revalidate();
        this.repaint();

    }



    public void discardCard(Card c) {
        if(Game.player.hand.contains(c)) {
            Game.player.discard(c);
        }
        revalidate();
        repaint();
    }
    public void removeCard(Card c) {
        Game.player.discard(c);
        remove(c);
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
                    Game.player.hand.remove(c);
                    slottedCards.add(c);
                    if(Game.devTools!=null)
                        Game.devTools.updateCounts();
                    return;//Stop checking the card slots and leave this method.
                }
                else
                {
                    if(slottedCards.contains(c)) {
                        Game.player.hand.add(c);
                        slottedCards.remove(c);
                        if(Game.devTools!=null)
                            Game.devTools.updateCounts();
                        c.setLocation(c.initX, c.initY);
                    }
                    //figure out the logic for removing the card from the card slot when its dragged out of it without overriding other slots.
                    //if(slot.slottedCard != null && !slot.intersects(c.getBounds()))
                    //  slot.slotCard(null);//This is overriding the cards played before it,
                }
            }
        //This code should only be reached if the card is not intersecting anything. In which case, it is not primed and reset to the hand.
        if(slottedCards.contains(c)) {
            Game.player.hand.add(c);
            slottedCards.remove(c);
        }
        if(Game.devTools!=null)
            Game.devTools.updateCounts();

        c.setLocation(c.initX, c.initY);
        c.primed = false;


    }

    public void unslotAllCards() {
        for(CardSlot slot : cardSlots) {
            if(slot.hasCard()) {
                Game.player.discard(slot.slottedCard);
                slot.unslotCard();
            }
        }
    }

    //This int is for tracking the card slot we are currently itterating through.
    public static int currentCardIndex = 0;


    //This method is triggered by the PLAY HAND BUTTON. This controls the flow of resolving the cards. When we get here, the game runs itself until the round starts again.
    //PlayHand Button triggers this method. This method calls CardSlot.resolve(). It does this for the first slot, then the second, and so on.
    public void resolveNextCard() throws IOException {
        if (currentCardIndex >= GameplayPane.cardSlots.size()) {
           // Game.changeStateToEnemyTurn();
           // currentCardIndex = 0;
            return;
        }

        if (Game.gui.gameScreen.northPanel.enemies.isEmpty()) {
            System.out.println("All enemies are dead");
            return;
        }

        CardSlot slot = GameplayPane.cardSlots.get(currentCardIndex);
        if (slot.slottedCard != null) {
            Game.resolutionQueue.add(() -> {
                try {
                    slot.resolve(() -> {
                        currentCardIndex++;
                        Game.gui.gameScreen.glassPane.removeCard(slot.slottedCard);
                        slot.unslotCard();
                        Game.resolutionQueue.add(() -> {
                            try {
                                resolveNextCard();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });  // Add next card resolution to queue
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            currentCardIndex++;
            Game.resolutionQueue.add(() -> {
                try {
                    resolveNextCard();
                } catch (IOException e) {
                    throw new RuntimeException(e); // Convert to unchecked exception
                }
            });
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
