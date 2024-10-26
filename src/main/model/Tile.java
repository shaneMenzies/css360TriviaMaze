package model;

/**
 * Interface for any type of tile inside a room.
 *
 * @author Shane Menzies
 * @version 10/25/24
 */
public interface Tile {

    /**
     * Method called when a player moves onto this tile.
     *
     * @param thePlayer The player who moved onto this tile.
     */
    void onMovedTo(Player thePlayer);

    /**
     * Whether this tile can be moved through by the player.
     *
     * @return True if the player can pass, false if not.
     */
    boolean isPassable();

    /**
     * Gets the TileID for this type of tile.
     *
     * @return Corresponding ID.
     */
    TileID getTileID();

}