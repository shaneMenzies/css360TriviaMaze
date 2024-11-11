package model.interfaces;

import model.GameState;

public interface GameStateUpdateListener {

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