package model;

import model.Coordinates;
import model.Room;
import model.tiles.EmptyTile;
import model.tiles.WallTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  Unit test class for the Coordinates class.
 *
 * @author Arafa Mohamed
 * @version 10/31/24
 */
public class CoordinatesTest {

    /** Holds the Coordinates instance to be tested. */
    private Coordinates coordinates;

    /** Holds the Room instance associated with the coordinates. */
    private Room room;

    /** The x-coordinate value for the Coordinates instance. */
    private int x;

    /** The y-coordinate value for the Coordinates instance. */
    private int y;

    /**
     * Initializes the test setup with default values for each instance variable.
     * This method is run before each test to create a consistent starting point for tests.
     */
    @BeforeEach
    public void setUp() {
        // Create a 2x2 grid of tiles with EmptyTile and WallTile for testing
        Tile[][] tiles = new Tile[2][2];
        tiles[0][0] = new EmptyTile();
        tiles[0][1] = new WallTile();
        tiles[1][0] = new WallTile();
        tiles[1][1] = new EmptyTile();

        // Initialize Room with RoomType and tiles
        room = new Room(Room.RoomType.STANDARD, tiles);
        x = 5;
        y = 10;

        // Initialize Coordinates with the constructed Room, x, and y
        coordinates = new Coordinates(room, x, y);
    }

    /**
     * Test the constructor and getter methods for proper initialization.
     */
    @Test
    public void testConstructorAndGetters() {
        assertEquals(room, coordinates.getRoom(), "Room should match the initial room.");
        assertEquals(x, coordinates.getX(), "X-coordinate should match the initial x.");
        assertEquals(y, coordinates.getY(), "Y-coordinate should match the initial y.");
    }

    /**
     * Test setRoom to verify it correctly updates the room.
     */
    @Test
    public void testSetRoom() {
        // Create a new Room with the required parameters
        Tile[][] newTiles = new Tile[2][2];
        newTiles[0][0] = new EmptyTile();
        newTiles[0][1] = new WallTile();
        newTiles[1][0] = new WallTile();
        newTiles[1][1] = new EmptyTile();
        Room newRoom = new Room(Room.RoomType.STANDARD, newTiles);

        // Set the new room in Coordinates
        coordinates.setRoom(newRoom);
        assertEquals(newRoom, coordinates.getRoom(), "Room should be updated to the new room.");
    }

    /**
     * Test setX to verify it correctly updates the x-coordinate.
     */
    @Test
    public void testSetX() {
        int newX = 15;
        coordinates.setX(newX);
        assertEquals(newX, coordinates.getX(),
                "X-coordinate should be updated to the new x.");
    }

    /**
     * Test setY to verify it correctly updates the y-coordinate.
     */
    @Test
    public void testSetY() {
        int newY = 20;
        coordinates.setY(newY);
        assertEquals(newY, coordinates.getY(),
                "Y-coordinate should be updated to the new y.");
    }

    /**
     * Test equals to ensure two Coordinates with identical values are equal.
     */
    @Test
    public void testEquals() {
        Coordinates identicalCoordinates = new Coordinates(room, x, y);

        // Create a different room for the test of non-equality
        Tile[][] differentTiles = new Tile[2][2];
        differentTiles[0][0] = new WallTile();
        differentTiles[0][1] = new EmptyTile();
        differentTiles[1][0] = new EmptyTile();
        differentTiles[1][1] = new WallTile();
        Room differentRoom = new Room(Room.RoomType.STANDARD, differentTiles);
        Coordinates differentCoordinates = new Coordinates(differentRoom, x, y); // Different room

        assertEquals(coordinates, identicalCoordinates,
                "Coordinates with identical values should be equal.");
        assertNotEquals(coordinates, differentCoordinates,
                "Coordinates with different rooms should not be equal.");
    }

    /**
     * Test hashCode to ensure identical Coordinates have the same hash code.
     */
    @Test
    public void testHashCode() {
        Coordinates identicalCoordinates = new Coordinates(room, x, y);
        assertEquals(coordinates.hashCode(), identicalCoordinates.hashCode(),
                "Hash codes should match for identical coordinates.");
    }

}







