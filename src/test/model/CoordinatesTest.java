package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  Unit test class for the Coordinates class.
 *
 * @author Arafa Mohamed
 * @author Shane Menzies
 * @version 11/6/24
 */
public class CoordinatesTest {

    /**
     * Default value for RoomX
     */
    private static final int DEFAULT_ROOM_X = 1;

    /**
     * Default value for RoomY
     */
    private static final int DEFAULT_ROOM_Y = 3;

    /**
     * Default value for X
     */
    private static final int DEFAULT_X = 5;

    /**
     * Default value for Y
     */
    private static final int DEFAULT_Y = 10;

    /**
     * Holds the Coordinates instance to be tested.
     **/
    private Coordinates myCoordinates;

    /**
     * Initializes the test setup with default values for each instance variable.
     * This method is run before each test to create a consistent starting point for tests.
     */
    @BeforeEach
    public void setUp() {
        // Initialize Coordinates with the constructed Room, x, and y
        myCoordinates = new Coordinates(DEFAULT_ROOM_X, DEFAULT_ROOM_Y, DEFAULT_X, DEFAULT_Y);
    }

    /**
     * Test the constructor and getter methods for proper initialization.
     */
    @Test
    public void testConstructorAndGetters() {
        assertEquals(DEFAULT_ROOM_X, myCoordinates.getRoomX(),
                "Room X-coordinate should match the initial room x.");
        assertEquals(DEFAULT_ROOM_Y, myCoordinates.getRoomY(),
                "Room Y-coordinate should match the initial room y.");
        assertEquals(DEFAULT_X, myCoordinates.getX(),
                "X-coordinate should match the initial x.");
        assertEquals(DEFAULT_Y, myCoordinates.getY(),
                "Y-coordinate should match the initial y.");
    }

    /**
     * Test setRoom to verify it correctly updates the room.
     */
    @Test
    public void testSetRoom() {
        // Create a new Room with the required parameters
        final int testRoomX = DEFAULT_ROOM_X + 9;
        final int testRoomY = DEFAULT_ROOM_Y + 28;

        // Set the new room in Coordinates
        myCoordinates.setRoom(testRoomX, testRoomY);
        assertEquals(testRoomX, myCoordinates.getRoomX(),
                "Room X should be updated to the new room X.");
        assertEquals(testRoomY, myCoordinates.getRoomY(),
                "Room Y should be updated to the new room Y.");
    }

    /**
     * Test setX to verify it correctly updates the x-coordinate.
     */
    @Test
    public void testSetX() {
        int newX = 15;
        myCoordinates.setX(newX);
        assertEquals(newX, myCoordinates.getX(),
                "X-coordinate should be updated to the new x.");
    }

    /**
     * Test setY to verify it correctly updates the y-coordinate.
     */
    @Test
    public void testSetY() {
        int newY = 20;
        myCoordinates.setY(newY);
        assertEquals(newY, myCoordinates.getY(),
                "Y-coordinate should be updated to the new y.");
    }

    /**
     * Tests Coordinates toString() override.
     */
    @Test
    public void testToString() {
        final String expected = "Room: (" + DEFAULT_ROOM_X + ", " + DEFAULT_ROOM_Y + "); "
                + "Tile: (" + DEFAULT_X + ", " + DEFAULT_Y + ")";
        assertEquals(expected, myCoordinates.toString());
    }

    /**
     * Test equals to ensure two Coordinates with identical values are equal.
     */
    @Test
    public void testEquals() {
        Coordinates identicalCoordinates
                = new Coordinates(DEFAULT_ROOM_X, DEFAULT_ROOM_Y, DEFAULT_X, DEFAULT_Y);

        // Create different room coordinates for the test of non-equality
        final int differentRoomX = DEFAULT_ROOM_X + 4;
        final int differentRoomY = DEFAULT_ROOM_Y - 2;
        Coordinates differentCoordinates
                = new Coordinates(differentRoomX, differentRoomY, DEFAULT_X, DEFAULT_Y); // Different room

        assertEquals(myCoordinates, identicalCoordinates,
                "Coordinates with identical values should be equal.");
        assertNotEquals(myCoordinates, differentCoordinates,
                "Coordinates with different rooms should not be equal.");
    }

    /**
     * Test hashCode to ensure identical Coordinates have the same hash code.
     */
    @Test
    public void testHashCode() {
        Coordinates identicalCoordinates
                = new Coordinates(DEFAULT_ROOM_X, DEFAULT_ROOM_Y, DEFAULT_X, DEFAULT_Y);
        assertEquals(myCoordinates.hashCode(), identicalCoordinates.hashCode(),
                "Hash codes should match for identical coordinates.");
    }
}