package model;

import java.util.ArrayList;
import java.util.List;
import model.interfaces.DoorUpdateListener;
import model.interfaces.QuestionHandler;
import model.interfaces.Tile;

/**
 * Controller for a pair of doors in a maze.
 *
 * @author Shane Menzies
 * @version 11/10/24
 */
public final class DoorController {

    /**
     * Exception message for an invalid question result.
     */
    private static final String INVALID_RESULT_STRING
            = "Invalid Result provided to DoorController!";

    /**
     * Pair of doors this controller is in charge of.
     */
    private final DoorTile[] myDoors;

    /**
     * Trivia Question this pair of doors is associated with.
     */
    private final TriviaQuestion myQuestion;

    /**
     * All update listeners for this door controller.
     */
    private final List<DoorUpdateListener> myListeners;

    /**
     * Question handler to send the question to ask it.
     */
    private QuestionHandler myQuestionHandler;

    /**
     * State of these doors.
     */
    private DoorState myState;

    /**
     * Constructs a door controller from the provided question, with 2 new door tiles.
     *
     * @param theQuestion Trivia Question this controller should be associated with.
     */
    public DoorController(final TriviaQuestion theQuestion) {
        myDoors = new DoorTile[2];
        myQuestion = theQuestion;
        myListeners = new ArrayList<>();
        myState = DoorState.UNANSWERED;

        // Initialize each door tile
        for (int i = 0; i < myDoors.length; i++) {
            myDoors[i] = new DoorTile();
        }
    }

    /**
     * Gets this controller's doors.
     *
     * @return This controller's doors.
     */
    public DoorTile[] getDoors() {
        return myDoors;
    }

    /**
     * Gets this door controller's current state.
     *
     * @return Current state.
     */
    public DoorState getState() {
        return myState;
    }

    /**
     * Sets the question handler this door should use.
     *
     * @param theHandler The new question handler to use.
     */
    public void setHandler(final QuestionHandler theHandler) {
        myQuestionHandler = theHandler;
    }

    /**
     * Adds a new update listener to this DoorController.
     *
     * @param theListener Update Listener to add.
     */
    public void addUpdateListener(final DoorUpdateListener theListener) {
        myListeners.add(theListener);
    }

    /**
     * Removes an existing update listener from this DoorController.
     *
     * @param theListener Update Listener to remove.
     * @return True if the provided listener was present and could be removed, False otherwise.
     */
    public boolean removeUpdateListener(final DoorUpdateListener theListener) {
        return myListeners.remove(theListener);
    }

    /**
     * Updates all listeners of this controller.
     */
    private void updateListeners() {
        for (final DoorUpdateListener listener : myListeners) {
            listener.doUpdate(this);
        }
    }

    /**
     * Handles the result of this door's question being asked.
     *
     * @param theResult Result to be handled.
     */
    private void handleQuestionResult(final QuestionHandler.QuestionResult theResult) {
        switch (theResult) {
            case CORRECT:
                myState = DoorState.OPEN;
                break;

            case INCORRECT:
                myState = DoorState.LOCKED;
                break;

            case CANCELLED:
                myState = DoorState.UNANSWERED;
                break;

            default:
                throw new IllegalArgumentException(INVALID_RESULT_STRING);
        }

        updateListeners();
    }

    /**
     * Triggers the associated question to be shown to the player.
     */
    private void triggerQuestion() {
        try {
            myQuestionHandler.askQuestion(myQuestion, new DoorAnswerCallback());
        } catch (final QuestionHandler.QuestionRejectedException exception) {
            // Can't currently ask the question, this shouldn't happen so log it and continue.
            System.err.println(exception.getMessage());
            myState = DoorState.UNANSWERED;
        }

        updateListeners();
    }

    /**
     * Enumeration of possible door states.
     */
    public enum DoorState {

        /**
         * Door with an unanswered question.
         */
        UNANSWERED,

        /**
         * Door which was answered incorrectly, making it locked.
         */
        LOCKED,

        /**
         * Door which was answered correctly, opening it.
         */
        OPEN,
    }

    /**
     * A door tile in a room.
     */
    public final class DoorTile implements Tile {

        /**
         * Constructs a new DoorTile. Should only be able to be called by
         *  its outer class, DoorController.
         */
        DoorTile() { }

        @Override
        public boolean tryMoveTo() {
            return switch (myState) {
                case UNANSWERED -> {
                    // Trigger associated question, but keep the player where they are.
                    triggerQuestion();
                    yield false;
                }
                case OPEN ->
                    // Open door can be moved through without any issue.
                        true;
                case LOCKED ->
                    // A locked door can't be moved through.
                        false;
            };
        }

        @Override
        public TileID getTileID() {
            return switch (myState) {
                case UNANSWERED -> TileID.DOOR_UNANSWERED;
                case LOCKED -> TileID.DOOR_LOCKED;
                case OPEN -> TileID.DOOR_OPEN;
            };
        }
    }

    private final class DoorAnswerCallback implements QuestionHandler.AnswerCallback {
        public void call(final QuestionHandler.QuestionResult theResult) {
            handleQuestionResult(theResult);
        }
    }
}