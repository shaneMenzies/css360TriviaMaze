package view;

import controller.Music;
import model.GameModel;
import model.GameState;
import model.QuestionsDatabase;
import model.enums.GamePlayPhase;
import model.interfaces.GameStateUpdateListener;
import java.awt.*;
import javax.swing.*;

/**
 * Represents the main view for the game screen. Displays map, avatar, direction buttons
 * and menu bar.
 *
 * @author Cynthia Lopez
 * @version 10/28/24
 */
public class GameplayFrame extends JFrame {

    /** Custom background color for main panel. */
    private static final Color BACKGROUND_COLOR = new Color(51, 51, 51);

    /** Panel for trivia questions. */
    private final QuestionsPanel myQuestionsPanel;

    /** Main game window frame. */
    private final JFrame myGameFrame;

    /** Main panel where game components are added. */
    private final JPanel myPanel = new JPanel(null);

    /** Game panel for main maze gameplay. */
    final GameplayPanel myGamePanel;

    /** Menu bar containing options for the file and help operations. */
    private final JMenuBar myMenuBar = new JMenuBar();

    /** The "File" menu containing save, load, exit. */
    private final JMenu myFile = new JMenu("File");

    /** The "Help" menu containing options for game instructions and information about the game. */
    private final JMenu myHelp = new JMenu("Help");

    /** Music object for background music. */
    private final Music myMusic = new Music();

    /** Constructs main game screen and initializes GUI components. */
    public GameplayFrame() {
        myGameFrame = new JFrame();
        myGamePanel = new GameplayPanel();
        myQuestionsPanel = new QuestionsPanel(myGamePanel.getGameState());
        setUpGUI();

        myGamePanel.getGameState().addUpdateListener(this::gameStateUpdate);
    }

    /** Sets up GUI by calling all component methods. */
    private void setUpGUI() {
        frame();
        menuBar();
        backgroundMusic();
        mazePanel();
        questionPanelInvisible();
    }

    /** Sets up main game frame with title, size, background color, and position. */
    private void frame() {
        myPanel.setBackground(BACKGROUND_COLOR);

        myGameFrame.add(myPanel);
        myGameFrame.setTitle("Level Up (A Trivia Maze)");
        myGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myGameFrame.setSize(920, 800);
        myGameFrame.setLocation(500, 100);
        myGameFrame.setResizable(false);

        myGameFrame.setVisible(true);
    }

    /**
     * Sets up the menu bar by adding menu items to "File" and "Help" menus.
     * Attaching those menus to the menu bar on the game window.
     */
    private void menuBar() {
        GameModel gameModel = new GameModel(myGamePanel.getMazeGenerator(), QuestionsDatabase.getInstance());

        final JMenuItem newGame = new JMenuItem("New");
        final JMenuItem save = new JMenuItem("Save");
        final JMenuItem load = new JMenuItem("Load");
        final JMenuItem exit = new JMenuItem("Exit");

        final JMenuItem about = new JMenuItem("About");
        final JMenuItem gameInstructions = new JMenuItem("Game Play Instructions");

        newGame.addActionListener(e -> { gameModel.newGame();
            myMusic.getMusicStop();
            myGameFrame.dispose();
            new GameplayFrame();
        });

        save.addActionListener(e -> { gameModel.saveGame();});
        load.addActionListener(e -> { gameModel.loadGame();});
        exit.addActionListener(e -> System.exit(0));

        about.addActionListener(e -> JOptionPane.showMessageDialog(myGameFrame, """
                Level Up (A Trivia Maze)
                
                 Team:
                 Cynthia Lopez\s
                 Shane Menzies\s
                 Arafa Mohamed\s
                 
                 Version 1.0"""));

        gameInstructions.addActionListener(e -> JOptionPane.showMessageDialog(myGameFrame, """
                Navigation:                         Objective:
                W ➜ Move Player Up                  To win level up, you must navigate through out a maze
                A ➜ Move Player Left                but in order to enter a new room, you have to answer a
                S ➜ Move Player Down                trivia question... answer wrong, and the door is locked.
                D ➜ Move Player Right               If all doors become locked, it's game over.
                """));

        myFile.add(newGame);
        myFile.add(save);
        myFile.add(load);
        myFile.add(exit);

        myHelp.add(about);
        myHelp.add(gameInstructions);

        myMenuBar.add(myFile);
        myMenuBar.add(myHelp);

        myGameFrame.setJMenuBar(myMenuBar);
    }

    /** Method to create background music using Music class. */
    private void backgroundMusic() {
        myMusic.getMusic("resources/sounds/game-music-loop-19-153393.wav");
    }

    /** Sets up main panel to show avatar moving in game. */
    private void mazePanel() {
        myGamePanel.setBounds(10, 10, 900, 450);
        myPanel.add(myGamePanel);
    }

    /** Hidden question panel. */
    private void questionPanelInvisible() {
        myQuestionsPanel.setVisible(false);
        myPanel.add(myQuestionsPanel);
    }

    /**
     * Handles updates to the game state to hide question panel and reveal it
     * depending on the game phase.
     *
     * @param updateType type of update received.
     * @param gameState game state of the current game.
     */
    private void gameStateUpdate(GameStateUpdateListener.UpdateType updateType, GameState gameState) {
        if (updateType == GameStateUpdateListener.UpdateType.PHASE) {
            GamePlayPhase currentPhase = gameState.getPhase();
            if (currentPhase == GamePlayPhase.TRIVIA) {

                myQuestionsPanel.setVisible(true);
                myQuestionsPanel.setBackground(Color.GRAY);
                myQuestionsPanel.displayQuestion();
            } else {
                myQuestionsPanel.setVisible(false);
            }
            myPanel.revalidate();
            myPanel.repaint();
        }

        myGamePanel.repaint();
    }
}