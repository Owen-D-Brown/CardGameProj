package Mentity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import MAP.GameStorePanel;
import MAP.KeyH;
import MAP.gamePanel;
import Tiles.TileManager;
import MainPackage.Game;

public class Mplayer extends Mentity {
    gamePanel gp;
    KeyH kh;

    public Mplayer(gamePanel gp, KeyH kh) {
        this.gp = gp;
        this.kh = kh;
        setDefaultVal();
        getPlayerImg();
    }

    // Initial position/speed/direction
    public void setDefaultVal() {
        x = 200;           //spawn pos
        y = 200;
        spd = 3;           // player speed
        direction = "down";
    }

    // Load the same image for all directions for now
    public void getPlayerImg() {
        try {
            up1    = ImageIO.read(getClass().getResource("/Resources/MapSprite/PlayerDown1.png"));
            up2    = up1;
            down1  = up1;
            down2  = up1;
            left1  = up1;
            left2  = up1;
            right1 = up1;
            right2 = up1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //logic and collision checking
    public void update() {

        // this stops player movement when the Merchant it opened
        //issues with the player continuing  to walk after opening shop
        if (gamePanel.gameState == gamePanel.S_SHOP || gamePanel.gameState == gamePanel.S_COMBAT_MAP) {
            return;
        }
        // ifX and Y positions and using them to check for blocked co-ords
        int ifX = x;
        int ifY = y;

        if (kh.UPp) {
            direction = "up";
            ifY -= spd;
        }
        else if (kh.DOWNp) {
            direction = "down";
            ifY += spd;
        }
        else if (kh.LEFTp) {
            direction = "left";
            ifX -= spd;
        }
        else if (kh.RIGHTp) {
            direction = "right";
            ifX += spd;
        }

        //checking blocked co-ords
        if (!isBlocked(ifX, ifY)) {
            //if there is no block work as normal
            x = ifX;
            y = ifY;
        }
        // press E to open the shop on tile 3 in the 2D array
        if (kh.Ep && isOnMerchantTile()) {
            //openMerchantShop();
            System.out.println("E Key Pressed: " + kh.Ep);

        }

        if (kh.Ep && isOnCombatMapTile()) {
            kh.Ep = false;
            gamePanel.gameState = gamePanel.S_COMBAT_MAP;
            System.out.println("Combat tile triggered! Loading map01.json...");

            SwingUtilities.invokeLater(() -> {
                Game.gui.showScreen(Game.gui.mapScreen);
            });
        }


        // is movement allowed here if not blocked update player pos
        if (!isBlocked(ifX, ifY)) {
            x = ifX;
            y = ifY;
        }

        //the check when accessing the merchant
        int playerRow = (y + gp.tileSize / 2) / gp.tileSize; //calc for the tile row
        int playerCol = (x + gp.tileSize / 2) / gp.tileSize; // calc for the column

        if (kh.Ep && gp.tileManager.MerchantTile(playerRow, playerCol)) {
            kh.Ep = false; // prevent merchant opening multiple times
            gp.gameState = gamePanel.S_SHOP;  // switch the game state to shop mode
            gp.tileManager.openStore(); // open the merchant window
        }

    }

    /*
     * // basic Jpanel operatio0n/method here private void openMerchantShop() {
     * SwingUtilities.invokeLater(() -> { //runs on edt for updating swing JFrame
     * shopFrame = new JFrame("Merchant Shop");
     * shopFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
     * shopFrame.add(new GameStorePanel()); shopFrame.pack();
     * shopFrame.setResizable(false); shopFrame.setVisible(true); }); }
     */


    private boolean isBlocked(int px, int py) {
        // Convert pixel coordinates to tile indices
        int tileColLeft   = px / gp.tileSize;
        int tileColRight  = (px + gp.tileSize - 1) / gp.tileSize;
        int tileRowTop    = py / gp.tileSize;
        int tileRowBottom = (py + gp.tileSize - 1) / gp.tileSize;

        // Ensure player doesn't move out of bounds (corrected to 50x50)
        if (tileColLeft < 0 || tileColRight >= 50 || tileRowTop < 0 || tileRowBottom >= 50) {
            return true;
        }
        // Debugging Log
        System.out.println("Checking collision at (" + tileRowTop + "," + tileColLeft + ") - " + gp.tileManager.cMap[tileRowTop][tileColLeft]);
        System.out.println("Checking collision at (" + tileRowTop + "," + tileColRight + ") - " + gp.tileManager.cMap[tileRowTop][tileColRight]);
        System.out.println("Checking collision at (" + tileRowBottom + "," + tileColLeft + ") - " + gp.tileManager.cMap[tileRowBottom][tileColLeft]);
        System.out.println("Checking collision at (" + tileRowBottom + "," + tileColRight + ") - " + gp.tileManager.cMap[tileRowBottom][tileColRight]);

        // check if player is in the corner of a tile
        return
                gp.tileManager.cMap[tileRowTop][tileColLeft] ||

                        gp.tileManager.cMap[tileRowTop][tileColRight] ||

                        gp.tileManager.cMap[tileRowBottom][tileColLeft] ||

                        gp.tileManager.cMap[tileRowBottom][tileColRight];
    }



    // is the player standing on a merchant tile
    private boolean isOnMerchantTile() {
        int tileRow = y / gp.tileSize; // row index for players y coord
        int tileCol = x / gp.tileSize; // tile column index like above
        return gp.tileManager.mapData[tileRow][tileCol] == 3; //checks the tile type and returns true
    }

    private boolean isOnCombatMapTile() {
        int tileRow = y / gp.tileSize;
        int tileCol = x / gp.tileSize;
        return gp.tileManager.mapData[tileRow][tileCol] == 10;
    }


    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":    image = up1;    break;
            case "down":  image = down1;  break;
            case "left":  image = left1;  break;
            case "right": image = right1; break;
        }

        int drawX = x - (50 - gp.tileSize) / 2;
        int drawY = y - (50 - gp.tileSize) / 2;

        // Draw the player at 64x64 size
        g2.drawImage(image, drawX, drawY, 64, 64, null);
    }

}
