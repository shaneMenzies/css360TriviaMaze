package view;

import model.Coordinates;
import model.GameModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class handles the visual representation and movement
 * of the player.
 *
 * @author Cynthia Lopez
 * @version 11/1/24
 */
public class PlayerView {

    /** PlayerManager instance to listen to input actions. */
    private final PlayerManager myPlayerListener;

    /** GameModel instance to get player position from. */
    private final GameModel myGameModel;

    /** BufferedImages for the players sprite different positions. */
    private BufferedImage myPlayerUp, myPlayerDown, myPlayerRight, myPlayerLeft;

    /**
     * Constructs a PlayerView instance with the specified game panel and player manager.
     *
     * @param thePlayerListener handling player input.
     * @param theGameModel Game Model to proc
     */
    public PlayerView(final PlayerManager thePlayerListener, final GameModel theGameModel) {
        myPlayerListener = thePlayerListener;
        myGameModel = theGameModel;

        loadPlayerImages();
    }

    public RoomViewHook getRoomViewHook() {
        return new MyRoomViewHook();
    }

    /** Gets the players sprites from image files. */
    private void loadPlayerImages() {
        try {
            myPlayerUp = ImageIO.read(new File("resources/images/player_up.png"));
            myPlayerDown = ImageIO.read(new File("resources/images/player_down.png"));
            myPlayerRight = ImageIO.read(new File("resources/images/player_right.png"));
            myPlayerLeft = ImageIO.read(new File("resources/images/player_left.png"));


        } catch (final IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private Image getPlayerImage() {
        return switch (myPlayerListener.getDirection()) {
            case UP -> myPlayerUp;
            case DOWN -> myPlayerDown;
            case RIGHT -> myPlayerRight;
            case LEFT -> myPlayerLeft;
        };
    }

    /**
     * RoomViewHook to draw the player on top of the room they're in.
     */
    private class MyRoomViewHook implements RoomViewHook {
        @Override
        public void doHook(final Graphics2D theGraphics, final RoomView theRoom) {
            final Coordinates playerPos = myGameModel.getState().getPlayer().getPosition();

            if (theRoom.getRoomX() == playerPos.getRoomX()
                && theRoom.getRoomY() == playerPos.getRoomY()) {
                // Need to draw player
                final int imageX = playerPos.getX() * theRoom.getTileWidth();
                final int imageY = (theRoom.getRoom().getHeight() - 1 - playerPos.getY())
                                   * theRoom.getTileHeight();
                theGraphics.drawImage(getPlayerImage(), imageX, imageY,
                                      theRoom.getTileWidth(), theRoom.getTileHeight(),
                                      null);
            }
        }
    }
}