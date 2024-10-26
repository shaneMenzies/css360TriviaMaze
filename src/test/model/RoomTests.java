package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Room class.
 *
 * @author Shane Menzies
 * @version 10/26/24
 */
class RoomTests {

    /**
     * Default room type for testing.
     */
    private static final Room.RoomType TEST_ROOM_TYPE = Room.RoomType.STANDARD;

    /**
     * Default room height for testing.
     */
    private static final int TEST_ROOM_HEIGHT = 3;

    /**
     * Default room width for testing.
     */
    private static final int TEST_ROOM_WIDTH = 5;

    /**
     * Instance of a Room for testing.
     */
    private Room myTestRoom;

    Tile[][] createRoomTiles(final int theHeight, final int theWidth) {
        // Start with empty tiles
        final Tile[][] tiles = new Tile[theHeight][theWidth];

        for (int y = 0; y < theHeight; y++) {
            for (int x = 0; x < theWidth; x++) {
                tiles[y][x] = new EmptyTile();
            }
        }

        // Add walls at left and right
        for (int i = 0; i < theHeight; i++) {
            tiles[i][0] = new WallTile();
            tiles[i][theWidth - 1] = new WallTile();
        }

        // Add walls on top and bottom
        for (int i = 1; i < (theWidth - 1); i++) {
            tiles[0][i] = new WallTile();
            tiles[theHeight - 1][i] = new WallTile();
        }

        return tiles;
    }

    /**
     * Prepares myTestRoom before each test.
     */
    @BeforeEach
    void setUp() {
        myTestRoom = new Room(TEST_ROOM_TYPE,
                createRoomTiles(TEST_ROOM_HEIGHT, TEST_ROOM_WIDTH));
    }

    /**
     * Tests the Room constructor.
     */
    @Test
    void testConstructor() {
        assertDoesNotThrow(() -> {
            myTestRoom = new Room(TEST_ROOM_TYPE,
                    createRoomTiles(TEST_ROOM_HEIGHT, TEST_ROOM_WIDTH));
            myTestRoom = new Room(TEST_ROOM_TYPE,
                    createRoomTiles(TEST_ROOM_HEIGHT + 2,
                            TEST_ROOM_WIDTH + 1));
        });

        assertThrows(NullPointerException.class, () ->
                myTestRoom = new Room(TEST_ROOM_TYPE, null)
        );

        assertThrows(IllegalArgumentException.class, () ->
            myTestRoom = new Room(TEST_ROOM_TYPE, new EmptyTile[0][0])
        );

        assertThrows(IllegalArgumentException.class, () ->
            myTestRoom = new Room(TEST_ROOM_TYPE, new EmptyTile[2][0])
        );

        final Tile[][] testNonRectangle = new EmptyTile[TEST_ROOM_HEIGHT][TEST_ROOM_WIDTH];
        testNonRectangle[1] = new EmptyTile[TEST_ROOM_WIDTH + 2];
        assertThrows(IllegalArgumentException.class, () ->
            myTestRoom = new Room(TEST_ROOM_TYPE, testNonRectangle)
        );
    }

    /**
     * Test's Room's getType() method.
     */
    @Test
    void getType() {
        for (final Room.RoomType type : Room.RoomType.values()) {
            myTestRoom = new Room(type, createRoomTiles(TEST_ROOM_HEIGHT, TEST_ROOM_WIDTH));
            assertEquals(type, myTestRoom.getType());
        }
    }

    /**
     * Test's Room's getHeight() method.
     */
    @Test
    void getHeight() {
        assertEquals(TEST_ROOM_HEIGHT, myTestRoom.getHeight());

        myTestRoom = new Room(TEST_ROOM_TYPE,
                createRoomTiles(TEST_ROOM_HEIGHT + 2,
                        TEST_ROOM_WIDTH + 1));

        assertEquals(TEST_ROOM_HEIGHT + 2, myTestRoom.getHeight());
    }

    /**
     * Test's Room's getWidth() method.
     */
    @Test
    void getWidth() {
        assertEquals(TEST_ROOM_WIDTH, myTestRoom.getWidth());

        myTestRoom = new Room(TEST_ROOM_TYPE,
                createRoomTiles(TEST_ROOM_HEIGHT + 2,
                        TEST_ROOM_WIDTH + 1));

        assertEquals(TEST_ROOM_WIDTH + 1, myTestRoom.getWidth());
    }

    /**
     * Test's Room's getTiles() method.
     */
    @Test
    void getTiles() {
        final Tile[][] testTiles = createRoomTiles(TEST_ROOM_HEIGHT, TEST_ROOM_WIDTH);

        myTestRoom = new Room(TEST_ROOM_TYPE, testTiles);
        assertEquals(testTiles, myTestRoom.getTiles());

        final Tile[][] newTestTiles = new EmptyTile[TEST_ROOM_HEIGHT + 2][TEST_ROOM_WIDTH + 1];

        myTestRoom = new Room(TEST_ROOM_TYPE, newTestTiles);
        assertEquals(newTestTiles, myTestRoom.getTiles());

    }

    /**
     * Test's Room's getTile() method.
     */
    @Test
    void getTile() {
        final Tile[][] testTiles = createRoomTiles(TEST_ROOM_HEIGHT, TEST_ROOM_WIDTH);
        myTestRoom = new Room(TEST_ROOM_TYPE, testTiles);

        for (int y = 0; y < TEST_ROOM_HEIGHT; y++) {
            for (int x = 0; x < TEST_ROOM_WIDTH; x++) {
                assertEquals(testTiles[y][x], myTestRoom.getTile(x, y));
            }
        }
    }
}