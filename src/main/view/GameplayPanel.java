package view;

import java.awt.*;
import javax.swing.*;
import model.*;

/**
 * GameplayPanel represents the main game area.
 * It handles rendering the game, managing updates, and tracking the player.
 *
 * @author Cynthia Lopez
 * @version 11/1/24
 */
public class GameplayPanel extends JPanel implements Runnable {
    /** Height of the panel. */
    private final int myHeight;

    /** Width of the panel. */
    private final int myWidth;

    /** Height of a single tile. */
    private final int myTileHeight;

    /** Width of a single tile. */
    private final int myTileWidth;

    /** GameState object representing the state of the game. */
    private final GameState myGameState;

    /** Thread for the running game loop. */
    private Thread myGameThread;

    /** Manages player input and actions. */
    private final PlayerManager myPlayerManager = new PlayerManager();

    /** Frames per second for the game. */
    private final int myFPS = 60;

    /** PlayerView instance to render the player. */
    private final PlayerView myPlayerView = new PlayerView(this, myPlayerManager);

    /**
     * Constructs a game panel.
     *
     * @param theGameState the GameState object to be used in the current game.
     */
    public GameplayPanel(final GameState theGameState) {
        myGameState = theGameState;
        myWidth = 780;
        myHeight = 440;

        // VERIFY SIZE IS APPROPRIATE FOR TILES
        myTileWidth = 20;
        myTileHeight = 20;

        // add tiles to game panel
        mainGameWindow();
        startGameThread();
    }

    /** Configures the main game window. */
    private void mainGameWindow() {
        setPreferredSize(new Dimension(myWidth, myHeight));
        setBackground(Color.BLACK);
        addKeyListener(myPlayerManager);
        setFocusable(true);

        setLayout(null);
    }

    /** sets up a mini map for user to see where avatar is located. */
    private void mapPanel() {

    }

    /** Starts game loop in a separate thread. */
    private void startGameThread() {
        myGameThread = new Thread(this); // passing Gameplay Panel to this constructor.
        myGameThread.start();
    }

    /** Updates the state of the game, including player movement and interactions. */
    private void update() {
        myPlayerView.getUpdate();
    }

    /**
     * Renders the gameplay area, including the player and other elements.
     *
     * @param theGraphics the graphics object to render components.
     */
    protected void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2D = (Graphics2D) theGraphics;

        myPlayerView.getDraw(g2D);

        g2D.dispose();
    }

    /** The game loop for updating and rendering the game at targeted FPS. */
    @Override
    public void run() {
        // accumulator method
        final double drawInterval = (double) 1000000000 / myFPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (myGameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
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
