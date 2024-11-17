package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *This class handles keyboard input for the controlling player movement.
 * It handles directional keys and updates them accordingly.
 *
 * @author Cynthia Lopez
 * @version 11/1/24
 */
public class PlayerManager implements KeyListener {

    /** Tracks which key is pressed (Up, Down, Left, or Right)*/
    private boolean myUpPressed, myDownPressed, myRightPressed, myLeftPressed;

    /**
     * Unused method.
     *
     * @param keyEvent unused.
     */
    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    /**
     * Handles key press events and updates the appropriate direction.
     *
     * @param keyEvent event triggered by pressing the key.
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int code = keyEvent.getKeyCode();

        if (code == KeyEvent.VK_W) { // up
            myUpPressed = true;

        }
        if (code == KeyEvent.VK_S) { // down
            myDownPressed = true;

        }
        if (code == KeyEvent.VK_A) { // left
            myLeftPressed = true;

        }
        if (code == KeyEvent.VK_D) { // right
            myRightPressed = true;

        }
    }

    /**
     * Handles the key release events and updates appropriate direction.
     *
     * @param keyEvent event triggered by releasing the key.
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int code = keyEvent.getKeyCode();

        if (code == KeyEvent.VK_W) { // up
            myUpPressed = false;

        }
        if (code == KeyEvent.VK_S) { // down
            myDownPressed = false;

        }
        if (code == KeyEvent.VK_A) { // left
            myLeftPressed = false;

        }
        if (code == KeyEvent.VK_D) { // right
            myRightPressed = false;

        }
    }

    /**
     * Getter for UP key being pressed.
     *
     * @return True if UP key is pressed, false otherwise.
     */
    public boolean getUpKey() {
        return myUpPressed;
    }

    /**
     * Getter for DOWN key being pressed.
     *
     * @return True if DOWN key is pressed, false otherwise.
     */
    public boolean getDownKey() {
        return myDownPressed;
    }

    /**
     * Getter for LEFT key being pressed.
     *
     * @return True if LEFT key is pressed, false otherwise.
     */
    public boolean getLeftKey() {
        return myLeftPressed;
    }

    /**
     * Getter for RIGHT key being pressed.
     *
     * @return True if RIGHT key is pressed, false otherwise.
     */
    public boolean getRightKey() {
        return myRightPressed;
    }
}
