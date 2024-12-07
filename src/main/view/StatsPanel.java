package view;

import model.GameModel;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Represents the stats panel for the player.
 * Displays the player's lives remaining and their score.
 * Updates according depending on the player answering a question right
 * or wrong.
 *
 * @author Cynthia Lopez
 * @version 12/1/24
 */
public class StatsPanel {

    /** Text field to display player lives. */
    private final transient JTextField myLives;

    /** Text field to display player score. */
    private final transient JTextField myScore;

    /** Game model object to refer for game's logic. */
    private final GameModel myGameModel;

    /**
     * Constructs Stats panel to display players lives and score in a custom
     * pixel text.
     *
     * @param theGameModel the game's logic.
     */
    public StatsPanel(final GameModel theGameModel) {
        myGameModel = theGameModel;

        myScore = new JTextField();
        myLives = new JTextField();

        showStats();
        customText();
    }

    /** Displays lives and score under player image. */
    private void showStats() {
        myLives.setBounds(700, 600, 150, 50);
        myScore.setBounds(700, 650, 150, 50);

        myLives.setBorder(null);
        myScore.setBorder(null);

        myLives.setBackground(new Color(0, 0, 0, 0));
        myScore.setBackground(new Color(0, 0, 0, 0));

        myLives.setEditable(false);
        myScore.setEditable(false);
    }

    /** Custom text for questions, answers and buttons. */
    private void customText() {
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/pixel_font_bold.ttf")).deriveFont(30f);
            final GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
            g.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/pixel_font_bold.ttf")));

        } catch (final IOException | FontFormatException exception) {
            font = new Font("Times New Roman", Font.BOLD, 20);
        }

        myLives.setFont(font);
        myScore.setFont(font);

        myLives.setForeground(Color.WHITE);
        myScore.setForeground(Color.WHITE);
    }

    /** Updates players lives and score through game play. */
    public void updateStats() {
        myLives.setText("Lives: " + myGameModel.getState().getPlayer().getLives());
        myScore.setText("Score: " + myGameModel.getState().getPlayer().getScore());

        myLives.repaint();
        myScore.repaint();
    }

    /** Getter for lives text field. */
    public JTextField getLives() {
        return myLives;
    }

    /** Getter for score text field. */
    public JTextField getScore() {
        return myScore;
    }
}
