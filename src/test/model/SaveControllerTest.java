package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests saving and loading game states, handling of invalid files,
 * and error conditions.
 *
 * @author Arafa Mohamed
 * @version 11/07/24
 */
public class SaveControllerTest {

    private static final String VALID_SAVE_PATH = "testGameState.ser";
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
        saveController = new SaveController(VALID_SAVE_PATH);

        // Initialize GameSettings and Maze for testing
        GameSettings settings = new GameSettings(3, 10, -5);
        Room[][] rooms = new Room[][]{{new Room(Room.RoomType.START, new Tile[1][1])}};
        Maze maze = new Maze(rooms, 0, 0, 0, 0);

        gameState = new GameState(settings, maze);
    }

    /**
     * Clean up test environment after each test.
     * Deletes test save files if they exist.
     */
    @AfterEach
    void tearDown() {
        File saveFile = new File(VALID_SAVE_PATH);
        if (saveFile.exists() && !saveFile.delete()) {
            System.err.println("Failed to delete test save file");
        }
    }

    /**
     * Tests saving and loading a null game state, expecting exceptions.
     */
    @Test
    void testSaveAndLoadNullGameState() {
        assertThrows(NullPointerException.class,
                () -> saveController.saveGame(null),
                "Should throw NullPointerException when saving null state");
        assertThrows(RuntimeException.class,
                saveController::loadGame,
                "Should throw RuntimeException when loading from potentially null/empty state");
    }

    /**
     * Tests handling of saving to a read-only location.
     */
    @Test
    void testSaveToReadOnlyLocation() {
        File readOnlyFile = new File(TEST_SAVE_FILE);
        try {
            if (readOnlyFile.createNewFile() && readOnlyFile.setReadOnly()) {
                SaveController readOnlySaveController = new SaveController(TEST_SAVE_FILE);
                assertThrows(RuntimeException.class,
                        () -> readOnlySaveController.saveGame(gameState),
                        "Should throw RuntimeException when saving to read-only location");
            } else {
                fail("Failed to set up read-only file");
            }
        } catch (IOException e) {
            fail("Test setup failed: " + e.getMessage());
        } finally {
            readOnlyFile.setWritable(true);
            readOnlyFile.delete();
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
     * Tests loading a game from a non-existent file.
     */
    @Test
    void testLoadGameFileNotFound() {
        SaveController nonExistentFileController = new SaveController("nonExistentFile.ser");
        RuntimeException exception = assertThrows(RuntimeException.class,
                nonExistentFileController::loadGame,
                "Loading from a non-existent file should throw a RuntimeException.");
        assertTrue(exception.getMessage().contains("Failed to load game"),
                "Exception message should indicate failure to load due to missing file.");
    }

    /**
     * Tests loading from a corrupted save file.
     */
    @Test
    void testLoadCorruptedFile() {
        try {
            Files.write(Path.of(VALID_SAVE_PATH), "corrupted data".getBytes());
            RuntimeException exception = assertThrows(RuntimeException.class,
                    saveController::loadGame,
                    "Loading a game from a corrupted file should throw a RuntimeException.");
            assertTrue(exception.getMessage().contains("Failed to load game"),
                    "Exception message should indicate failure to load due to corrupted file content.");
        } catch (IOException e) {
            fail("Test setup failed: Unable to create corrupted file.");
        }
    }
}







