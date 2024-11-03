package model;

import static org.junit.jupiter.api.Assertions.*;

import model.tiles.EmptyTile;
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
     * Tests EmptyTile's tryMoveTo() function, which should always return true.
     */
    @Test
    void tryMoveTo() {
        assertTrue(myTestTile.tryMoveTo());
    }

    /**
     * Tests EmptyTile's getTileID() method, which should always return
     *  the Empty TileID.
     */
    @Test
    void getTileID() {
        assertEquals(TileID.EMPTY, myTestTile.getTileID());
    }
}