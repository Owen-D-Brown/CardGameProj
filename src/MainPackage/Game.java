package MainPackage;

import Entities.Enemy;
import Entities.Player;
import GUI.CardSlot;
import GUI.DevTools;
import GUI.GameplayPane;
import GUI.MainGui;

import java.lang.reflect.Array;
import java.util.ArrayList;


//Game class - Master class for running the game. Everything else sits inside of this.
public class Game implements Runnable {

    //Static entites for access by the entire application.
    public static Player player = new Player();//Player instance

    public static MainGui gui; //Main GUI Instance
    public static Config.GameState gameState;//Gamestate instance to control game state

    //Constructor
    public Game() {
        //
        DevTools devTools = new DevTools();
        //
        gui = new MainGui(this);
        gameState = Config.GameState.CARD_PLAY;//Putting the state into the first one.

        //Temporary for adding card slots for testing.
        gui.glassPane.addCardSlot();
        gui.glassPane.addCardSlot();
        gui.glassPane.addCardSlot();
        //Temp code for setting up Cards in hand.
        gui.glassPane.drawCard();
        gui.glassPane.drawCard();
        gui.glassPane.drawCard();

    }

    //TODO - Discard pile reshuffles into deck when player has no cards. ??

    //Draw a card to the player's hand.
    public static void drawCard() {
        gui.glassPane.drawCard();
    }

    ///
    public static void changeStateToCardPlay() {
        gameState = Config.GameState.CARD_PLAY;
        for(int i = player.hand.size(); i< 5; i++) {
            drawCard();
        }
    }
    public static void changeStateToCardResolution() {
        gameState = Config.GameState.CARD_RESOLUTION;
    }

    static int currentEnemyIndex = 0;
    public static void changeStateToEnemyTurn() {
        gameState = Config.GameState.ENEMY_PHASE;
        System.out.println("THE GAME STATE HAS CHANGED -- ENEMY TURN.");
        resolveNextEnemy();
        changeStateToCardPlay();
    }
    public static void checkEnemyStatus(ArrayList<Enemy> enemies) {
        int index = 0;
        if(enemies.size()>0) {
            for (Enemy enemy : enemies) {
                if (enemy.currentHealth <= 0) {
                    gui.northPanel.removeEnemy(index);

                }
                index++;
            }
        }
    }

    //Run method. Has its own thread running constantly. Idle animations are played out here.
    int FPS_SET = 30;//MOVE TO CONFIG - CONTROLS GAME FPS
    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;//One second in nanotime divided by how many frames we want per second, to give us nanoseconds per frame.


        long previousTime = System.nanoTime();//Logging the time from the last iteration of the loop.
        long currentTime = System.nanoTime();//Logging the current time to compare against the previous time to determine if it's time to display a new frame
        double deltaF = 0;//We're using this to store the difference between the previous and current time, divided by frames per second.

        while (true) {
            currentTime = System.nanoTime();//Updating the current time
            deltaF += (currentTime - previousTime) / timePerFrame;//Gets how long its been since the last iteration, and adds it to deltaF. If that time is over 1 when divided by time per frame, then at least the time we've set for each frame has passed.
            previousTime = currentTime;//Setting the previous time to the current time

            if (deltaF >= 1) {//By running the game loop this way, it prevents the calculations from being messed up from a nanosecond or two slipping through the cracks. It catches up with itself.
               // gui.attackPlane.repaint();//Actually repainting the panel to display changes/animations
                //gui.attackPlane.updateAnimations();
                deltaF--;//Removing one from deltaF, keeping any leftover time we may have for the next iteration.
            }
        }
    }

        public static void resolveNextEnemy() {

            //If we have resolved all the card slots.
            if (currentEnemyIndex >= gui.northPanel.enemies.size()) {
                Game.changeStateToCardPlay();
                //Game.runEnemyTurn();
                currentEnemyIndex = 0;
                return; //No more cards to process
            }

            //Getting the slot we are currently working on.
            Enemy enemy = gui.northPanel.enemies.get(currentEnemyIndex);

            //Check if there is a card in the slot
            if (enemy != null) {

                //Resolve the slot, passing a callback function. This function will run when slot.resolve finishes.
                //In this case, it increases the index and calls itself again. This allows it to iterate through each
                //Slot sequentially. Callback triggers when dissolve + animation finish.
                enemy.attack(() -> {
                    currentEnemyIndex++;
                    resolveNextEnemy(); // Move to next card
                });
            }

            else //If the card slot doesn't have a card in it.
            {
                currentEnemyIndex++;
                resolveNextEnemy(); // Skip empty slots
            }
        }
        }
/*
TO DO:
Make all these classes singletons.
Move GameState control to this class. Triggers for the changes simply call methods here.
 */