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
 *
 * Extends JFrame to handle frame and component setup.
 *
 * @author Cynthia Lopez
 * @version 10/27/24
 */

public class TitleScreen extends JFrame {
    
    /**
     * Main frame for title.
     */
    private final JFrame myTitleWindow = new JFrame();

    /**
     * Button to start game.
     */
    private final JButton myStart;

    /**
     * Button to exit game.
     */
    private final JButton myExit;

    /**
     * Label for animated title.
     */
    private JLabel myTitleLabel;

    /**
     * Vertical position for game title to be able to animate.
     */
    private int myTitleScreenY;

    /**
     * Main panel to organize buttons within title frame.
     */
    private final JPanel myLayout = new JPanel();

    /**
     * Constructor for class and initializes JButtons.
     * Sets up all components, animation and music.
     */
    public TitleScreen() {
        myStart = new JButton("Play");
        myExit = new JButton("Exit");

        setUpTitle();
    }

    /**
     * Configures all components.
     */
    private void setUpTitle() {
        moveTitle();
        buttons();
        frame();
        backgroundMusic();
    }

    /**
     * Configures frame size, location, title and background image.
     */
    private void frame() {
        myTitleWindow.setLocation(700, 200);
        myTitleWindow.setSize(558, 732);
        myTitleWindow.setResizable(false);
        myTitleWindow.setTitle("Trivia Maze");
        myTitleWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myTitleWindow.setVisible(true);


        final ImageIcon imageTV = new ImageIcon("src/images/title_background.jpg");
        final JLabel tvLabel = new JLabel(imageTV);

        myTitleWindow.add(tvLabel);
    }

    /**
     * Adds start and exit button to the bottom of the frame.
     */
    private void buttons() {
        myLayout.add(myStart);
        myLayout.add(myExit);
        myTitleWindow.add(myLayout, BorderLayout.SOUTH);
    }

    /**
     * Animates title image by moving it vertically in a floating motion.
     */
    private void moveTitle() {
        final ImageIcon imageTitle = new ImageIcon("src/images/title.png");
        myTitleLabel = new JLabel(imageTitle);

        final Timer titleScreenTimer = new Timer(40, new ActionListener() {
            boolean myMovingUp = true;

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
        myTitleWindow.add(myTitleLabel);
    }

    /**
     * Plays background music on title screen.
     */
    private void backgroundMusic() {
        final Music music = new Music();
        music.getMusic("src/sounds/game-music-teste-1-204326.wav");
    }
}
