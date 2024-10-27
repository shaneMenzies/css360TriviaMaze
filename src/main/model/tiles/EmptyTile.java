package model.tiles;

import model.Player;
import model.Tile;
import model.TileID;

/**
 * An empty tile.
 *
 * @author Shane Menzies
 * @version 10/25/24
 */
public final class EmptyTile implements Tile {

    /**
     * Constructs a new Empty Tile.
     */
    public EmptyTile() { }

    @Override
    public boolean tryMoveTo(final Player thePlayer) {
        // A player can always move to an empty tile.
        return true;
    }

    @Override
    public TileID getTileID() {
        return TileID.EMPTY;
    }
}