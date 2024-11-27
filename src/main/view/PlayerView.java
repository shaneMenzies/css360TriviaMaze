package view;

import model.Coordinates;
import model.GameState;
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

    /** GameplayPanel instance where the player is displayed. */
    private final GameplayPanel myGamePanel;

    /** PlayerManager instance to listen to input actions. */
    private final PlayerManager myPlayerListener;

    /** BufferedImages for the players sprite different positions. */
    private BufferedImage myPlayerUp, myPlayerDown, myPlayerRight, myPlayerLeft;

    /** Current game state. */
    private final GameState myGameState;

    /**
     * Constructs a PlayerView instance with the specified game panel and player manager.
     *
     * @param theGamePanel where the player is displayed.
     * @param thePlayerListener handling player input.
     */
    public PlayerView(final GameplayPanel theGamePanel, final PlayerManager thePlayerListener) {
        myPlayerListener = thePlayerListener;
        myGamePanel = theGamePanel;
        myGameState = myGamePanel.getGameState();

        getPlayerImage();
    }

    /** Gets the players sprites from image files. */
    private void getPlayerImage() {
        try {
            myPlayerUp = ImageIO.read(new File("resources/images/player_up.png"));
            myPlayerDown = ImageIO.read(new File("resources/images/player_down.png"));
            myPlayerRight = ImageIO.read(new File("resources/images/player_right.png"));
            myPlayerLeft = ImageIO.read(new File("resources/images/player_left.png"));


        } catch (final IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /** renders player in new position. */
    public void update() {
        myPlayerListener.update(myGameState);
    }

    /**
     * Renders the players sprite at current position.
     *
     * @param theGraphics used for rendering.
     */
    public void draw(final Graphics2D theGraphics) {
        final BufferedImage image = switch (myPlayerListener.getDirection()) {
            case UP -> myPlayerDown;
            case DOWN -> myPlayerUp;
            case RIGHT -> myPlayerRight;
            case LEFT -> myPlayerLeft;
        };

        Coordinates myPosition = myGameState.getPlayer().getPosition();
        final int   roomWidth  = myGameState.getMaze().getRoom(0, 0).getWidth();
        final int roomHeight = myGameState.getMaze().getRoom(0, 0).getHeight();

        final int playerX = (myPosition.getRoomX() * roomWidth) + myPosition.getX();
        final int playerY = (myPosition.getRoomY() * roomHeight) + myPosition.getY();

        final int tileWidth = myGamePanel.getTileWidth();
        final int tileHeight = myGamePanel.getTileHeight();

        theGraphics.drawImage(image, playerX * tileWidth, playerY * tileHeight,
                tileWidth, tileHeight, null);
    }
}