package model;

import static org.junit.jupiter.api.Assertions.*;

import model.enums.Direction;
import model.interfaces.MazeGenerator;
import model.interfaces.QuestionSource;
import model.utilities.EmptyMazeGenerator;
import org.junit.jupiter.api.Test;

/**
 * Tests of the GameModel class.
 *
 * @author Shane Menzies
 * @version 11/23/24
 */
public class GameModelTests {

    /**
     * The size of the test maze.
     */
    private static final int TEST_MAZE_SIZE = 4;

    /**
     * The size of the rooms in the test maze.
     */
    private static final int TEST_ROOM_SIZE = 5;

    /**
     * The X-coordinate of the starting room in the test maze.
     */
    private static final int TEST_START_X = 2;

    /**
     * The Y-coordinate of the starting room in the test maze.
     */
    private static final int TEST_START_Y = 3;

    /**
     * The X-coordinate of the exit room in the test maze.
     */
    private static final int TEST_EXIT_X = 1;

    /**
     * The Y-coordinate of the exit room in the test maze.
     */
    private static final int TEST_EXIT_Y = 0;

    /**
     * Maze Generator to use in testing.
     */
    private static final MazeGenerator TEST_MAZE_GEN
            = new EmptyMazeGenerator(TEST_MAZE_SIZE, TEST_ROOM_SIZE,
                                     TEST_START_X, TEST_START_Y,
                                      TEST_EXIT_X, TEST_EXIT_Y);

    /**
     * Test Question to be used as a QuestionSource.
     */
    private static final TriviaQuestion TEST_QUESTION
            = new TriviaQuestion("Test Question",
                    "Test Answer", TriviaQuestion.QuestionType.SHORT_ANSWER);

    /**
     * QuestionSource to provide to GameModel.
     */
    private static final QuestionSource TEST_QUESTION_SOURCE = () -> TEST_QUESTION;

    /**
     * Game Settings for tests.
     */
    private static final GameSettings TEST_GAME_SETTINGS =
            new GameSettings(8, 124, -932);

    /**
     * Save Controller for tests.
     */
    private static final SaveController TEST_SAVE_CONTROLLER =
            new SaveController("testgame.save");

    /**
     * Instance of GameModel for use in tests.
     */
    private GameModel myGameModel;

    /**
     * Tests GameModel's short constructor and getters.
     */
    @Test
    void testShortConstructor() {
        myGameModel = new GameModel(TEST_MAZE_GEN, TEST_QUESTION_SOURCE);

        // Game State should be null until the game is either created or loaded.
        assertNull(myGameModel.getState(), "GameModel's GameState should be "
                + "null until newGame() or loadGame() are called!");
        assertSame(TEST_QUESTION_SOURCE, myGameModel.getDatabase(),
                "GameModel's Question Source is different than what it "
                        + "was constructed with!");

        myGameModel.newGame();

        assertNotNull(myGameModel.getState(), "GameModel should have "
                + "a non-null GameState after newGame() being called!");
    }

    /**
     * Tests GameModel's long constructor and getters.
     */
    @Test
    void testLongConstructor() {
        myGameModel = new GameModel(TEST_MAZE_GEN, TEST_QUESTION_SOURCE,
                                    TEST_GAME_SETTINGS, TEST_SAVE_CONTROLLER);

        // Game State should be null until the game is either created or loaded.
        assertNull(myGameModel.getState(), "GameModel's GameState should be "
                + "null until newGame() or loadGame() are called!");
        assertSame(TEST_QUESTION_SOURCE, myGameModel.getDatabase(),
                "GameModel's Question Source is different than what it "
                        + "was constructed with!");

        myGameModel.newGame();

        assertNotNull(myGameModel.getState(), "GameModel should have "
                + "a non-null GameState after newGame() being called!");
        assertEquals(TEST_GAME_SETTINGS.getInitialPlayerLives(),
                myGameModel.getState().getPlayer().getLives(),
                "GameModel's newly created game started the player with "
                        + myGameModel.getState().getPlayer().getLives() + " instead of "
                        + TEST_GAME_SETTINGS.getInitialPlayerLives() + " as provided "
                        + "by the provided GameSettings!");
    }

    /**
     * Tests GameModel's newGame() method.
     */
    @Test
    void newGame() {
        myGameModel = new GameModel(TEST_MAZE_GEN, TEST_QUESTION_SOURCE,
                                    TEST_GAME_SETTINGS, TEST_SAVE_CONTROLLER);


        myGameModel.newGame();

        assertNotNull(myGameModel.getState(), "GameModel should have "
                                              + "a non-null GameState after newGame() being called!");
        assertEquals(TEST_GAME_SETTINGS.getInitialPlayerLives(),
                     myGameModel.getState().getPlayer().getLives(),
                     "GameModel's newly created game started the player with "
                     + myGameModel.getState().getPlayer().getLives() + " instead of "
                     + TEST_GAME_SETTINGS.getInitialPlayerLives() + " as provided "
                     + "by the provided GameSettings!");

        final Maze receivedMaze = myGameModel.getState().getMaze();

        // Maze Dimensions
        assertEquals(TEST_MAZE_SIZE, receivedMaze.getHeight(),
                     "Maze from GameModel.newGame() should have the expected"
                     + " dimensions from the generator!");
        assertEquals(TEST_MAZE_SIZE, receivedMaze.getWidth(),
                     "Maze from GameModel.newGame() should have the expected"
                     + " dimensions from the generator!");

        // Room Dimensions
        final Room receivedRoom = receivedMaze.getRoom(0, 0);
        assertEquals(TEST_ROOM_SIZE, receivedRoom.getHeight(),
                     "Room from GameModel.newGame() should have the expected"
                     + " dimensions from the generator!");
        assertEquals(TEST_ROOM_SIZE, receivedRoom.getWidth(),
                     "Room from GameModel.newGame() should have the expected"
                     + " dimensions from the generator!");

        // Start and exit coordinates
        assertEquals(TEST_START_X, receivedMaze.getStartingRoomX(),
                     "Maze from GameModel.newGame() did have the expected"
                     + " starting room coordinates!");
        assertEquals(TEST_START_Y, receivedMaze.getStartingRoomY(),
                     "Maze from GameModel.newGame() did have the expected"
                     + " starting room coordinates!");
        assertEquals(TEST_EXIT_X, receivedMaze.getExitRoomX(),
                     "Maze from GameModel.newGame() did have the expected"
                     + " exit room coordinates!");
        assertEquals(TEST_EXIT_Y, receivedMaze.getExitRoomY(),
                     "Maze from GameModel.newGame() did have the expected"
                     + " exit room coordinates!");
    }

    /**
     * Tests GameModel's saveGame() and loadGame() methods.
     */
    @Test
    void saveLoadGame() {
        myGameModel = new GameModel(TEST_MAZE_GEN, TEST_QUESTION_SOURCE);
        myGameModel.newGame();

        final Coordinates oldPosition = myGameModel.getState().getPlayer().getPosition();

        // Save current state
        myGameModel.saveGame();

        // Move the player
        myGameModel.getState().movePlayer(Direction.UP);
        assertNotEquals(oldPosition, myGameModel.getState().getPlayer().getPosition(),
                        "Something went wrong during this test, the player was "
                        + "unable to move correctly.");

        // Load old state
        myGameModel.loadGame();
        assertEquals(oldPosition, myGameModel.getState().getPlayer().getPosition(),
                     "GameModel did not correctly save and load the GameState!"
                     + " The Player's position was different after loading!");
    }
}