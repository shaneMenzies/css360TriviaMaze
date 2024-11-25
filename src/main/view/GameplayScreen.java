package view;

import controller.Music;
import java.awt.*;
import javax.swing.*;
import model.GameSettings;
import model.GameState;
import model.Maze;
import model.Room;
import model.enums.TileID;
import model.interfaces.Tile;

/**
 * Represents the main view for the game screen. Displays map, avatar, direction buttons
 * and menu bar.
 *
 * @author Cynthia Lopez
 * @version 10/28/24
 */
public class GameplayScreen extends JFrame {

    /** Custom background color for main panel. */
    private static final Color BACKGROUND_COLOR = new Color(51, 51, 51);

    /** Panel for maze window. */
    private final GameplayPanel myGamePanel;

    /** Main game window frame. */
    private final JFrame myGameWindow = new JFrame();

    /** Main panel where game components are added. */
    private final JPanel myPanel = new JPanel(null);

    /** Menu bar containing options for the file and help operations. */
    private final JMenuBar myMenuBar = new JMenuBar();

    /** The "File" menu containing save, load, exit. */
    private final JMenu myFile = new JMenu("File");

    /** The "Help" menu containing options for game instructions and information about the game. */
    private final JMenu myHelp = new JMenu("Help");

    /** Music object for background music. */
    private final Music myMusic = new Music();

    /** Constructs main game screen and initializes GUI components. */
    public GameplayScreen() {
        // TODO: IMPLEMENT MAZE

        final Room[][] rooms = new Room[30][30];

        for (int row = 0; row < rooms.length; row++) {
            for (int col = 0; col < rooms.length; col++) {

                final Room.RoomType roomType;
                if (row == 0 & col == 0) {
                    roomType = Room.RoomType.START;
                } else if (row == 29 && col == 29) {
                    roomType = Room.RoomType.EXIT;
                } else {
                    roomType = Room.RoomType.STANDARD;
                }

                final Tile[][] tiles = new Tile[10][10];
                for (int i = 0; i < tiles.length; i++) {
                    for (int j = 0; j < tiles.length; j++) {
                        tiles[i][j] = new Tile() {
                            @Override
                            public boolean tryMoveTo() {
                                return false;
                            }

                            @Override
                            public TileID getTileID() {
                                return null;
                            }
                        };
                    }
                }

                rooms[row][col] = new Room(roomType, tiles);
            }
        }

        final GameSettings settings = new GameSettings(2, 10, -5);
        final Maze maze = new Maze(rooms, null, 0, 0, 29, 29);

        final GameState gameState = new GameState(settings, maze);

        myGamePanel = new GameplayPanel(gameState);

        setUpGUI();
    }

    /** Sets up GUI by calling all component methods. */
    private void setUpGUI() {
        frame();
        menuBar();
        questionPanel();
        backgroundMusic();
        mainPanel();
    }

    /** Sets up main game frame with title, size, background color, and position. */
    private void frame() {
        myPanel.setBackground(BACKGROUND_COLOR);

        myGameWindow.add(myPanel);
        myGameWindow.setTitle("Level Up (A Trivia Maze)");
        myGameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myGameWindow.setSize(800, 700);
        myGameWindow.setLocation(600, 200);
        myGameWindow.setResizable(false);

        myGameWindow.setVisible(true);
    }

    /** Sets up the menu bar by adding menu items to "File" and "Help" menus.
     *  Attaching those menus to the menu bar on the game window. */
    private void menuBar() {
        // TODO: ADD SAVE AND LOAD FEATURE
        final JMenuItem save = new JMenuItem("Save");
        final JMenuItem load = new JMenuItem("Load");
        final JMenuItem exit = new JMenuItem("Exit");

        final JMenuItem about = new JMenuItem("About");
        final JMenuItem gameInstructions = new JMenuItem("Game Play Instructions");

        save.addActionListener(e -> JOptionPane.showMessageDialog(myGameWindow, "DEBUG: SAVED GAME"));
        load.addActionListener(e -> JOptionPane.showMessageDialog(myGameWindow, "DEBUG: GAME LOADED"));
        exit.addActionListener(e -> System.exit(0));

        about.addActionListener(e -> JOptionPane.showMessageDialog(myGameWindow, """
                Level Up (A Trivia Maze)
                
                 Team:
                 Cynthia Lopez\s
                 Shane Menzies\s
                 Arafa Mohamed\s
                 
                 Version 1.0"""));

        gameInstructions.addActionListener(e -> JOptionPane.showMessageDialog(myGameWindow, """
                Navigation:                         Objective:
                W ➜ Move Player Up                  To win level up, you must navigate through out a maze
                A ➜ Move Player Left                but in order to enter a new room, you have to answer a
                S ➜ Move Player Down                trivia question... answer wrong, and the door is locked.
                D ➜ Move Player Right               If all doors become locked, it's game over.
                """));

        myFile.add(save);
        myFile.add(load);
        myFile.add(exit);

        myHelp.add(about);
        myHelp.add(gameInstructions);

        myMenuBar.add(myFile);
        myMenuBar.add(myHelp);

        myGameWindow.setJMenuBar(myMenuBar);
    }

    /** Sets up panel for questions. */
    private void questionPanel() {
        final JPanel questionPanel = new JPanel();
        questionPanel.setBackground(Color.LIGHT_GRAY);
        questionPanel.setBounds(10, 460, 300, 180);
        questionPanel.setLayout(new GridBagLayout());

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Example of adding a question label and a button
        // TODO: CONNECT DATABASE
        final JLabel questionLabel = new JLabel("DEBUG: Who created SIMS?");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        questionPanel.add(questionLabel, gbc);

        final JButton answer1Button = new JButton("DEBUG: EA");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        questionPanel.add(answer1Button, gbc);

        final JButton answer2Button = new JButton("DEBUG: Nintendo");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        questionPanel.add(answer2Button, gbc);

        final JButton answer3Button = new JButton("DEBUG: SONY");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        questionPanel.add(answer3Button, gbc);

        final JButton answer4Button = new JButton("DEBUG: XBOX");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        questionPanel.add(answer4Button, gbc);

        myPanel.add(questionPanel);
    }

    /** Method to create background music using Music class. */
    private void backgroundMusic() {
        myMusic.getMusic("resources/sounds/game-music-loop-19-153393.wav");
    }

    /** Sets up main panel to show avatar moving in game. */
    private void mainPanel() {
        myGamePanel.setBounds(10, 10, 780, 440);
        myPanel.add(myGamePanel);
    }

}