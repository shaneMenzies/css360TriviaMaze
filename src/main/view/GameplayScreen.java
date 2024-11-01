package view;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the main view for the game screen. Displays map, avatar, direction buttons
 * and menu bar.
 *
 * @author Cynthia Lopez
 * @version 10/28/24
 */
public class GameplayScreen extends JFrame {

    /**
     * Main game window frame.
     */
    private final JFrame myGameWindow = new JFrame();

    /**
     * Main panel where game components are added.
     */
    private final JPanel myPanel = new JPanel();

    /**
     * Menu bar containing options for the file and help operations.
     */
    private final JMenuBar myMenuBar = new JMenuBar();

    /**
     * The "File" menu containing save, load, exit.
     */
    private final JMenu myFile = new JMenu("File");

    /**
     *  The "Help" menu containing options for game instructions and information
     *  about the game.
     */
    private final JMenu myHelp = new JMenu("Help");

    /**
     * Custom background color for main panel.
     */
    private final Color backgroundColor = new Color(51,51,51);

    /**
     * Constructs main game screen and initializes GUI components.
     */
    public GameplayScreen() {
        setUpGUI();
    }

    /**
     * Sets up GUI by calling all component methods.
     */
    private void setUpGUI() {
        frame();
        menuBar();
    }

    /**
     * Sets up main game frame with title, size, background color, and position.
     */
    private void frame() {
        myPanel.setBackground(backgroundColor);
        myGameWindow.add(myPanel);

        myGameWindow.setTitle("Level Up");
        myGameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myGameWindow.setSize(800,700);
        myGameWindow.setLocation(600, 200);
        myGameWindow.setResizable(false);

        myGameWindow.setVisible(true);
    }

    /**
     * Sets up the menu bar by adding menu items to "File" and "Help" menus.
     * Attaching those menus to the menu bar on the game window.
     */
    private void menuBar() {
        final JMenuItem save = new JMenuItem("Save");
        final JMenuItem load = new JMenuItem("Load");
        final JMenuItem exit = new JMenuItem("Exit");

        final JMenuItem about = new JMenuItem("About");
        final JMenuItem gameInstructions = new JMenuItem("Game Play Instructions");

        myFile.add(save);
        myFile.add(load);
        myFile.add(exit);

        myHelp.add(about);
        myHelp.add(gameInstructions);

        myMenuBar.add(myFile);
        myMenuBar.add(myHelp);

        myGameWindow.setJMenuBar(myMenuBar);
    }
}