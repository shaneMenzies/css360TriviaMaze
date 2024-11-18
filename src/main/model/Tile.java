package model;

import java.io.Serializable;

/**
 * Interface for any type of tile inside a room.
 *
 * @author Shane Menzies
 * @version 11/1/24
 */
public interface Tile extends Serializable {
    static final long serialVersionUID = 1L;

    /**
     * Method called when a player attempts to move onto this tile.
     *
     * @return Whether the player successfully moved onto this tile.
     */
    boolean tryMoveTo();

    /**
     * Gets the TileID for this type of tile.
     *
     * @return Corresponding ID.
     */
    TileID getTileID();
}