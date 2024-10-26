package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the EmptyTile class.
 *
 * @author Shane Menzies
 * @version 10/25/24
 */
class EmptyTileTests {

    /**
     * Empty Tile Instance for tests.
     */
    private EmptyTile myTestTile;

    /**
     * Sets up the test instance before each test.
     */
    @BeforeEach
    void setUp() {
        myTestTile = new EmptyTile();
    }

    /**
     * Tests EmptyTile's onMovedTo() function, which should always do nothing, and so
     *  should not cause any exceptions.
     */
    @Test
    void onMovedTo() {
        assertDoesNotThrow(() -> {
            // TODO: Add a real player instance to this test once Player Class is done.
            myTestTile.onMovedTo(null);
        });
    }

    /**
     * Tests EmptyTile's isPassable() method, which should always return true.
     */
    @Test
    void isPassable() {
        assertTrue(myTestTile.isPassable());
    }

    /**
     * Tests EmptyTile's getTileID() method, which should always return
     *  the Empty TileID.
     */
    @Test
    void getTileID() {
        assertEquals(TileID.Empty, myTestTile.getTileID());
    }
}