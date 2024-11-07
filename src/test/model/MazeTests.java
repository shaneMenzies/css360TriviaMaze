package model;

import static org.junit.jupiter.api.Assertions.*;

import model.tiles.EmptyTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Unit tests for the Maze class.
 *
 * @author Shane Menzies
 * @version 11/2/24
 */
class MazeTests {

    /**
     * Basic room for testing.
     */
    private static final Room TEST_ROOM
            = new Room(Room.RoomType.STANDARD, new EmptyTile[3][3]);

    /**
     * Basic 2D array of Rooms for testing.
     */
    private static final Room[][] TEST_ROOMS = new Room[3][3];

    /**
     * X coordinate for the starting room for the test maze.
     */
    private static final int TEST_START_X = 1;

    /**
     * Y coordinate for the starting room for the test maze.
     */
    private static final int TEST_START_Y = 1;

    /**
     * X coordinate for the exit room for the test maze.
     */
    private static final int TEST_EXIT_X = 1;

    /**
     * Y coordinate for the exit room for the test maze.
     */
    private static final int TEST_EXIT_Y = 0;

    /**
     * Instance to use for testing.
     */
    private Maze myMaze;

    /**
     * Prepare test instance before each test.
     */
    @BeforeEach
    void setUp() {
        for (final Room[] row : TEST_ROOMS) {
            Arrays.fill(row, TEST_ROOM);
        }

        myMaze = new Maze(TEST_ROOMS,
                TEST_START_X, TEST_START_Y,
                TEST_EXIT_X, TEST_EXIT_Y);
    }

    /**
     * Test Maze's constructor.
     */
    @Test
    void testConstructor() {
        // Zero height
        assertThrows(IllegalArgumentException.class, () -> new Maze(new Room[0][0],
                        TEST_START_X, TEST_START_Y,
                        TEST_EXIT_X, TEST_EXIT_Y),
                "Maze constructor did not cause an exception when given an array " +
                        "with zero height!");

        // Zero width
        assertThrows(IllegalArgumentException.class, () -> new Maze(new Room[3][0],
                        TEST_START_X, TEST_START_Y,
                        TEST_EXIT_X, TEST_EXIT_Y),
                "Maze constructor did not cause an exception when given an array " +
                        "with zero width!");

        // Non-rectangular
        final Room[][] tempRooms = new Room[3][];
        tempRooms[0] = new Room[3];
        tempRooms[1] = new Room[4];
        tempRooms[2] = new Room[3];
        assertThrows(IllegalArgumentException.class, () -> new Maze(tempRooms,
                        TEST_START_X, TEST_START_Y,
                        TEST_EXIT_X, TEST_EXIT_Y),
                "Maze constructor did not cause an exception when given a " +
                "non-rectangular array!");

        // Double check with valid argument
        assertDoesNotThrow(() -> new Maze(TEST_ROOMS,
                        TEST_START_X, TEST_START_Y,
                        TEST_EXIT_X, TEST_EXIT_Y),
                "Maze constructor caused an exception with a valid array!");
    }

    /**
     * Test Maze's getRooms() method.
     */
    @Test
    void getRooms() {
        assertEquals(TEST_ROOMS, myMaze.getRooms(),
                "getRooms() did not return the expected TEST_ROOMS array!");

        final Room[][] tempRooms = new Room[2][2];
        myMaze = new Maze(tempRooms, 0, 0, 1, 1);
        assertEquals(tempRooms, myMaze.getRooms(),
                "getRooms() did not return the expected array it was constructed with!");
    }

    /**
     * Test Maze's getRoomAt() method.
     */
    @Test
    void getRoomAt() {
        // Create independent set of rooms for checking
        final Room[][] tempRooms = new Room[3][3];
        for (int i = 0; i < tempRooms.length; i++) {
            for (int j = 0; j < tempRooms[0].length; j++) {
                tempRooms[i][j] = new Room(TEST_ROOM);
            }
        }

        myMaze = new Maze(tempRooms,
                TEST_START_X, TEST_START_Y,
                TEST_EXIT_X, TEST_EXIT_Y);

        for (int i = 0; i < tempRooms.length; i++) {
            for (int j = 0; j < tempRooms[0].length; j++) {
                assertSame(tempRooms[i][j], myMaze.getRoomAt(j, i),
                        "Maze.getRoomAt(" + j + ", " + i + ") " +
                                "did not return the expected room!");
            }
        }
    }

    /**
     * Test Maze's getStartingRoomX() and getStartingRoomY() methods.
     */
    @Test
    void getStartingRoom() {
        assertEquals(TEST_START_X, myMaze.getStartingRoomX());
        assertEquals(TEST_START_Y, myMaze.getStartingRoomY());

        myMaze = new Maze(TEST_ROOMS, 2, 2, 0, 0);
        assertEquals(2, myMaze.getStartingRoomX());
        assertEquals(2, myMaze.getStartingRoomY());
    }

    /**
     * Test Maze's getExitRoomX() and getExitRoomY() methods.
     */
    @Test
    void getExitRoom() {
        assertEquals(TEST_EXIT_X, myMaze.getExitRoomX());
        assertEquals(TEST_EXIT_Y, myMaze.getExitRoomY());

        myMaze = new Maze(TEST_ROOMS, 2, 2, 0, 0);
        assertEquals(0, myMaze.getExitRoomX());
        assertEquals(0, myMaze.getExitRoomY());
    }

    /**
     * Test Maze's getHeight() method.
     */
    @Test
    void getHeight() {
        assertEquals(TEST_ROOMS.length, myMaze.getHeight());
    }

    /**
     * Test Maze's getWidth() method.
     */
    @Test
    void getWidth() {
        assertEquals(TEST_ROOMS[0].length, myMaze.getWidth());
    }
}