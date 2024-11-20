package model.tiles;

import model.enums.TileID;
import model.interfaces.Tile;

/**
 * A solid wall tile, impassable by the player.
 *
 * @author Shane Menzies
 * @version 10/25/24
 */
public final class WallTile implements Tile {

    /**
     * Constructs a new Wall Tile.
     */
    public WallTile() { }

    @Override
    public boolean tryMoveTo() {
        // Players can not move to a wall tile.
        return false;
    }

    @Override
    public TileID getTileID() {
        return TileID.WALL;
    }

    @Override
    public String toString() {
        return "▮";
    }
}