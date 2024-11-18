package model;

import static org.junit.jupiter.api.Assertions.*;

import model.enums.TileID;
import model.tiles.WallTile;
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
     * Tests WallTile's tryMoveTo() method, which should always return false.
     */
    @Test
    void tryMoveTo() {
        assertFalse(myTestTile.tryMoveTo());
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