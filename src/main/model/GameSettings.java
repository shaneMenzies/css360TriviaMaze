package model;

import java.io.Serializable;

/**
 * Holds the settings for an active game.
 *
 * @author Shane Menzies
 * @version 11/3/24
 */
public final class GameSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Setting for how many lives the player starts with.
     */
    private final int myInitialPlayerLives;

    /**
     * Setting for how the player's score changes after a question
     * is answered correctly.
     */
    private final int myCorrectAnswerScore;

    /**
     * Setting for how the player's score changes after a question
     * is answered incorrectly.
     */
    private final int myWrongAnswerScore;

    /**
     * Makes a new GameSettings with the provided settings.
     *
     * @param theInitialLives How many lives the player starts with.
     * @param theCorrectScore Change to player's score on a correct answer.
     * @param theWrongScore Change to player's score on an incorrect answer.
     */
    public GameSettings(final int theInitialLives,
                        final int theCorrectScore, final int theWrongScore) {
        myInitialPlayerLives = theInitialLives;
        myCorrectAnswerScore = theCorrectScore;
        myWrongAnswerScore = theWrongScore;
    }

    /**
     * Gets how many lives the player should start with.
     *
     * @return Number of lives the player should start with.
     */
    public int getInitialPlayerLives() {
        return myInitialPlayerLives;
    }

    /**
     * Gets the value which should be added to the player's score
     * when they answer a question correctly.
     *
     * @return Value to be added to score on a correct answer.
     */
    public int getCorrectAnswerScore() {
        return myCorrectAnswerScore;
    }

    /**
     * Gets the value which should be added to the player's score
     * when they answer a question incorrectly.
     *
     * @return Value to be added to score on an incorrect answer.
     */
    public int getWrongAnswerScore() {
        return myWrongAnswerScore;
    }
}