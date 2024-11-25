package view;

import controller.Music;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.Timer;

/**
 * Represents the main view for the title screen of the game.
 * Displays animated title, background image, and buttons for starting and exiting the game.
 * Extends JFrame to handle frame and component setup.
 *
 * @author Cynthia Lopez
 * @version 10/27/24
 */

public class TitleScreen extends JFrame {

    /** Main frame for title. */
    private final JFrame myTitleWindow = new JFrame();

    /** Main panel to organize buttons within title frame. */
    private final JPanel myLayout = new JPanel();

    /** Layer for background image and title image. */
    private final JLayeredPane myImageLayer = new JLayeredPane();

    /** Image for title. */
    private final ImageIcon myImageTitle = new ImageIcon("resources/images/title.png");

    /** Label for animated title. */
    private final JLabel myTitleLabel = new JLabel(myImageTitle);

    /** Vertical position for game title to be able to animate. */
    private int myTitleScreenY;

    /** Music object for background music. */
    private final Music myMusic = new Music();

    /** Button to start game. */
    private final JButton myStart;

    /** Button to exit game. */
    private final JButton myExit;

    /**
     * Constructor for class and initializes JButtons.
     * Sets up all components, animation and music.
     */
    public TitleScreen() {
        myStart = new JButton("Play");
        myExit = new JButton("Exit");

        setUpTitle();
    }

    /** Configures all components. */
    private void setUpTitle() {
        moveTitle();
        buttons();
        frame();
        backgroundMusic();
    }

    /** Configures frame size, location, title and background image. */
    private void frame() {
        myImageLayer.setPreferredSize(new Dimension(558, 732));

        final ImageIcon imageBackground = new ImageIcon("resources/images/title_background.jpg");
        final JLabel backgroundLabel = new JLabel(imageBackground);
        backgroundLabel.setBounds(0, 0, 558, 732);
        myImageLayer.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        myTitleLabel.setBounds(150, 50, 560, 600);
        myImageLayer.add(myTitleLabel, JLayeredPane.PALETTE_LAYER);

        myTitleWindow.add(myImageLayer);
        myTitleWindow.setLocation(700, 200);
        myTitleWindow.setSize(558, 732);
        myTitleWindow.setResizable(false);
        myTitleWindow.setTitle("Level Up (A Trivia Maze)");
        myTitleWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myTitleWindow.setVisible(true);
    }

    /** Adds start and exit button to the bottom of the frame. */
    private void buttons() {
        myLayout.add(myStart);
        myLayout.add(myExit);
        myTitleWindow.add(myLayout, BorderLayout.SOUTH);

        myStart.addActionListener(e -> {
            new GameplayFrame();
            myMusic.getMusicStop();
            myTitleWindow.dispose();
        });

        myExit.addActionListener(e -> System.exit(0));
    }

    /** Animates title image by moving it vertically in a floating motion. */
    private void moveTitle() {
        final Timer titleScreenTimer = new Timer(40, new ActionListener() {
            private boolean myMovingUp = true;

            @Override
            public void actionPerformed(final ActionEvent theException) {
                if (myMovingUp) {
                    myTitleScreenY -= 1;
                    if (myTitleScreenY <= 0) {
                        myMovingUp = false;
                    }
                } else {
                    myTitleScreenY += 1;
                    if (myTitleScreenY >= 30) {
                        myMovingUp = true;
                    }
                }
                myTitleLabel.setLocation(getX(), myTitleScreenY);
            }
        });
        titleScreenTimer.start();
    }

    /** Plays background music on title screen. */
    private void backgroundMusic() {
        myMusic.getMusic("resources/sounds/game-music-teste-1-204326.wav");
    }
}