package model.interfaces;

import model.GameState;

public interface GameStateUpdateListener {

    /**
     * Performs some action when the GameState updates.
     * @param theUpdateType Type of change in the GameState.
     * @param theGameState GameState which changed.
     */
    void doUpdate(UpdateType theUpdateType, GameState theGameState);

    /**
     * Enum for the different types of possible updates.
     */
    enum UpdateType {

        /**
         * A change in the game phase.
         */
        PHASE,

        /**
         * A change in the player's state.
         */
        PLAYER,

        /**
         * A change in the state of the maze's doors.
         */
        DOORS,
    }

}