package model;

import java.util.ArrayList;
import java.util.List;
import model.enums.Direction;
import model.enums.GamePlayPhase;
import model.interfaces.GameStateUpdateListener;
import model.interfaces.QuestionControllerUpdateListener;

/**
 * Represents the entire state of a game being played.
 *
 * @author Shane Menzies
 * @version 11/10/24
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
     * Question controller for this game.
     */
    private final QuestionController myQuestionController;

    /**
     * Update listeners for the entire game state.
     */
    private final List<GameStateUpdateListener> myListeners;

    /**
     * Current phase of this game.
     */
    private GamePlayPhase myPlayPhase;

    /**
     * Temporarily saved GamePlayPhase for when Trivia activates.
     */
    private GamePlayPhase myStoredPhase;

    /**
     * Makes a new game from the provided settings and maze.
     *
     * @param theSettings Settings for the game.
     * @param theMaze Maze for the game.
     */
    public GameState(final GameSettings theSettings, final Maze theMaze) {
        mySettings = theSettings;
        myMaze = theMaze;

        myPlayer = preparePlayer();

        // Make and link the question controller.
        myQuestionController = new QuestionController();
        myQuestionController.addListener(new QuestionControllerListener());
        for (final DoorController nextDoor : myMaze.getDoors()) {
            nextDoor.setHandler(myQuestionController);
        }

        myListeners = new ArrayList<>();

        myPlayPhase = GamePlayPhase.NOT_STARTED;
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
     * Adds an update listener to this game state.
     *
     * @param theListener New listener to add.
     */
    public void addUpdateListener(final GameStateUpdateListener theListener) {
        myListeners.add(theListener);
    }

    /**
     * Remove a previously added update listener from this game state.
     *
     * @param theListener Previously added listener to remove.
     * @return True if successfully removed, false otherwise.
     */
    public boolean removeUpdateListener(final GameStateUpdateListener theListener) {
        return myListeners.remove(theListener);
    }

    /**
     * Gets the current phase of this game.
     *
     * @return Current phase of this game.
     */
    public GamePlayPhase getPhase() {
        return myPlayPhase;
    }

    /**
     * Changes the phase of this game.
     *
     * @param theGamePlayPhase New phase for this game.
     */
    public void setPhase(final GamePlayPhase theGamePlayPhase) {
        myPlayPhase = theGamePlayPhase;

        updateListeners(GameStateUpdateListener.UpdateType.PHASE);
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

        updateListeners(GameStateUpdateListener.UpdateType.PLAYER);
    }

    /**
     * Prepares an initial Player state.
     *
     * @return Player instance prepared for a new game.
     */
    private Player preparePlayer() {
        // Determine appropriate initial coordinates for the player
        final Room initialRoom
                = myMaze.getRoom(myMaze.getStartingRoomX(), myMaze.getStartingRoomY());
        final Coordinates initialCoordinates
                = new Coordinates(myMaze.getStartingRoomX(),
                myMaze.getStartingRoomY(),
                initialRoom.getWidth() / 2,
                initialRoom.getHeight() / 2);

        final int initialLives = mySettings.getInitialPlayerLives();

        return new Player(initialCoordinates, 0, initialLives);
    }

    /**
     * Updates all listeners for a certain update.
     *
     * @param theUpdateType Type of update.
     */
    private void updateListeners(final GameStateUpdateListener.UpdateType theUpdateType) {
        for (final GameStateUpdateListener listener : myListeners) {
            listener.doUpdate(theUpdateType, this);
        }
    }

    /**
     * Handles an update from the QuestionController.
     *
     * @param theUpdated The QuestionController which updated.
     */
    private void handleQuestionControllerUpdate(final QuestionController theUpdated) {
        if (theUpdated.hasQuestion()) {
            myStoredPhase = myPlayPhase;
            setPhase(GamePlayPhase.TRIVIA);
        } else {
            setPhase(myStoredPhase);
        }
    }

    /**
     * Basic update listener for this game state to listen to updates from
     * the QuestionController.
     */
    private final class QuestionControllerListener
            implements QuestionControllerUpdateListener {
        @Override
        public void doUpdate(final QuestionController theQuestionController) {
            handleQuestionControllerUpdate(theQuestionController);
        }
    }
}