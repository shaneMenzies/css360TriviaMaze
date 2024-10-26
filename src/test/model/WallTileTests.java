package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the WallTile class.
 *
 * @author Shane Menzies
 * @version 10/25/24
 */
class WallTileTests {

    /**
     * Instance of WallTile for use in tests.
     */
    private WallTile myTestTile;

    /**
     * Prepares the test instance of WallTile before each test.
     */
    @BeforeEach
    void setUp() {
        myTestTile = new WallTile();
    }

    /**
     * Tests WallTile's onMovedTo method, which should never get called,
     *  which is why it should always raise an exception if it is called.
     */
    @Test
    void onMovedTo() {
        assertThrows(WallTile.InvalidPositionException.class, ()
                -> myTestTile.onMovedTo(null)
        );
    }

    /**
     * Tests WallTile's isPassable method, which should always
     *  return false, since walls are never passable.
     */
    @Test
    void isPassable() {
        assertFalse(myTestTile.isPassable());
    }

    /**
     * Tests WallTile's getTileID method, which should always return
     *  the corresponding Wall TileID.
     */
    @Test
    void getTileID() {
        assertEquals(TileID.WALL, myTestTile.getTileID());
    }
}