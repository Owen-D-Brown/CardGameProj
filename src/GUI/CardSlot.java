package GUI;

import Entities.AttackPlane;
import Entities.Card;
import MainPackage.Game;

import java.awt.*;
import java.io.IOException;

public class CardSlot extends Rectangle {
    public Card slottedCard;

    public boolean resolved = false;

    public CardSlot(int x, int y) {
        super(x, y, 160, 220);
    }

    public void slotCard(Card playedCard) {
        this.slottedCard = playedCard;
    }

    public void unslotCard() {
        if (this.slottedCard != null) {
            this.slottedCard.primed = false;
            this.slottedCard.setBounds(this.slottedCard.initX, this.slottedCard.initY, this.slottedCard.getWidth(), this.slottedCard.getHeight());
            this.slottedCard = null;

        }

    }

    public boolean hasCard() {
        return (this.slottedCard != null);
    }

    ;

    //This method is called by ResolveNextCard() in gameplay pane, which in turn is called when the PlayHandButton is clicked.
    public void resolve(Runnable onComplete) throws IOException {//Callback function passed iterates through the gameplay pane card slots. So, when this finishes, resolveNextCArd() is called again, which in turn calls this.resolve() again.
        //

        //
        Game.gui.gameScreen.northPanel.initAniBounds();
        //System.out.println("testing here for player pos " + Game.gui.gameScreen.northPanel.playerY);
        slottedCard.initCardAniBounds();
//align bounds here
        AttackPlane.addAniToQue(slottedCard.animation);

        //PlayHandBtn -> GameplayPane.ResolveNextCard() -> CardSlot.Resolve -> Card.disolve() -> Call Back Function that Plays Animation -> Card.Effect().
        //After Effect() Resolve -> GameplayPane.ResolveNextCard() for the next card slot.

        //Calls the disolve() method in the Card instance. Passes a callback function that plays the animation of the card. This executes once disolve() finishes.
        slottedCard.disolve(() -> {
            // Play attack animation AFTER dissolve finishes
            Game.gui.gameScreen.northPanel.attackPlane.playAnimation(() -> {
            //at this point, the animation is set to moving. Play animation starts teh animation timer, so no longer needed. Remove call to play animation, move logic to update animation
                //for stopping it. ie. if State == finished -> stop animation, remove card.

                // Only now move to the next card
                if (onComplete != null) {
                    slottedCard.effect();
                    System.out.println("Card Slot Resolution Finished");
                    AttackPlane.animations.get(0).stopAnimation();
                    // Game.checkEnemyStatus(Game.gui.gameScreen.northPanel.enemies);
                    Game.gui.gameScreen.glassPane.removeCard(slottedCard);

                    onComplete.run();//unslots the card/moves to next one
                }
            });
        });

        //Change so that the card passes and calls its own animation, then the program returns here to call onNext/ResolveNextCard
        //
        //Run effect after the animation

    }
public boolean isResolved = false;
    public boolean currentlyResolving = false;
    public void addSlotResolutionToQueue() {
        isResolved = false;
        currentlyResolving = false;
        if (slottedCard != null) {
            Game.cardSlots.add(this);
            Game.resolutionQueue.add(() -> {


                if (this.slottedCard != null) {
                    this.currentlyResolving = true;
                    try {
                        resolve(() -> {

                            Game.gui.gameScreen.glassPane.removeCard(slottedCard);
                            unslotCard();
                            this.isResolved = true;
                            System.out.println("Card Slot Resolution Finished");
                            //Game.cardSlots.remove(this);
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}
