package model;

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
    public void onMovedTo(final Player thePlayer) {
        // Nothing happens for an empty tile.
    }

    @Override
    public boolean isPassable() {
        return true;
    }

    @Override
    public TileID getTileID() {
        return TileID.EMPTY;
    }
}