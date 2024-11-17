package model.tiles;

import model.Tile;
import model.TileID;

import java.io.Serializable;

/**
 * A solid wall tile, impassable by the player.
 *
 * @author Shane Menzies
 * @version 10/25/24
 */
public final class WallTile implements Tile, Serializable {
    private static final long serialVersionUID = 1L;

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
}