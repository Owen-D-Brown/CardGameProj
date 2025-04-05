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


    public void resolve(Runnable onComplete) throws IOException {
        // Step 1: Init bounds and card animation
        Game.gui.gameScreen.northPanel.initPlayerAniBounds();
        System.out.println("REACHING RESOLVE IN CARD SLOT");

        slottedCard.initCardAniBounds(Game.player, Game.gui.gameScreen.northPanel.enemies.get(0));
        AttackPlane.addAniToQue(slottedCard.animation);

        AttackPlane.animations.get(0).currentState = Animation.State.WAITING;

        // Step 2: Dissolve card FIRST
        slottedCard.disolve(() -> {

            // Step 3: THEN play player attack animation
            Game.player.setStateToAttacking(() -> {
                // Step 4: THEN play attack animation (fireball, etc.)
                Animation attackAni = AttackPlane.animations.get(0);
                attackAni.onComplete = onComplete;
                attackAni.currentState = Animation.State.MOVING;
                attackAni.isMoving = true;
                attackAni.card = slottedCard;

            });
        });
    }

public boolean isResolved = false;
    public boolean currentlyResolving = false;
    public void addSlotResolutionToQueue() {
        isResolved = false;
        currentlyResolving = false;

        if (slottedCard != null) {

            Game.cardSlots.add(this);
           // System.out.println("Adding to the queue");
            Game.resolutionQueue.add(() -> {


                if (this.slottedCard != null) {
                    this.currentlyResolving = true;

                    try {
                        resolve(() -> {
                            System.out.println("entering last callback");
                           // Game.gui.gameScreen.glassPane.removeCard(slottedCard);
                            unslotCard();
                            this.isResolved = true;
                           // System.out.println("Reached the end of the final runnable in the task");
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}
