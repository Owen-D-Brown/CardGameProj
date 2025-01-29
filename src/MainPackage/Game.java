package MainPackage;

import Entities.Enemy;
import Entities.Player;
import GUI.MainGui;

public class Game {
    public static Player player = new Player();
    public static Enemy enemy = new Enemy();
    public static MainGui gui;
    public static Config.GameState gameState;
    public Game() {
        gui = new MainGui(this);
        gameState = Config.GameState.CARD_PLAY;
    }
}
