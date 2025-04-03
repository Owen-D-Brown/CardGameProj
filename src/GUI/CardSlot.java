package GUI;

import Entities.Animation;
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
        Runnable toDo = () -> {
            Game.gui.gameScreen.northPanel.initPlayerAniBounds();
            //
            try {
                slottedCard.initCardAniBounds(Game.player, Game.gui.gameScreen.northPanel.enemies.get(0));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//align bounds here
            AttackPlane.addAniToQue(slottedCard.animation);
            AttackPlane.animations.get(0).currentState = Animation.State.WAITING;
            //System.out.println("current state "+AttackPlane.animations.get(0).currentState);
            //PlayHandBtn -> GameplayPane.ResolveNextCard() -> CardSlot.Resolve -> Card.disolve() -> Call Back Function that Plays Animation -> Card.Effect().
            //After Effect() Resolve -> GameplayPane.ResolveNextCard() for the next card slot.

            //Calls the disolve() method in the Card instance. Passes a callback function that plays the animation of the card. This executes once disolve() finishes.
            slottedCard.disolve(() -> {
                // System.out.println("entered callback on card.disolve "+AttackPlane.animations.get(0));
                AttackPlane.animations.get(0).onComplete = onComplete;
                AttackPlane.animations.get(0).currentState = Animation.State.MOVING;
                // System.out.println("just masde state moving in disolve callback:: "+AttackPlane.animations.get(0).currentState);
                AttackPlane.animations.get(0).isMoving = true;
                AttackPlane.animations.get(0).card = slottedCard;
                // System.out.println("finished disolve callback");


            });
        };
        Game.player.setStateToAttacking(toDo);


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
            System.out.println("Adding to the queue");
            Game.resolutionQueue.add(() -> {


                if (this.slottedCard != null) {
                    this.currentlyResolving = true;

                    try {
                        resolve(() -> {
                            System.out.println("entering last callback");
                            Game.gui.gameScreen.glassPane.removeCard(slottedCard);
                            unslotCard();
                            this.isResolved = true;
                            System.out.println("Reached the end of the final runnable in the task");
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}
