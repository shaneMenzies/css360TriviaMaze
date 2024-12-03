package view;

import java.awt.*;
import javax.swing.*;
import model.*;
import model.enums.GamePlayPhase;
import model.enums.TileID;

/**
 * GameplayPanel represents the main game area.
 * It handles rendering the game, managing updates, and tracking the player.
 *
 * @author Cynthia Lopez
 * @version 11/1/24
 */
public class GameplayPanel extends JPanel {

    /** Target update frame rate. */
    private static final int TARGET_FPS = 60;

    /** Height of a single tile. */
    private int myTileHeight, myTileWidth;

    /** Manages player input and actions. */
    private final PlayerManager myPlayerManager;

    /** PlayerView instance to render the player. */
    private final PlayerView myPlayerView;

    /** Object for game logic. */
    private final GameModel myGameModel;

    /** Constructs a game panel. */
    public GameplayPanel(final GameModel theGameModel) {

        myGameModel = theGameModel;
        myPlayerManager = new PlayerManager();
        myPlayerView = new PlayerView(this, myPlayerManager, theGameModel);
        theGameModel.getState().setPhase(GamePlayPhase.IN_PROGRESS);

        myTileWidth = 0;
        myTileHeight = 0;

        mainGameWindow();
        startGameThread();
    }

    /** Configures the main game window. */
    private void mainGameWindow() {
        setBackground(Color.BLACK);
        addKeyListener(myPlayerManager);
        setFocusable(true);
        setLayout(null);
    }

    /**
     * Renders the gameplay area, including the player and other elements.
     *
     * @param theGraphics the graphics object to render components.
     */
    protected void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2D = (Graphics2D) theGraphics;

        final SpriteMap tileMap = SpriteMap.getInstance();

        final Maze maze = myGameModel.getState().getMaze();

        final int roomHeight = maze.getRoom(0, 0).getHeight();
        final int roomWidth = maze.getRoom(0, 0).getWidth();

        final int totalHeight = maze.getHeight() * roomHeight;
        final int totalWidth = maze.getWidth() * roomWidth;

        myTileHeight = (getHeight() / totalHeight);
        myTileWidth = (getWidth() / totalWidth);

        for (int y = totalHeight - 1; y >= 0; y--) {
            for (int x = 0; x < (maze.getWidth() * roomWidth); x++) {
                final int roomX = x / roomWidth;
                final int roomY = y / roomHeight;
                final int tileX = x % roomWidth;
                final int tileY = y % roomHeight;

                final TileID tileID = maze.getRoom(roomX, roomY).getTile(tileX, tileY).getTileID();
                final Image tileImage = tileMap.get(tileID);

                g2D.drawImage(tileImage, x * myTileWidth, y * myTileHeight,
                        myTileWidth, myTileHeight, null);
            }
        }

        myPlayerView.draw(g2D);

        g2D.dispose();
    }

    /** Starts game loop in a separate thread. */
    private void startGameThread() {
        // Target delay (in milliseconds)
        final int delay = 1000 / TARGET_FPS;
        new Timer(delay, ActionListener -> refresh()).start();
    }

    /** Updates the state of the game, including player movement and interactions. */
    private void refresh() {
        myPlayerView.update();
        repaint();
    }

    /**
     * Returns width of a single tile.
     *
     * @return tile width.
     */
    public int getTileWidth() {
        return myTileWidth;
    }

    /**
     * Returns height of a single tile.
     *
     * @return tile height.
     */
    public int getTileHeight() {
        return myTileHeight;
    }

}