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
    Empty,

    /**
     * Wall tile.
     */
    Wall,

    /**
     * A closed door tile.
     */
    DoorClosed,

    /**
     * An open door tile.
     */
    DoorOpen,
}