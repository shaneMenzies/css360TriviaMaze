package model.tiles;

import model.Tile;
import model.TileID;

import java.io.Serializable;

/**
 * An empty tile.
 *
 * @author Shane Menzies
 * @version 10/25/24
 */
public final class EmptyTile implements Tile, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new Empty Tile.
     */
    public EmptyTile() { }

    @Override
    public boolean tryMoveTo() {
        // A player can always move to an empty tile.
        return true;
    }

    @Override
    public TileID getTileID() {
        return TileID.EMPTY;
    }
}