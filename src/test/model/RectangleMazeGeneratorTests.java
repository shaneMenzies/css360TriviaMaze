package model;

import static org.junit.jupiter.api.Assertions.*;

import model.tiles.EmptyTile;
import model.tiles.WallTile;
import org.junit.jupiter.api.Test;

/**
 * Tests the RectangleMazeGenerator class.
 *
 * @author Shane Menzies
 * @version 11/21/24
 */
class RectangleMazeGeneratorTests {

    /**
     * Height of the testing maze in rooms.
     */
    private final int TEST_MAZE_HEIGHT = 8;

    /**
     * Width of the testing maze in rooms.
     */
    private final int TEST_MAZE_WIDTH = 12;

    /**
     * Height of the testing rooms in tiles.
     */
    private final int TEST_ROOM_HEIGHT = 6;

    /**
     * Width of the testing rooms in tiles.
     */
    private final int TEST_ROOM_WIDTH = 5;

    /**
     * X-coordinate of starting room for testing.
     */
    private final int TEST_START_X = 2;

    /**
     * Y-coordinate of starting room for testing.
     */
    private final int TEST_START_Y = 3;

    /**
     * X-coordinate of exit room for testing.
     */
    private final int TEST_EXIT_X = 6;

    /**
     * Y-coordinate of exit room for testing.
     */
    private final int TEST_EXIT_Y = 10;

    /**
     * Test question to provide to generator.
     */
    private final TriviaQuestion TEST_QUESTION
            = new TriviaQuestion("Test Question",
            "Test Answer",
            TriviaQuestion.QuestionType.SHORT_ANSWER);

    /**
     * Test basic generation of a rectangular maze.
     */
    @Test
    void generateBasic() {
        final RectangleMazeGenerator generator = new RectangleMazeGenerator(TEST_MAZE_HEIGHT, TEST_MAZE_WIDTH,
                TEST_ROOM_HEIGHT, TEST_ROOM_WIDTH, () -> TEST_QUESTION);

        final String expected = expectedFor(TEST_MAZE_HEIGHT, TEST_MAZE_WIDTH,
                TEST_ROOM_HEIGHT, TEST_ROOM_WIDTH);

        final Maze generatedMaze = generator.generate();
        final String received = generator.generate().toString();

        assertTrue(generatedMaze.getStartingRoomX() > -1);
        assertTrue(generatedMaze.getStartingRoomY() > -1);
        assertTrue(generatedMaze.getExitRoomX() > -1);
        assertTrue(generatedMaze.getExitRoomY() > -1);
        assertEquals(expected, received, "Basic generation test failed on "
                     + TEST_MAZE_HEIGHT + 'x' + TEST_MAZE_WIDTH + " maze of "
                     + TEST_ROOM_HEIGHT + 'x' + TEST_ROOM_WIDTH + " rooms!");
    }

    /**
     * Test specifying start coordinates.
     */
    @Test
    void setStartCoordinates() {
        final RectangleMazeGenerator generator = new RectangleMazeGenerator(TEST_MAZE_HEIGHT, TEST_MAZE_WIDTH,
                TEST_ROOM_HEIGHT, TEST_ROOM_WIDTH, () -> TEST_QUESTION);
        generator.setStartCoordinates(TEST_START_X, TEST_START_Y);

        final Maze generatedMaze = generator.generate();

        assertEquals(TEST_START_X, generatedMaze.getStartingRoomX());
        assertEquals(TEST_START_Y, generatedMaze.getStartingRoomY());
    }

    /**
     * Test specifying exit coordinates.
     */
    @Test
    void setExitCoordinates() {
        final RectangleMazeGenerator generator = new RectangleMazeGenerator(TEST_MAZE_HEIGHT, TEST_MAZE_WIDTH,
                TEST_ROOM_HEIGHT, TEST_ROOM_WIDTH, () -> TEST_QUESTION);
        generator.setExitCoordinates(TEST_EXIT_X, TEST_EXIT_Y);

        final Maze generatedMaze = generator.generate();

        assertEquals(TEST_EXIT_X, generatedMaze.getExitRoomX());
        assertEquals(TEST_EXIT_Y, generatedMaze.getExitRoomY());
    }

    /**
     * Test generating a smaller maze.
     */
    @Test
    void generateSmaller() {
        final int mazeHeight = (TEST_MAZE_HEIGHT / 2) + 1;
        final int mazeWidth = (TEST_MAZE_WIDTH / 2) + 1;
        final int roomHeight = (TEST_ROOM_HEIGHT / 2) + 1;
        final int roomWidth = (TEST_ROOM_WIDTH / 2) + 1;

        final RectangleMazeGenerator generator = new RectangleMazeGenerator(mazeHeight, mazeWidth,
                roomHeight, roomWidth, () -> TEST_QUESTION);

        final String expected = expectedFor(mazeHeight, mazeWidth,
                roomHeight, roomWidth);

        final Maze generatedMaze = generator.generate();
        final String received = generator.generate().toString();

        assertTrue(generatedMaze.getStartingRoomX() > -1);
        assertTrue(generatedMaze.getStartingRoomY() > -1);
        assertTrue(generatedMaze.getExitRoomX() > -1);
        assertTrue(generatedMaze.getExitRoomY() > -1);
        assertEquals(expected, received, "Basic generation test failed on "
                + mazeHeight + 'x' + mazeWidth + " maze of "
                + roomHeight + 'x' + roomWidth + " rooms!");

    }

