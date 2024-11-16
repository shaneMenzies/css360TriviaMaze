package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.enums.Direction;
import model.enums.GamePlayPhase;
import model.interfaces.GameStateUpdateListener;
import model.interfaces.QuestionHandler;
import model.utilities.EmptyMazeGenerator;
import model.utilities.RealisticSquareMazeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the GameState class.
 *
 * @author Shane Menzies
 * @version 11/14/24
 */
class GameStateTests {

    /**
     * Size of test room.
     */
    private static final int TEST_ROOM_SIZE = 3;

    /**
     * Size of the test maze.
     */
    private static final int TEST_MAZE_SIZE = 3;

    /**
     * GameSettings for testing.
     */
    private static final GameSettings TEST_SETTINGS
            = new GameSettings(3, 100, -100);

    /**
     * Question for testing.
     */
    private static final TriviaQuestion TEST_QUESTION
            = new TriviaQuestion(
                    "Test Question",
                    "Correct Answer",
                    TriviaQuestion.QuestionType.SHORT_ANSWER
            );

    /**
     * Starting room's X coordinate.
     */
    private static final int TEST_START_X = 1;

    /**
     * Starting room's Y coordinate.
     */
    private static final int TEST_START_Y = 1;

    /**
     * Exit room's X coordinate.
     */
    private static final int TEST_EXIT_X = 0;

    /**
     * Exit room's Y coordinate.
     */
    private static final int TEST_EXIT_Y = 0;

    /**
     * Basic update listener for testing.
     */
    private final GameStateUpdateListener myTestListener
            = new GameStateUpdateListener() {
        @Override
        public void doUpdate(final UpdateType theUpdateType, final GameState theGameState) {
            myLastUpdate = theUpdateType;
            myUpdateCount++;
        }
    };

    /**
     * Update type to track last type of update sent to the update listener.
     */
    private GameStateUpdateListener.UpdateType myLastUpdate;

    /**
     * Update count to track calls to the update listener.
     */
    private int myUpdateCount;

    /**
     * Maze for testing.
     */
    private Maze myMaze;

    /**
     * Instance of GameState for use in tests.
     */
    private GameState myGameState;

    /**
     * Sets up the GameState instance before each test.
     */
    @BeforeEach
    void setUp() {
        // Setup Maze
        final EmptyMazeGenerator emptyMazeGenerator = new EmptyMazeGenerator(
                TEST_MAZE_SIZE, TEST_ROOM_SIZE,
                TEST_START_X, TEST_START_Y,
                TEST_EXIT_X, TEST_EXIT_Y
        );
        myMaze = emptyMazeGenerator.generate();

        // Setup GameState
        myGameState = new GameState(TEST_SETTINGS, myMaze);

        // Reset update listener fields
        myLastUpdate = null;
        myUpdateCount = 0;
    }

    /**
     * Tests GameState's getMaze() method.
     */
    @Test
    void getMaze() {
        assertSame(myMaze, myGameState.getMaze());
    }

    /**
     * Tests GameState's getPlayer() method.
     */
    @Test
    void getPlayer() {
        final Coordinates expectedCoordinates =
                new Coordinates(
                        TEST_START_X, TEST_START_Y,
                        TEST_ROOM_SIZE / 2, TEST_ROOM_SIZE / 2
                );

        final Player received = myGameState.getPlayer();

        assertEquals(expectedCoordinates, received.getPosition(),
                "GameState.getPlayer() returned a player in unexpected " +
                        "position " + received.getPosition() + "!");
        assertEquals(TEST_SETTINGS.getInitialPlayerLives(), received.getLives(),
                "GameState.getPlayer() returned a player with "
                        + received.getLives() + " lives instead of expected "
                        + TEST_SETTINGS.getInitialPlayerLives() + "!");
        assertEquals(0, received.getScore(),
                "GameState.getPlayer() returned a player with a non-zero score!");
    }

    /**
     * Tests GameState's addUpdateListener() method.
     */
    @Test
    void addUpdateListener() {
        myGameState.addUpdateListener(myTestListener);
        assertEquals(0, myUpdateCount,
                "GameState sent update to listeners before expected!");

        // Later tests will test each individual method for update listeners,
        // so we use the simplest one here.
        myGameState.setPhase(GamePlayPhase.PAUSED);
        assertEquals(1, myUpdateCount,
                "GameState did not update listeners when it should have.");
    }

    /**
     * Tests GameState's removeUpdateListener() method.
     */
    @Test
    void removeUpdateListener() {
        myGameState.addUpdateListener(myTestListener);

        assertTrue(myGameState.removeUpdateListener(myTestListener),
                "Failed to remove the previously added update listener!");

        myGameState.setPhase(GamePlayPhase.PAUSED);
        assertEquals(0, myUpdateCount,
                "GameState updated listener that should have been " +
                        "removed!");
    }

