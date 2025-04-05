package MainPackage;

import Entities.*;
import GUI.*;

import javax.swing.*;
import java.io.IOException;
import java.util.*;


//Game class - Master class for running the game. Everything else sits inside of this.
public class Game implements Runnable {

    //// Static entites for access by the entire application. ////

    public static Player player;//Player instance
    public static int randomCombatMinWeight = 0;
    public static int randomCombatMaxWeight = 10;
    public static int randomCombatNodeClicks = 0; // Tracks how many combats started

    static {
        try {
            player = new Player();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static RootContainer gui; //Main GUI Instance
    public static Config.GameState gameState;//Gamestate instance to control game state
    public static DevTools devTools; //Development Tools window
    static int currentEnemyIndex = 0;
    public static boolean allEnemiesAlive = true;
    public static boolean enemyWaiting = false;
    public static ArrayList<Runnable> resolutionQueue = new ArrayList<>();
    public static ArrayList<CardSlot> cardSlots = new ArrayList<>();
    //////////////////////////////

    //Constructor
    public Game() throws IOException {

        player = new Player();//Initializing player.

        //Development Tools Frame
        devTools = new DevTools();

        //Main GUI Frame
        gui = new RootContainer(this);

        //Initializing Game
        gameState = Config.GameState.CARD_PLAY;//Putting the state into the first one.

        //Temporary for adding card slots for testing.
        gui.gameScreen.glassPane.addCardSlot();
        gui.gameScreen.glassPane.addCardSlot();
        gui.gameScreen.glassPane.addCardSlot();

        //END OF INITIALIZATION

        //Starting game
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    ///----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    ///////////////////////////
    //// INTERFACE METHODS ////
    ///////////////////////////
    //Draw a card to the player's hand.
    public static void drawCard() {
        gui.gameScreen.glassPane.drawCard();
    }

    //Add a card slot to the field
    public static void addCardSlot() {gui.gameScreen.glassPane.addCardSlot();}

    //Remove a card slot from the field
    public static void removeCardSlot() {gui.gameScreen.glassPane.removeCardSlot();}

    //TO DO - DISCARD CARD.

    public static void unslotAllCards() {gui.gameScreen.glassPane.unslotAllCards();}
    //////////////////////////////



    ////////////////////////
    //// ENGINE METHODS ////
    ////////////////////////
    public static void changeStateToCardPlay() {
        gameState = Config.GameState.CARD_PLAY;
        System.out.println("\n****THE GAME STATE HAS CHANGED -- CARD PLAY****\n");

        //Draw up to five cards in the player's hand
        for(int i = player.hand.size(); i< 5; i++) {
            drawCard();
        }
        gui.gameScreen.southPanel.updateSouthPanel();
    }

    public static void changeStateToCardResolution() {
        gameState = Config.GameState.CARD_RESOLUTION;
        System.out.println("\n****THE GAME STATE HAS CHANGED -- CARD RESOLUTION****\n.");
    }

    public static void changeStateToEnemyTurn() throws IOException {
        gameState = Config.GameState.ENEMY_PHASE;
        System.out.println("\n****THE GAME STATE HAS CHANGED -- ENEMY TURN****\n");
        resolveNextEnemy();
    }

    //Recursive algorithm for iterating and resolving enemy attacks
    public static void resolveNextEnemy() throws IOException {

        //If we have resolved all the card slots.
        if (currentEnemyIndex >= gui.gameScreen.northPanel.enemies.size()) {

            Game.changeStateToCardPlay();

            currentEnemyIndex = 0;
            return; //No more cards to process
        }

        //Getting the slot we are currently working on.
        Enemy enemy = gui.gameScreen.northPanel.enemies.get(currentEnemyIndex);

        //Check if there is a card in the slot

        if (enemy != null && !enemy.dead) {

            //Resolve the slot, passing a callback function. This function will run when slot.resolve finishes.
            //In this case, it increases the index and calls itself again. This allows it to iterate through each
            //Slot sequentially. Callback triggers when dissolve + animation finish.
            //System.out.println("enemy should now be attacking "+Game.gui.gameScreen.northPanel.enemies.size());
            enemy.attack(() -> {
                currentEnemyIndex++;
                try {
                    resolveNextEnemy(); // Move to next card
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, player.getX(), player.getY());
        }

        else //If the card slot doesn't have a card in it.
        {
            currentEnemyIndex++;
            resolveNextEnemy(); // Skip empty slots
        }
    }

    //Methods called within the game loop for checks and updates.
    protected void shouldGoToRewardScreen() {
        if(gui.gameScreen.northPanel.enemies.isEmpty()) {

            gui.gameScreen.cardLayout.show(gui.gameScreen.centerContainer, "rewardScreen");//Display the reward screen.
            AttackPlane.animations.clear();//Clear any remaining queued animations
            gui.gameScreen.glassPane.setVisible(false);//Turn off the card slots.

        } else if(!gui.gameScreen.glassPane.isVisible()) {
            // gui.gameScreen.glassPane.setVisible(true);
        }
    }

    protected void updateEnemies() {
        if (!gui.gameScreen.northPanel.enemies.isEmpty()) {
            Iterator<Enemy> iterator = gui.gameScreen.northPanel.enemies.iterator();

            while(iterator.hasNext()) {
                Enemy enemy = iterator.next();
                enemy.animate();
                enemy.revalidate();
                enemy.repaint();
                enemy.updateEnemyStatus(iterator); // Pass iterator for safe removal
            }
        }
    }

    protected void checkAndRunPlayedCards() {
        if(!cardSlots.isEmpty() && gameState == Config.GameState.CARD_RESOLUTION) {

            Runnable task = resolutionQueue.get(0); // Get next task
            CardSlot slot = cardSlots.get(0);

            if(!slot.isResolved) {


                if(!slot.currentlyResolving) {

                    if(allEnemiesAlive&&task!=null) {

                        task.run();
                    }

                }

            } else if(slot.isResolved) {

                resolutionQueue.remove(0);
                cardSlots.remove(0);
                slot.isResolved = false;
                slot.currentlyResolving = false;
                System.out.println("\n***--slot task has finished resolving--*** \ncurrentlyResolving: "+slot.currentlyResolving+" isResolved: "+slot.isResolved);
            }
        }
    }

    protected void animateAttackPlaneAnimations() {
        Iterator<Animation> iterator1 = AttackPlane.animations.iterator();
        while(iterator1.hasNext()) {
            Animation ani = iterator1.next();
            ani.updateAni();//UPDATE MOVEMENT
            try {
                ani.checkForUpdates(iterator1);//STATE CONTROL
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            ani.revalidate();//READJUST POSITIONING
            ani.repaint();//REDRAW THE IMAGE


        }
    }

    protected void checkAndMoveToEnemyPhase() {
        if (resolutionQueue.isEmpty() && gameState == Config.GameState.CARD_RESOLUTION) {
            // Change state to enemy turn once all cards have been resolved

            if (gameState != Config.GameState.ENEMY_PHASE && allEnemiesAlive && !enemyWaiting) {
                try {
                    GameplayPane.currentCardIndex = 0;
                    changeStateToEnemyTurn(); // Transition to enemy turn state
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    //////////////////////////////



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

                SwingUtilities.invokeLater(() -> {
                    //If there is a combat instance loaded
                    if (Game.gui.gameScreen.northPanel != null) {

                        //Animate the player sprite.
                        player.animate();
                        player.revalidate();
                        player.repaint();

                        //Update any instances of floating text that need to be animated
                        FloatingText.update();

                        //If there are no more enemies in the master enemy array. Bring the player to the rewardscreen.
                        shouldGoToRewardScreen();

                        //Remove any finished floating text instances
                        FloatingText.removeInstances();

                        //Running updates for all enemies
                        updateEnemies();

                        //Performing a check to see if there are enemies still alive. Used for game flow
                        if (gui.gameScreen.northPanel.enemies.size() <= 0) {
                            allEnemiesAlive = false;
                        }

                        /// FOR THESE METHODS TO RUN - THE GAME SHOULD BE IN CARD RESOLUTION STATE
                        //checks the gamestate, then runs tasks that have been loaded into the resolution queue by clicking on the play hand button.
                        checkAndRunPlayedCards();

                        //Animate animations queued up in the attack plane.
                        animateAttackPlaneAnimations();

                        //Check if all cards have resolved, and any dead enemies have finished their animations. Then, moves to enemy phase.
                        checkAndMoveToEnemyPhase();

                        //Repaint the north panel where everything is located/
                        gui.gameScreen.northPanel.revalidate();
                        gui.gameScreen.northPanel.repaint();


                    }
                });
                deltaF--;//Removing one from deltaF, keeping any leftover time we may have for the next iteration.
            }
        }
    }

    }
