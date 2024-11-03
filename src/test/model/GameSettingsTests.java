package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * Unit tests for the GameSettings Class.
 *
 * @author Shane Menzies
 * @version 11/3/24
 */
class GameSettingsTests {

    /**
     * Default value for initial lives.
     */
    private static final int TEST_INITIAL_LIVES = 3;

    /**
     * Default value for the correct answer score.
     */
    private static final int TEST_CORRECT_SCORE = 100;

    /**
     * Default value for the wrong answer score.
     */
    private static final int TEST_WRONG_SCORE = -100;

    /**
     * Maximum possible value to consider for tests.
     */
    private static final int MAX_TEST_VALUE = 1000000;

    /**
     * How many times to repeat tests involving random values.
     */
    private static final int RANDOM_TEST_REPETITIONS = 100;

    /**
     * GameSettings object for use in tests.
     */
    private GameSettings mySettings;

    /**
     * Prepares mySettings before each unit test.
     */
    @BeforeEach
    void setUp() {
        mySettings
                = new GameSettings(TEST_INITIAL_LIVES, TEST_CORRECT_SCORE, TEST_WRONG_SCORE);
    }

    /**
     * Tests getInitialPlayerLives() getter method.
     */
    @Test
    void getInitialPlayerLives() {
        assertEquals(TEST_INITIAL_LIVES, mySettings.getInitialPlayerLives());
    }

    /**
     * Tests getCorrectAnswerScore() getter method.
     */
    @Test
    void getCorrectAnswerScore() {
        assertEquals(TEST_CORRECT_SCORE, mySettings.getCorrectAnswerScore());
    }

    /**
     * Tests getWrongAnswerScore() getter method.
     */
    @Test
    void getWrongAnswerScore() {
        assertEquals(TEST_WRONG_SCORE, mySettings.getWrongAnswerScore());
    }

    /**
     * Tests getters with random values.
     */
    @Test
    void randomValuesGettersTest() {
        Random rand  = new Random();

        for (int i = 0; i < RANDOM_TEST_REPETITIONS; i++) {
            int[] expected = getRandomValues(rand);

            mySettings = new GameSettings(expected[0], expected[1], expected[2]);

            assertEquals(expected[0], mySettings.getInitialPlayerLives());
            assertEquals(expected[1], mySettings.getCorrectAnswerScore());
            assertEquals(expected[2], mySettings.getWrongAnswerScore());
        }
    }

    /**
     * Helper function to generate a set of possible values for GameSettings.
     *
     * @param theRandom Random generator to take random values from.
     * @return int array of appropriate values for GameSettings.
     */
    private int[] getRandomValues(final Random theRandom) {
        // Array of values corresponding to the parts of GameSettings
        int[] testValues = new int[3];

        // Get random (somewhat appropriate) values
        for (int i = 0; i < testValues.length; i++) {
            // Gets a random value between [1, MAX_TEST_VALUE] (inclusive)
            testValues[i] = theRandom.nextInt(MAX_TEST_VALUE) + 1;
        }

        // Wrong answer score should be negative
        testValues[2] = -testValues[2];

        return testValues;
    }
}