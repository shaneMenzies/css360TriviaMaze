package model;

import model.enums.Direction;
import model.enums.GameWinState;
import java.io.Serializable;

/**
 * Represents the entire state of a game being played.
 *
 * @author Shane Menzies
 * @version 11/3/24
 */
public final class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Settings for this game.
     */
    private final GameSettings mySettings;

    /**
     * Maze for this game.
     */
    private final Maze myMaze;

    /**
     * Player of this game.
     */
    private final Player myPlayer;

    /**
     * Current state of this game.
     */
    private GameWinState myWinState;

    /**
     * Makes a new game from the provided settings and maze.
     *
     * @param theSettings Settings for the game.
     * @param theMaze Maze for the game.
     */
    public GameState(final GameSettings theSettings, final Maze theMaze) {
        mySettings = theSettings;
        myMaze = theMaze;

        // Determine appropriate initial coordinates for the player
        final Room initialRoom
                = myMaze.getRoomAt(myMaze.getStartingRoomX(), myMaze.getStartingRoomY());
        final Coordinates initialCoordinates
                = new Coordinates(myMaze.getStartingRoomX(),
                myMaze.getStartingRoomY(),
                initialRoom.getWidth() / 2,
                initialRoom.getHeight() / 2);

        final int initialLives = mySettings.getInitialPlayerLives();

        myPlayer = new Player(initialCoordinates, 0, initialLives);

        myWinState = GameWinState.NOT_STARTED;
    }

    /**
     * Gets the Maze this game is being played in.
     *
     * @return Maze for this game.
     */
    public Maze getMaze() {
        return myMaze;
    }

    /**
     * Gets the player for this game.
     *
     * @return Player for this game.
     */
    public Player getPlayer() {
        return myPlayer;
    }

    public void movePlayer(final Direction theDirection) {

        final Coordinates oldPos = myPlayer.getPosition();
        final int oldRoomX = oldPos.getRoomX();
        final int oldRoomY = oldPos.getRoomY();
        final int oldX = oldPos.getX();
        final int oldY = oldPos.getY();

        // Determine where Player should end up
        int newRoomX = oldRoomX;
        int newRoomY = oldRoomY;
        int newX = oldX;
        int newY = oldY;
        switch (theDirection) {
            case UP:
                newY++;
                break;
            case DOWN:
                newY--;
                break;
            case LEFT:
                newX--;
                break;
            case RIGHT:
                newY++;
                break;

            default:
                // This shouldn't be able to happen?
                throw new IllegalStateException("Unexpected value: " + theDirection);
        }

        // Need to determine if the Player should (and can) move to a new room
        if (newY > myMaze.getRoomAt(newRoomX, newRoomY).getHeight()
            && newRoomY < (myMaze.getHeight() - 1)) {
            newRoomY++;
            newY = 0;

        } else if (newY < 0
            && newRoomY > 0) {
            newRoomY--;
            newY = myMaze.getRoomAt(newRoomX, newRoomY).getHeight() - 1;

        } else if (newX > myMaze.getRoomAt(newRoomX, newRoomY).getWidth()
            && newRoomX < (myMaze.getWidth() - 1)) {
            newRoomX++;
            newX = 0;

        } else if (newX < 0
            && newRoomX > 0) {
            newRoomX--;
            newX = myMaze.getRoomAt(newRoomX, newRoomY).getWidth() - 1;
        }

        // Check if player can actually move here
        final boolean canMove = myMaze.getRoomAt(newRoomX, newRoomY).
                getTile(newX, newY).tryMoveTo();
        if (canMove) {
            myPlayer.setPosition(new Coordinates(newRoomX, newRoomY, newX, newY));
        }
    }
}