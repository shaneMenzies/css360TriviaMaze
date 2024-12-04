package view;

import model.GameModel;
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

    /** GameModel to send input to */
    private final GameModel myGameModel;

    /** Direction instance handling which direction the player is heading. */
    private Direction myDirection;

    /** Constructs the player manager and set player to default direction. */
    public PlayerManager(final GameModel theGameModel) {
        myGameModel = theGameModel;
        myDirection = Direction.DOWN;
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
            myDirection = Direction.UP;

        } else if (code == KeyEvent.VK_S) { // down
            myDirection = Direction.DOWN;

        } else if (code == KeyEvent.VK_A) { // left
            myDirection = Direction.LEFT;

        } else if (code == KeyEvent.VK_D) { // right
            myDirection = Direction.RIGHT;
        }

        myGameModel.getState().movePlayer(myDirection);
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
    public void keyReleased(KeyEvent keyEvent) {}

    /**
     * Unused method.
     *
     * @param keyEvent unused.
     */
    @Override
    public void keyTyped(KeyEvent keyEvent) {}
}