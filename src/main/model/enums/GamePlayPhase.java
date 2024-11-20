package model.enums;

/**
 * Enum representing the active phase of the game.
 *
 * @author Arafa Mohamed
 * @author Shane Menzies
 * @version 11/14/24
 */
public enum GamePlayPhase {

    /**
     * Game has not been started yet.
     */
    NOT_STARTED,

    /**
     * Indicates the game is in the normal progress phase.
     */
    IN_PROGRESS,

    /**
     * Indicates the game is paused.
     */
    PAUSED,

    /**
     * Player is answering a question.
     */
    TRIVIA,

    /**
     * Indicates the player has won the game.
     */
    VICTORY,


    /**
     * Indicates the player has lost the game.
     */
    FAILURE,

}