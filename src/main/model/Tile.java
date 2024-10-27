package model;

/**
 * Interface for any type of tile inside a room.
 *
 * @author Shane Menzies
 * @version 10/25/24
 */
public interface Tile {

    /**
     * Method called when a player attempts to move onto this tile.
     *
     * @param thePlayer The player who is trying to move onto this tile.
     * @return Whether the player successfully moved onto this tile.
     */
    boolean tryMoveTo(Player thePlayer);

    /**
     * Gets the TileID for this type of tile.
     *
     * @return Corresponding ID.
     */
    TileID getTileID();
}