    /**
     * Tests GameState's getPhase() and setPhase() methods.
     */
    @Test
    void getSetPhase() {
        GamePlayPhase received = myGameState.getPhase();
        assertEquals(GamePlayPhase.NOT_STARTED, received,
                "GameState.getPhase() returned " + received
                        + " instead of NOT_STARTED!");

        for (final GamePlayPhase testPhase : GamePlayPhase.values()) {
            myGameState.setPhase(testPhase);
            received = myGameState.getPhase();
            assertEquals(testPhase, received,
                    "GameState.getPhase() returned " + received
                            + " instead of "
                            +  testPhase + "!");
        }
    }

    /**
     * Tests GameState's movePlayer() method.
     * (Basic test of movement only)
     */
    @Test
    void movePlayer() {
        myGameState.addUpdateListener(myTestListener);

        final Coordinates initialPos = myGameState.getPlayer().getPosition();
        Coordinates expectedPos = new Coordinates(
                initialPos.getRoomX(), initialPos.getRoomY(),
                initialPos.getX(), initialPos.getY() + 1
        );
        myGameState.movePlayer(Direction.UP);
        assertEquals(expectedPos, myGameState.getPlayer().getPosition(),
                "Moving Player Up did not work as expected!");

        myGameState.movePlayer(Direction.DOWN);
        assertEquals(initialPos, myGameState.getPlayer().getPosition(),
                "Moving Player Down did not work as expected!");

        expectedPos = new Coordinates(
                initialPos.getRoomX(), initialPos.getRoomY(),
                initialPos.getX() + 1, initialPos.getY()
        );

        myGameState.movePlayer(Direction.RIGHT);
        assertEquals(expectedPos, myGameState.getPlayer().getPosition(),
                "Moving Player Right did not work as expected!");

        myGameState.movePlayer(Direction.LEFT);
        assertEquals(initialPos, myGameState.getPlayer().getPosition(),
                "Moving Player Left did not work as expected!");

    }

    /**
     * Helper method that generates myMaze using a realistic
     * maze generator.
     */
    void makeRealisticMaze() {
        final RealisticSquareMazeGenerator realisticMazeGenerator = new RealisticSquareMazeGenerator(
                TEST_MAZE_SIZE, TEST_ROOM_SIZE,
                TEST_START_X, TEST_START_Y,
                TEST_EXIT_X, TEST_EXIT_Y,
                TEST_QUESTION
        );
        myMaze = realisticMazeGenerator.generate();
    }

    /**
     * Tests that doors work correctly (when given a correct answer) inside GameState.
     */
    @Test
    void testDoorCorrectAnswer() {
        makeRealisticMaze();
        myGameState.addUpdateListener(myTestListener);

        assertEquals(0, myUpdateCount,
                "Unexpected updates sent to update listener!");

        Coordinates initialCoordinates = myGameState.getPlayer().getPosition();

        // Move player up to door.
        final int spacesToMove = TEST_ROOM_SIZE / 2;
        for (int i = 0; i < spacesToMove; i++) {
            myGameState.movePlayer(Direction.UP);
        }

        // Player should have been stopped just before the door.
        Coordinates expectedCoordinates = new Coordinates(
                initialCoordinates.getRoomX(),
                initialCoordinates.getRoomY(),
                initialCoordinates.getX(),
                initialCoordinates.getY() + spacesToMove - 1
        );
        Coordinates receivedCoordinates = myGameState.getPlayer().getPosition();
        System.out.println(myGameState.getMaze().getTile(receivedCoordinates).getClass());
        assertEquals(expectedCoordinates, receivedCoordinates,
                "Found player at " + receivedCoordinates + "! Expected "
                        + expectedCoordinates);


        // GamePlayPhase should have changed to Trivia
        assertEquals(GamePlayPhase.TRIVIA, myGameState.getPhase(),
                "GameState phase didn't change to Trivia mode when expected!");
        assertSame(TEST_QUESTION, myGameState.getQuestion(),
                "GameState returned a different question than expected!");

        // Try correct answer
        assertEquals(QuestionHandler.QuestionResult.CORRECT,
                myGameState.answerQuestion(TEST_QUESTION.getAnswer()),
                "GameState.answerQuestion didn't recognize correct answer"
                        + " as being correct!");

        // Player should now be able to move up correctly
        initialCoordinates = myGameState.getPlayer().getPosition();
        expectedCoordinates = new Coordinates(
                initialCoordinates.getRoomX(),
                initialCoordinates.getRoomY(),
                initialCoordinates.getX(),
                initialCoordinates.getY() + 1
        );

        myGameState.movePlayer(Direction.UP);

        receivedCoordinates = myGameState.getPlayer().getPosition();
        assertEquals(expectedCoordinates, receivedCoordinates,
                "Found player at " + receivedCoordinates + "! Expected "
                        + expectedCoordinates);
    }

