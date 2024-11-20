package model;

import java.util.ArrayList;
import java.util.List;
import model.enums.Direction;
import model.enums.GamePlayPhase;
import model.interfaces.GameStateUpdateListener;
import model.interfaces.QuestionControllerUpdateListener;
import model.interfaces.QuestionHandler;

/**
 * Represents the entire state of a game being played.
 *
 * @author Shane Menzies
 * @version 11/10/24
 */
public final class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * String for an exception caused by an invalid update type.
     */
    private static final String INVALID_UPDATE_TYPE_MESSAGE = "QuestionController "
            + "updated GameState with invalid UpdateType!";

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
        if (myMaze.getDoors() != null) {
            for (final DoorController nextDoor : myMaze.getDoors()) {
                nextDoor.setHandler(myQuestionController);
            }
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
     * Gets the current question in the game.
     *
     * @return Current trivia question.
     */
    public TriviaQuestion getQuestion() {
        return myQuestionController.getQuestion();
    }

    /**
     * Provide an answer to the current question.
     *
     * @param theInput Answer to judge.
     * @return Result of question with provided answer.
     */
    public QuestionHandler.QuestionResult answerQuestion(final String theInput) {
        return myQuestionController.answerQuestion(theInput);
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

        if (myPlayPhase == GamePlayPhase.TRIVIA) {
            // Cancel active question
            myQuestionController.cancelQuestion();
        }

        final Coordinates oldPos = myPlayer.getPosition();
        final Coordinates newPos = myMaze.moveCoordinates(oldPos, theDirection);

        // Move the player if they're position has changed
        if (!newPos.equals(oldPos)) {
            if (myMaze.getTile(newPos).tryMoveTo()) {
                myPlayer.setPosition(newPos);

                if (oldPos.getRoomX() != newPos.getRoomX()
                    || oldPos.getRoomY() != newPos.getRoomY()) {
                    onRoomChange(newPos.getRoomX(), newPos.getRoomY());
                }
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
     * Performs necessary actions when the player changes rooms.
     *
     * @param theNewRoomX X-coordinate of the new room.
     * @param theNewRoomY Y-coordinate of the new room.
     */
    private void onRoomChange(final int theNewRoomX, final int theNewRoomY) {
        if (theNewRoomX == myMaze.getExitRoomX()
            && theNewRoomY == myMaze.getExitRoomY()) {
            setPhase(GamePlayPhase.VICTORY);
        }
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
     * @param theUpdateType The type of update in the QuestionController.
     */
    private void handleQuestionControllerUpdate(
            final QuestionControllerUpdateListener.UpdateType theUpdateType) {
        switch (theUpdateType) {
            case NEW_QUESTION:
                myStoredPhase = myPlayPhase;
                setPhase(GamePlayPhase.TRIVIA);
                break;

            case ANSWERED_CORRECTLY:
                myPlayer.setScore(myPlayer.getScore() + mySettings.getCorrectAnswerScore());
                setPhase(myStoredPhase);
                break;

            case ANSWERED_INCORRECTLY:
                myPlayer.setScore(myPlayer.getScore() + mySettings.getWrongAnswerScore());
                myPlayer.setLives(myPlayer.getLives() - 1);
                if (myPlayer.getLives() == 0) {
                    setPhase(GamePlayPhase.FAILURE);
                } else {
                    setPhase(myStoredPhase);
                }
                break;

            case CANCELLED:
                setPhase(myStoredPhase);
                break;

            default:
                throw new IllegalArgumentException(INVALID_UPDATE_TYPE_MESSAGE);
        }
    }

    /**
     * Basic update listener for this game state to listen to updates from
     * the QuestionController.
     */
    private final class QuestionControllerListener
            implements QuestionControllerUpdateListener {
        @Override
        public void doUpdate(
                final QuestionControllerUpdateListener.UpdateType theUpdateType,
                final QuestionController theQuestionController) {
            handleQuestionControllerUpdate(theUpdateType);
        }
    }
}
