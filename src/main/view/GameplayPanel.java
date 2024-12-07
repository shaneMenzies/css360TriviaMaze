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
    private final static int TILE_HEIGHT = 50;

    /** Width of a single tile. */
    private final static int TILE_WIDTH = 50;

    /** Manages player input and actions. */
    private final PlayerManager myPlayerManager;

    /** MazeView instance to render the maze. */
    private final MazeView myMazeView;

    /** Object for game logic. */
    private final GameModel myGameModel;

    /** Constructs a game panel. */
    public GameplayPanel(final GameModel theGameModel) {
        myGameModel = theGameModel;
        myPlayerManager = new PlayerManager(myGameModel);
        myMazeView = new MazeView(TILE_WIDTH, TILE_HEIGHT, myGameModel);
        final PlayerView playerView = new PlayerView(myPlayerManager, myGameModel);

        myMazeView.addRoomViewHook(playerView.getRoomViewHook());
        myMazeView.addRoomViewHook(new ExitViewHook());

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
        final int delay = 1000 / TARGET_FPS;
        new Timer(delay, ActionListener -> refresh()).start();
    }

    /** Updates the state of the game, including player movement and interactions. */
    private void refresh() {
        repaint();
    }

    public MazeView getMazeView() {
        return myMazeView;
    }

    /**
     * RoomViewHook to draw the maze's exit door.
     */
    private final class ExitViewHook implements RoomViewHook {
        @Override
        public void doHook(final Graphics2D theGraphics, final RoomView theRoom) {
            final int exitX = myGameModel.getState().getMaze().getExitRoomX();
            final int exitY = myGameModel.getState().getMaze().getExitRoomY();

            final int exitTileX = 2;
            final int exitTileY = 2;

            final Image exitDoor = new ImageIcon("resources/images/exit_door.png").getImage();

            if (theRoom.getRoomX() == exitX && theRoom.getRoomY() == exitY) {

                final int imageX = exitTileX * theRoom.getTileWidth();
                final int imageY = (theRoom.getRoom().getHeight() - 1 - exitTileY)
                        * theRoom.getTileHeight();

                theGraphics.drawImage(exitDoor, imageX, imageY,
                        theRoom.getTileWidth(), theRoom.getTileHeight(),
                        null);
            }
        }
    }
}