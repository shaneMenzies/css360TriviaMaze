package model;

import model.enums.Direction;
import model.enums.GameWinState;

/**
 * Represents the entire state of a game being played.
 *
 * @author Shane Menzies
 * @version 11/3/24
 */
public final class GameState {

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
                = myMaze.getRoom(myMaze.getStartingRoomX(), myMaze.getStartingRoomY());
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

    /**
     * Moves the player in a certain direction.
     *
     * @param theDirection Direction to move in.
     */
    public void movePlayer(final Direction theDirection) {

        final Coordinates oldPos = myPlayer.getPosition();
        final Coordinates newPos = myMaze.moveCoordinates(oldPos, theDirection);

        // Move the player if they're position has changed
        if (!newPos.equals(oldPos)) {
            if (myMaze.getTile(newPos).tryMoveTo()) {
                myPlayer.setPosition(newPos);
            }
        }
    }
}