package view;

import java.util.ArrayList;
import model.GameModel;
import model.Maze;
import model.interfaces.GameModelUpdateListener;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Class for rendering a view of the maze.
 */
public final class MazeView {

    private static final String ILLEGAL_TILE_DIMENSIONS = "Illegal Tile Dimensions provided!";

    private static final String NULL_MODEL = "Provided GameModel was null!";

    private int myTileWidth;

    private int myTileHeight;

    private final GameModel myTargetModel;

    private final List<RoomViewHook> myRoomViewHooks;

    private RoomView[][] myRoomViews;

    /**
     * Constructs a MazeView with the specified tile dimensions and target model.
     *
     * @param theTileWidth Width of tiles (in pixels).
     * @param theTileHeight Height of tiles (in pixels).
     * @param theModel GameModel to track, containing the maze to render.
     */
    public MazeView(final int theTileWidth, final int theTileHeight, final GameModel theModel) {
        if (theTileWidth == 0
            || theTileHeight == 0) {
            throw new IllegalArgumentException(ILLEGAL_TILE_DIMENSIONS);
        } else if (theModel == null) {
            throw new IllegalArgumentException(NULL_MODEL);
        }

        myTileWidth = theTileWidth;
        myTileHeight = theTileHeight;
        myTargetModel = theModel;

        myTargetModel.addUpdateListener(this::onGameModelUpdate);

        myRoomViewHooks = new ArrayList<>();

        refresh();
    }

    /**
     * Sets the tile dimensions in pixels.
     *
     * @param theTileWidth Width of a tile in pixels.
     * @param theTileHeight Height of a tile in pixels.
     */
    public void setTileDimensions(final int theTileWidth, final int theTileHeight) {
        if (theTileHeight == 0
            || theTileWidth == 0) {
            throw new IllegalArgumentException(ILLEGAL_TILE_DIMENSIONS);
        }

        myTileWidth = theTileWidth;
        myTileHeight = theTileHeight;

        refresh();
    }

    /**
     * Add a new RoomViewHook.
     *
     * @param theHook The new RoomViewHook to add.
     */
    public void addRoomViewHook(final RoomViewHook theHook) {
        myRoomViewHooks.add(theHook);
        refresh();
    }

    /**
     * Remove a previously added RoomViewHook.
     *
     * @param theHook The RoomViewHook to remove.
     * @return True if successful, false if it failed.
     */
    public boolean removeRoomViewHook(final RoomViewHook theHook) {
        final boolean result = myRoomViewHooks.remove(theHook);
        refresh();

        return result;
    }

    /**
     * Renders an image view of the entire maze.
     *
     * @return Image of entire maze.
     */
    public Image getFull() {
        final Maze target = myTargetModel.getState().getMaze();

        final int roomHeightTiles = target.getRoom(0, 0).getHeight();
        final int roomWidthTiles = target.getRoom(0, 0).getWidth();

        final int imageHeight = target.getHeight() * roomHeightTiles * myTileHeight;
        final int imageWidth = target.getWidth() * roomWidthTiles * myTileWidth;

        final BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        final Graphics2D context = image.createGraphics();

        int imageY = 0;
        int imageX = 0;
        for (int roomY = target.getHeight() - 1; roomY >= 0; roomY--) {
            for (int roomX = 0; roomX < target.getWidth(); roomX++) {

                context.drawImage(myRoomViews[roomY][roomX].asImage(), imageX, imageY, null);
                imageX += roomWidthTiles * myTileWidth;
            }
            imageX = 0;
            imageY += roomHeightTiles * myTileHeight;
        }

        return image;
    }

