package view;

import controller.Music;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.Timer;
import model.GameModel;

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

    /** Layer for background image and title image. */
    private final JLayeredPane myImageLayer = new JLayeredPane();

    /** Image for title. */
    private final ImageIcon myImageTitle = new ImageIcon("resources/images/title_images/title.png");

    /** Label for animated title. */
    private final JLabel myTitleLabel = new JLabel(myImageTitle);

    /** Vertical position for game title to be able to animate. */
    private int myTitleScreenY;

    /** Music object for background music. */
    private final Music myMusic = new Music();

    /** Reference for game's logic. */
    private final GameModel myGameModel;

    /**
     * Constructor for class and initializes JButtons.
     * Sets up all components, animation and music.
     */
    public TitleScreen(final GameModel theGameModel) {
        myGameModel = theGameModel;

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

        final ImageIcon imageBackground = new ImageIcon("resources/images/title_images/title_background.jpg");
        final JLabel backgroundLabel = new JLabel(imageBackground);
        backgroundLabel.setBounds(0, 0, 558, 732);
        myImageLayer.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        myTitleLabel.setBounds(150, 0, 560, 340);
        myImageLayer.add(myTitleLabel, JLayeredPane.PALETTE_LAYER);

        myTitleWindow.add(myImageLayer);
        myTitleWindow.setLocation(700, 200);
        myTitleWindow.setSize(558, 732);
        myTitleWindow.setResizable(false);
        myTitleWindow.setTitle("Level Up (A Trivia Maze)");
        myTitleWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myTitleWindow.setVisible(true);
    }

    /** Adds clickable images as buttons. */
    private void buttons() {
        final ImageIcon startIcon = scaleImage("resources/images/title_images/START.png");
        final ImageIcon loadIcon = scaleImage("resources/images/title_images/LOAD.png");
        final ImageIcon exitIcon = scaleImage("resources/images/title_images/EXIT.png");

        final JLabel startLabel = new JLabel(startIcon);
        final JLabel loadLabel = new JLabel(loadIcon);
        final JLabel exitLabel = new JLabel(exitIcon);

        startLabel.setBounds(200, 320, 140, 130);
        loadLabel.setBounds(200, 440, 140, 130);
        exitLabel.setBounds(200, 560, 140, 130);

        startLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(final java.awt.event.MouseEvent theException) {
                myGameModel.newGame();
                new GameplayFrame(myGameModel);
                myMusic.getMusicStop();
                myTitleWindow.dispose();
            }
        });

        loadLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(final java.awt.event.MouseEvent theException) {
                myGameModel.loadGame();
                new GameplayFrame(myGameModel);
                myMusic.getMusicStop();
                myTitleWindow.dispose();
            }
        });

        exitLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(final java.awt.event.MouseEvent theException) {
                System.exit(0);
            }
        });

        myImageLayer.add(startLabel, JLayeredPane.PALETTE_LAYER);
        myImageLayer.add(loadLabel, JLayeredPane.PALETTE_LAYER);
        myImageLayer.add(exitLabel, JLayeredPane.PALETTE_LAYER);
    }

    /** Scales an image to the specified width and height. */
    private ImageIcon scaleImage(final String theImagePath) {
        final ImageIcon originalIcon = new ImageIcon(theImagePath);
        final Image scaledImage = originalIcon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
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
        myMusic.getMusic("resources/sounds/title_bgm.wav");
    }
}