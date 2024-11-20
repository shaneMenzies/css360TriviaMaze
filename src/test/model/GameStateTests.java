package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
    private static final int TEST_ROOM_SIZE = 5;

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
     * A wrong answer to test the GameState with.
     */
    private static final String TEST_WRONG_ANSWER = "This is not the answer!";

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

    Maze makeEmptyMaze() {
        final EmptyMazeGenerator emptyMazeGenerator = new EmptyMazeGenerator(
                TEST_MAZE_SIZE, TEST_ROOM_SIZE,
                TEST_START_X, TEST_START_Y,
                TEST_EXIT_X, TEST_EXIT_Y
        );
        return emptyMazeGenerator.generate();
    }

    /**
     * Helper method that generates a Maze using a realistic
     * maze generator.
     */
    Maze makeRealisticMaze() {
        final RealisticSquareMazeGenerator realisticMazeGenerator = new RealisticSquareMazeGenerator(
                TEST_MAZE_SIZE, TEST_ROOM_SIZE,
                TEST_START_X, TEST_START_Y,
                TEST_EXIT_X, TEST_EXIT_Y,
                TEST_QUESTION
        );
        return realisticMazeGenerator.generate();
    }

    /**
     * Sets up the GameState instance before each test.
     */
    @BeforeEach
    void setUp() {
        // Clear old gamestate
        myGameState = null;

        // Reset update listener fields
        myLastUpdate = null;
        myUpdateCount = 0;
    }

    /**
     * Tests GameState's getMaze() method.
     */
    @Test
    void getMaze() {
        myMaze = makeEmptyMaze();
        myGameState = new GameState(TEST_SETTINGS, myMaze);

        assertSame(myMaze, myGameState.getMaze());
    }

    /**
     * Tests GameState's getPlayer() method.
     */
    @Test
    void getPlayer() {
        myMaze = makeEmptyMaze();
        myGameState = new GameState(TEST_SETTINGS, myMaze);

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
        myMaze = makeEmptyMaze();
        myGameState = new GameState(TEST_SETTINGS, myMaze);

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
        myMaze = makeEmptyMaze();
        myGameState = new GameState(TEST_SETTINGS, myMaze);

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
        myMaze = makeEmptyMaze();
        myGameState = new GameState(TEST_SETTINGS, myMaze);

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
        myMaze = makeEmptyMaze();
        myGameState = new GameState(TEST_SETTINGS, myMaze);

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
     * Tests GameState's logic for determining the player has won.
     */
    @Test
    void playerVictory() {
        myMaze = makeEmptyMaze();
        myGameState = new GameState(TEST_SETTINGS, myMaze);

        myGameState.addUpdateListener(myTestListener);

        // Determine path to exit
        final int roomDiffX = TEST_START_X - TEST_EXIT_X;
        final int roomDiffY = TEST_START_Y - TEST_EXIT_Y;

        int moveX = roomDiffX * TEST_ROOM_SIZE;
        Direction xDirection = Direction.LEFT;
        if (moveX < 0) {
            xDirection = Direction.RIGHT;
            moveX = -moveX;
        }

        int moveY = roomDiffY * TEST_ROOM_SIZE;
        Direction yDirection = Direction.DOWN;
        if (moveY < 0) {
            yDirection = Direction.UP;
            moveY = -moveY;
        }

        // Move in X then Y
        for (int i = 0; i < moveX; i++) {
            myGameState.movePlayer(xDirection);
        }
        for (int i = 0; i < moveY; i++) {
            myGameState.movePlayer(yDirection);
        }

        // Player should have won
        assertEquals(GamePlayPhase.VICTORY, myGameState.getPhase(),
                "Player should have won!");
    }

    /**
     * Tests GameState's logic for determining the player has lost.
     */
    @Test
    void playerLost() {
        // For simplicity, remake the GameState with a player with a single life.
        myMaze = makeRealisticMaze();
        myGameState = new GameState(new GameSettings(
                1,
                TEST_SETTINGS.getCorrectAnswerScore(),
                TEST_SETTINGS.getWrongAnswerScore()
        ), myMaze);

        myGameState.addUpdateListener(myTestListener);

        // Move player up to door.
        final int spacesToMove = TEST_ROOM_SIZE / 2;
        for (int i = 0; i < (spacesToMove - 1); i++) {
            myGameState.movePlayer(Direction.UP);
        }
        myGameState.movePlayer(Direction.UP);

        assertEquals(GamePlayPhase.TRIVIA, myGameState.getPhase(),
                "Phase should have changed to TRIVIA!");

        // Provide an incorrect answer, should trigger failure.
        myGameState.answerQuestion(TEST_WRONG_ANSWER);
        assertEquals(GamePlayPhase.FAILURE, myGameState.getPhase(),
                "Player should have lost the game!");
    }

    /**
     * Tests that doors work correctly (when given a correct answer) inside GameState.
     */
    @Test
    void testDoorCorrectAnswer() {
        myMaze = makeRealisticMaze();
        myGameState = new GameState(TEST_SETTINGS, myMaze);

        myGameState.addUpdateListener(myTestListener);

        assertEquals(0, myUpdateCount,
                "Unexpected updates sent to update listener!");

        Coordinates initialCoordinates = myGameState.getPlayer().getPosition();

        // Move player up to door.
        final int spacesToMove = TEST_ROOM_SIZE / 2;
        for (int i = 0; i < (spacesToMove - 1); i++) {
            myGameState.movePlayer(Direction.UP);
            assertEquals(1, myUpdateCount,
                    "Update Listener should have been called!");
            assertEquals(GameStateUpdateListener.UpdateType.PLAYER,
                    myLastUpdate,
                    "Last update was not the expected type!");
            myUpdateCount = 0;
        }

        // Last move up should trigger at least 1 update
        // (Somewhat debatable whether it should
        //  always trigger an update when a player
        //  attempts to move but can't so we just
        //  check for at least 1)
        myGameState.movePlayer(Direction.UP);
        assertTrue(myUpdateCount >= 1,
                "Update should have been called"
                        + " when player moved into door!");

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
        myUpdateCount = 0;
        assertEquals(QuestionHandler.QuestionResult.CORRECT,
                myGameState.answerQuestion(TEST_QUESTION.getAnswer()),
                "GameState.answerQuestion didn't recognize correct answer"
                        + " as being correct!");
        assertEquals(1, myUpdateCount,
                "Update listener should have been called"
                        + " when question was answered!");

        assertNotEquals(GamePlayPhase.TRIVIA, myGameState.getPhase(),
                "GameState's phase should have changed from TRIVIA!");

        // Player should now be able to move up correctly
        myUpdateCount = 0;
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
        assertEquals(1, myUpdateCount,
                "Update listener should have been called!");

        assertEquals(TEST_SETTINGS.getInitialPlayerLives(),
                myGameState.getPlayer().getLives(),
                "Player shouldn't have lost lives!");
        assertEquals(TEST_SETTINGS.getCorrectAnswerScore(), myGameState.getPlayer().getScore(),
                "Player's score should have been increased"
                        + " according to correct answer score!");
    }

    /**
     * Tests that doors work correctly (when given an incorrect answer) inside GameState.
     */
    @Test
    void testDoorIncorrectAnswer() {
        myMaze = makeRealisticMaze();
        myGameState = new GameState(TEST_SETTINGS, myMaze);

        myGameState.addUpdateListener(myTestListener);

        assertEquals(0, myUpdateCount,
                "Unexpected updates sent to update listener!");

        Coordinates initialCoordinates = myGameState.getPlayer().getPosition();

        // Move player up to door.
        final int spacesToMove = TEST_ROOM_SIZE / 2;
        for (int i = 0; i < (spacesToMove - 1); i++) {
            myGameState.movePlayer(Direction.UP);
            assertEquals(1, myUpdateCount,
                    "Update Listener should have been called!");
            assertEquals(GameStateUpdateListener.UpdateType.PLAYER,
                    myLastUpdate,
                    "Last update was not the expected type!");
            myUpdateCount = 0;
        }

        // Last move up should trigger at least 1 update
        // (Somewhat debatable whether it should
        //  always trigger an update when a player
        //  attempts to move but can't so we just
        //  check for at least 1)
        myGameState.movePlayer(Direction.UP);
        assertTrue(myUpdateCount >= 1,
                "Update should have been called"
                        + " when player moved into door!");

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

        // Try wrong answer
        myUpdateCount = 0;
        assertEquals(QuestionHandler.QuestionResult.INCORRECT,
                myGameState.answerQuestion(TEST_WRONG_ANSWER),
                "GameState.answerQuestion didn't recognize wrong answer"
                        + " as being wrong!");
        assertEquals(1, myUpdateCount,
                "Update listener should have been called"
                        + " when question was answered!");

        assertNotEquals(GamePlayPhase.TRIVIA, myGameState.getPhase(),
                "GameState's phase should have changed from TRIVIA!");

        // Player should still not be able to move up
        initialCoordinates = myGameState.getPlayer().getPosition();
        expectedCoordinates = new Coordinates(
                initialCoordinates.getRoomX(),
                initialCoordinates.getRoomY(),
                initialCoordinates.getX(),
                initialCoordinates.getY()
        );

        myGameState.movePlayer(Direction.UP);
        receivedCoordinates = myGameState.getPlayer().getPosition();
        assertEquals(expectedCoordinates, receivedCoordinates,
                "Found player at " + receivedCoordinates + "! Expected "
                        + expectedCoordinates);

        // Verify that Player's stats have been deducted
        assertEquals(TEST_SETTINGS.getInitialPlayerLives() - 1,
                myGameState.getPlayer().getLives(),
                "Player should have lost a life!");
        assertEquals(TEST_SETTINGS.getWrongAnswerScore(), myGameState.getPlayer().getScore(),
                "Player's score should have changed according"
                        + "to the wrong answer setting!");
    }

    /**
     * Tests that doors work correctly (when cancelled) inside GameState.
     */
    @Test
    void testDoorCancel() {
        myMaze = makeRealisticMaze();
        myGameState = new GameState(TEST_SETTINGS, myMaze);

        myGameState.addUpdateListener(myTestListener);

        assertEquals(0, myUpdateCount,
                "Unexpected updates sent to update listener!");

        Coordinates initialCoordinates = myGameState.getPlayer().getPosition();

        // Move player up to door.
        final int spacesToMove = TEST_ROOM_SIZE / 2;
        for (int i = 0; i < (spacesToMove - 1); i++) {
            myGameState.movePlayer(Direction.UP);
            assertEquals(1, myUpdateCount,
                    "Update Listener should have been called!");
            assertEquals(GameStateUpdateListener.UpdateType.PLAYER,
                    myLastUpdate,
                    "Last update was not the expected type!");
            myUpdateCount = 0;
        }

        // Last move up should trigger at least 1 update
        // (Somewhat debatable whether it should
        //  always trigger an update when a player
        //  attempts to move but can't so we just
        //  check for at least 1)
        myGameState.movePlayer(Direction.UP);
        assertTrue(myUpdateCount >= 1,
                "Update should have been called"
                        + " when player moved into door!");

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

        // Try cancelling by moving away
        myUpdateCount = 0;
        myGameState.movePlayer(Direction.DOWN);
        assertTrue(myUpdateCount >= 1,
                "Update listener should have been called"
                        + " when question was cancelled!");

        assertNotEquals(GamePlayPhase.TRIVIA, myGameState.getPhase(),
                "GameState's phase should have changed from TRIVIA!");

        // Player should be able to move back up
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

        // Should be able to reattempt the question
        myGameState.movePlayer(Direction.UP);
        receivedCoordinates = myGameState.getPlayer().getPosition();
        assertEquals(expectedCoordinates, receivedCoordinates,
                "Found player at " + receivedCoordinates + "! Expected "
                        + expectedCoordinates);
        assertEquals(GamePlayPhase.TRIVIA, myGameState.getPhase(),
                "GameState phase didn't change to Trivia mode when expected!");
        assertSame(TEST_QUESTION, myGameState.getQuestion(),
                "GameState returned a different question than expected!");

        // Verify that Player's stats haven't been affected
        assertEquals(TEST_SETTINGS.getInitialPlayerLives(),
                myGameState.getPlayer().getLives(),
                "Player shouldn't have lost lives!");
        assertEquals(0, myGameState.getPlayer().getScore(),
                "Player's score shouldn't have changed!");
    }

}