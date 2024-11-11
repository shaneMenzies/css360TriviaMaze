package model;

import static org.junit.jupiter.api.Assertions.*;

import model.interfaces.DoorUpdateListener;
import model.interfaces.QuestionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the DoorController class, and it's inner class, DoorTile.
 *
 * @author Shane Menzies
 * @version 11/1/24
 */
class DoorControllerTests {

    /**
     * TriviaQuestion for testing.
     */
    private static final TriviaQuestion TEST_QUESTION = new TriviaQuestion(
            "Test Question",
            "Test Answer",
            TriviaQuestion.QuestionType.TRUE_FALSE);

    /**
     * Basic implementation of QuestionHandler for testing DoorController.
     */
    private final QuestionHandler myQuestionHandler = new QuestionHandler() {
        @Override
        public void askQuestion(TriviaQuestion theQuestion, AnswerCallback theCallback) {
            myHasQuestion = true;
            myAnswerCallback = theCallback;
        }
    };

    /**
     * Answer Callback stored for QuestionHandler.
     */
    private QuestionHandler.AnswerCallback myAnswerCallback;

    /**
     * Whether the QuestionHandler has received a question or not.
     */
    private boolean myHasQuestion;

    /**
     * Basic update listener for update listener tests.
     */
    private final DoorUpdateListener myUpdateListener = new DoorUpdateListener() {
        @Override
        public void doUpdate(DoorController theController) {
            myUpdateCount++;
        }
    };

    /**
     * Update counter for update listener to increment.
     */
    private int myUpdateCount;

    /**
     * Instance of DoorController for use in tests.
     */
    private DoorController myController;

    /**
     * Reset state before each test.
     */
    @BeforeEach
    void setUp() {
        myUpdateCount = 0;
        myController = new DoorController(TEST_QUESTION);
    }

    /**
     * Test's addUpdateListener.
     */
    @Test
    void addUpdateListener() {
        assertDoesNotThrow(() -> myController.addUpdateListener(myUpdateListener));
    }

    /**
     * Test's removeUpdateListener.
     */
    @Test
    void removeUpdateListener() {
        // Test removing a listener which hasn't been added yet
        assertFalse(myController.removeUpdateListener(myUpdateListener),
                "DoorController.removeUpdateListener failed test for removing "
                        + "a listener which has not been added!");

        myController.addUpdateListener(myUpdateListener);

        // Normal removal
        assertTrue(myController.removeUpdateListener(myUpdateListener),
                "DoorController.removeUpdateListener failed test for normal removal"
                        + " of a listener!");

        assertFalse(myController.removeUpdateListener(myUpdateListener),
                "DoorController.removeUpdateListener failed test for removing "
                        + "a listener which has already been removed!");
    }

    /**
     * Test's getDoors.
     */
    @Test
    void getDoors() {
        for (final DoorController.DoorTile tile : myController.getDoors()) {
            assertInstanceOf(DoorController.DoorTile.class, tile);
        }
    }

    /**
     * Test's the getState() method.
     */
    @Test
    void getState() {
        assertEquals(DoorController.DoorState.UNANSWERED, myController.getState());
    }

    /**
     * Tests setHandler() method.
     */
    @Test
    void setHandler() {
        myController.setHandler(myQuestionHandler);

        // Must call tryMoveTo() on a tile to get the
        // DoorController to send a question to the handler.
        assertFalse(myHasQuestion,
                "Question Handler was given a question before it should have been!");
        myController.getDoors()[0].tryMoveTo();
        assertTrue(myHasQuestion,
                "QuestionHandler wasn't given a question when it should have been!");

        // Check that the state gets changed according to the question result.
        myAnswerCallback.call(QuestionHandler.QuestionResult.CORRECT);
        myHasQuestion = false;
        assertEquals(DoorController.DoorState.OPEN, myController.getState(),
                "DoorController didn't change state to open after the question was" +
                        " answered correctly!");

        myController = new DoorController(TEST_QUESTION);
        myController.setHandler(myQuestionHandler);

        myController.getDoors()[0].tryMoveTo();
        myAnswerCallback.call(QuestionHandler.QuestionResult.INCORRECT);
        myHasQuestion = false;
        assertEquals(DoorController.DoorState.LOCKED, myController.getState(),
                "DoorController didn't change state to locked after the question was" +
                        " answered incorrectly!");

        myController = new DoorController(TEST_QUESTION);
        myController.setHandler(myQuestionHandler);

        myController.getDoors()[0].tryMoveTo();
        myAnswerCallback.call(QuestionHandler.QuestionResult.CANCELLED);
        myHasQuestion = false;
        assertEquals(DoorController.DoorState.UNANSWERED, myController.getState(),
                "DoorController changed it's state even though question was " +
                        "cancelled!");
    }

    /**
     * Test's trying to move to one of the door's tiles.
     */
    @Test
    void tileMoveTo() {
        myController.setHandler(myQuestionHandler);
        myController.addUpdateListener(myUpdateListener);
        assertEquals(0, myUpdateCount);

        // First attempt should not be passable, and update listeners.
        assertFalse(myController.getDoors()[0].tryMoveTo(), "DoorTile.tryMoveTo let" +
                "player move through unanswered door!");
        assertEquals(1, myUpdateCount, "DoorTile.tryMoveTo did not " +
                "update listeners!");
    }

    /**
     * Tests DoorTile.getTileID
     */
    @Test
    void tileGetTileID() {
        for (final DoorController.DoorTile tile : myController.getDoors()) {
            assertEquals(TileID.DOOR_UNANSWERED, tile.getTileID());
        }

        // TODO: Add more complete testing once more of the question handling is implemented.
    }
}