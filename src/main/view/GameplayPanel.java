package view;

import java.awt.*;
import javax.swing.*;
import model.*;
import model.enums.GamePlayPhase;

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

    /** MazeView instance to render the maze. */
    private final MazeView myMazeView;

    /** PlayerView instance to render the player. */
    private final PlayerView myPlayerView;

    /** Object for game logic. */
    private final GameModel myGameModel;

    /** Constructs a game panel. */
    public GameplayPanel(final GameModel theGameModel) {

        myTileWidth = 50;
        myTileHeight = 50;

        myGameModel = theGameModel;
        myPlayerManager = new PlayerManager(myGameModel);
        myMazeView = new MazeView(myTileWidth, myTileHeight, myGameModel);
        myPlayerView = new PlayerView(myPlayerManager, myGameModel);

        myMazeView.addRoomViewHook(myPlayerView.getRoomViewHook());

        theGameModel.getState().setPhase(GamePlayPhase.IN_PROGRESS);

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

        final Coordinates playerPos = myGameModel.getState().getPlayer().getPosition();

        final Image mazeImage = myMazeView.getPortion(playerPos.getRoomX(), playerPos.getRoomY(),
                                                      super.getWidth(), super.getHeight());

        g2D.drawImage(mazeImage, 0, 0, super.getWidth(), super.getHeight(), null);

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