    /**
     * Test generating a larger maze.
     */
    @Test
    void generateLarger() {
        final int mazeHeight = (TEST_MAZE_HEIGHT * 2) + 1;
        final int mazeWidth = (TEST_MAZE_WIDTH * 2) + 1;
        final int roomHeight = (TEST_ROOM_HEIGHT * 2) + 1;
        final int roomWidth = (TEST_ROOM_WIDTH * 2) + 1;

        final RectangleMazeGenerator generator = new RectangleMazeGenerator(mazeHeight, mazeWidth,
                roomHeight, roomWidth, () -> TEST_QUESTION);

        final String expected = expectedFor(mazeHeight, mazeWidth,
                roomHeight, roomWidth);

        final Maze generatedMaze = generator.generate();
        final String received = generator.generate().toString();

        assertTrue(generatedMaze.getStartingRoomX() > -1);
        assertTrue(generatedMaze.getStartingRoomY() > -1);
        assertTrue(generatedMaze.getExitRoomX() > -1);
        assertTrue(generatedMaze.getExitRoomY() > -1);
        assertEquals(expected, received, "Basic generation test failed on "
                + mazeHeight + 'x' + mazeWidth + " maze of "
                + roomHeight + 'x' + roomWidth + " rooms!");

    }

    /**
     * Tests the smallest possible maze generation.
     */
    @Test
    void generateMinimum() {
        final int mazeHeight = 1;
        final int mazeWidth = 1;
        final int roomHeight = 3;
        final int roomWidth = 3;

        final RectangleMazeGenerator generator = new RectangleMazeGenerator(mazeHeight, mazeWidth,
                roomHeight, roomWidth, () -> TEST_QUESTION);

        final String expected = expectedFor(mazeHeight, mazeWidth,
                roomHeight, roomWidth);

        final Maze generatedMaze = generator.generate();
        final String received = generator.generate().toString();

        assertTrue(generatedMaze.getStartingRoomX() > -1);
        assertTrue(generatedMaze.getStartingRoomY() > -1);
        assertTrue(generatedMaze.getExitRoomX() > -1);
        assertTrue(generatedMaze.getExitRoomY() > -1);
        assertEquals(expected, received, "Basic generation test failed on "
                + mazeHeight + 'x' + mazeWidth + " maze of "
                + roomHeight + 'x' + roomWidth + " rooms!");
    }

    /**
     * Generate the expected toString output for a rectangular maze with the
     * provided parameters.
     *
     * @param theMazeHeight Maze height in rooms.
     * @param theMazeWidth Maze width in rooms.
     * @param theRoomHeight Room height in tiles.
     * @param theRoomWidth Room width in tiles.
     * @return Expected toString() output for a Maze with identical parameters.
     */
    private String expectedFor(final int theMazeHeight, final int theMazeWidth,
                               final int theRoomHeight, final int theRoomWidth) {
        final char wallChar = new WallTile().toString().charAt(0);
        final char emptyChar = new EmptyTile().toString().charAt(0);
        final char doorChar = new DoorController(TEST_QUESTION).
                getDoors()[0].toString().charAt(0);

        final int verticalDoorIndex = theRoomHeight / 2;
        final int horizontalDoorIndex = theRoomWidth / 2;

        final StringBuilder stringBuilder = new StringBuilder();

        // Top maze border
        stringBuilder.append(String.valueOf(wallChar).repeat((theMazeWidth * theRoomWidth)));
        stringBuilder.append('\n');

        for (int y = (theMazeHeight * theRoomHeight) - 2; y >= 1; y--) {
            final int tileY = y % theRoomHeight;

            // Left maze border
            stringBuilder.append(wallChar);

            for (int x = 1; x <= (theMazeWidth * theRoomWidth) - 2; x++) {
                final int tileX = x % theRoomWidth;

                final char nextChar;
                if (tileY == 0 || tileY == theRoomHeight - 1) {
                    // Top or Bottom Wall
                    if (tileX == horizontalDoorIndex) {
                        nextChar = doorChar;
                    } else {
                        nextChar = wallChar;
                    }
                } else if (tileX == 0 || tileX == theRoomWidth - 1) {
                    // Left or Right Wall
                    if (tileY == verticalDoorIndex) {
                        nextChar = doorChar;
                    } else {
                        nextChar = wallChar;
                    }
                } else {
                    // Interior
                    nextChar = emptyChar;
                }

                stringBuilder.append(nextChar);
            }

            // Right maze border
            stringBuilder.append(wallChar);
            stringBuilder.append('\n');
        }

        // Bottom maze border
        stringBuilder.append(String.valueOf(wallChar).repeat((theMazeWidth * theRoomWidth)));
        stringBuilder.append('\n');

        return stringBuilder.toString();
    }
}