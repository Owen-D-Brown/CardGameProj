package MAP;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;



/*
The class KeyH IMPLEMENTS KeyListener for listening to key inputs
// Boolean variables to track key states of the keys
	Declaring the Logic:
		boolean UPp = false
		boolean DOWNp = false
		boolean RIGHTp = false
		boolean LEFTp = false*/

public class KeyH implements KeyListener{
    public boolean UPp, DOWNp, RIGHTp, LEFTp, Ep;

    //Not used in this case, needed to be generated
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

        int code = e.getKeyCode(); // Get the key code or implement of the pressed key


        if(code == KeyEvent.VK_W) { // W is marked as pressing Up in the key code
            UPp = true;
        }
        if(code == KeyEvent.VK_A) { // A is marked as pressing left in the key code
            LEFTp = true;
        }

        if(code == KeyEvent.VK_S) { // S is marked as pressing Down in the key code
            DOWNp = true;
        }

        if(code == KeyEvent.VK_D) { // D is marked as pressing Right in teh key code
            RIGHTp = true;
        }

        if (code == KeyEvent.VK_E) // E as an event trigger or interact
            Ep = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

        int code = e.getKeyCode(); // Key code for released function

        /*
         * Instead of pressed here im working with stopped as in the key is not pressed
         * anymore so movement is stopped
         */

        if(code == KeyEvent.VK_W) {
            UPp = false;
        }
        if(code == KeyEvent.VK_A) {
            LEFTp = false;
        }

        if(code == KeyEvent.VK_S) {
            DOWNp = false;
        }

        if(code == KeyEvent.VK_D) {
            RIGHTp = false;
        }
        if (code == KeyEvent.VK_E)
            Ep = false;

    }

}
