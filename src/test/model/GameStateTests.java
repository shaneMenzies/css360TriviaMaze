package model;

import static org.junit.jupiter.api.Assertions.*;

import model.enums.Direction;
import model.enums.GamePlayPhase;
import model.interfaces.GameStateUpdateListener;
import model.interfaces.Tile;
import model.tiles.EmptyTile;
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
     * Tiles for each room.
     */
    private static final Tile[][] TEST_ROOM_CONTENTS
            = new Tile[3][3];

    /**
     * Basic room for testing.
     */
    private static final Room TEST_ROOM
            = new Room(Room.RoomType.STANDARD, TEST_ROOM_CONTENTS);

    /**
     * GameSettings for testing.
     */
    private static final GameSettings TEST_SETTINGS
            = new GameSettings(3, 100, -100);

    /**
     * Size of the test maze.
     */
    private static final int TEST_MAZE_SIZE = 3;

    /**
     * Rooms for the Maze.
     */
    private static final Room[][] TEST_ROOMS = new Room[TEST_MAZE_SIZE][TEST_MAZE_SIZE];

    /**
     * Basic DoorController array for testing.
     */
    private static final DoorController[] TEST_DOORS = new DoorController[3];

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
        // Setup rooms
        for (int i = 0; i < TEST_ROOM_SIZE; i++) {
            for (int j = 0; j < TEST_ROOM_SIZE; j++) {
                TEST_ROOM_CONTENTS[i][j] = new EmptyTile();
            }
        }

        for (int i = 0; i < TEST_MAZE_SIZE; i++) {
            for (int j = 0; j < TEST_MAZE_SIZE; j++) {
                TEST_ROOMS[i][j] = new Room(TEST_ROOM);
            }
        }

        // Setup maze
        myMaze = new Maze(
                    TEST_ROOMS, TEST_DOORS,
                    TEST_START_X, TEST_START_Y,
                    TEST_EXIT_X, TEST_EXIT_Y
                );

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
                        TEST_ROOM.getWidth() / 2, TEST_ROOM.getWidth() / 2
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
        assertEquals(myUpdateCount, 0,
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
     * Tests that doors work correctly inside GameState.
     */
    @Test
    void testDoors() {

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