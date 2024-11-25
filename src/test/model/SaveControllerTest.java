package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import model.interfaces.Tile;
import model.tiles.EmptyTile;
import model.tiles.WallTile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests saving and loading game states, handling of invalid files,
 * and error conditions.
 *
 * @author Arafa Mohamed
 * @version 11/15/24
 */
public class SaveControllerTest {

    private static final String SAVE_PATH = "testGameState.ser";
    private static final String INVALID_SAVE_PATH = "/invalid/path/testGameState.ser";
    private static final String TEST_SAVE_FILE = "test_save.dat";

    private SaveController saveController;
    private GameState gameState;

    /**
     * Set up test environment before each test.
     * Creates a new SaveController instance and initializes a test GameState.
     */
    @BeforeEach
    void setUp() {
        // Deletes any existing test file before each test
        deleteTestFile(SAVE_PATH);

        saveController = new SaveController(SAVE_PATH);

        // Initializes GameSettings and Maze for testing
        GameSettings settings = new GameSettings(3, 10, -5);

        // Creates a simple test room with tiles
        Tile[][] tiles = new Tile[2][2];
        // Creating a simple room with empty tiles and wall tiles
        tiles[0][0] = new EmptyTile();  // Walkable tile
        tiles[0][1] = new WallTile();   // Non-walkable tile
        tiles[1][0] = new EmptyTile();
        tiles[1][1] = new WallTile();

        // Creates a single room for testing
        Room[][] rooms = new Room[][]{{new Room(Room.RoomType.START, tiles)}};

        // Creates maze with single room
        Maze maze = new Maze(rooms,null, 0, 0, 0, 0);

        gameState = new GameState(settings, maze);
    }

    /**
     * Clean up test environment after each test.
     */
    @AfterEach
    void tearDown() {
        deleteTestFile(SAVE_PATH);
        deleteTestFile(TEST_SAVE_FILE);
    }

    /**
     * Helper method to delete test files.
     */
    private void deleteTestFile(String filePath) {
        try {
            Files.deleteIfExists(Path.of(filePath));
        } catch (IOException e) {
            System.err.println("Failed to delete test file: " + e.getMessage());
        }
    }

    /**
     * Tests saving a null game state.
     */
    @Test
    void testSaveNullGameState() {
        assertThrows(NullPointerException.class,
                () -> saveController.saveGame(null),
                "Should throw NullPointerException when saving null state");
    }

    /**
     * Tests loading from empty/non-existent file.
     */
    @Test
    void testLoadEmptyOrNonExistentFile() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> saveController.loadGame(),
                "Should throw RuntimeException when loading from non-existent file");

        assertTrue(exception.getMessage().contains("save file is empty or does not exist"),
                "Exception should mention empty or non-existent file");
    }

    /**
     * Tests handling of saving to a read-only location.
     */
    @Test
    void testSaveToReadOnlyLocation() {
        File readOnlyFile = new File(TEST_SAVE_FILE);
        try {
            // Creates the file and make it read-only
            readOnlyFile.createNewFile();
            readOnlyFile.setReadOnly();

            SaveController readOnlySaveController = new SaveController(TEST_SAVE_FILE);
            assertThrows(RuntimeException.class,
                    () -> readOnlySaveController.saveGame(gameState),
                    "Should throw RuntimeException when saving to read-only location");
        } catch (IOException e) {
            fail("Test setup failed: " + e.getMessage());
        } finally {
            // Ensures we can delete the file in tearDown
            readOnlyFile.setWritable(true);
        }
    }

    /**
     * Tests saving a game to an invalid file path.
     */
    @Test
    void testSaveGameInvalidPath() {
        SaveController invalidPathController = new SaveController(INVALID_SAVE_PATH);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> invalidPathController.saveGame(gameState),
                "Saving to an invalid path should throw a RuntimeException.");
        assertTrue(exception.getMessage().contains("Failed to save game"),
                "Exception message should indicate failure to save.");
    }

    /**
     * Tests loading from a corrupted save file.
     */
    @Test
    void testLoadCorruptedFile() {
        try {
            // Create a file with invalid content
            Files.write(Path.of(SAVE_PATH), "corrupted data".getBytes());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> saveController.loadGame(),
                    "Loading a corrupted file should throw a RuntimeException");

            assertTrue(exception.getMessage().contains("Failed to load game"),
                    "Exception message should indicate failure to load");
        } catch (IOException e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }

    /**
     * Tests saving and then loading a GameState.
     */
    @Test
    void testSaveAndLoadGameState() {
        // Saves the game state
        saveController.saveGame(gameState);

        // Verifies the file exists and is not empty
        File savedFile = new File(SAVE_PATH);
        assertTrue(savedFile.exists(), "Save file should exist");
        assertTrue(savedFile.length() > 0, "Save file should not be empty");

        // Loads the game state
        GameState loadedState = saveController.loadGame();

        // Verifies the loaded state matches the original
        assertNotNull(loadedState, "Loaded state should not be null");

        // Compares mazes
        Maze originalMaze = gameState.getMaze();
        Maze loadedMaze = loadedState.getMaze();
        assertEquals(originalMaze.getWidth(), loadedMaze.getWidth(), "Maze width should match");
        assertEquals(originalMaze.getHeight(), loadedMaze.getHeight(), "Maze height should match");

        // Compares specific tile types in the first room
        Room originalRoom = originalMaze.getRoom(0, 0);
        Room loadedRoom = loadedMaze.getRoom(0, 0);

        // Checks tile types match
        assertEquals(originalRoom.getTile(0, 0).getTileID(),
                loadedRoom.getTile(0, 0).getTileID(),
                "Tile at (0,0) should match");
        assertEquals(originalRoom.getTile(0, 1).getTileID(),
                loadedRoom.getTile(0, 1).getTileID(),
                "Tile at (0,1) should match");
    }

}