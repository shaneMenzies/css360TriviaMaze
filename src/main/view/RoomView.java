package view;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

import model.GameModel;
import model.Room;
import model.enums.TileID;
import java.awt.*;
import java.awt.image.BufferedImage;

public final class RoomView {

    /**
     * Error message for invalid tile dimensions being provided.
     */
    private static final String ILLEGAL_TILE_DIMENSIONS = "Illegal Tile Dimensions provided!";

    /**
     * Error message for a null GameModel being provided.
     */
    private static final String NULL_MODEL = "Provided GameModel was null!";

    /**
     * Error message for an invalid Room being provided.
     */
    private static final String INVALID_ROOM =
            "Provided room coordinates did not correspond to a valid room!";

    /**
     * X-coordinate of the target Room.
     */
    private final int myRoomX;

    /**
     * Y-coordinate of the target Room.
     */
    private final int myRoomY;

    /**
     * Target GameModel to source Room data from.
     */
    private final GameModel myModel;

    /**
     * The target Room.
     */
    private final Room myRoom;

    /**
     * Width of a tile in pixels.
     */
    private final int myTileWidth;

    /**
     * Height of a tile in pixels.
     */
    private final int myTileHeight;

    /**
     * Generated image of the target room.
     */
    private final BufferedImage myImage;

    /**
     * Creates a view of a single room.
     *
     * @param theTileWidth Width of a tile.
     * @param theTileHeight Height of a tile.
     * @param theModel GameModel to get room data from.
     * @param theRoomX X-coordinate of target room.
     * @param theRoomY Y-coordinate of target room.
     */
    public RoomView(final int theTileWidth, final int theTileHeight,
                    final GameModel theModel,
                    final int theRoomX, final int theRoomY) {
        if (theTileWidth == 0
            || theTileHeight == 0) {
            throw new IllegalArgumentException(ILLEGAL_TILE_DIMENSIONS);
        }
        myTileWidth = theTileWidth;
        myTileHeight = theTileHeight;

        if (theModel == null) {
            throw new IllegalArgumentException(NULL_MODEL);
        }
        myModel = theModel;

        if (myModel.getState().getMaze().getRoom(theRoomX, theRoomY) == null) {
            throw new IllegalArgumentException(INVALID_ROOM);
        }
        myRoomX = theRoomX;
        myRoomY = theRoomY;
        myRoom = myModel.getState().getMaze().getRoom(theRoomX, theRoomY);

        myImage = createInitialView();
    }

    /**
     * Gets the view of this room as an image.
     *
     * @return Image of this room's contents.
     */
    public BufferedImage asImage() {
        return myImage;
    }

    /**
     * Get this RoomView's target room's X coordinate.
     *
     * @return This RoomView's target room's X coordinate.
     */
    public int getRoomX() {
        return myRoomX;
    }

    /**
     * Get this RoomView's target room's Y coordinate.
     *
     * @return This RoomView's target room's Y coordinate.
     */
    public int getRoomY() {
        return myRoomY;
    }

    /**
     * Get the GameModel this RoomView is based on.
     *
     * @return The source GameModel.
     */
    public GameModel getModel() {
        return myModel;
    }

    /**
     * Get the Room this RoomView is based on.
     *
     * @return The original Room.
     */
    public Room getRoom() {
        return myRoom;
    }

    /**
     * Get width of a tile in pixels.
     *
     * @return Tile width.
     */
    public int getTileWidth() {
        return myTileWidth;
    }

    /**
     * Get height of a tile in pixels.
     *
     * @return Tile height.
     */
    public int getTileHeight() {
        return myTileHeight;
    }

    /**
     * Fills out this view's image representation of the room.
     */
    private BufferedImage createInitialView() {
        final Room room = myModel.getState().getMaze().getRoom(myRoomX, myRoomY);

        final int imageWidth = room.getWidth() * myTileWidth;
        final int imageHeight = room.getHeight() * myTileHeight;

        final BufferedImage image = new BufferedImage(imageWidth, imageHeight, TYPE_INT_RGB);
        final Graphics2D context = image.createGraphics();

        final SpriteMap sprites = SpriteMap.getInstance();

        int imageX = 0;
        int imageY = 0;
        for (int tileY = room.getHeight() - 1; tileY >= 0; tileY--) {
            for (int tileX = 0; tileX < room.getWidth(); tileX++) {
                final TileID targetTile = room.getTile(tileX, tileY).getTileID();
                final Image targetSprite = sprites.get(targetTile);

                context.drawImage(targetSprite, imageX, imageY,
                                  myTileWidth, myTileHeight, null);
                imageX += myTileWidth;
            }
            imageX = 0;
            imageY += myTileHeight;
        }

        context.dispose();

        return image;
    }
}