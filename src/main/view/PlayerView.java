package view;

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

    /** Used to render player. */
    private Graphics2D myGraphics;

    /** Player X coordinates. */
    private int myPlayerX;

    /** Player Y coordinates. */
    private int myPlayerY;

    /** Players speed per update. */
    private final int myPlayerSpeed;

    /** BufferedImages for the players sprite different positions. */
    private BufferedImage myPlayerUp, myPlayerDown, myPlayerRight, myPlayerLeft;

    /** Current direction the player is facing. */
    private String myDirection;

    /**
     * Constructs a PlayerView instance with the specified game panel and player manager.
     *
     * @param theGamePanel where the player is displayed.
     * @param thePlayerListener handling player input.
     */
    public PlayerView(final GameplayPanel theGamePanel, final PlayerManager thePlayerListener) {
        myPlayerListener = thePlayerListener;
        myGamePanel = theGamePanel;

        myPlayerX = 100;
        myPlayerY = 100;
        myPlayerSpeed = 4;
        myDirection = "down";

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

    /** Updates the players position and direction based on input. */
    private void update() {
        if (myPlayerListener.getUpKey()) {
            myDirection = "up";
            myPlayerY -= myPlayerSpeed;

        } else if (myPlayerListener.getDownKey()) {
            myDirection = "down";
            myPlayerY += myPlayerSpeed;

        } else if (myPlayerListener.getLeftKey()) {
            myDirection = "left";
            myPlayerX -= myPlayerSpeed;

        } else if (myPlayerListener.getRightKey()) {
            myDirection = "right";
            myPlayerX += myPlayerSpeed;
        }
    }

    /**
     * Renders the players sprite at current position.
     *
     * @param theGraphics used for rendering.
     */
    private void draw(final Graphics2D theGraphics) {
        myGraphics = theGraphics;
        final BufferedImage image = switch (myDirection) {
            case "up" -> myPlayerUp;
            case "down" -> myPlayerDown;
            case "right" -> myPlayerRight;
            case "left" -> myPlayerLeft;
            default -> null;
        };

        // eventually update with tile ID information
        theGraphics.drawImage(image, myPlayerX, myPlayerY, myGamePanel.getTileWidth(), myGamePanel.getTileHeight(), null);
    }

    /** Triggers update for players position and state. */
    public void getUpdate() {
        update();
    }

    /**
     *  Triggers rendering of player.
     *
     * @param theGraphics used for rendering.
     */
    public void getDraw(final Graphics2D theGraphics) {
        draw(theGraphics);
    }
}
