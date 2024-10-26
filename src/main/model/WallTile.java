package model;

/**
 * A solid wall tile, impassable by the player.
 *
 * @author Shane Menzies
 * @version 10/25/24
 */
public class WallTile implements Tile {

    /**
     * Exception message for a player incorrectly moving onto a wall tile.
     */
    private static final String MOVED_TO_EXCEPTION_MESSAGE
            = "Player Moved onto a wall resulting in an invalid position!";

    /**
     * Constructs a new Wall Tile.
     */
    public WallTile() { }

    @Override
    public void onMovedTo(final Player thePlayer) {
        // This should not be able to happen.
        throw new InvalidPositionException(MOVED_TO_EXCEPTION_MESSAGE);
    }

    @Override
    public boolean isPassable() {
        return false;
    }

    @Override
    public TileID getTileID() {
        return TileID.Wall;
    }

    /**
     * Exception caused from the Player moving onto a wall tile, resulting in an
     *  invalid position.
     *
     * @author Shane Menzies
     * @version 10/25/24
     */
    public static class InvalidPositionException extends RuntimeException {

        /**
         * Raises an InvalidPositionException with the provided message.
         *
         * @param theMessage Exception message
         */
        public InvalidPositionException(final String theMessage) {
            super(theMessage);
        }
    }

}