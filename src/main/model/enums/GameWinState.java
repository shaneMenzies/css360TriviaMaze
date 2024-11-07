package model.enums;

/**
 * Enum representing the win state of the game.
 * It can be in progress, a victory, or a failure.
 *
 * @author Arafa Mohamed
 * @version 10/26/24
 */
public enum GameWinState {

    /**
     * Game has not been started yet.
     */
    NOT_STARTED,

    /** Indicates the player has won the game. */
    VICTORY,

    /** Indicates the game is currently ongoing. */
    IN_PROGRESS,

    /** Indicates the player has lost the game. */
    FAILURE,

}