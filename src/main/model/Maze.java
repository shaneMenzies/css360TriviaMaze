package model;

/**
 * Represents an entire maze consisting of a 2D array of Rooms.
 *
 * @author Shane Menzies
 * @version 11/2/24
 */
public class Maze {

    /**
     * Exception message for an invalid rooms array with height of 0.
     */
    private static final String INVALID_ROOMS_HEIGHT_0_MESSAGE
            = "Invalid Rooms Array with height 0 provided to Maze()!";

    /**
     * Exception message for an invalid rooms array with width of 0.
     */
    private static final String INVALID_ROOMS_WIDTH_0_MESSAGE
            = "Invalid Rooms Array with width 0 provided to Maze()!";

    /**
     * Exception message for an invalid rooms array with width of 0.
     */
    private static final String INVALID_ROOMS_NOT_RECTANGLE_MESSAGE
            = "Invalid Rooms Array with non-rectangular shape provided to Maze()!";

    /**
     * 2D array of this maze's rooms.
     */
    private final Room[][] myRooms;

    /**
     * Constructs a Maze using the provided 2D array of Rooms.
     *
     * @param theRooms 2D Array of Rooms, must be rectangular.
     * @throws IllegalArgumentException If theRooms was not a rectangular array of rooms.
     */
    public Maze(final Room[][] theRooms) throws IllegalArgumentException {
        myRooms = theRooms;

        // Double check room Array is valid
        if (myRooms.length == 0) {
            throw new IllegalArgumentException(INVALID_ROOMS_HEIGHT_0_MESSAGE);
        }

        // Make sure room Array is a rectangle
        final int width = myRooms[0].length;
        if (width == 0) {
            throw new IllegalArgumentException(INVALID_ROOMS_WIDTH_0_MESSAGE);
        } else {
            for (Room[] row : myRooms) {
                if (row.length != width) {
                    throw new IllegalArgumentException(INVALID_ROOMS_NOT_RECTANGLE_MESSAGE);
                }
            }
        }
    }

    /**
     * Gets this maze's array of Rooms.
     *
     * @return This maze's rectangular array of Rooms.
     */
    public Room[][] getRooms() {
        return myRooms;
    }

    /**
     * Gets a certain Room inside this Maze.
     *
     * @param theX X / Column coordinate.
     * @param theY Y / Row coordinate.
     * @return Room at the provided coordinates.
     */
    public Room getRoomAt(final int theX, final int theY) {
        return myRooms[theY][theX];
    }

    /**
     * Gets the height of this Maze.
     *
     * @return Height of this Maze.
     */
    public int getHeight() {
        return myRooms.length;
    }

    /**
     * Gets the width of this Maze.
     *
     * @return Width of this Maze.
     */
    public int getWidth() {
        return myRooms[0].length;
    }
}