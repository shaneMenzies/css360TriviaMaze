package model.tiles;

import model.Player;
import model.Tile;
import model.TileID;

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
    public boolean tryMoveTo(final Player thePlayer) {
        // Players can not move to a wall tile.
        return false;
    }

    @Override
    public TileID getTileID() {
        return TileID.WALL;
    }
}