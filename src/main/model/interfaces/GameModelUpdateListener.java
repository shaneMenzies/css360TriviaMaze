package model.interfaces;

import model.GameModel;

/**
 * Performs an action whenever the GameModel's state has changed.
 *
 * @author Shane Menzies
 * @version 11/27/24
 */
public interface GameModelUpdateListener {
    void doUpdate(UpdateType theUpdateType, GameModel theGameModel);

    /**
     * Different possible types of updates.
     */
    enum UpdateType {
        /**
         * A change in the GameState's phase.
         */
        GAME_STATE_PHASE,

        /**
         * A change in the GameState's player.
         */
        GAME_STATE_PLAYER,

        /**
         * A change in the GameState's doors.
         */
        GAME_STATE_DOORS,

        /**
         * The game was saved.
         */
        SAVED,

        /**
         * The game was loaded.
         */
        LOADED,

        /**
         * A new game was started.
         */
        NEW_GAME;

        public static UpdateType fromGameStateUpdate(
                final GameStateUpdateListener.UpdateType theType) {
           return switch (theType) {
               case PHASE:
                   yield GAME_STATE_PHASE;
               case PLAYER:
                   yield GAME_STATE_PLAYER;
               case DOORS:
                   yield GAME_STATE_DOORS;
           };
        }
    }
}