    /**
     * Tests that doors work correctly (when given an incorrect answer) inside GameState.
     */
    @Test
    void testDoorIncorrectAnswer() {
        makeRealisticMaze();
        myGameState.addUpdateListener(myTestListener);

        assertEquals(0, myUpdateCount,
                "Unexpected updates sent to update listener!");

        Coordinates initialCoordinates = myGameState.getPlayer().getPosition();

        // Move player up to door.
        final int spacesToMove = TEST_ROOM_SIZE / 2;
        for (int i = 0; i < spacesToMove; i++) {
            myGameState.movePlayer(Direction.UP);
        }

        // Player should have been stopped just before the door.
        Coordinates expectedCoordinates = new Coordinates(
                initialCoordinates.getRoomX(),
                initialCoordinates.getRoomY(),
                initialCoordinates.getX(),
                initialCoordinates.getY() + spacesToMove - 1
        );
        Coordinates receivedCoordinates = myGameState.getPlayer().getPosition();
        assertEquals(expectedCoordinates, receivedCoordinates,
                "Found player at " + receivedCoordinates + "! Expected "
                        + expectedCoordinates);


        // GamePlayPhase should have changed to Trivia
        assertEquals(GamePlayPhase.TRIVIA, myGameState.getPhase(),
                "GameState phase didn't change to Trivia mode when expected!");
        assertSame(TEST_QUESTION, myGameState.getQuestion(),
                "GameState returned a different question than expected!");

        // Try correct answer
        assertEquals(QuestionHandler.QuestionResult.CORRECT,
                myGameState.answerQuestion(TEST_QUESTION.getAnswer()),
                "GameState.answerQuestion didn't recognize correct answer"
                        + " as being correct!");

        // Player should now be able to move up correctly
        initialCoordinates = myGameState.getPlayer().getPosition();
        expectedCoordinates = new Coordinates(
                initialCoordinates.getRoomX(),
                initialCoordinates.getRoomY(),
                initialCoordinates.getX(),
                initialCoordinates.getY() + 1
        );

        myGameState.movePlayer(Direction.UP);

        receivedCoordinates = myGameState.getPlayer().getPosition();
        assertEquals(expectedCoordinates, receivedCoordinates,
                "Found player at " + receivedCoordinates + "! Expected "
                        + expectedCoordinates);
    }

    /**
     * Tests that doors work correctly (when cancelled) inside GameState.
     */
    @Test
    void testDoorCancel() {
        makeRealisticMaze();
        myGameState.addUpdateListener(myTestListener);

        assertEquals(0, myUpdateCount,
                "Unexpected updates sent to update listener!");

        Coordinates initialCoordinates = myGameState.getPlayer().getPosition();

        // Move player up to door.
        final int spacesToMove = TEST_ROOM_SIZE / 2;
        for (int i = 0; i < spacesToMove; i++) {
            myGameState.movePlayer(Direction.UP);
        }

        // Player should have been stopped just before the door.
        Coordinates expectedCoordinates = new Coordinates(
                initialCoordinates.getRoomX(),
                initialCoordinates.getRoomY(),
                initialCoordinates.getX(),
                initialCoordinates.getY() + spacesToMove - 1
        );
        Coordinates receivedCoordinates = myGameState.getPlayer().getPosition();
        assertEquals(expectedCoordinates, receivedCoordinates,
                "Found player at " + receivedCoordinates + "! Expected "
                        + expectedCoordinates);


        // GamePlayPhase should have changed to Trivia
        assertEquals(GamePlayPhase.TRIVIA, myGameState.getPhase(),
                "GameState phase didn't change to Trivia mode when expected!");
        assertSame(TEST_QUESTION, myGameState.getQuestion(),
                "GameState returned a different question than expected!");

        // Try correct answer
        assertEquals(QuestionHandler.QuestionResult.CORRECT,
                myGameState.answerQuestion(TEST_QUESTION.getAnswer()),
                "GameState.answerQuestion didn't recognize correct answer"
                        + " as being correct!");

        // Player should now be able to move up correctly
        initialCoordinates = myGameState.getPlayer().getPosition();
        expectedCoordinates = new Coordinates(
                initialCoordinates.getRoomX(),
                initialCoordinates.getRoomY(),
                initialCoordinates.getX(),
                initialCoordinates.getY() + 1
        );

        myGameState.movePlayer(Direction.UP);

        receivedCoordinates = myGameState.getPlayer().getPosition();
        assertEquals(expectedCoordinates, receivedCoordinates,
                "Found player at " + receivedCoordinates + "! Expected "
                        + expectedCoordinates);
    }

    /**
     * Tests that questions correctly affect the player's score.
     */
    @Test
    void testScore() {

    }

    /**
     * Tests GameState's logic for determining the player has won.
     */
    @Test
    void playerVictory() {

    }

    /**
     * Tests GameState's logic for determining the player has lost.
     */
    @Test
    void playerLost() {

    }
}