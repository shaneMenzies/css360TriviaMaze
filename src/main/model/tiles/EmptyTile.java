package model.tiles;

import model.enums.TileID;
import model.interfaces.Tile;

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
    public boolean tryMoveTo() {
        // A player can always move to an empty tile.
        return true;
    }

    @Override
    public TileID getTileID() {
        return TileID.EMPTY;
    }

    @Override
    public String toString() {
        return " ";
    }
}