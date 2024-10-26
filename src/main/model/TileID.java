package model;

/**
 * ID enumeration for each type of tile to associate itself with
 *  for later identification.
 *
 * @author Shane Menzies
 * @version 10/25/24
 */
public enum TileID {
    /**
     * Empty tile.
     */
    EMPTY,

    /**
     * Wall tile.
     */
    WALL,

    /**
     * A closed door tile.
     */
    DOOR_CLOSED,

    /**
     * An open door tile.
     */
    DOOR_OPEN,
}