package model;

import static org.junit.jupiter.api.Assertions.*;

import model.enums.Direction;
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
     * Basic DoorController array for testing.
     */
    private static final DoorController[] TEST_DOORS = new DoorController[3];

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

        myMaze = new Maze(TEST_ROOMS, TEST_DOORS,
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
                        TEST_DOORS,
                        TEST_START_X, TEST_START_Y,
                        TEST_EXIT_X, TEST_EXIT_Y),
                "Maze constructor did not cause an exception when given an array " +
                        "with zero height!");

        // Zero width
        assertThrows(IllegalArgumentException.class, () -> new Maze(new Room[3][0],
                        TEST_DOORS,
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
                        TEST_DOORS,
                        TEST_START_X, TEST_START_Y,
                        TEST_EXIT_X, TEST_EXIT_Y),
                "Maze constructor did not cause an exception when given a " +
                "non-rectangular array!");

        // Double check with valid argument
        assertDoesNotThrow(() -> new Maze(TEST_ROOMS,
                        TEST_DOORS,
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
        myMaze = new Maze(tempRooms, TEST_DOORS,
                0, 0, 1, 1);
        assertEquals(tempRooms, myMaze.getRooms(),
                "getRooms() did not return the expected array it was constructed with!");
    }

    /**
     * Test Maze's getDoors() method.
     */
    @Test
    void getDoors() {
        assertEquals(TEST_DOORS, myMaze.getDoors(),
                "getDoors() did not return the expected TEST_DOORS array!");

        final DoorController[] tempDoors = new DoorController[2];
        myMaze = new Maze(TEST_ROOMS, tempDoors,
                0, 0, 1, 1);
        assertEquals(tempDoors, myMaze.getDoors(),
                "getDoors() did not return the expected array it was constructed with!");
    }

    /**
     * Test Maze's getRoomAt() method.
     */
    @Test
    void getRoom() {
        // Create independent set of rooms for checking
        final Room[][] tempRooms = new Room[3][3];
        for (int i = 0; i < tempRooms.length; i++) {
            for (int j = 0; j < tempRooms[0].length; j++) {
                tempRooms[i][j] = new Room(TEST_ROOM);
            }
        }

        myMaze = new Maze(tempRooms,
                TEST_DOORS,
                TEST_START_X, TEST_START_Y,
                TEST_EXIT_X, TEST_EXIT_Y);

        for (int i = 0; i < tempRooms.length; i++) {
            for (int j = 0; j < tempRooms[0].length; j++) {
                assertSame(tempRooms[i][j], myMaze.getRoom(j, i),
                        "Maze.getRoomAt(" + j + ", " + i + ") " +
                                "did not return the expected room!");
            }
        }
    }

    /**
     * Tests Maze's getTileAt() method.
     */
    @Test
    void getTile() {
        for (int roomY = 0; roomY < TEST_ROOMS.length; roomY++) {
            for (int roomX = 0; roomX < TEST_ROOMS[0].length; roomX++) {
                for (int y = 0; y < TEST_ROOMS[roomY][roomX].getHeight(); y++) {
                    for (int x = 0; x < TEST_ROOMS[roomY][roomX].getWidth(); x++) {
                        final Coordinates currentCoordinates
                                = new Coordinates(roomX, roomY, x, y);
                        assertEquals(TEST_ROOMS[roomY][roomX].getTile(x, y),
                                myMaze.getTile(currentCoordinates),
                                "Maze.getTileAt(" + currentCoordinates
                                        + ") did not return the expected tile!");
                    }
                }
            }
        }
    }

    /**
     * Test Maze's hasNeighbor() method.
     */
    @Test
    void hasNeighbor() {
        // Center Room should have neighbors in every direction
        for (final Direction nextDirection : Direction.values()) {
            assertTrue(myMaze.hasNeighbor(1, 1, nextDirection),
                    "Center Room should have a neighbor in "
                            + nextDirection
                            + " direction!");
        }

        // Edge Room should have neighbors in all directions but 1
        assertTrue(myMaze.hasNeighbor(0, 1, Direction.UP),
                "Edge Room should have a neighbor above it!");
        assertTrue(myMaze.hasNeighbor(0, 1, Direction.DOWN),
                "Edge Room should have a neighbor below it!");
        assertTrue(myMaze.hasNeighbor(0, 1, Direction.RIGHT),
                "Edge Room should have a neighbor to its right!");

        assertFalse(myMaze.hasNeighbor(0, 1, Direction.LEFT),
                "Edge Room shouldn't have a neighbor to its left!");

        // Corner Room should have neighbors in 2 directions
        assertTrue(myMaze.hasNeighbor(0, 0, Direction.UP),
                "Corner Room should have a neighbor above it!");
        assertTrue(myMaze.hasNeighbor(0, 0, Direction.RIGHT),
                "Corner Room should have a neighbor to its right!");

        assertFalse(myMaze.hasNeighbor(0, 0, Direction.DOWN),
                "Corner Room shouldn't have a neighbor below it!");
        assertFalse(myMaze.hasNeighbor(0, 0, Direction.LEFT),
                "Corner Room shouldn't have a neighbor to its left!");
    }

    /**
     * Test Maze's getNeighbor() method.
     */
    @Test
    void getNeighbor() {
        // Center Room should have neighbors in every direction
        for (final Direction nextDirection : Direction.values()) {
            assertNotNull(myMaze.getNeighbor(1, 1, nextDirection),
                    "Center Room's neighbor in "
                            + nextDirection.toString()
                            + "direction shouldn't be null!");
        }

        // Edge Room should have neighbors in all directions but 1
        assertNotNull(myMaze.getNeighbor(0, 1, Direction.UP),
                "Edge Room's upward neighbor shouldn't be null!");
        assertNotNull(myMaze.getNeighbor(0, 1, Direction.DOWN),
                "Edge Room's downward neighbor shouldn't be null!");
        assertNotNull(myMaze.getNeighbor(0, 1, Direction.RIGHT),
                "Edge Room's right neighbor shouldn't be null!");

        assertNull(myMaze.getNeighbor(0, 1, Direction.LEFT),
                "Edge Room's left neighbor should be null!");

        // Corner Room should have neighbors in 2 directions
        assertNotNull(myMaze.getNeighbor(0, 0, Direction.UP),
                "Corner Room's upward neighbor shouldn't be null!");
        assertNotNull(myMaze.getNeighbor(0, 0, Direction.RIGHT),
                "Corner Room's right neighbor shouldn't be null!");

        assertNull(myMaze.getNeighbor(0, 0, Direction.DOWN),
                "Corner Room's downward neighbor should be null!");
        assertNull(myMaze.getNeighbor(0, 0, Direction.LEFT),
                "Corner Room's left neighbor should be null!");
    }

    /**
     * Test Maze's moveCoordinates() method.
     */
    @Test
    void moveCoordinates() {
        // Start with coordinates at (0,0),(0,0)
        Coordinates testCoords = new Coordinates(0, 0, 0, 0);

        // Moves that shouldn't change the test coordinates.
        Coordinates recievedCoords = myMaze.moveCoordinates(testCoords, Direction.LEFT);
        assertEquals(testCoords, recievedCoords,
                "Received " + recievedCoords
                        + " instead of " + testCoords + "!");

        recievedCoords = myMaze.moveCoordinates(testCoords, Direction.DOWN);
        assertEquals(testCoords, recievedCoords,
                "Received " + recievedCoords
                        + " instead of " + testCoords + "!");

        // Moves that should change the test coordinates
        Coordinates expectedCoords
                = new Coordinates(0, 0, 1, 0);
        recievedCoords = myMaze.moveCoordinates(testCoords, Direction.RIGHT);
        assertEquals(expectedCoords, recievedCoords,
                "Received " + recievedCoords
                        + " instead of " + expectedCoords + "!");

        expectedCoords = new Coordinates(0, 0, 0, 1);
        recievedCoords = myMaze.moveCoordinates(testCoords, Direction.UP);
        assertEquals(expectedCoords, recievedCoords,
                "Received " + recievedCoords
                        + " instead of " + expectedCoords + "!");

        // Moves that cross rooms
        testCoords = new Coordinates(0, 0, 2, 2);

        expectedCoords = new Coordinates(1, 0, 0, 2);
        recievedCoords = myMaze.moveCoordinates(testCoords, Direction.RIGHT);
        assertEquals(expectedCoords, recievedCoords,
                "Received " + recievedCoords
                        + " instead of " + expectedCoords + "!");

        expectedCoords = new Coordinates(0, 1, 2, 0);
        recievedCoords = myMaze.moveCoordinates(testCoords, Direction.UP);
        assertEquals(expectedCoords, recievedCoords,
                "Received " + recievedCoords
                        + " instead of " + expectedCoords + "!");
    }

    /**
     * Test Maze's getStartingRoomX() and getStartingRoomY() methods.
     */
    @Test
    void getStartingRoom() {
        assertEquals(TEST_START_X, myMaze.getStartingRoomX());
        assertEquals(TEST_START_Y, myMaze.getStartingRoomY());

        myMaze = new Maze(TEST_ROOMS, TEST_DOORS,
                2, 2, 0, 0);
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

        myMaze = new Maze(TEST_ROOMS, TEST_DOORS,
                2, 2, 0, 0);
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