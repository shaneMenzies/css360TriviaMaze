package view;

import java.awt.*;
import javax.swing.*;
import model.*;
import model.enums.GamePlayPhase;
import model.enums.TileID;
import model.interfaces.MazeGenerator;

/**
 * GameplayPanel represents the main game area.
 * It handles rendering the game, managing updates, and tracking the player.
 *
 * @author Cynthia Lopez
 * @version 11/1/24
 */
public class GameplayPanel extends JPanel {

    /**
     * Target update frame rate.
     */
    private static final int TARGET_FPS = 60;

    /** Height of a single tile. */
    private int myTileHeight, myTileWidth;

    /** GameState object representing the state of the game. */
    private final GameState myGameState;

    /** Manages player input and actions. */
    private final PlayerManager myPlayerManager;

    /** PlayerView instance to render the player. */
    private final PlayerView myPlayerView;

    /** Maze instance. */
    private final Maze myMaze;

    /** Generator for rectangular maze.*/
    private final RectangleMazeGenerator myRMG;

    /** Game settings. */
    private final GameSettings mySettings;

    /**
     * Constructs a game panel.
     */
    public GameplayPanel() {
        mySettings = new GameSettings(3, 10, -10);

        myRMG = new RectangleMazeGenerator(6, 6,
                5, 5,
                QuestionsDatabase.getInstance());
        myMaze = myRMG.generate();
        myGameState = new GameState(mySettings, myMaze);
        myPlayerManager = new PlayerManager();
        myPlayerView = new PlayerView(this, myPlayerManager);
        myGameState.setPhase(GamePlayPhase.IN_PROGRESS);

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

        SpriteMap tileMap = SpriteMap.getInstance();

        int roomHeight = myMaze.getRoom(0, 0).getHeight();
        int roomWidth = myMaze.getRoom(0, 0).getWidth();

        int totalHeight = myMaze.getHeight() * roomHeight;
        int totalWidth = myMaze.getWidth() * roomWidth;

        myTileHeight = (getHeight() / totalHeight);
        myTileWidth = (getWidth() / totalWidth);

        for (int y = totalHeight - 1; y >= 0; y--) {
            for (int x = 0; x < (myMaze.getWidth() * roomWidth); x++) {
                int roomX = x / roomWidth;
                int roomY = y / roomHeight;
                int tileX = x % roomWidth;
                int tileY = y % roomHeight;

                final TileID tileID = myMaze.getRoom(roomX, roomY).getTile(tileX, tileY).getTileID();
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
    }

    /** Getter for current game state.
     *
     * @return the current game state.
     */
    public GameState getGameState() {
        return myGameState;
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

    /**
     * Getter for Maze Generator (Rectangular)
     *
     * @return the rectangular maze.
     */
    public MazeGenerator getMazeGenerator() {
        return myRMG;
    }
}