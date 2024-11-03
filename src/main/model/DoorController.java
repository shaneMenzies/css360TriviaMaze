package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for a pair of doors in a maze.
 *
 * @author Shane Menzies
 * @version 10/27/24
 */
public final class DoorController {

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
     * Triggers the associated question to be shown to the player.
     */
    private void triggerQuestion() {
        // TODO: Make associated question active in GameState
        System.out.println("DEBUG: Question would be asked here:\n\tQuestion: "
                + myQuestion.getQuestion() + "\n\tAnswer: " + myQuestion.getAnswer());

        System.out.println("DEBUG: Opening door.");
        myState = DoorState.OPEN;

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
}