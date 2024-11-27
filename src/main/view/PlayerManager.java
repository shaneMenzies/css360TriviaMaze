package view;

import model.GameState;
import model.enums.Direction;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class handles keyboard input for the controlling player movement.
 * It handles directional keys and updates them accordingly.
 *
 * @author Cynthia Lopez
 * @version 11/1/24
 */
public class PlayerManager implements KeyListener {

    /** Tracks which key is pressed (Up, Down, Left, or Right)*/
    private boolean myUpPressed, myDownPressed, myRightPressed, myLeftPressed;

    /** Direction instance handling which direction the player is heading. */
    private Direction myDirection;

    /** Constructs the player manager and set player to default direction. */
    public PlayerManager() {
        myDirection = Direction.UP;

    }

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

        } else if (code == KeyEvent.VK_S) { // down
            myDownPressed = true;

        } else if (code == KeyEvent.VK_A) { // left
            myLeftPressed = true;

        } else if (code == KeyEvent.VK_D) { // right
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

        } else if (code == KeyEvent.VK_S) { // down
            myDownPressed = false;

        } else if (code == KeyEvent.VK_A) { // left
            myLeftPressed = false;

        } else if (code == KeyEvent.VK_D) { // right
            myRightPressed = false;

        }
    }

    /**
     * Updates player movement depending on the key pressed.
     * Handles inverted y-axis.
     *
     * @param theGameState current game state to update players movement.
     */
    public void update(GameState theGameState) {
        if (myUpPressed) {
            myDirection = Direction.DOWN; // to handle inverted y-axis
            theGameState.movePlayer(myDirection);
            myUpPressed = false;

        } else if (myDownPressed) {
            myDirection = Direction.UP; // to handle inverted y-axis
            theGameState.movePlayer(myDirection);
            myDownPressed = false;

        } else if (myLeftPressed) {
            myDirection = Direction.LEFT;
            theGameState.movePlayer(myDirection);
            myLeftPressed = false;

        } else if (myRightPressed) {
            myDirection = Direction.RIGHT;
            theGameState.movePlayer(myDirection);
            myRightPressed = false;
        }

    }

    /** Getter for the player's direction. */
    public Direction getDirection() {
        return myDirection;
    }

    /**
     * Unused method.
     *
     * @param keyEvent unused.
     */
    @Override
    public void keyTyped(KeyEvent keyEvent) {}
}