package controller;

import model.GameModel;
import view.GameplayFrame;

import javax.swing.*;

/**
 * Represents the updating the GUI with different game states of save, load, and new game.
 *
 * @author Cynthia Lopez
 * @version 12/1/24
 */
public class GameOperations {

    /** Game model object for logic. */
    private final GameModel myGameModel;

    /** Game frame object for GUI. */
    private final GameplayFrame myGameFrame;

    /** Constructs new game operations object to then handle whichever functionality. */
    public GameOperations(final GameModel theGameModel, final GameplayFrame theGameFrame) {
        myGameModel = theGameModel;
        myGameFrame = theGameFrame;
    }

    /** Starts a new game. */
    public void startNewGame() {
        myGameModel.newGame();
        myGameFrame.dispose();
        myGameFrame.getMainGameMusic().getMusicStop();
        new GameplayFrame(myGameModel);
    }

    /** Saves game. */
    public void saveGame() {
        try {
            myGameModel.saveGame();

            JOptionPane.showMessageDialog(myGameFrame,
                    "Game saved successfully!",
                    "Save Game",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (final Exception exception) {
            JOptionPane.showMessageDialog(myGameFrame,
                    "An error occurred while saving the game: " + exception.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Loads game and updates GUI. */
    public void loadGame() {
        try {
            myGameModel.loadGame();

            myGameFrame.getMainGameMusic().getMusicStop();
            myGameFrame.getMainGameMusic().getMusic("resources/sounds/game_bgm.wav");

            myGameFrame.getOutcome().setVisible(false);
            myGameFrame.getGamePanel().setVisible(true);

            myGameFrame.getGamePanel().requestFocusInWindow();

            JOptionPane.showMessageDialog(myGameFrame,
                    "Game loaded successfully!",
                    "Load Game",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (final Exception exception) {
            JOptionPane.showMessageDialog(myGameFrame,
                    "An error occurred while loading the game: " + exception.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
