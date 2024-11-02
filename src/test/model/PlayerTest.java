package model;

import model.interfaces.PlayerUpdateListener;
import model.tiles.EmptyTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  Test class for Player.
 *
 * @author Arafa Mohamed
 * @version 10/27/24
 */
public class PlayerTest {

    /**
     * Basic room for testing.
     */
    private static final Room TEST_ROOM = new Room(Room.RoomType.STANDARD, new EmptyTile[3][3]);

    /** Instance of Player to be tested. */
    private Player player;

    /** Initial position of the player. */
    private Coordinates initialPosition;

    /** Initial score of the player. */
    private int initialScore;

    /** Initial number of lives of the player. */
    private int initialLives;

    /**
     * Sets up the test environment before each test.
     * Initializes a Player instance with default coordinates, score, and lives.
     */
    @BeforeEach
    public void setUp() {
        initialPosition = new Coordinates(TEST_ROOM, 0, 0); // Assuming Room() is a valid constructor
        initialScore = 0;
        initialLives = 3;
        player = new Player(initialPosition, initialScore, initialLives); // Create a new Player instance
    }

    /**
     * Tests the getPosition method of the Player class.
     * Asserts that the initial position matches the expected position.
     */
    @Test
    public void testGetPosition() {
        assertEquals(initialPosition, player.getPosition(), "The position should match the initial position.");
    }

    /**
     * Tests the getScore method of the Player class.
     * Asserts that the initial score matches the expected score.
     */
    @Test
    public void testGetScore() {
        assertEquals(initialScore, player.getScore(), "The score should match the initial score.");
    }

    /**
     * Tests the getLives method of the Player class.
     * Asserts that the initial number of lives matches the expected number of lives.
     */
    @Test
    public void testGetLives() {
        assertEquals(initialLives, player.getLives(), "The number of lives should match the initial lives.");
    }

    /**
     * Tests the setPosition method of the Player class.
     * Changes the player's position and asserts that the new position is correctly updated.
     */
    @Test
    public void testSetPosition() {
        Coordinates newPosition = new Coordinates(TEST_ROOM, 1, 1); // New position
        player.setPosition(newPosition);
        assertEquals(newPosition, player.getPosition(), "The position should be updated to the new position.");
    }

    /**
     * Tests the setScore method of the Player class.
     * Changes the player's score and asserts that the new score is correctly updated.
     */
    @Test
    public void testSetScore() {
        int newScore = 10; // New score
        player.setScore(newScore);
        assertEquals(newScore, player.getScore(), "The score should be updated to the new score.");
    }

    /**
     * Tests the setLives method of the Player class.
     * Changes the number of lives for the player and asserts that the new number of lives is correctly updated.
     */
    @Test
    public void testSetLives() {
        int newLives = 2; // New number of lives
        player.setLives(newLives);
        assertEquals(newLives, player.getLives(), "The number of lives should be updated to the new lives.");
    }

    /**
     * Tests the addUpdateListener method of the Player class.
     * Adds a listener and asserts that it is notified when the player's score is updated.
     */
    @Test
    public void testAddUpdateListener() {
        // Create a named listener implementation to keep track of notifications
        class TestListener implements PlayerUpdateListener {
            boolean notified = false; // Flag to track if the listener was notified

            @Override
            public void doUpdate(Player player) {
                notified = true; // Mark as notified
            }
        }

        TestListener listener = new TestListener(); // Instantiate the listener
        player.addUpdateListener(listener); // Add the listener to the player
        player.setScore(5); // Change to trigger notification
        assertTrue(listener.notified, "The listener should be notified when the player's score is updated.");
    }

    /**
     * Tests the removeUpdateListener method of the Player class.
     * Adds a listener, removes it, and asserts that it is not notified after being removed.
     */
    @Test
    public void testRemoveUpdateListener() {
        // Create a named listener implementation to keep track of notifications
        class TestListener implements PlayerUpdateListener {
            boolean notified = false; // Flag to track if the listener was notified

            @Override
            public void doUpdate(Player player) {
                notified = true; // Mark as notified
            }
        }

        TestListener listener = new TestListener(); // Instantiate the listener
        player.addUpdateListener(listener); // Add the listener to the player
        player.removeUpdateListener(listener); // Remove the listener
        player.setLives(2); // Change should not trigger notification
        assertFalse(listener.notified, "The listener should not be notified after being removed.");
    }


}