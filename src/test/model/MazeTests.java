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

    private Maze myMaze;

    /**
     * Prepare test instance before each test.
     */
    @BeforeEach
    void setUp() {
        for (final Room[] row : TEST_ROOMS) {
            Arrays.fill(row, TEST_ROOM);
        }

        myMaze = new Maze(TEST_ROOMS);
    }

    /**
     * Test Maze's constructor.
     */
    @Test
    void testConstructor() {
        // Zero height
        assertThrows(IllegalArgumentException.class, () -> new Maze(new Room[0][0]),
                "Maze constructor did not cause an exception when given an array " +
                        "with zero height!");

        // Zero width
        assertThrows(IllegalArgumentException.class, () -> new Maze(new Room[3][0]),
                "Maze constructor did not cause an exception when given an array " +
                        "with zero width!");

        // Non-rectangular
        final Room[][] tempRooms = new Room[3][];
        tempRooms[0] = new Room[3];
        tempRooms[1] = new Room[4];
        tempRooms[2] = new Room[3];
        assertThrows(IllegalArgumentException.class, () -> new Maze(tempRooms),
                "Maze constructor did not cause an exception when given a " +
                "non-rectangular array!");

        // Double check with valid argument
        assertDoesNotThrow(() -> new Maze(TEST_ROOMS), "Maze constructor caused" +
                "an exception with a valid array!");
    }

    /**
     * Test Maze's getRooms() method.
     */
    @Test
    void getRooms() {
        assertEquals(TEST_ROOMS, myMaze.getRooms(),
                "getRooms() did not return the expected TEST_ROOMS array!");

        final Room[][] tempRooms = new Room[2][2];
        myMaze = new Maze(tempRooms);
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

        myMaze = new Maze(tempRooms);

        for (int i = 0; i < tempRooms.length; i++) {
            for (int j = 0; j < tempRooms[0].length; j++) {
                assertSame(tempRooms[i][j], myMaze.getRoomAt(j, i),
                        "Maze.getRoomAt(" + j + ", " + i + ") " +
                                "did not return the expected room!");
            }
        }
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