    /**
     * Renders an image view of only a portion of the maze.
     *
     * @param theCenterRoomX Center room's X-coordinate.
     * @param theCenterRoomY Center room's Y-coordinate.
     * @param theImageWidth Width of the resulting image.
     * @param theImageHeight Height of the resulting image.
     * @return Image containing only the specified portion of the maze.
     */
    public Image getPortion(final int theCenterRoomX, final int theCenterRoomY,
                            final int theImageWidth, final int theImageHeight) {
        final Maze target = myTargetModel.getState().getMaze();

        final int roomWidthTiles = target.getRoom(theCenterRoomX, theCenterRoomY).getWidth();
        final int roomHeightTiles = target.getRoom(theCenterRoomX, theCenterRoomY).getHeight();

        final int roomImageHeight = roomHeightTiles * myTileHeight;
        final int roomImageWidth = roomWidthTiles * myTileWidth;

        // Determine room bounds for this portion
        int leftRoomBound = theCenterRoomX;
        int rightRoomBound = theCenterRoomX;

        int leftRemaining = (theImageWidth / 2)
                - (roomImageWidth / 2);
        int rightRemaining = leftRemaining;

        // Left bound
        while (leftRemaining > 0 && leftRoomBound >= 1) {
            leftRoomBound--;
            leftRemaining -= roomImageWidth;
        }

        // Right bound
        while (rightRemaining > 0 && rightRoomBound < (target.getWidth() - 1)) {
            rightRoomBound++;
            rightRemaining -= roomImageWidth;
        }

        int topRoomBound = theCenterRoomY;
        int bottomRoomBound = theCenterRoomY;

        int topRemaining = (theImageHeight / 2)
                - (roomImageHeight / 2);
        int bottomRemaining = topRemaining;

        // Bottom bound
        while (bottomRemaining > 0 && bottomRoomBound >= 1) {
            bottomRoomBound--;
            bottomRemaining -= roomImageHeight;
        }

        // Top bound
        while (topRemaining > 0 && topRoomBound < (target.getHeight() - 1)) {
            topRoomBound++;
            topRemaining -= roomImageHeight;
        }

        final Image sourceImage = getRoomRect(leftRoomBound, rightRoomBound,
                                              bottomRoomBound, topRoomBound);

        final BufferedImage image = new BufferedImage(theImageWidth, theImageHeight, BufferedImage.TYPE_INT_RGB);
        final Graphics2D context = image.createGraphics();

        int sourceLeft = -(leftRemaining);
        int sourceRight = sourceLeft + theImageWidth;
        int sourceTop = -(topRemaining);
        int sourceBottom = sourceTop + theImageHeight;

        context.drawImage(sourceImage, 0, 0, theImageWidth - 1, theImageHeight - 1,
                          sourceLeft, sourceTop, sourceRight, sourceBottom, null);

        context.dispose();

        return image;
    }

    /**
     * Gets an image of only a specified range of rooms.
     *
     * @param theLeftBound Left bound, inclusive (lowest X-coordinate)
     * @param theRightBound Right bound, inclusive (highest X-coordinate)
     * @param theBottomBound Bottom bound, inclusive (lowest Y-coordinate)
     * @param theTopBound Top bound, inclusive (highest Y-coordinate)
     * @return Image of all the rooms inside specified bounds.
     */
    private Image getRoomRect(final int theLeftBound, final int theRightBound,
                              final int theBottomBound, final int theTopBound) {
        final Maze target = myTargetModel.getState().getMaze();

        final int roomHeightTiles = target.getRoom(theLeftBound, theTopBound).getHeight();
        final int roomWidthTiles = target.getRoom(theLeftBound, theTopBound).getWidth();

        final int imageHeight = (theTopBound - theBottomBound + 1) * roomHeightTiles * myTileHeight;
        final int imageWidth = (theRightBound - theLeftBound + 1) * roomWidthTiles * myTileWidth;

        final BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        final Graphics2D context = image.createGraphics();

        int imageY = 0;
        int imageX = 0;
        for (int roomY = theTopBound; roomY >= theBottomBound; roomY--) {
            for (int roomX = theLeftBound; roomX <= theRightBound; roomX++) {

                context.drawImage(myRoomViews[roomY][roomX].asImage(), imageX, imageY, null);
                imageX += roomWidthTiles * myTileWidth;
            }
            imageX = 0;
            imageY += roomHeightTiles * myTileHeight;
        }

        return image;
    }

    /**
     * Run all the RoomViewHooks for a certain room.
     *
     * @param theGraphics Graphics2D handle for drawing.
     * @param theRoom The RoomView being drawn.
     */
    private void runRoomViewHooks(final Graphics2D theGraphics, final RoomView theRoom) {
        for (final RoomViewHook nextHook : myRoomViewHooks) {
            nextHook.doHook(theGraphics, theRoom);
        }
    }

    /**
     * Refreshes all the contained RoomViews.
     */
    private void refresh() {
        final Maze target = myTargetModel.getState().getMaze();
        final RoomView[][] newRooms = new RoomView[target.getHeight()][target.getWidth()];

        // Initialize individual room views.
        for (int roomY = 0; roomY < target.getHeight(); roomY++) {
            for (int roomX = 0; roomX < target.getWidth(); roomX++) {
                final RoomView nextRoom = new RoomView(myTileWidth, myTileHeight,
                                                         myTargetModel, roomX, roomY);
                runRoomViewHooks(nextRoom.asImage().createGraphics(),
                                 nextRoom);
                newRooms[roomY][roomX] = nextRoom;
            }
        }

        myRoomViews = newRooms;
    }

    /**
     * Updates this view when the GameModel updates.
     *
     * @param theType Type of update.
     * @param theModel GameModel which updated.
     */
    private void onGameModelUpdate(final GameModelUpdateListener.UpdateType theType,
                                   final GameModel theModel) {
        refresh();
    }
}