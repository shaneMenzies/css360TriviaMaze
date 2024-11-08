package model;

/**
 * Represents an entire room in a maze.
 *
 * @author Shane Menzies
 * @version 10/26/24
 */
public final class Room {

    /**
     * Exception message for an invalid tiles array with height of 0.
     */
    private static final String INVALID_TILES_HEIGHT_0_MESSAGE
            = "Invalid Tiles Array with height 0 provided to Room()!";

    /**
     * Exception message for an invalid tiles array with width of 0.
     */
    private static final String INVALID_TILES_WIDTH_0_MESSAGE
            = "Invalid Tiles Array with width 0 provided to Room()!";

    /**
     * Exception message for an invalid tiles array with width of 0.
     */
    private static final String INVALID_TILES_NOT_RECTANGLE_MESSAGE
            = "Invalid Tiles Array with non-rectangular shape provided to Room()!";

    /**
     * What type of room this is.
     */
    private final RoomType myType;

    /**
     * 2D Array of this room's tiles.
     */
    private final Tile[][] myTiles;

    /**
     * Makes a room from the provided values.
     *
     * @param theType Type of room.
     * @param theTiles 2D Array of the new room's tiles.
     */
    public Room(final RoomType theType, final Tile[][] theTiles)
            throws IllegalArgumentException {
        myType = theType;
        myTiles = theTiles;

        // Double check tile Array is valid
        if (myTiles.length == 0) {
            throw new IllegalArgumentException(INVALID_TILES_HEIGHT_0_MESSAGE);
        }

        // Make sure tile Array is a rectangle
        final int width = myTiles[0].length;
        if (width == 0) {
            throw new IllegalArgumentException(INVALID_TILES_WIDTH_0_MESSAGE);
        } else {
            for (Tile[] row : myTiles) {
                if (row.length != width) {
                    throw new IllegalArgumentException(INVALID_TILES_NOT_RECTANGLE_MESSAGE);
                }
            }
        }
    }

    /**
     * Creates a new Room as a copy of an existing Room.
     * <p>
     * This creates a new 2D Tile array for the new Room, however
     * the contents of the new array will still start as copies of
     * the original's Tile references.
     * </p>
     *
     * @param theSource Room instance to copy.
     */
    public Room(final Room theSource) {
        myType = theSource.myType;

        // Need to clone each row individually
        myTiles = new Tile[theSource.getHeight()][];
        for (int i = 0; i < myTiles.length; i++) {
            myTiles[i] = theSource.myTiles[i].clone();
        }
    }

    /**
     * Gets this room's type.
     *
     * @return Type of this room.
     */
    public RoomType getType() {
        return myType;
    }

    /**
     * Gets this room's height.
     *
     * @return Height of this room.
     */
    public int getHeight() {
        return myTiles.length;
    }

    /**
     * Gets this room's width.
     *
     * @return Width of this room.
     */
    public int getWidth() {
        return myTiles[0].length;
    }

    /**
     * Checks if X and Y coordinates are inside the bounds of this room.
     *
     * @param theX X coordinate to check
     * @param theY Y coordinate to check
     * @return True if inside the bounds of this room,
     *          false otherwise.
     */
    public boolean insideBounds(final int theX, final int theY) {
        return theX >= 0
                && theX < getWidth()
                && theY >= 0
                && theY < getHeight();
    }

    /**
     * Gets this room's tile array.
     *
     * @return This room's 2D tile array.
     */
    public Tile[][] getTiles() {
        return myTiles;
    }

    /**
     * Gets a certain tile in this room.
     *
     * @param theX X coordinate of target tile.
     * @param theY Y coordinate of target tile.
     * @return Tile at provided coordinates.
     */
    public Tile getTile(final int theX, final int theY) {
        return myTiles[theY][theX];
    }

    public enum RoomType {

        /**
         * A Standard room with nothing special about it.
         */
        STANDARD,

        /**
         * The room the player starts in.
         */
        START,

        /**
         * The exit of the maze.
         */
        EXIT,
    }
}