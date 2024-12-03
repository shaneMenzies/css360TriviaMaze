package view;

import controller.GameOperations;
import controller.Music;
import model.GameModel;
import model.enums.GamePlayPhase;
import model.interfaces.GameModelUpdateListener;
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

    /** Panel for trivia questions. */
    private final QuestionsPanel myQuestionsPanel;

    /** Game panel for main maze gameplay. */
    private final GameplayPanel myGamePanel;

    /** Game operations object to track state of save, load and new game. */
    private final GameOperations myGameOp;

    /** Stats panel object for Player lives and score. */
    private final StatsPanel myStats;

    /** Main game window frame. */
    private final JFrame myGameFrame;

    /** Main panel where game components are added. */
    private final JPanel myPanel = new JPanel(null);

    /** Game logo. */
    private JLabel myLogoLabel;

    /** Menu bar containing options for the file and help operations. */
    private final JMenuBar myMenuBar = new JMenuBar();

    /** The "File" menu containing save, load, exit. */
    private final JMenu myFile = new JMenu("File");

    /** The "Help" menu containing options for game instructions and information about the game. */
    private final JMenu myHelp = new JMenu("Help");

    /** Music object for background music. */
    private final Music myMusic = new Music();


    /** Constructs main game screen and initializes GUI components. */
    public GameplayFrame(final GameModel theGameModel) {
        myGameFrame = new JFrame();
        myLogoLabel = new JLabel();
        myGameOp = new GameOperations(theGameModel, this);
        myGamePanel = new GameplayPanel(theGameModel);
        myQuestionsPanel = new QuestionsPanel(theGameModel);
        myStats = new StatsPanel(theGameModel);

        setUpGUI();

        theGameModel.addUpdateListener(this::gameModelUpdate);
    }

    /** Sets up GUI by calling all component methods. */
    private void setUpGUI() {
        frame();
        menuBar();
        backgroundMusic();
        mazePanel();
        questionPanel();
        statsPanel();
    }

    /** Sets up main game frame with title, size, background color, and position. */
    private void frame() {
        myPanel.setBackground(Color.BLACK);

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

        final JMenuItem newGame = new JMenuItem("New");
        final JMenuItem save = new JMenuItem("Save");
        final JMenuItem load = new JMenuItem("Load");
        final JMenuItem exit = new JMenuItem("Exit");

        final JMenuItem about = new JMenuItem("About");
        final JMenuItem gameInstructions = new JMenuItem("Game Play Instructions");

        newGame.addActionListener(e -> myGameOp.startNewGame());

        save.addActionListener(e -> myGameOp.saveGame());

        load.addActionListener(e -> myGameOp.loadGame());

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
        myMusic.getMusic("resources/sounds/game_bgm.wav");
    }

    /** Sets up main panel to show avatar moving in game. */
    private void mazePanel() {
        myGamePanel.setBounds(10, 10, 900, 450);
        myPanel.add(myGamePanel);
    }

    /** Hidden question panel. */
    private void questionPanel() {
        final ImageIcon logo = scaleImage("resources/images/title_images/title.png", 300, 300);
        myLogoLabel = new JLabel(logo);
        myLogoLabel.setBounds(20, 500, 300, 180);

        myPanel.add(myLogoLabel);
        myLogoLabel.setVisible(true);

        myQuestionsPanel.setVisible(false);
        myPanel.add(myQuestionsPanel);
    }

    /** Stats panel for player lives, score and displays player image. */
    private void statsPanel() {
        final ImageIcon icon = scaleImage("resources/images/player_down.png", 100, 100);
        final JLabel iconLabel = new JLabel(icon);
        iconLabel.setBounds(700, 450, 100, 180);

        myPanel.add(iconLabel);
        myPanel.add(myStats.getScore());
        myPanel.add(myStats.getLives());

        iconLabel.setVisible(true);
    }

    /** Scales an image to the specified width and height. */
    private ImageIcon scaleImage(final String theImagePath, final int theWidth, final int theHeight) {
        final ImageIcon originalIcon = new ImageIcon(theImagePath);
        final Image scaledImage = originalIcon.getImage().getScaledInstance(theWidth, theHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    /**
     * Handles updates to the game model to hide question panel and reveal it
     * depending on the game phase.
     *
     * @param theUpdateType type of update received.
     * @param theGameModel game model of the current game.
     */
    private void gameModelUpdate(final GameModelUpdateListener.UpdateType theUpdateType, final GameModel theGameModel) {
        if (theUpdateType == GameModelUpdateListener.UpdateType.GAME_STATE_PHASE) {
            final GamePlayPhase currentPhase = theGameModel.getState().getPhase();
            if (currentPhase == GamePlayPhase.TRIVIA) {

                myLogoLabel.setVisible(false);
                myQuestionsPanel.setVisible(true);
                myQuestionsPanel.setBackground(Color.BLACK);
                myQuestionsPanel.displayQuestion();
                myStats.updateStats();
            } else {
                myQuestionsPanel.setVisible(false);
                myLogoLabel.setVisible(true);
                myStats.updateStats();
            }

            myStats.updateStats();
            myPanel.revalidate();
            myPanel.repaint();
        }
        myGamePanel.repaint();
    }

    /** Getter for background music. */
    public Music getMainGameMusic() {
        return myMusic;
    }
}