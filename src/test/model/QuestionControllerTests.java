package model;

import static org.junit.jupiter.api.Assertions.*;

import model.interfaces.QuestionControllerUpdateListener;
import model.interfaces.QuestionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the QuestionController class.
 *
 * @author Shane Menzies
 * @version 11/10/24
 */
class QuestionControllerTests {

    /**
     * Simple trivia question to test the QuestionController with.
     */
    private static final TriviaQuestion TEST_QUESTION = new TriviaQuestion(
            "This is a test Question.", "This is the answer.",
            TriviaQuestion.QuestionType.SHORT_ANSWER);

    /**
     * String for testing an incorrect answer.
     */
    private static final String INCORRECT_ANSWER = "This answer is wrong.";

    /**
     * Basic update listener for testing.
     */
    private final QuestionControllerUpdateListener myControllerUpdateListener =
            new QuestionControllerUpdateListener() {
                @Override
                public void doUpdate(final UpdateType theType, final QuestionController theQuestionController) {
                    myUpdateCount++;
                }
            };

    /**
     * Basic callback method for testing.
     */
    private final QuestionHandler.AnswerCallback myAnswerCallback =
            new QuestionHandler.AnswerCallback() {
                @Override
                public void call(final QuestionHandler.QuestionResult theResult) {
                    myLastResult = theResult;
                }
            };

    /**
     * Tracks calls to the update listener.
     */
    private int myUpdateCount;

    /**
     * Tracks last result returned to the callback.
     */
    private QuestionHandler.QuestionResult myLastResult;

    /**
     * Test instance of QuestionController.
     */
    private QuestionController myController;

    /**
     * Sets up the test object to a known state before each test.
     */
    @BeforeEach
    void setUp() {
        myController = new QuestionController();
        myController.addListener(myControllerUpdateListener);
        myLastResult = null;
        myUpdateCount = 0;
    }

    /**
     * Tests the askQuestion() method.
     */
    @Test
    void askQuestion() {
        assertDoesNotThrow(() -> myController.askQuestion(TEST_QUESTION, myAnswerCallback));
    }

    /**
     * Tests the hasQuestion() method.
     */
    @Test
    void hasQuestion() {
        assertFalse(myController.hasQuestion(),
                "QuestionController should not already have a question!");

        assertDoesNotThrow(() -> myController.askQuestion(TEST_QUESTION, myAnswerCallback));

        assertTrue(myController.hasQuestion(),
                "QuestionController should have a question after being"
                + " provided one!");

        assertThrows(QuestionHandler.QuestionRejectedException.class,
                () -> myController.askQuestion(TEST_QUESTION, myAnswerCallback),
                "QuestionHandler should raise an exception when a second "
                        + "question is asked without the first being resolved!");
    }

    /**
     * Tests the getQuestion() method.
     */
    @Test
    void getQuestion() {
        assertDoesNotThrow(() -> myController.askQuestion(TEST_QUESTION, myAnswerCallback));

        assertSame(TEST_QUESTION, myController.getQuestion());
    }

    /**
     * Tests the addListener() method and listener functionality.
     */
    @Test
    void addListener() {
        assertEquals(0, myUpdateCount,
                "UpdateListener was called before it should have been!");

        assertDoesNotThrow(() -> myController.askQuestion(TEST_QUESTION, myAnswerCallback));

        assertEquals(1, myUpdateCount,
                "UpdaterListener was called "
                + myUpdateCount + " times instead of 1!");

        myController.answerQuestion(TEST_QUESTION.getAnswer());
        assertEquals(2, myUpdateCount,
                "UpdaterListener was called "
                        + myUpdateCount + " times instead of 2!");
    }

    /**
     * Tests the removeListener() method.
     */
    @Test
    void removeListener() {
        assertTrue(myController.removeListener(myControllerUpdateListener));
    }

    /**
     * Tests the answerQuestion() method.
     */
    @Test
    void answerQuestion() {
        assertDoesNotThrow(() -> myController.askQuestion(TEST_QUESTION, myAnswerCallback));

        assertNull(myLastResult, "Callback shouldn't have been called yet!");

        myController.answerQuestion(TEST_QUESTION.getAnswer());
        assertEquals(QuestionHandler.QuestionResult.CORRECT, myLastResult,
                "Callback got " + myLastResult + " instead of CORRECT!");

        assertDoesNotThrow(() -> myController.askQuestion(TEST_QUESTION, myAnswerCallback));

        myController.answerQuestion(INCORRECT_ANSWER);
        assertEquals(QuestionHandler.QuestionResult.INCORRECT, myLastResult,
                "Callback got " + myLastResult + " instead of INCORRECT!");
    }

    /**
     * Tests the cancelQuestion() method.
     */
    @Test
    void cancelQuestion() {
        assertDoesNotThrow(() -> myController.askQuestion(TEST_QUESTION, myAnswerCallback));

        assertNull(myLastResult, "Callback shouldn't have been called yet!");

        myController.cancelQuestion();
        assertEquals(QuestionHandler.QuestionResult.CANCELLED, myLastResult,
                "Callback got " + myLastResult + " instead of CANCELLED!");
    }
}